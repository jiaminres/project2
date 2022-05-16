<template>
  <div class="edit">
    <div id="head">
      <div @click="skipToSetting" class="triangle"></div>
      详细资料
    </div>
    <ul>
      <li>
        性别<input v-model="sex" type="text" placeholder="编辑你的性别" />
      </li>
      <hr />
      <li>
        年龄<input v-model="age" type="text" placeholder="编辑你的年龄" />
      </li>
      <hr />
      <li>
        生日<input v-model="birthday" type="date" placeholder="编辑你的生日" />
      </li>
      <hr />
      <li>
        用户地址<input
          v-model="location"
          type="text"
          placeholder="编辑你的地址"
        />
      </li>
      <hr />
      <li>
        个性签名<input
          v-model="persionalSign"
          type="text"
          placeholder="编辑你的个性签名"
        />
      </li>
    </ul>
    <button @click="modifyDetail" id="editBtn" type="button">修改资料</button>
  </div>
</template>


<script>
import { mapState, mapMutations } from "vuex";
export default {
  name: "Edit",
  data() {
    //初始化时，与host.detail一致
    //当字段没有数据，即null的时候，表单显示默认提示信息
    return {
      sex: "",
      age: "",
      birthday: "",
      location: "",
      persionalSign: "",
    };
  },
  updated() {
    console.log(this.birthday);
  },
  computed: {
    //添加用户详情信息
    ...mapState("group", ["host"]),
  },
  methods: {
    ...mapMutations("group", ["updateDetail"]),
    //点击<跳转到setting页面
    skipToSetting() {
      this.$router.replace({
        name: "setting",
      });
    },
    modifyDetail() {
      // 先判断用户是否更改了信息，再决定是否发送ajax请求
      // 只要有一个不等就是不等
      if (
        this.sex !== this.host.detail.sex ||
        this.age !== this.host.detail.age ||
        //birthday手机到的数据格式是 20xx-xx-xx,而后台传回的数据是时间戳，需要先将其转化
        (this.birthday &&
          new Date(this.birthday).getTime() !== this.host.detail.birthday) ||
        this.location !== this.host.detail.location ||
        this.persionalSign !== this.host.detail.persionalSign
      ) {
        //再判断上传到数据库的数据大小是否越界
        if (this.sex.length > 1 || (this.sex !== "男" && this.sex !== "女")) {
          alert("性别只能为男或女");
        } else if (
          this.age.length > 3 ||
          this.age.split("").filter((ch) => !(ch >= "0" && ch <= "9"))
            .length !== 0
        ) {
          alert("年龄只能是0~999");
        } else if (this.location.length > 100) {
          alert("居住地址的长度不能超过100");
        } else if (this.persionalSign.length > 100) {
          alert("个性签名的长度不能超过100");
        } else {
          this.$axios
            .get("http://localhost:8080/detail/put", {
              params: {
                //记住不要将userID忘记
                userID: this.host.userID,
                sex: this.sex,
                age: this.age,
                birthday: new Date(this.birthday).getTime(),
                location: this.location,
                persionalSign: this.persionalSign,
              },
            })
            .then((res) => {
              //更新详情失败
              if (res.data !== true) {
                alert("更新失败\n" + res.message);
              } else {
                alert("更新成功");
                console.log("前");
                //更新成功后，更改host.detail里的数据
                this.updateDetail({
                  sex: this.sex,
                  age: this.age,
                  birthday: this.birthday
                    ? new Date(this.birthday).getTime()
                    : null,
                  location: this.location,
                  persionalSign: this.persionalSign,
                });
                console.log("后");
              }
            })
            .catch((res) => {
              alert("网络错误\n" + res.message);
            });
        }
      } else {
        alert("你没有更改任何信息");
      }
    },
  },
  //不能在data()中初始化数据，组件只是创建一次
  activated() {
    console.log("settingactived");
    let date = new Date(this.host.detail.birthday);
    this.sex = this.host.detail.sex;
    this.age = this.host.detail.age;
    this.birthday =
      date.getFullYear() +
      "-" +
      (date.getMonth() + 1 <= 9
        ? "0" + (date.getMonth() + 1)
        : date.getMonth() + 1) +
      "-" +
      (date.getDay() + 1 <= 9 ? "0" + (date.getDay() + 1) : date.getDay() + 1);
    this.location = this.host.detail.location;
    this.persionalSign = this.host.detail.persionalSign;
  },
  beforeUpdate() {
    console.log("editBeforeUpdate");
  },
  updated() {
    console.log("editUpdated");
  },
};
</script>

<style scoped>
hr {
  border: 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}
input {
  margin-left: 50px;
  border: 0;
  outline: none;
  font-size: 16px;
  color: #cfcfcf;
}

.edit {
  position: relative;
  float: left;
  margin-left: 100px;
  width: 980px;
  height: 500px;
  background-color: #fff;
  overflow: hidden;
}

ul li {
  margin: 24px 5px;
}

.triangle {
  position: absolute;
  top: 13px;
  left: 13px;
  right: 30px;
  width: 10px;
  height: 10px;
  border-top: 2px solid #fff;
  border-right: 2px solid #cfff;
  transform: rotate(-135deg);
}

#editBtn,
#head {
  left: 50%;
  transform: translateX(-50%);
  border: 1px solid #cdcfd0;
  color: white;
  background-color: #12b7f5;
  line-height: 40px;
  text-align: center;
  font-size: 20px;
}

#editBtn {
  position: absolute;
  bottom: 0;
  width: 300px;
  height: 40px;
  cursor: pointer;
}

#head {
  position: relative;
  width: 100%;
}
</style>
