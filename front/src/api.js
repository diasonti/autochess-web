import Vue from 'vue'
import VueAxios from 'vue-axios'
import axios from 'axios'
import store from './store'
import {baseUrl} from './config'

axios.defaults.baseURL = baseUrl
axios.defaults.withCredentials = false

axios.interceptors.request.use((request) => {
    if (store.getters.getToken) {
        request.headers.common['Authorization'] = 'Bearer ' + store.getters.getToken
    }
    return request
}, (error) => {
    console.error('Axios request error:', error)
    return Promise.reject(error)
});

Vue.use(VueAxios, axios)
