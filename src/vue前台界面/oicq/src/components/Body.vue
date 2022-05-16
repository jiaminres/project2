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
#app {
  height: 690px;
}

#app > div {
  float: left;
  height: 690px;
}
#user {
  width: 25%;
  background-color: #fff;
}
#privateChatBox {
  width: 50%;
  background-color: #ebebeb;
}
#publicChatBox {
  width: 25%;
}
</style>