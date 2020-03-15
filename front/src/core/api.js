import Vue from 'vue'
import VueAxios from 'vue-axios'
import axios from 'axios'
import VueSSE from 'vue-sse'
import store from './store'
import router from './router'
import {baseUrl} from './config'

axios.defaults.baseURL = baseUrl
axios.defaults.withCredentials = true

axios.interceptors.response.use((response) => {
    return response
}, (error) => {
    if (error.response && error.response.status === 401) {
        store.dispatch('setAuthErrorAction', {error: error.response})
        router.push({path: '/login', query: {back: 'history'}})
    }
    return Promise.reject(error)
})

Vue.use(VueAxios, axios)
Vue.use(VueSSE)

export default axios
