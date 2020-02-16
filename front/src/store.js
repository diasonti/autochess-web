import Vue from 'vue'
import Vuex from 'vuex'
import {apiMap} from './config'

Vue.use(Vuex)

const initialState = {
    authToken: localStorage.getItem('auth_token'),
    authError: null,
}

const getters = {
    tokenGetter: (state) => state.authToken,
    authErrorGetter: (state) => state.authError,
    isAuthenticatedGetter: (state) => state.authToken && !state.authError,
}

const mutations = {
    loginSuccessMutation(state, {token}) {
        state.authToken = token
        state.authError = null
    },
    setAuthErrorMutation(state, {error}) {
        state.authToken = null
        state.authenticatedUser = null
        state.authError = error
    },
    clearAuthErrorMutation(state) {
        state.authError = null
    },
    logoutMutation(state) {
        state.authToken = null
        state.authenticatedUser = null
        state.authError = null
    }
}

const actions = {
    loginSuccessAction(context, {token}) {
        localStorage.setItem('auth_token', token)
        context.commit('loginSuccessMutation', {token})
    },
    loginErrorAction(context, {error}) {
        if (error) {
            context.commit('setAuthErrorMutation', {error})
        } else {
            context.commit('clearAuthErrorMutation')
        }
    },
    refreshTokenAction(context) {
        Vue.axios.post(apiMap.refreshToken)
            .then(response => {
                context.dispatch('loginSuccessAction', response.data)
            })
            .catch((error) => {
                context.dispatch('loginErrorAction', {error})
            })
    },
    logoutAction(context) {
        localStorage.removeItem('auth_token')
        context.commit('logoutMutation')
    },
}

export default new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state: initialState,
    getters,
    mutations,
    actions,
})
