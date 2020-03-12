import Vue from 'vue'
import Vuex from 'vuex'
import {apiMap} from './config'

Vue.use(Vuex)

const initialState = {
    authenticatedUser: null,
    authError: null,
}

const getters = {
    isAuthenticatedGetter: (state) => !!state.authenticatedUser,
    authenticatedUserGetter: (state) => state.authenticatedUser,
    authErrorGetter: (state) => state.authError,
}

const mutations = {
    fetchSessionMutation(state, {user}) {
        state.authenticatedUser = user
        state.authError = null
    },
    logOutMutation(state) {
        state.authenticatedUser = null
        state.authError = null
    },
    authErrorMutation(state, {error}) {
        state.authError = error
        if (error) {
            state.authenticatedUser = null
        }
    },
}

const actions = {
    fetchSessionAction(context) {
        Vue.axios.get(apiMap.fetch)
            .then((response) => {
                context.commit('fetchSessionMutation', {user: response.data})
            })
            .catch((error) => {
                context.commit('logOutMutation', {error: error.response})
            })
    },
    loginSuccessAction(context) {
        context.dispatch('fetchSessionAction')
    },
    setAuthErrorAction(context, {error}) {
        context.commit('authErrorMutation', {error})
    },
    clearAuthErrorAction(context) {
        context.commit('authErrorMutation', {error: null})
    },
    logoutAction(context) {
        Vue.axios.post(apiMap.logout)
            .then((response) => {
                context.commit('logOutMutation')
            })
    },
}

export default new Vuex.Store({
    strict: process.env.NODE_ENV !== 'production',
    state: initialState,
    getters,
    mutations,
    actions,
})
