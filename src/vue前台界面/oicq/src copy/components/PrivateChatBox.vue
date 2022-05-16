<template>
  <div id="privateChatBox">
    <div id="chatBox">
      <ShowBox />
      <UserChooseBox />
      <EditBox />
    </div>
  </div>
</template>

<script>
import ShowBox from "./ShowBox.vue";
import UserChooseBox from "./UserChooseBox.vue";
import EditBox from "./EditBox.vue";
export default {
  name: "PrivateChatBox",
  components: { ShowBox, UserChooseBox, EditBox },
};
</script>

<style>
#chatBox {
  position: relative;
  left: -35%;
  top: 50px;
  margin-left: 50%;
  width: 70%;
  height: 570px;
  /* background-color: brown; */
}
</style>