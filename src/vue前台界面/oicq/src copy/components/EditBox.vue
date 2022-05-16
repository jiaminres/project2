<template>
  <div id="editBox">
    <textarea v-model="msg" :style="{ color, backgroundColor }" />
    <button id="close" :style="{ color }">关闭</button>
    <button @click="send" id="send" :disabled="isDisabled()" :style="color">
      发送
    </button>
  </div>
</template>

<script>
import { mapActions, mapState, mapGetters } from "vuex";
export default {
  name: "EditBox",
  data() {
    return {
      selected: -1,
      msg: "",
      public: false,
      color: "#000",
      backgroundColor: "#fff",
    };
  },
  computed: {
    ...mapState("group", ["agreeUsers", "host"]),
    ...mapGetters("group", ["fullName"]),
  },
  methods: {
    ...mapActions("group", ["sendPrivate", "sendPublic"]),
    isDisabled() {
      return this.selected === -1 && !this.public;
    },
    send() {
      if (!this.public) {
        this.sendPrivate(this.wrap());
      } else {
        this.sendPublic(this.wrap2());
      }
    },
    wrap() {
      let obj = {
        type: "/complex/sendMessageToUser",
        text:
          "{" +
          '"target": ' +
          '"' +
          this.agreeUsers[selected] +
          '", ' +
          '"userName": ' +
          '"' +
          this.fullName +
          '", ' +
          '"userNameWithoutId": ' +
          '"' +
          this.host.name +
          '", ' +
          '"headImageAddress": ' +
          '"' +
          this.host.headImageAddress +
          '", ' +
          '"userMessage": ' +
          '"' +
          this.msg +
          '"' +
          "}",
      };

      return obj;
    },
    wrap2() {
      let obj = {
        type: "/simple/greeting",
        text:
          "{" +
          '"userName": ' +
          '"' +
          this.fullName +
          '",' +
          '"content": ' +
          '"' +
          this.msg +
          '"' +
          "}",
      };
      return obj;
    },
    clickBtn() {
      if (!this.public) {
        this.sendPrivate(this.wrap());
      } else {
        this.sendPublic(this.wrap2());
      }
    },
  },
  mounted() {
    this.$bus.$on("chooseBtn", (index) => {
      this.selected = index;
    });
    this.$bus.$on("choosePublic", (bool) => {
      this.public = bool;
    });
    this.$bus.$on("chooseColor", (color) => {
      this.color = color;
    });
    this.$bus.$on("chooseBackGroundColor", (bgc) => {
      this.backgroundColor = bgc;
    });
  },
};
</script>

<style scoped>
textarea {
  outline: none;
  border-width: 0;
  width: 100%;
  height: 100%;
  resize: none;
}
#editBox {
  position: relative;
  height: 210px;
  background-color: #ffffff;
}

#editBox #close,
#editBox #send {
  position: absolute;
  right: 110px;
  bottom: 10px;
  width: 90px;
  height: 30px;
  border: 1px solid #cdcfd0;
  line-height: 30px;
  text-align: center;
  cursor: pointer;
}

#editBox #send {
  right: 10px;
  color: white;
  background-color: #12b7f5;
}
</style>