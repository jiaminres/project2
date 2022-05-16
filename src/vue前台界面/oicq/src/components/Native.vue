<template>
  <div>
    <Nav />
    <Body />
  </div>
</template>

<script>
import { mapActions, mapState, mapMutations } from "vuex";
import Nav from "./Nav.vue";
import Body from "./Body.vue";
import vue from "../main";
export default {
  name: "Native",
  components: { Nav, Body },
  props: ["groupID", "leaderID", "groupName", "headImageAddress"],
  computed: {
    ...mapState("group", ["host", "room", "users"]),
  },
  methods: {
    ...mapActions("group", [
      "initUsers",
      "initRoom",
      "connect",
      "disconnect",
      "destroyData",
      "destroyMsg",
    ]),
    ...mapMutations("group", ["updatePrivateMsg"]),
  },
  activated() {
    // 如果与上次访问的群组不相同，则销毁公发数据,私发数据,并向数据库申请私发数据
    let needRequest = false;
    if (this.room.groupID !== this.groupID) {
      this.destroyMsg();
      needRequest = true;
    }
    //根据群组ID 以及 自身ID 获取该群组内的用户列表
    this.$axios
      .get("http://localhost:8080/list/user", {
        params: {
          userID: this.host.userID,
          groupID: this.groupID,
        },
      })
      .then((res) => {
        if (!res.data) {
          alert("获取群组列表失败");
          this.$router.replace({
            name: "group",
          });
        } else {
          //获取用户列表成功
          this.destroyData();
          this.initRoom({
            groupID: this.groupID,
            leaderID: this.leaderID,
            groupName: this.groupName,
            headImageAddress: this.headImageAddress,
          });
          this.initUsers(res.data);
          //获取用户列表后再依次获取与各个用户的聊天数据
          if (needRequest) {
            for (let user of this.users) {
              this.$axios
                .get("http://localhost:8080/list/record", {
                  params: {
                    groupID: this.room.groupID,
                    userID: this.host.userID,
                    oppositeID: user.userID,
                    time: Date.now(),
                  },
                })
                .then((res) => {
                  if (!res.data || !res.data.length) {
                  } else {
                    for (let i = res.data.length - 1; i >= 0; i--) {
                      let privateMsg = res.data[i];
                      privateMsg.userNameWithoutId =
                        privateMsg.sourceID == user.userID
                          ? user.userName
                          : this.host.userName;
                      privateMsg.headImageAddress =
                        privateMsg.sourceID == user.userID
                          ? user.headImageAddress
                          : this.host.headImageAddress;
                      privateMsg.sourceName =
                        privateMsg.userNameWithoutId +
                        "@" +
                        privateMsg.sourceID;
                      privateMsg.targetName =
                        (privateMsg.targetID == user.userID
                          ? user.userName
                          : this.host.userName) +
                        "@" +
                        privateMsg.targetID;

                      this.updatePrivateMsg(privateMsg);
                    }
                  }
                });
            }
          }
          //必须得把connect放在下面，可能连接上，广播消息，然后接收消息的时候会先于destroyData方法的调用
          this.connect();
        }
      })
      .catch((res) => {
        this.error = "请求发生异常，请检查网络环境";
        this.$router.replace({
          name: "group",
        });
      });
  },
  deactivated() {
    //退出时，先删除用户关联
    this.destroyData();
    this.disconnect();
  },
  //当用户跳转去其他网站，又跳转回来时，需要重新登录
  beforeRouteEnter(to, from, next) {
    // !vue出现在vue程序完全从头开始构建组件时，此时还没vue实例
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
    //继续判断是否是新注册用户，没有昵称，跳转到setting页面
    if (!vue.$store.state.group.host.userName) {
      alert("你还没有设置昵称");
      vue.$router.replace({
        name: "setting",
      });
      return;
    }
    next();
  },
};
</script>

<style>
</style>