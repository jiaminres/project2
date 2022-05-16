<template>
  <div style="float: left; width: 980px">
    <div class="b">
      <div style="height: 50px; line-height: 50px">
        <h1 class="ti">我加入的群组</h1>
        <p class="join-total">{{ groups.length }}</p>

        <button
          v-show="showAdd"
          @click="
            showAdd = false;
            showSearch = true;
          "
          class="add"
          type="button"
        >
          添加群组
        </button>
        <!-- 互斥 -->
        <input
          v-show="showSearch"
          ref="search"
          id="search"
          type="text"
          placeholder="输入你要加入的群组的群号"
        />
        <!-- 搜索群组 -->
        <button @click="searchGroup" v-show="showSearch" id="sure">确认</button>
      </div>
      <hr />
      <!-- 没有加入任何群组 -->
      <h1 class="msg" v-show="groups.length === 0 && !showOne && !showNone">
        你没有加入任何群组
      </h1>
      <!-- 找不到群组 -->
      <div v-show="showNone" id="none">
        <i>没有找到群组</i>
        <br />
        <button
          @click="
            showNone = false;
            showAdd = true;
            showSearch = false;
          "
          class="return"
        >
          返回
        </button>
      </div>

      <!-- 找得到群组 -->
      <div v-show="showOne" id="one">
        <div class="g">
          <a>
            <img :src="group.headImageAddress" />
          </a>
          <a>
            <h1>{{ group.groupName }}</h1>
          </a>
        </div>
        <button @click="joinGroup" class="return">确认</button>
        <button
          @click="
            showOne = false;
            showAdd = true;
            showSearch = false;
          "
          class="back"
        >
          返回
        </button>
      </div>

      <div
        v-show="!showNone && !showOne"
        v-for="group in groups"
        :key="group.groupID"
        class="g"
      >
        <router-link
          :to="{
            name: 'native',
            query: {
              groupID: group.groupID,
              leaderID: group.leaderID,
              groupName: group.groupName,
              headImageAddress: group.headImageAddress,
            },
          }"
        >
          <img :src="group.headImageAddress" />
        </router-link>
        <router-link
          :to="{
            name: 'native',
            query: {
              groupID: group.groupID,
              leaderID: group.leaderID,
              groupName: group.groupName,
              headImageAddress: group.headImageAddress,
            },
          }"
        >
          <h1>{{ group.groupName }}</h1>
        </router-link>
      </div>
    </div>
    <div class="b">
      <div style="height: 50px; line-height: 50px">
        <h1 class="ti">我管理的群组</h1>
        <p class="join-total">数量</p>
        <button class="add" type="button">创建群组</button>
      </div>
      <hr />
      <!-- 没有加入任何群组 -->
      <h1 class="msg" v-if="msg != ''">提示信息</h1>

      <div class="g">
        <a>
          <img src="用户2.jpeg" />
        </a>
        <a>
          <h1>群名</h1>
        </a>
      </div>
    </div>
  </div>
</template>


