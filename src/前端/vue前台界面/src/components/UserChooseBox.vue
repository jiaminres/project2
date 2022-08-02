<template>
  <div id="userChooseBox">
    <button
      v-for="(totalUser, index) in totalUsers"
      :key="totalUser"
      class="user"
      :class="{
        selected: selected === index,
        offline: index > agreeUsers.length - 1,
      }"
      @click="clickSelected(index)"
    >
      {{ totalUser.substring(0, totalUser.lastIndexOf("@")) }}
    </button>
    <span @click="changeMode" class="laba" ref="laba"></span>
  </div>
</template>

<script>
import { mapState, mapGetters, mapActions, mapMutations } from "vuex";
export default {
  name: "UserChooseBox",
  data() {
    return {
      public: false,
    };
  },
  computed: {
    ...mapState("group", [
      "agreeUsers",
      "aliveUsers",
      "newMsgUsers",
      "selected",
    ]),
    ...mapGetters("group", ["totalUsers"]),
  },
  methods: {
    ...mapActions("group", ["updateSelected"]),
    ...mapMutations("group", ["updateNewMsgUsers"]),
    clickSelected(index) {
      let userName = this.totalUsers[index];
      let newMsgUsers = this.newMsgUsers.filter((name) => name !== userName);
      this.updateSelected(index);
      this.updateNewMsgUsers(newMsgUsers);
    },
    changeMode() {
      this.public = !this.public;
      let bgc = "";
      let color = "";
      if (this.public) {
        this.$refs.laba.innerText = "";
        bgc = "#0b0c10";
        color = "#fff000";
      } else {
        this.$refs.laba.innerText = "";
        bgc = "#fff";
        color = "#000";
      }
      this.$bus.$emit("choosePublic", this.public);
      this.$bus.$emit("chooseColor", color);
      this.$bus.$emit("chooseBackGroundColor", bgc);
    },
  },
};
</script>

<style>
#userChooseBox {
  position: relative;
  height: 20px;
  border: 1px #ebebeb solid;
  background-color: #fff;
}
#userChooseBox .user:hover {
  border: 1px solid #12b7f5;
  background-color: #fff;
  color: #12b7f5;
}
#userChooseBox .user {
  float: left;
  border: 0;
  padding: 0 5px 0;
  height: 18px;
  border-radius: 15px;
  line-height: 18px;
  color: #cab4b4;
}

#userChooseBox .selected {
  border: 1px solid #12b7f5;
  background-color: #fff;
  color: #12b7f5;
}
#userChooseBox .offline {
  background-color: #000;
  color: red;
}
#userChooseBox .laba {
  position: absolute;
  top: 0%;
  left: 100%;
  border-radius: 10px;
  font-family: "icomoon";
  font-size: 20px;
  color: #fff000;
  background-color: #000;
  cursor: pointer;
}
</style>