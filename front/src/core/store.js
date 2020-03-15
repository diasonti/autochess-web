import Vue from 'vue'
import Vuex from 'vuex'
import {apiMap} from './config'

Vue.use(Vuex)

const initialState = {
    authenticatedUser: null,
    matchHistory: [],
    matchHistoryPage: 0,
    authError: null,
    playersOnline: 0,
}

const getters = {
    isAuthenticatedGetter: (state) => !!state.authenticatedUser,
    authenticatedUserGetter: (state) => state.authenticatedUser,
    matchHistoryGetter: (state) => state.matchHistory,
    matchHistoryPageGetter: (state) => state.matchHistoryPage,
    authErrorGetter: (state) => state.authError,
    playersOnlineGetter: (state) => state.playersOnline,
}

const mutations = {
    fetchSessionMutation(state, {user}) {
        state.authenticatedUser = user
        state.authError = null
    },
    fetchMatchHistoryMutation(state, {matchHistory}) {
        state.matchHistory = [...state.matchHistory, ...matchHistory]
        if (matchHistory.length === 10) {
            state.matchHistoryPage++;
        } else {
            state.matchHistoryPage = -1;
        }
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
    playersOnlineMutation(state, {value}) {
        state.playersOnline = value
    },
}

const actions = {
    fetchPlayersOnline(context) {
        Vue.axios.get(apiMap.playersOnline)
            .then((response) => {
                const playersOnline = response.data;
                if (context.getters.playersOnlineGetter !== playersOnline) {
                    context.commit('playersOnlineMutation', {value: response.data})
                }
            })
            .catch((error) => {
                console.error("fetchPlayersOnline error", error)
            })
    },
    fetchSessionAction(context) {
        Vue.axios.get(apiMap.fetchPlayer)
            .then((response) => {
                context.commit('fetchSessionMutation', {user: response.data})
            })
            .catch((error) => {
                context.commit('logOutMutation', {error: error.response})
            })
    },
    fetchMatchHistoryAction(context) {
        Vue.axios.get(apiMap.fetchMatchHistory, {params: {page: context.getters.matchHistoryPageGetter}})
            .then((response) => {
                context.commit('fetchMatchHistoryMutation', {matchHistory: response.data})
            })
            .catch((error) => {
                console.error("fetchMatchHistoryAction error", error)
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
