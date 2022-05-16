<template>
  <div id="nav">
    <ul id="funcWindow">
      <li id="imageAndUserName">
        <img :src="host.headImageAddress" />
        <span id="userName">{{ host.name }}+'@'+{{ host.accountID }}"</span>
      </li>
      <li>微信</li>
      <li>QQ</li>
      <li>搜索班群</li>
      <li>客服</li>
    </ul>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "Nav",
  data() {
    return {};
  },
  computed: {
    ...mapState("session", ["host"]),
  },
};
</script>

<style>
</style>