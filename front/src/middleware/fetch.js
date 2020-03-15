import store from '../core/store'
import axios from '../core/api'
import {apiMap} from '../core/config'

export default function fetch({router, from, to, next}) {
    axios.get(apiMap.fetchPlayer)
        .then((response) => {
            store.commit('fetchSessionMutation', {user: response.data})
        })
        .catch((error) => {
            store.commit('logOutMutation', {error: error.response})
        })
        .finally(() => next())
}
