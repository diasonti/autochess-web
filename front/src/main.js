import Vue from 'vue'
import App from './App.vue'
import 'normalize.css'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import router from './core/router'
import store from './core/store'
import './core/api'
import './core/filters'
import './core/fontawesome'

window.$ = require('jquery')
window.JQuery = require('jquery')

Vue.config.productionTip = false

new Vue({
    router,
    store,
    render: h => h(App),
}).$mount('#app')
