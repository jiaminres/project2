<template>
  <div>
    <form action="/login" method="get" id="login">
      <input
        v-model.trim="userID"
        type="text"
        name="userID"
        class="key"
        placeholder=" 账号"
      />
      <br />
      <input
        v-model.trim="password"
        type="password"
        name="password"
        class="key"
        placeholder=" 密码"
      />
      <br />
      <img @click="changeImg" id="verifyCodeImg" :src="verifyCodeImg" />
      <input
        v-model.trim="verifyCodeStr"
        id="verifyCodeStr"
        type="text"
        placeholder="请输入验证码"
      />
      <br />
      <span style="color: red">{{ error }}</span>
      <transition-group enter-active-class="animate__swing">
        <button
          v-show="isShow"
          @click="login"
          class="submit"
          :class="{ animate__animated: isShow }"
          type="button"
          key="1"
        >
          登录
        </button>
        <button
          v-show="!isShow"
          @click="register"
          class="submit"
          :class="{ animate__animated: !isShow }"
          type="button"
          key="2"
        >
          注册
        </button>
      </transition-group>
      <a
        v-show="isShow"
        @click.prevent="isShow = !isShow"
        href="http://www.baidu.com"
        class="tip"
        >没有账号?注册一个吧</a
      >
      <a v-show="!isShow" @click.prevent="isShow = !isShow" href="#" class="tip"
        >已有账号？</a
      >
    </form>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import "animate.css";
export default {
  name: "Login",
  data() {
    return {
      userID: "",
      password: "",
      verifyCodeStr: "",
      verifyCodeImg: "http://localhost:8080/login/verifyCodeImg",
      error: "",
      //选择注册or登录
      isShow: true,
    };
  },
  methods: {
    ...mapActions("group", ["initHost"]),
    changeImg() {
      //重新申请验证码
      //防止浏览器缓存不刷新
      this.verifyCodeImg =
        "http://localhost:8080/login/verifyCodeImg?t=" + Date.now();
    },
    checkComplete() {
      //不管是登录还是注册 首先判断 账号 密码 验证码 是否填写齐全
      if (!this.userID || !this.password) {
        alert("请输入账号密码");
        return false;
      } else if (!this.verifyCodeStr) {
        alert("请输入验证码");
        return false;
      }
      return true;
    },
    login() {
      if (!this.checkComplete()) return;
      //第二步 发送网络请求 确定验证码正确
      this.$axios
        .get("http://localhost:8080/login/verifyCodeStr", {
          params: {
            verifyCodeStr: this.verifyCodeStr,
          },
        })
        .then((res) => {
          //res.data返回true | false
          if (!res.data) {
            //验证码错误
            this.error = "验证码输入错误,请重新输入";
            //更改验证码图片
            this.changeImg();
          } else {
            //验证码通过,继续发送登录请求
            this.$axios
              .get("http://localhost:8080/login", {
                params: {
                  userID: this.userID,
                  password: this.password,
                },
              })
              .then((res) => {
                //账号密码错误
                if (!res.data || !res.data.userID) {
                  this.error = "输入的密码和账号错误,请重新输入";
                  //更改验证码图片
                  this.changeImg();
                } else {
                  //检查该用户是否已经登录
                  if (res.data.userID === "0") {
                    this.error = "该用户已经登录,请勿重复登录";
                    return;
                  }
                  //验证成功
                  this.initHost(res.data);
                  //跳入群组页面
                  //群组页面应该配置前置路由，防止无账号密码登录,故在本地缓存记录登录信息
                  localStorage.setItem("login", "true");
                  localStorage.setItem("host", JSON.stringify(res.data));
                  this.$router.replace({
                    name: "group",
                  });
                }
              })
              //验证账号密码发生错误
              .catch((res) => {
                this.error = "请求发生异常,请检查网络环境";
              });
          }
        })
        //验证验证码发生错误
        .catch((res) => {
          this.error = "请求发生异常，请检查网络环境";
        });
    },
    register() {
      //检查输入信息的完备性
      if (!this.checkComplete()) return;
      //检查userID和password,要求账号>=7位, <= 15，只能是数字；密码>= 6 <= 15
      if (this.userID.length < 7) {
        alert("账号必须大于或等于7位");
        this.changeImg();
        return;
      } else if (this.userID.length > 15) {
        alert("账号必须小于或等于15位");
        this.changeImg();
        return;
      } else if (
        this.userID.split("").filter((ch) => !(ch >= "0" && ch <= "9"))
          .length !== 0
      ) {
        alert("账号中含有非数字字符");
        this.changeImg();
        return;
      } else if (this.password.length < 6) {
        alert("密码必须大于或等于6位");
        this.changeImg();
        return;
      } else if (this.password.length > 15) {
        alert("密码必须小于或等于15位");
        this.changeImg();
        return;
      }
      //第三步 发送ajax请求，验证验证码正确性
      this.$axios
        .get("http://localhost:8080/login/verifyCodeStr", {
          params: {
            verifyCodeStr: this.verifyCodeStr,
          },
        })
        .then((res) => {
          //res.data返回true | false
          if (!res.data) {
            //验证码错误
            this.error = "验证码输入错误,请重新输入";
            //更改验证码图片
            this.changeImg();
          } else {
            //验证码通过,继续发送注册请求
            this.$axios
              .get("http://localhost:8080/register/user", {
                params: {
                  userID: this.userID,
                  password: this.password,
                },
              })
              .then((res) => {
                //注册失败，账号重复
                if (res.data !== true) {
                  this.error = "输入的账号已注册,请重新输入";
                  //更改验证码图片
                  this.changeImg();
                } else {
                  //注册成功
                  //创建host对象，该对象除了账号之外，其余属性全部为null
                  //因此在之后 进入聊天室界面时,应该设置路由，防止用户无昵称进入聊天室
                  let host = {
                    userID: this.userID,
                    password: this.password,
                    userName: null,
                    headImageAddress: null,
                    detail: {
                      userID: this.userID,
                      sex: null,
                      age: null,
                      birthday: null,
                      location: null,
                      persionalSign: null,
                    },
                  };
                  //初始化host对象
                  this.initHost(host);
                  //跳入群组页面
                  //群组页面应该配置前置路由，防止无账号密码登录,故在本地缓存记录登录信息
                  console.log(res.data);
                  localStorage.setItem("login", "true");
                  localStorage.setItem("host", JSON.stringify(res.data));
                  this.$router.replace({
                    name: "group",
                  });
                  console.log("register后");
                }
              })
              //注册发生错误
              .catch((res) => {
                this.error = "请求发生异常,请检查网络环境";
              });
          }
        })
        //注册发生错误
        .catch((res) => {
          this.error = "请求发生异常，请检查网络环境";
        });
    },
  },
};
</script>

