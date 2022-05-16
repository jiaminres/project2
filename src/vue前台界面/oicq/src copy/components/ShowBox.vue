<template>
  <div id="showBox">
    <h1>聊天室</h1>
    <table id="defaultShowBox">
      <tr v-for="val in 5" :key="val">
        <td v-for="val in 5" :key="val"></td>
      </tr>
    </table>
    <div class="oppositeUser">
      <img class="oppositeUserImage" src="./image/用户7.jpeg" />
      <span class="oppositeUserName">张三</span>
      <br />
      <div class="widthLimit">
        <i class="triangle"></i>
        <p class="oppositeUserMessage">
          你好啊大苏打啊打发士大夫撒大
          &#10;苏打实打实大苏打阿斯顿顶顶顶顶顶顶顶顶顶顶阿萨阿达水水水水水水水
        </p>
      </div>
    </div>
    <div class="user">
      <div class="bind">
        <span class="userName">张三</span>
        <img class="userImage" src="./image/用户7.jpeg" />
      </div>
      <br />
      <div class="widthLimit">
        <p class="userMessage">
          你好啊大苏打啊打发士大夫撒大
          &#10;苏打实打实大苏打阿斯顿顶顶顶顶顶顶顶顶顶顶阿萨阿达水水水水水水水
        </p>
        <i class="triangle"></i>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ShowBox",
};
</script>

<style>
#showBox {
  overflow: auto;
  height: 340px;
  border-radius: 20px 20px 0 0;
  background-color: #fff;
}

#showBox .oppositeUser,
#showBox .user {
  position: relative;
}

#showBox .oppositeUser {
  margin-left: 15px;
}

#showBox .user {
  margin-right: 15px;
}

#showBox .oppositeUser .oppositeUserImage,
#showBox .user .userImage {
  width: 30px;
  height: 30px;
  border-radius: 15px;
  vertical-align: text-top;
}

#showBox .user .bind {
  /* 这里转化为行内块元素，这样</br>不会另起一行 */
  display: inline-block;
  position: relative;
  left: calc(100% - 50px);
}

#showBox .oppositeUser .oppositeUserName,
#showBox .user .userName {
  font-size: 10px;
  color: #7f7f7f;
}

#showBox .oppositeUser .oppositeUserName {
  padding-left: 5px;
}

#showBox .user .userName {
  padding-right: 5px;
}

#showBox .oppositeUser .widthLimit,
#showBox .user .widthLimit {
  position: relative;
  width: 70%;
}

#showBox .user .widthLimit {
  left: 30%;
}

#showBox .oppositeUser .oppositeUserMessage,
#showBox .user .userMessage {
  position: relative;
  top: -10px;
  display: inline-block;
  padding: 0 10px 0 10px;
  border-radius: 5px;
  line-height: 25px;
  word-wrap: break-word;
}

#showBox .oppositeUser .oppositeUserMessage {
  background-color: #e5e5e5;
}

#showBox .user .userMessage {
  background-color: #12b7f5;
  color: #fff;
}

#showBox .oppositeUser .oppositeUserMessage {
  /* left: 36px; */
  margin-left: 36px;
}

#showBox .user .userMessage {
  margin-right: 36px;
}

#showBox .oppositeUser .triangle,
#showBox .user .triangle {
  position: absolute;
  top: 0px;
  display: inline-block;
  border-top: 5px solid transparent;
}

#showBox .oppositeUser .triangle {
  left: 25px;
  transform: rotate(35deg);
  border-right: 15px solid #e5e5e5;
}

#showBox .user .triangle {
  right: 25px;
  transform: rotate(145deg);
  border-right: 15px solid #12b7f5;
}

#showBox #defaultShowBox {
  /* 需改动____________________ */
  display: table;
  width: 100%;
  border-collapse: collapse;
}

#showBox h1 {
  /* 需改动____________________ */
  display: block;
  position: absolute;
  left: -45px;
  top: 155px;
  margin-left: 50%;
  font: 30px "Microsoft YaHei";
}

#showBox #defaultShowBox td {
  /* width: 20%; */
  height: 68px;
  border-radius: 20px;
  background-color: #efefef;
  border-spacing: 0;
}
</style>