<template>
  <div id="user">
    <ul id="userList">
      <li
        v-for="user in users"
        :key="user.userID"
        :class="{ newMsg: isNewMsgUser(full(user)) }"
        @click="sendApply($event, wrap(full(user)), full(user))"
      >
        <img :src="user.headImageAddress" />
        <em>{{ user.userName }}</em>
        <div class="mask">
          <i
            class="isAlive"
            :class="{
              alive: isAlive(full(user)),
              offline: !isAlive(full(user)),
            }"
            >
          </i>
        </div>
        <i
          class="isApply"
          :data-user="full(user)"
          v-show="isApply(full(user))"
          @click="sendAgree(wrap2(full(user)))"
        >
          
        </i>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
export default {
  name: "User",
  data() {
    return {
      color: "red",
    };
  },
  computed: {
    ...mapState("group", [
      "users",
      "aliveUsers",
      "applyUsers",
      "agreeUsers",
      "newMsgUsers",
    ]),
    ...mapGetters("group", ["fullName"]),
  },
  methods: {
    full(user) {
      return user.userName + "@" + user.userID;
    },
    isNewMsgUser(userName) {
      return this.newMsgUsers.indexOf(userName) !== -1;
    },
    isAlive(userName) {
      return this.aliveUsers.indexOf(userName) !== -1;
    },
    isApply(userName) {
      return this.applyUsers.indexOf(userName) !== -1;
    },
    wrap(target) {
      if (this.agreeUsers.indexOf(target) !== -1) return;
      if (this.aliveUsers.indexOf(target) === -1) return;
      let obj = {
        type: "/complex/applyConnect",
        text:
          "{" +
          '"sourceUserName": ' +
          '"' +
          this.fullName +
          '", ' +
          '"targetUserName": ' +
          '"' +
          target +
          '"' +
          "}",
      };
      return obj;
    },
    wrap2(target) {
      let obj = {
        type: "/complex/agreeConnect",
        text:
          "{" +
          '"sourceUserName": ' +
          '"' +
          this.fullName +
          '", ' +
          '"targetUserName": ' +
          '"' +
          target +
          '"' +
          "}",
      };
      return obj;
    },
    sendApply(event, obj, target) {
      if (event.target.className === "isApply") return;
      //已经建立连接
      if (this.agreeUsers.indexOf(target) !== -1) return;
      //对方不在线
      if (this.aliveUsers.indexOf(target) === -1) return;
      this.$store.dispatch("group/sendApply", obj);
    },
    sendAgree(obj) {
      this.$store.dispatch("group/sendAgree", obj);
    },
  },
};
</script>

<style scoped>
@keyframes newMsg {
  from {
    background-color: red;
    opacity: 0.8;
  }
  to {
    background-color: red;
    opacity: 0;
  }
}

.newMsg::before {
  content: "";
  position: absolute;
  width: 100%;
  height: 100%;
  animation: newMsg 1.3s infinite;
}
.alive {
  color: green;
}
.offline {
  color: red;
}

#user ul li {
  position: relative;
  height: 98px;
}

#user ul li img {
  width: 16%;
  height: 80%;
  padding: 3% 0 0 6%;
  border-radius: 50%;
  vertical-align: middle;
}

#user ul li .mask {
  display: none;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
}

#user ul li:hover .mask {
  display: block;
}

#user ul li .mask .isAlive {
  position: absolute;
  font-family: "icomoon";
  font-size: 30px;
  /* color: red; */
  text-align: center;
  line-height: 98px;
  padding-left: 50%;
  left: -15px;
  cursor: pointer;
}

#user ul li .isApply {
  /* display: none; */
  position: absolute;
  left: 100%;
  top: 50%;
  margin-top: -15px;
  font-family: "icomoon";
  font-size: 30px;
  color: #12b7f5;
  cursor: pointer;
}

#user ul li em {
  padding-left: 1%;
  font-style: normal;
  /* vertical-align: middle; */
}
</style>