<script>
import { mapState, mapActions } from "vuex";
export default {
  name: "Block",
  data() {
    return {
      msg: "",
      init: false,
      showAdd: true,
      showSearch: false,
      showOne: false,
      showNone: false,
      group: {
        headImageAddress: "",
        groupName: "",
        groupID: "",
        leaderID: "",
      },
    };
  },
  computed: {
    ...mapState("group", ["groups", "host", "users"]),
  },
  methods: {
    ...mapActions("group", ["initGroups"]),
    //搜索群组
    searchGroup() {
      let groupID = this.$refs.search.value.trim();
      if (
        groupID === "" ||
        groupID.split("").filter((ch) => !(ch >= "0" && ch <= "9")).length !== 0
      ) {
        alert("搜索栏为空或含有非数字");
        return;
      }
      this.$axios
        .get("http://localhost:8080/search/group", {
          params: {
            groupID: groupID,
          },
        })
        .then((res) => {
          if (!res.data || !res.data.groupID) {
            //没有该群组
            //展示界面
            this.showNone = true;
            this.showOne = false;
          } else {
            //存在该群组
            //展示界面
            this.showOne = true;
            this.showNone = false;
            //展示群组
            this.group = res.data;
          }
        })
        .catch((res) => {
          alert("网络错误" + res.message);
        });
    },
    //检索群组后，添加群组
    joinGroup() {
      this.$axios
        .get("http://localhost:8080/join", {
          params: {
            userID: this.host.userID,
            groupID: this.group.groupID,
          },
        })
        .then((res) => {
          if (res.data !== true) {
            alert("你已经在群组中");
          } else {
            //添加群主成功
            //重置界面
            this.showAdd = true;
            this.showSearch = false;
            this.showOne = false;
            //修改this.groups
            this.initGroups(this.group);
            alert("添加群组成功");
          }
        })
        .catch((res) => {
          alert("网络错误" + res.message);
        });
    },
  },

  activated() {
    if (!this.init) {
      this.$axios
        .get("http://localhost:8080/list/group", {
          params: {
            userID: this.host.userID,
          },
        })
        .then((res) => {
          if (!res.data) {
            this.msg = "你没有加入任何群组";
            //因为组件没有销毁，若不加此句，会显示前面登录用户的信息
            this.initGroups([]);
          } else {
            this.initGroups(res.data);
          }
          this.init = true;
        })
        .catch((res) => {
          this.msg = "请求出现异常,请检查网络环境";
        });
    }
  },
};
</script>

<style scoped>
.msg {
  display: inline-block;
  position: relative;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  color: rgba(0, 0, 0, 0.3);
}

.b {
  float: left;
  margin-left: 100px;
  /* xx */
  margin-bottom: 50px;
  width: 980px;
  height: 500px;
  background-color: #fff;
  overflow: hidden;
}

.ti {
  float: left;
  margin-left: 20px;
  font-size: 16px;
  font-weight: normal;
  color: #212121;
}

.join-total {
  float: left;
  position: absolute;
  margin-left: 136px;
  color: #626262;
  font-size: 14px;
}

.g {
  float: left;
  margin-left: 16px;
  margin-top: 8px;
}

.g img {
  width: 225px;
  height: 130px;
  vertical-align: middle;
  background-color: red;
}

.g h1 {
  font-size: 14px;
  color: #212121;
  text-align: center;
  font-weight: normal;
  margin: 10px;
  overflow: hidden;
  width: 205px;
  text-overflow: ellipsis;
  height: 22px;
  white-space: nowrap;
}

.add {
  float: right;
  height: 30px;
  width: 120px;
  margin-top: 10px;
  margin-right: 10px;
}

#sure {
  float: right;
  margin-right: 10px;
  margin-top: 10px;
  width: 90px;
  height: 30px;
  border: 1px solid #cdcfd0;
  cursor: pointer;
  color: white;
  background-color: #12b7f5;
}

#search {
  float: right;
  margin-right: 10px;
  margin-top: 10px;
  height: 28px;
  outline: none;
  background-color: #f5f5f5;
  border: 1px solid #cdcfd0;
}

.return {
  width: 100px;
  height: 30px;
  color: #12b7f5;
  border: 1px solid #12b7f5;
  border-radius: 6px;
  background-color: #fff;
}

.back {
  width: 100px;
  height: 30px;
  color: #cfcfcf;
  border: 1px solid #cfcfcf;
  border-radius: 6px;
  background-color: #fff;
  margin-left: 10px;
}

#none {
  display: inline-block;
  position: relative;
  left: 50%;
  top: 50px;
  transform: translateX(-50%);
}

#none i {
  display: inline-block;
  margin-left: 8px;
  margin-bottom: 6px;
  color: #bfb5bd;
}

#one button {
  position: relative;
  left: 30px;
  top: 100px;
}
</style>