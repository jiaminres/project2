<template>
  <div>
    <!-- 容纳Login 或 Group 或 Native -->
    <!-- <Group /> -->
    <keep-alive>
      <router-view></router-view>
    </keep-alive>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import Group from "./components/nav/Group.vue";
export default {
  name: "App",
  data() {
    return {};
  },
  methods: {
    ...mapActions("group", ["initHost", "destroyHost"]),
    //先尝试跳转到group页面
    toGroup() {
      //渲染组件完毕后，首先查看本地是否已经有该用户的先前的登录信息
      if (localStorage.getItem("login")) {
        //但此时可能存在本地存储的账号密码是过期的信息
        let host = JSON.parse(localStorage.getItem("host"));
        //验证该用户账号和密码的正确性
        this.$axios
          .get("http://localhost:8080/login", {
            params: { userID: host.userID, password: host.password },
          })
          .then((res) => {
            //用户账号密码过期
            if (!res.data || !res.data.userID) {
              //删除本地过期信息
              localStorage.clear();
              alert("验证过期,请重新登录");
              //跳转到登录页
              this.$router.replace({
                name: "login",
              });
            }
            //该用户已经处于上线状态
            else if (res.data.userID === "0") {
              alert("已登录，请勿重复登录");
              //跳转到登录页
              this.$router.replace({
                name: "login",
              });
            }
            //直接登录
            else {
              //初始化用户信息
              this.initHost(res.data);
              //跳转进群组页面
              this.$router.replace({
                name: "group",
              });
            }
          })
          .catch((res) => {
            alert("网络异常\n" + res.message);
            localStorage.clear();
            this.$router.replace({
              //发生异常，也跳转到登录页面
              name: "login",
            });
          });
      } else {
        //没有上次缓存信息
        this.$router.replace({
          name: "login",
        });
      }
    },
  },
  computed: {
    ...mapState("group", ["host"]),
  },

  mounted() {
    console.log("mounted");
    window.onbeforeunload = () => {
      console.log("before");
      //再通知服务器用户下线
      // ！！发现在onbeforeunload这个回调里,在google里,axios在关闭浏览器时无法实现，故只能委托给websocket服务器解决
      if (this.host.userID) {
        this.$axios.get("http://localhost:8080/offLine", {
          params: { userID: this.host.userID },
        });
      }
      //用户关闭浏览器应尝试关闭已有的websocket
      this.$store.dispatch("group/disconnect");
      //容易漏掉对host的删除，因为这里可能是先暂时跳转到其他页面，之后又跳转回来，但此时已经
      //向服务器声明退出了，所以通过删除host作为native和group组件的检测标值
      this.destroyHost();
    };
    //选择登录页 or 群组页面
    this.toGroup();
  },
};
</script>


<style>
@font-face {
  font-family: "icomoon";
  src: url("/font/fonts/icomoon.eot?f88m88");
  src: url("/font/fonts/icomoon.eot?f88m88#iefix") format("embedded-opentype"),
    url("/font/fonts/icomoon.ttf?f88m88") format("truetype"),
    url("/font/fonts/icomoon.woff?f88m88") format("woff"),
    url("/font/fonts/icomoon.svg?f88m88#icomoon") format("svg");
  font-weight: normal;
  font-style: normal;
  font-display: block;
}

* {
  margin: 0px;
  padding: 0px;
  font-size: 14px;
}

li {
  list-style: none;
}

div {
  box-sizing: border-box;
}

i {
  font-style: normal;
}

a {
  text-decoration: none;
}

table {
  border-width: 0px;
}
</style>
