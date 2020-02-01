import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const state = {
  token: localStorage.getItem('auth_token'),
  user: null
}

const getters = {
  token: state => {
    return state.token
  },
  user: state => {
    return state.user
  }
}

const mutations = {
  LOGIN_SUCCESS (state, token) {
    state.token = token
  },
  LOGIN_FAILURE (state, error) {
    state.token = null
  },
  LOGOUT (state) {
    state.token = null
  }
}

const actions = {
  checkCredentials (context, auth) {
    const formData = new FormData()
    formData.append('email', auth.email)
    formData.append('password', auth.password)

    return Vue.axios.post('/login', formData)
        .then(response => {
          if(response.data != null && response.data.endsWith("=")) {
            localStorage.setItem('auth_token', response.data)
            context.commit('LOGIN_SUCCESS', response.data)
          } else {
            const error = response.data;
            context.commit('LOGIN_FAILURE', error)
            throw error;
          }
        }).catch((error) => {
          context.commit('LOGIN_FAILURE', error)
          throw error
        })
  },
  logOut (context) {
    localStorage.removeItem('auth_token')
    context.commit('LOGOUT');
  }
}

export default new Vuex.Store({
  strict: process.env.NODE_ENV !== 'production',
  state,
  getters,
  mutations,
  actions
})
