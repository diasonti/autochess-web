import Vue from 'vue'
import VueAxios from 'vue-axios'
import axios from 'axios'
import VueSSE from 'vue-sse'
import store from './store'
import {baseUrl} from './config'

axios.defaults.baseURL = baseUrl
axios.defaults.withCredentials = false

axios.interceptors.request.use((request) => {
    if (store.getters.tokenGetter) {
        request.headers.common['Authorization'] = 'Bearer ' + store.getters.tokenGetter
    }
    return request
}, (error) => {
    console.error('Axios request error:', error)
    return Promise.reject(error)
})

Vue.use(VueAxios, axios)
Vue.use(VueSSE)
