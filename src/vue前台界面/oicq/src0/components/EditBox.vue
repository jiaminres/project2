<template>
  <div id="editBox">
    <textarea />
    <button id="close">关闭</button>
    <button id="send" disabled="disabled">发送</button>
  </div>
</template>

<script>
export default {
  name: "EditBox",
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
</style>