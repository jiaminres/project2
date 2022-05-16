<template>
  <div id="publicChatBox">
    <div id="laba">
      <div id="signal">喇叭</div>
    </div>
    <div @scroll="isFocus" id="p_showBox" ref="showBox">
      <p v-for="(obj, index) in msgList" :key="index">
        <span style="color: red">{{ obj.userName + " " }}</span>
        <span>{{ obj.content }}</span>
      </p>
    </div>
    <div id="extraTool">额外带扩展窗口</div>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "PublicChatBox",
  data() {
    return {
      focus: false,
    };
  },
  computed: {
    ...mapState("group", ["msgList"]),
  },
  methods: {
    isFocus(e) {
      this.focus = true;
      let tar = e.target;
      if (tar.scrollHeight - tar.clientHeight - tar.scrollTop < 5) {
        focus = false;
      }
    },
  },
  updated() {
    if (this.focus) {
      let tar = this.$refs.showBox;
      publicShow.scrollTop = publicShow.scrollHeight - publicShow.clientHeight;
    }
  },
};
</script>

<style>
#publicChatBox #laba {
  height: 24px;
  background-color: #1e242e;
}

#publicChatBox #laba #signal {
  position: relative;
  top: 3px;
  width: 50px;
  height: 20px;
  border: 1px solid #e9de8e;
  border-radius: 2px;
  background-color: #383121;
  color: #fff000;
  text-align: center;
  line-height: 20px;
}

#publicChatBox #p_showBox {
  border: 5px solid #4a3b26;
  float: left;
  box-sizing: border-box;
  width: 100%;
  height: 432px;
  line-height: 25px;
  overflow: auto;
  word-wrap: break-word;
  background-color: #0b0c10;
  color: #fff000;
}

#publicChatBox #p_showBox::-webkit-scrollbar {
  background-color: #0b0c10;
  width: 5px;
}

#publicChatBox #p_showBox::-webkit-scrollbar-thumb {
  background-color: #4a3b26;
  border-radius: 2px;
}
#publicChatBox #extraTool {
  float: left;
  width: 100%;
  height: 230px;
  background-color: #fff;
}
</style>