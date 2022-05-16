<template>
  <div id="publicChatBox">
    <div id="laba">
      <div id="signal">喇叭</div>
    </div>
    <div id="p_showBox">
        <p v-for="(obj,index) in msgLists" :key="index">
            <span style="color:red">{{obj.userName}}</span>
            <span>{{obj.content}}</span>
        </p>
    </div>
    <div id="extraTool">额外带扩展窗口</div>
  </div>
</template>

<script>
import {mapState} from 'vuex'
export default {
  name: "PublicChatBox",
  data(){
      return{
          
      }
  },
  computed: {
     ...mapState('chat',['msgLists'])
  }
};
</script>

<style>
</style>