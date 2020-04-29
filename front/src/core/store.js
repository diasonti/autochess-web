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
    loginSuccessMutation(state, {user}) {
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
    loginSuccessAction(context) {
        context.commit('loginSuccessMutation', {user: {}})
        document.cookie = 'authenticated=true;'
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
                document.cookie = 'authenticated=false;'
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
