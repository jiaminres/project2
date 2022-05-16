<template>
  <div @scroll="scrollHandler" id="showBox" ref="showBox">
    <h1 v-show="totalUsers.length === 0">聊天室</h1>
    <table v-show="totalUsers.length === 0" id="defaultShowBox">
      <tr v-for="val in 5" :key="val">
        <td v-for="val in 5" :key="val"></td>
      </tr>
    </table>

    <div
      v-for="(totalUser, index) in totalUsers"
      :key="totalUser"
      v-show="index === selected"
    >
      <!-- 当仍有数据仍读取完毕时 -->
      <div class="download" v-show="noMsg.indexOf(totalUser) == -1 && isTop()">
        <div class="point"></div>
        <div class="point"></div>
        <div class="point"></div>
        <div class="point"></div>
        <div class="point"></div>
        <div class="point"></div>
        <div class="point"></div>
        <div class="point"></div>
      </div>
      <div
        v-for="(msg, index) in privateMsg[totalUser]"
        :key="index"
        :class="{
          oppositeUser: msg.sourceID !== host.userID,
          user: msg.sourceID === host.userID,
        }"
        :ref="totalUser"
      >
        <!-- <template v-if="msg.direction === 'opposite'"> -->
        <template v-if="msg.sourceID !== host.userID">
          <img class="oppositeUserImage" :src="msg.headImageAddress" />
          <span class="oppositeUserName">{{ msg.userNameWithoutId }}</span>
          <br />
          <div class="widthLimit">
            <i class="triangle"></i>
            <p class="oppositeUserMessage">
              {{ msg.message }}
            </p>
          </div>
        </template>
        <template v-else>
          <div class="bind" ref="bind">
            <span class="userName">{{ msg.userNameWithoutId }}</span>
            <img class="userImage" :src="msg.headImageAddress" />
          </div>
          <br />
          <div class="widthLimit" ref="widthLimit">
            <p class="userMessage" ref="userMessage">
              {{ msg.message }}
            </p>
            <i class="triangle"></i>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapGetters, mapMutations } from "vuex";
export default {
  name: "ShowBox",
  data() {
    return {
      firstScroll: [],
      noMsg: [],
    };
  },
  computed: {
    ...mapState("group", [
      "agreeUsers",
      "privateMsg",
      "selected",
      "host",
      "room",
      "users",
    ]),
    ...mapGetters("group", ["totalUsers", "fullName"]),
  },
  methods: {
    ...mapMutations("group", ["updatePrivateMsgBefore"]),
    isTop() {
      return (
        this.$refs.showBox.scrollTop === 0 &&
        this.$refs.showBox.scrollHeight !== this.$refs.showBox.clientHeight
      );
    },
    style(userMessage, widthLimit, bind) {
      let msgWid = userMessage.offsetWidth + 36;
      if (msgWid < widthLimit.clientWidth) {
        userMessage.style.left = widthLimit.clientWidth - msgWid + "px";
      } else {
        userMessage.style.width = widthLimit.clientWidth - 36 - 20 + "px";
      }
      bind.style.left = "calc(100% - " + bind.clientWidth + "px)";
    },
    scrollHandler() {
      let tar = this.$refs.showBox;
      if (this.isTop()) {
        if (this.noMsg.indexOf(this.totalUsers[this.selectedUser]) === -1) {
          this.getData();
        }
      }
    },
    getData() {
      let selectedUser = this.totalUsers[this.selected];
      let selectedUserID = selectedUser.substring(
        selectedUser.lastIndexOf("@") + 1
      );
      let selectedUserWithoutID = selectedUser.substring(
        0,
        selectedUser.lastIndexOf("@")
      );
      this.$axios
        .get("http://localhost:8080/list/record", {
          params: {
            groupID: this.room.groupID,
            userID: this.host.userID,
            oppositeID: selectedUserID,
            time: this.privateMsg[selectedUser][0].time,
          },
        })
        .then((res) => {
          if (!res.data || !res.data.length) {
            //未获取到数据 说明已经到末尾
            this.noMsg.push(selectedUser);
          } else {
            for (let privateMsg of res.data) {
              privateMsg.userNameWithoutId =
                privateMsg.sourceID === selectedUserID
                  ? selectedUserWithoutID
                  : this.host.userName;
              privateMsg.headImageAddress =
                privateMsg.sourceID === selectedUserID
                  ? this.users.filter(
                      (user) => user.userID === selectedUserID
                    )[0].headImageAddress
                  : this.host.headImageAddress;
              privateMsg.sourceName =
                privateMsg.userNameWithoutId + "@" + privateMsg.sourceID;
              privateMsg.targetName =
                (privateMsg.targetID === selectedUserID
                  ? selectedUserWithoutID
                  : this.host.userName) +
                "@" +
                privateMsg.targetID;
              this.updatePrivateMsgBefore(privateMsg);
              //向上拉取数据的时候 vue会复用dom，将原本下面的dom平移到新拉取的数据，所以，界面需要重绘
              //先将标志取消，不然调用wholeStyle()没有效果
              this.firstScroll = this.firstScroll.filter((userName) => {
                userName !== this.totalUsers[this.selected];
              });
              //dom必须更新，才能操作dom
              this.$nextTick(() => {
                this.wholeStyle();
              });
            }
          }
        });
    },
    wholeStyle() {
      if (this.firstScroll.indexOf(this.totalUsers[this.selected]) !== -1)
        return;
      this.firstScroll.push(this.totalUsers[this.selected]);
      let array = this.$refs[this.totalUsers[this.selected]];
      if (!array) return;
      for (let i = array.length - 1; i >= 0; i--) {
        let msg = array[i];
        if (msg.className === "oppositeUser") continue;
        let bind = msg.querySelector(".bind");
        let widthLimit = msg.querySelector(".widthLimit");
        let userMessage = widthLimit.querySelector(".userMessage");
        this.style(userMessage, widthLimit, bind);
      }
    },
  },
  beforeUpdate() {
    let tar = this.$refs.showBox;
    if (tar.scrollHeight - tar.clientHeight - tar.scrollTop < 5) {
      this.$nextTick(() => {
        //因为第一次出现滚动条,因为出现滚动条，样式会跑偏，需要全部重新设置样式
        tar.scrollTop = tar.scrollHeight - tar.clientHeight;
        if (tar.scrollTop !== 0) {
          this.wholeStyle();
        }
      });
    }
  },
  updated() {
    let array = this.$refs[this.totalUsers[this.selected]];
    if (!array) return;
    for (let i = array.length - 1; i >= 0; i--) {
      let msg = array[i];
      if (msg.className === "oppositeUser") continue;
      if (msg.querySelector(".bind").style.left) break;

      let bind = msg.querySelector(".bind");
      let widthLimit = msg.querySelector(".widthLimit");
      let userMessage = widthLimit.querySelector(".userMessage");
      this.style(userMessage, widthLimit, bind);
    }
  },
};
</script>

