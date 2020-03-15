import store from '../store'
import axios from '../api'
import {apiMap} from '../config'

export default function fetch({router, from, to, next}) {
    axios.get(apiMap.fetch)
        .then((response) => {
            store.commit('fetchSessionMutation', {user: response.data})
        })
        .catch((error) => {
            store.commit('logOutMutation', {error: error.response})
        })
        .finally(() => next())
}
