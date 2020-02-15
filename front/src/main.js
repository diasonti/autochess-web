import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import './api'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueSSE from 'vue-sse'

Vue.use(VueSSE);

window.$ = require('jquery')
window.JQuery = require('jquery')

Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
