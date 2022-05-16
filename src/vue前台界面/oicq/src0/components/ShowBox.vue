<template>
  <div id="showBox">
    <h1>聊天室</h1>
    <table id="defaultShowBox">
      <tr v-for="val in 5" :key="val">
        <td v-for="val in 5" :key="val"></td>
      </tr>
    </table>
  </div>
</template>

<script>
export default {
    name: 'ShowBox'
};
</script>

<style>
</style>