import Vue from 'vue'
import Vuex from 'vuex'
import {apiMap} from './config'

Vue.use(Vuex)

const initialState = {
    token: localStorage.getItem('auth_token'),
    error: null,
}

const getters = {
    getToken: (state) => state.token,
    getError: (state) => state.error,
}

const mutations = {
    loginSuccess(state, {token}) {
        localStorage.setItem('auth_token', token)
        state.token = token
        state.error = null
    },
    loginFailure(state, {error}) {
        state.token = null
        state.error = error
    },
    logout(state) {
        localStorage.removeItem('auth_token')
        state.token = null
    },
}

const actions = {
    setToken(context, {token}) {
        context.commit('loginSuccess', {token: token})
        setTimeout(() => {
            context.dispatch('refreshToken')
        }, 600000) // 10 minutes
    },
    setLoginError(context, {error}) {
        context.commit('loginFailure', {error: error})
    },
    refreshToken(context) {
        Vue.axios.post(apiMap.refreshToken)
            .then(response => {
                context.dispatch('setToken', {token: response.data.token})
            })
            .catch((error) => {
                context.dispatch('setLoginError', {error: error})
            })
    },
    logOut(context) {
        context.commit('LOGOUT')
    },
}

export default new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state: initialState,
    getters,
    mutations,
    actions,
})