<style scoped>
@font-face {
  font-family: "icomoon";
  src: url("/font/fonts/icomoon.eot?h5j9ar");
  src: url("/font/fonts/icomoon.eot?h5j9ar#iefix") format("embedded-opentype"),
    url("/font/fonts/icomoon.ttf?h5j9ar") format("truetype"),
    url("/font/fonts/icomoon.woff?h5j9ar") format("woff"),
    url("/font/fonts/icomoon.svg?h5j9ar#icomoon") format("svg");
  font-weight: normal;
  font-style: normal;
  font-display: block;
}
body {
  background-color: #efefef;
}

input {
  border: 0;
  border-bottom: 1px solid #e5e5e5;
  outline: none;
}

#login {
  position: relative;
  box-sizing: border-box;
  width: 425px;
  height: 325px;
  margin: 100px auto;
  text-align: center;
  padding-top: 100px;
  background-color: #fff;
}

#login .key {
  height: 30px;
  width: 260px;
  margin-bottom: 10px;
  font-family: icomoon;
  font-size: 12px;
  font-weight: 700;
  line-height: 20px;
}

#login .submit {
  height: 30px;
  width: 260px;
  border: 0;
  border-radius: 2px;
  background-color: #12b7f5;
  line-height: 30px;
  text-align: center;
  color: #fff;
}
#verifyCodeImg {
  width: 160px;
  height: 50px;
  background-color: pink;
  vertical-align: middle;
  margin-bottom: 10px;
}
#verifyCodeStr {
  margin-left: 10px;
  width: 85px;
  height: 15px;
}
.tip {
  position: absolute;
  right: 15px;
  bottom: 20px;
  color: #cfcfcf;
  text-decoration-line: underline;
  cursor: pointer;
}
.disappear {
  display: none;
}
</style>