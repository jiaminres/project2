<template>
  <div>
    <div class="head">
      <img :src="host.headImageAddress" />
      <h1>{{ host.userName }}</h1>
    </div>
    <div class="content">
      <div class="left-menu">
        <div @click="gClick" class="group-menu">
          <div
            class="menu-head"
            :style="{
              backgroundColor: groupClick ? '#FE8333' : '#fff',
            }"
          >
            <img
              :style="{ display: groupClick ? 'inline-block' : 'none' }"
              class="menu-head-img-open"
              src="image/互动.png"
            />
            <img
              :style="{ display: !groupClick ? 'inline-block' : 'none' }"
              class="menu-head-img-close"
              src="image/互动白底.png"
            />
            <p class="menu-title">群组</p>
            <img
              :style="{ display: groupClick ? 'inline-block' : 'none' }"
              class="menu-arrow-open"
              src="image/下拉箭头.png"
            />
            <img
              :style="{ display: !groupClick ? 'inline-block' : 'none' }"
              class="menu-arrow-close"
              src="image/向右箭头.png"
            />
          </div>
          <ul
            :style="{ display: groupClick ? 'block' : 'none' }"
            class="menu-list"
          >
            <li>
              <!-- <a href="#">我加入的群组</a> -->
              <router-link to="/group/block">我加入的群组</router-link>
            </li>
          </ul>
        </div>
        <div @click="sClick" class="setting-menu">
          <div
            class="menu-head"
            :style="{
              backgroundColor: settingClick ? '#FE8333' : '#fff',
            }"
          >
            <img
              :style="{ display: settingClick ? 'inline-block' : 'none' }"
              class="menu-head-img-open"
              src="image/设置.png"
            />
            <img
              :style="{ display: !settingClick ? 'inline-block' : 'none' }"
              class="menu-head-img-close"
              src="image/设置白底.png"
            />
            <p class="menu-title">设置中心</p>
            <img
              :style="{ display: settingClick ? 'inline-block' : 'none' }"
              class="menu-arrow-open"
              src="image/下拉箭头.png"
            />
            <img
              :style="{ display: !settingClick ? 'inline-block' : 'none' }"
              class="menu-arrow-close"
              src="image/向右箭头.png"
            />
          </div>
          <ul
            :style="{ display: settingClick ? 'block' : 'none' }"
            class="menu-list"
          >
            <li>
              <!-- 跳转到setting页面 -->
              <router-link to="/group/setting">个人信息</router-link>
            </li>
            <li>
              <!-- 退出重新登录，同时要删除本地缓存的所有信息，并跳转到login页面 -->
              <a @click="reLogin" href="#">退出</a>
            </li>
          </ul>
        </div>
      </div>
      <!-- <Block> <Edit> <Setting>-->
      <keep-alive>
        <router-view></router-view>
      </keep-alive>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import vue from "../../main";
export default {
  name: "Group",
  data() {
    return {
      groupClick: false,
      settingClick: false,
    };
  },
  computed: {
    ...mapState("group", ["host"]),
  },
  methods: {
    ...mapActions("group", ["disconnect", "destroyMsg", "destroyHost"]),
    gClick() {
      this.groupClick = !this.groupClick;
    },
    sClick() {
      this.settingClick = !this.settingClick;
    },
    reLogin() {
      // 删除本地缓存
      localStorage.clear();
      //通知服务器删除该登录用户
      this.offLine();
    },
    offLine() {
      //通知服务器删除该登录用户
      this.$axios
        .get("http://localhost:8080/offLine", {
          params: { userID: this.host.userID },
        })
        .then((res) => {
          //切到login页面，因为退出就必须重新登录 所以必须用replace
          this.$router.replace({
            name: "login",
          });
        })
        .catch((res) => {
          alert("网络错误" + "\n" + res.message);
        });
    },
    beforeUpdate() {
      console.log("GroupBeforeUpdate");
    },
    updated() {
      console.log("GroupUpdated");
    },
  },

  // Group组件除了转向Native外，其他均要格式化存在的聊天数据，并且向服务器声明退出,
  // 还要清楚本地缓存，因为路由守卫是根据是否存在本地记录，判断该用户是否登录的
  // 最好将host也删掉，与beforeRouteEnter联合，再对是否存在userID进行检测

  // 不知何种缘故，即便程序所有页面跳转均是使用replace，但是仍旧会积压group，并且当前后
  // 跳转时,会在一些页面等同于浏览器的初次申请，所以会产生如下执行顺序
  // 先创建子组件group,因此会执行到beforeRouteEnter,此时vue实例还没初始化，因此报错
  // 然后创建父组件,执行mounted方法，根据缓存跳转到group,再次进入beforeRouteEnter方法
  //此时 vue实例已经存在
  beforeRouteLeave(to, from, next) {
    if (to.name === "login") {
      //登记退出
      Object.getPrototypeOf(vue).$axios.get("http://localhost:8080/offLine", {
        params: { userID: vue.$store.state.group.host.userID },
      });
      //销毁Setting,防止页面渲染报错
      Object.getPrototypeOf(vue).$bus.$emit("destroySetting");
      vue.$store.state.group.host = {};
      vue.$store.dispatch("group/destroyMsg");
      localStorage.clear();
    }
    next();
  },
  beforeRouteEnter(to, from, next) {
    //!vue出现在vue程序完全从头开始构建组件时，此时还没vue实例
    //此时直接返回，程序会自动构建父节点，然后App.vue根据条件自动
    //跳转到login或group
    if (!vue) {
      return;
    }
    if (!vue.$store.state.group.host.userID) {
      alert("你还没有登录");
      //重新跳转到登录页
      vue.$router.replace({
        name: "login",
      });
      return;
    }
    next();
  },
};
</script>

<style>
body {
  background-color: rgba(245, 245, 245);
}

hr {
  border: 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.3);
}

.head {
  width: 100%;
  height: 280px;
  background-color: #7cc2fd;
  text-align: center;
  margin-bottom: 20px;
}

.head img {
  width: 125px;
  height: 125px;
  border-radius: 100%;
  margin-top: 50px;
  border: 5px solid rgba(255, 255, 255, 0.3);
}

.content {
  margin: auto;
  width: 1500px;
  overflow: hidden;
}

.left-menu {
  float: left;
  margin-left: 100px;
  width: 220px;
  text-align: center;
  background-color: #fff;
}

.menu-head {
  background-color: #fff;
  padding: 20px 0;
  color: #000;
  font-size: 16px;
  cursor: pointer;
}

.menu-head-click {
  background-color: rgb(254, 131, 51);
  padding: 20px 0;
  color: #fff;
  font-size: 16px;
}

.menu-head-img-open {
  vertical-align: middle;
}

.menu-head-img-close {
  vertical-align: middle;
}

.menu-title {
  display: inline-block;
  vertical-align: middle;
}

.menu-arrow-open {
  vertical-align: middle;
}

.menu-arrow-close {
  display: inline-block;
  vertical-align: middle;
}

.menu-list li {
  text-align: center;
  line-height: 60px;
}
.menu-list li a {
  color: #a5a5a5;
  font-size: 16px;
}
</style>