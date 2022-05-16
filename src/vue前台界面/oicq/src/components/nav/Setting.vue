<template>
  <div class="setting">
    <ul>
      <li>
        <img id="head" :src="host.headImageAddress" />
        <div id="persion">
          <span id="name">{{ host.userName }}</span>
          <br />
          <span id="userID">{{ host.userID }}</span>
        </div>
        <!-- 三根横线 -->
        <i @click="showUpdateList = !showUpdateList" class="rectangle"></i>
        <i @click="showUpdateList = !showUpdateList" class="rectangle"></i>
        <i @click="showUpdateList = !showUpdateList" class="rectangle"></i>
        <ul v-show="showUpdateList" id="updateList">
          <!-- 更改头像和更改昵称互斥 -->

          <li
            @click="
              showUpdateHeadImage = true;
              showUpdateUserName = false;
            "
          >
            更改头像
          </li>
          <li
            @click="
              showUpdateUserName = true;
              showUpdateHeadImage = false;
            "
          >
            更改昵称
          </li>
        </ul>
        <div v-show="showUpdateHeadImage" id="updateHeadImage">
          <div class="tip">选择一张图片</div>
          <div class="upload">
            <!-- 上传新头像 -->
            <input type="file" ref="photo" />
            <button @click="uploadHeadImage" type="button">上传</button>
          </div>
          <div @click="showUpdateHeadImage = false" class="close">
            <i class="leftLine"></i>
            <i class="rightLine"></i>
          </div>
        </div>
        <div v-show="showUpdateUserName" id="updateUserName">
          <div class="tip">修改昵称</div>
          <div class="upload">
            编辑昵称 <input type="text" placeholder="昵称" ref="userName" />
            <button @click="uploadUserName" type="button">更新</button>
          </div>
          <div @click="showUpdateUserName = false" class="close">
            <i class="leftLine"></i>
            <i class="rightLine"></i>
          </div>
        </div>
      </li>
      <li>
        <span></span>
        <span :class="{ noMsg: !host.detail.sex }">{{
          host.detail.sex ? host.detail.sex : "性别"
        }}</span
        ><i class="line"></i>
        <span :class="{ noMsg: !host.detail.age }">{{
          host.detail.age ? host.detail.age : "年龄"
        }}</span
        ><i class="line"></i>
        <span :class="{ noMsg: !host.detail.birthday }">{{
          host.detail.birthday ? getBirthday(host.detail.birthday) : "出生日期"
        }}</span
        ><i class="line"></i>
        <span :class="{ noMsg: !host.detail.location }">{{
          host.detail.location ? host.detail.location : "居住地址"
        }}</span>
        <i @click="skipToEdit" class="triangle"></i>
      </li>
      <li>
        <span></span>
        <span :class="{ noMsg: !host.detail.persionalSign }">{{
          host.detail.persionalSign ? host.detail.persionalSign : "个性签名"
        }}</span>
        <i @click="skipToEdit" class="triangle"></i>
      </li>
      <li>
        <span>xx</span>
        <i @click="skipToEdit" class="triangle"></i>
      </li>
      <li>
        <span>xx</span>
        <i @click="skipToEdit" class="triangle"></i>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapState, mapMutations } from "vuex";
export default {
  name: "Setting",
  data() {
    return {
      showUpdateList: false,
      showUpdateHeadImage: false,
      showUpdateUserName: false,
    };
  },
  computed: {
    ...mapState("group", ["host"]),
  },
  methods: {
    ...mapMutations("group", ["updateHeadImage", "updateUserName"]),
    //点击>跳转到edit的页面
    skipToEdit() {
      this.$router.replace({
        name: "edit",
      });
    },
    getBirthday(birthday) {
      console.log(birthday);
      let date = new Date(birthday);
      return (
        date.getFullYear() +
        "年" +
        (date.getMonth() + 1) +
        "月" +
        (date.getDay() + 1) +
        "日"
      );
    },
    uploadHeadImage() {
      let formData = new FormData();
      if (!this.$refs.photo.files[0]) {
        alert("没有选择要上传得头像图片");
        return;
      }
      let file = this.$refs.photo.files[0];
      if (file.size > 1024 * 1024) {
        alert("上传的图片大于1M");
        return;
      }
      //获取后缀名
      let index = file.name.lastIndexOf(".");
      let suffix;
      //没有后缀名，就不管了
      if (index == -1) {
        suffix = "";
      } else {
        suffix = file.name.substring(index + 1);
      }
      //文件名 userID + . + 后缀名
      let fileName = this.host.userID + (suffix === "" ? "" : "." + suffix);
      formData.append(fileName, file);
      this.$axios
        .post("http://localhost/upload/headImage", formData)
        .then((res) => {
          //上传失败
          if (res.status !== 200) {
            alert("上传头像失败");
          } else {
            //上传成功
            //再次发送请求，更新数据库信息
            this.$axios
              .get("http://localhost:8080/update/headImage", {
                params: {
                  userID: this.host.userID,
                  headImageAddress: "image/" + fileName,
                },
              })
              .then((res) => {
                if (res === false) {
                  alert("更新头像失败");
                } else {
                  //插入数据库成功
                  alert("更新头像成功");
                  //更新host信息
                  this.updateHeadImage("image/" + fileName);
                  //将窗口关闭
                  this.showUpdateList = false;
                  this.showUpdateHeadImage = false;
                  this.showUpdateUserName = false;
                }
              })
              .catch((res) => {
                alert("网络错误" + res.message);
              });
          }
        })
        .catch((res) => {
          alert("网络错误" + res.message);
        });
    },
    uploadUserName() {
      let userName = this.$refs.userName.value.trim();
      if (userName === "") {
        alert("昵称为空");
        return;
      }
      this.$axios
        .get("http://localhost:8080/update/userName", {
          params: {
            userID: this.host.userID,
            userName,
          },
        })
        .then((res) => {
          if (res.data === false) {
            alert("更新昵称失败");
          } else {
            alert("更新昵称成功");
            //更新host信息
            this.updateUserName(userName);
            //关闭所有窗口
            this.showUpdateList = false;
            this.showUpdateHeadImage = false;
            this.showUpdateUserName = false;
          }
        })
        .catch((res) => {
          alert("网络错误" + res.message);
        });
    },
  },
  mounted() {
    //绑定一个销毁Setting的方法,为的是当程序跳到登录面,因为会删除vuex的数据,页面渲染找不到数据会报错
    this.$bus.$on("destroySetting", () => {
      this.$destroy();
    });
  },
  beforeDestroy() {
    console.log("setting要销毁了");
    this.$bus.$off("destroySetting");
  },
};
</script>

