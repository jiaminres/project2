<template>
  <div id="nav">
    <ul id="funcWindow" ref="ul">
      <li ref="owner" id="imageAndUserName">
        <img :src="host.headImageAddress" />
        <span id="userName">{{ host.name }}+'@'+{{ host.accountID }}"</span>
      </li>
      <li ref="items" v-for="(item, index) in items" :key="index">
        {{ item }}
      </li>
    </ul>
  </div>
</template>

<script>
import { mapState } from "vuex";
export default {
  name: "Nav",
  data() {
    return {
      items: ["微信", "QQ", "搜索班群", "客服"],
    };
  },
  methods: {
    setPadding() {
      let list = [];
      let owner = this.$refs.owner;
      let items = this.$refs.items;
      let ul = this.$refs.ul;
      list.push(owner);
      items.forEach((item) => {
        list.push(item);
      });

      let ulWid = ul.clientWidth;
      let lisWid = 0;
      let dif;
      for (let i = 0; i < list.length; i++) {
        lisWid += list[i].clientWidth;
      }
      // 这里随便减个5，只是为了避免计算精度的问题，导致nav没显示全
      dif = ulWid - lisWid - 5;
      dif = dif / (list.length * 2);
      for (let i = 0; i < list.length; i++) {
        let paddingLeft = parseInt(
          list[i].style.paddingLeft.substring(
            0,
            list[i].style.paddingLeft.length - 2
          )
        );
        let paddingRight = parseInt(
          list[i].style.paddingRight.substring(
            0,
            list[i].style.paddingRight.length - 2
          )
        );
        list[i].style.paddingLeft = paddingLeft + dif + "px";
        list[i].style.paddingRight = paddingRight + dif + "px";
      }
    },
  },
  computed: {
    ...mapState("group", ["host"]),
  },
  mounted() {
    let list = [];
    let owner = this.$refs.owner;
    let items = this.$refs.items;
    let ul = this.$refs.ul;
    list.push(owner);
    items.forEach((item) => {
      list.push(item);
    });
    for (let i = 0; i < list.length; i++) {
      list[i].style.paddingLeft = 0 + "px";
      list[i].style.paddingRight = 0 + "px";
    }
    this.setPadding();
    window.addEventListener("resize", this.setPadding);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.setPadding);
  },
};
</script>

<style>
#nav {
  height: 30px;
  background-color: #12b7f5;
  color: #fff;
  overflow: hidden;
}

#nav li {
  float: left;
  line-height: 30px;
}

#nav li img {
  width: 30px;
  height: 30px;
  border-radius: 15px;
  vertical-align: middle;
}
</style>