<template>
  <div id="app">
    <User />
    <PrivateChatBox />
    <PublicChatBox />
  </div>
</template>

<script>
import User from "./User.vue";
import PrivateChatBox from "./PrivateChatBox.vue";
import PublicChatBox from "./PublicChatBox.vue";
export default {
  name: "Body",
  components: { User, PrivateChatBox, PublicChatBox },
};
</script>

<style>
</style>