<style>
.download {
  position: absolute;
  margin: 5px 47%;
  width: 6%;
  height: 10%;
}

.point {
  position: absolute;
  top: 10%;
  left: 50%;
  width: 20%;
  height: 20%;
  border-radius: 50%;
  background-color: #787878;
  transform: translateX(-50%);
  transform-origin: 50% 200%;
}

.download .point:nth-child(1) {
  transform: translateX(-50%) rotate(0deg);
  animation: cache 1s 0s infinite;
}

.download .point:nth-child(2) {
  transform: translateX(-50%) rotate(45deg);
  animation: cache 1s 0.125s infinite;
}

.download .point:nth-child(3) {
  transform: translateX(-50%) rotate(90deg);
  animation: cache 1s 0.25s infinite;
}

.download .point:nth-child(4) {
  transform: translateX(-50%) rotate(135deg);
  animation: cache 1s 0.375s infinite;
}

.download .point:nth-child(5) {
  transform: translateX(-50%) rotate(180deg);
  animation: cache 1s 0.5s infinite;
}

.download .point:nth-child(6) {
  transform: translateX(-50%) rotate(225deg);
  animation: cache 1s 0.625s infinite;
}

.download .point:nth-child(7) {
  transform: translateX(-50%) rotate(270deg);
  animation: cache 1s 0.75s infinite;
}

.download .point:nth-child(8) {
  transform: translateX(-50%) rotate(315deg);
  animation: cache 1s 0.875s infinite;
}

@keyframes cache {
  from {
    width: 20%;
    height: 20%;
    background-color: #787878;
  }

  to {
    width: 25%;
    height: 25%;
    background-color: #fff;
  }
}
#showBox {
  position: relative;
  overflow: auto;
  height: 340px;
  border-radius: 20px 20px 0 0;
  background-color: #fff;
}

#showBox .oppositeUser,
#showBox .user {
  position: relative;
}

#showBox .oppositeUser {
  margin-left: 15px;
}

#showBox .user {
  margin-right: 15px;
}

#showBox .oppositeUser .oppositeUserImage,
#showBox .user .userImage {
  width: 30px;
  height: 30px;
  border-radius: 15px;
  vertical-align: text-top;
}

#showBox .user .bind {
  /* 这里转化为行内块元素，这样</br>不会另起一行 */
  display: inline-block;
  position: relative;
  left: calc(100% - 50px);
}

#showBox .oppositeUser .oppositeUserName,
#showBox .user .userName {
  font-size: 10px;
  color: #7f7f7f;
}

#showBox .oppositeUser .oppositeUserName {
  padding-left: 5px;
}

#showBox .user .userName {
  padding-right: 5px;
}

#showBox .oppositeUser .widthLimit,
#showBox .user .widthLimit {
  position: relative;
  width: 70%;
}

#showBox .user .widthLimit {
  left: 30%;
}

#showBox .oppositeUser .oppositeUserMessage,
#showBox .user .userMessage {
  position: relative;
  top: -10px;
  display: inline-block;
  padding: 0 10px 0 10px;
  border-radius: 5px;
  line-height: 25px;
  word-wrap: break-word;
}

#showBox .oppositeUser .oppositeUserMessage {
  background-color: #e5e5e5;
}

#showBox .user .userMessage {
  background-color: #12b7f5;
  color: #fff;
}

#showBox .oppositeUser .oppositeUserMessage {
  /* left: 36px; */
  margin-left: 36px;
}

#showBox .user .userMessage {
  margin-right: 36px;
}

#showBox .oppositeUser .triangle,
#showBox .user .triangle {
  position: absolute;
  top: 0px;
  display: inline-block;
  border-top: 5px solid transparent;
}

#showBox .oppositeUser .triangle {
  left: 25px;
  transform: rotate(35deg);
  border-right: 15px solid #e5e5e5;
}

#showBox .user .triangle {
  right: 25px;
  transform: rotate(145deg);
  border-right: 15px solid #12b7f5;
}

#showBox #defaultShowBox {
  /* 需改动____________________ */
  display: table;
  width: 100%;
  border-collapse: collapse;
}

#showBox h1 {
  /* 需改动____________________ */
  display: block;
  position: absolute;
  left: -45px;
  top: 155px;
  margin-left: 50%;
  font: 30px "Microsoft YaHei";
}

#showBox #defaultShowBox td {
  /* width: 20%; */
  height: 68px;
  border-radius: 20px;
  background-color: #efefef;
  border-spacing: 0;
}
</style>