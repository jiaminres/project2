import Vue from 'vue'
import App from './App.vue'
import store from './store/index'
Vue.config.productionTip = false

import axios from 'axios'
import router from './router/index'
//允许axios在发送数据时携带cookie
axios.defaults.withCredentials = true
export default new Vue({
  el: '#app',
  store,
  render: h => h(App),
  router,
  beforeCreate() {
    Vue.prototype.$bus = this
    Vue.prototype.$axios = axios
  }
})
