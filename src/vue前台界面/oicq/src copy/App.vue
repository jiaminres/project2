<template>
  <div>
    <Nav />
    <Body />
  </div>
</template>

<script>
import Nav from "./components/Nav";
import Body from "./components/Body";

export default {
  name: "App",
  data() {
    return {};
  },
  components: { Nav, Body },

  mounted() {
    let init = {
      host: {
        accountID: "1234561",
        userName: "小明",
        headImageAddress: "image/用户1.jpeg",
      },
      users: [
        {
          accountID: "1234567",
          name: "小七",
          headImageAddress: "image/用户7.jpeg",
        },
        {
          accountID: "1234562",
          userName: "小红",
          headImageAddress: "image/用户2.jpeg",
        },
        {
          accountID: "1234563",
          userName: "张三",
          headImageAddress: "image/用户3.jpeg",
        },
        {
          accountID: "1234564",
          userName: "李四",
          headImageAddress: "image/用户4.jpeg",
        },
        {
          accountID: "1234565",
          userName: "华五",
          headImageAddress: "image/用户5.jpeg",
        },
        {
          accountID: "1234566",
          userName: "小六",
          headImageAddress: "image/用户6.jpeg",
        },
      ],
    };
    this.$store.dispatch("group/init", init);
    this.$store.dispatch("group/connect");
    window.onbeforeunload = () => {
      this.$store.dispatch("group/disconnect");
    };
  },
};
</script>

<style>
@font-face {
  font-family: "icomoon";
  src: url("./assets/font/fonts/icomoon.eot?f88m88");
  src: url("./assets/font/fonts/icomoon.eot?f88m88#iefix")
      format("embedded-opentype"),
    url("./assets/font/fonts/icomoon.ttf?f88m88") format("truetype"),
    url("./assets/font/fonts/icomoon.woff?f88m88") format("woff"),
    url("./assets/font/fonts/icomoon.svg?f88m88#icomoon") format("svg");
  font-weight: normal;
  font-style: normal;
  font-display: block;
}

* {
  margin: 0px;
  padding: 0px;
  font-size: 14px;
}

li {
  list-style: none;
}

div {
  box-sizing: border-box;
  /* border-radius: 20px; */
}

i {
  font-style: normal;
}

table {
  border-width: 0px;
}
</style>
