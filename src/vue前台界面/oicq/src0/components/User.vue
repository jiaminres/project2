<template>
  <div id="user">
    <ul id="userList">
      <li v-for="user in users" :key="user.accountID">
        <img :src="user.headImageAddress" />
        <em>{{ user.userName }}</em>
        <div class="mask">
          <i
            :class="{
              isAlive: isAlive(fullName(user)),
              offline: !isAlive(fullName(user)),
            }"
            ></i
          >
        </div>
        <i class="isApply" :data-user="fullName(user)">  </i>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "User",
  data() {
    return {
      color: "red",
    };
  },
  methods: {
    fullName(user) {
      return user.userName + "@" + user.accountID;
    },
  },
  computed: {
    ...mapState("session", ["users"]),
    ...mapState("chat", ["aliveUsers"]),
    isAlive(userName) {
      return (
        this.aliveUsers.filters((name) => name === userName).length() === 1
      );
    },
  },
};
</script>

<style scoped>
.isAlive {
  color: green;
}
.offline {
  color: red;
}
</style>