<style scoped >
li {
  position: relative;
  margin-bottom: 40px;
}
.noMsg {
  font-style: italic;
  font-weight: 200;
}
.setting {
  float: left;
  margin-left: 100px;
  width: 980px;
  height: 500px;
  background-color: #fff;
  overflow: hidden;
}

#persion {
  position: relative;
  display: inline-block;
  top: 25px;
  margin-left: 5px;
}

#persion #name {
  display: inline-block;
  font: 350 26px "Microsoft Yahei";
  margin-bottom: 10px;
}

#persion #userID {
  font-size: 18px;
}

#head {
  width: 150px;
  height: 150px;
  margin: 5px;
  margin-left: 30px;
  border-radius: 50%;
  vertical-align: middle;
}

.triangle {
  position: absolute;
  top: 50%;
  right: 30px;
  width: 10px;
  height: 10px;
  border-top: 2px solid #cfcfcf;
  border-right: 2px solid #cfcfcf;
  transform: rotate(45deg);
}

.line {
  width: 0;
  height: 25px;
  border-right: 1px solid #cfcfcf;
}

ul li:nth-child(2) span:nth-child(n + 2),
ul li:nth-child(3) span:nth-child(n + 2),
ul li:nth-child(4) span:nth-child(n + 2),
ul li:nth-child(5) span:nth-child(n + 2) {
  padding: 5%;
}

ul li:nth-child(2) span:nth-child(1),
ul li:nth-child(3) span:nth-child(1),
ul li:nth-child(4) span:nth-child(1),
ul li:nth-child(5) span:nth-child(1) {
  font-family: "icomoon";
  margin-left: 30px;
  color: #6e6e6e;
}
.rectangle {
  position: absolute;
  right: 30px;
  top: 8px;
  width: 27px;
  height: 2px;
  background-color: #cfcfcf;
  cursor: pointer;
}

.rectangle:nth-of-type(2) {
  top: 18px;
}

.rectangle:nth-of-type(3) {
  top: 28px;
}

#updateList {
  position: absolute;
  right: 60px;
  top: 25px;
  width: 80px;
  height: 90px;
  border: 1px solid #cfcfcf;
  border-radius: 13px;
  color: #909090;
  text-decoration: underline;
}

#updateList li {
  text-align: center;
  line-height: 45px;
  margin: 0;
  cursor: pointer;
}

#updateHeadImage,
#updateUserName {
  z-index: 1;
  position: absolute;
  border: 1px solid #cfcfcf;
  left: 50%;
  transform: translateX(-50%);
  height: 150px;
  width: 200px;
  background-color: #f6f6f6;
  overflow: hidden;
}

#updateHeadImage {
  height: 150px;
}

#updateUserName {
  height: 100px;
}

#updateUserName .upload {
  text-align: center;
}

.tip {
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #12b7f5;
  color: #fff;
}

[type="file"] {
  background-color: #fff;
}

[type="button"] {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
}

[type="text"] {
  width: 50px;
}

.close {
  position: absolute;
  right: 3px;
  top: 4px;
  width: 20px;
  height: 20px;
  border-radius: 10px;
  background-color: rgba(207, 207, 207, 0.7);
  overflow: hidden;
  cursor: pointer;
}

.leftLine {
  position: absolute;
  left: 9px;
  height: 100%;
  width: 2px;
  background-color: #b3b3b3;
  transform: rotate(-45deg);
}

.rightLine {
  position: absolute;
  left: 9px;
  height: 100%;
  width: 2px;
  background-color: #b3b3b3;
  transform: rotate(45deg);
}
</style>