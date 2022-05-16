<template>
  <div>
    <ul>
      <li v-for="val in 3" :key="val" ref="li">
        <!-- <div v-for="val in 2" :key="val" ref="div">div</div> -->
        <div ref="div">
          div
          <div ref="div">div</div>
        </div>
      </li>
      <li v-for="val in 3" :key="val" ref="li">
        <!-- <div v-for="val in 2" :key="val" ref="div">div</div> -->
        <div ref="div">
          div
          <div ref="div">div</div>
        </div>
      </li>
    </ul>

    <a href="http://www.baidu.com">aa</a>
  </div>
</template>

<script>
export default {
  name: "App",
  data() {
    return {
      array: [],
    };
  },
  methods: {
    addItem() {
      this.array.push(1);
    },
  },
  watch: {
    array(newVal, oldVal) {
      console.log(newVal + "@" + oldVal);
    },
  },
  mounted() {
    console.log(this);
  },
  beforeDestroy() {
    console.log("distroy");
  },
};
</script>

<style>
</style>
