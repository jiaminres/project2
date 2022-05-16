<template>
  <div id="userChooseBox">
    <button
      v-for="(agreeUser, index) in agreeUsers"
      :key="agreeUser.sourceUserName"
      class="user"
      :class="{ selected: isSelected[index], offline: isOffline(index) }"
      @click="clickSelected(index)"
    >
      {{
        agreeUser.sourceUserName.substring(
          0,
          obj.sourceUserName.lastIndexOf("@")
        )
      }}
    </button>
    <span @click="changeMode" class="laba" ref="laba"></span>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "UserChooseBox",
  data() {
    return {
      isSelected: [],
      public: false,
    };
  },
  computed: {
    ...mapState("group", ["agreeUsers", "aliveUsers"]),
  },
  methods: {
    clickSelected(index) {
      for (let i = 0; i < this.isSelected.length; i++) {
        this.isSelected[i] = false;
      }
      this.isSelected[index] = true;
      this.$bus.$emit("chooseShowBox", index);
      this.$bus.$emit("chooseBtn", index);
    },
    isOffline(index) {
      let cur = agreeUsers[index].sourceUserName;
      return this.aliveUsers.filter((name) => name === cur).length === 1;
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

  watch: {
    agreeUsers(array) {
      for (let i = this.isSelected.length; i < array.length; i++) {
        this.isSelected.push(false);
      }
      if (this.isSelected.length === 1) {
        this.isSelected[0] = true;
        this.$bus.$emit("chooseShowBox", 0);
        this.$bus.$emit("chooseBtn", 0);
      }
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