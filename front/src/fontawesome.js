import {library} from '@fortawesome/fontawesome-svg-core'
import {faSignInAlt, faSignOutAlt, faUserCircle} from '@fortawesome/free-solid-svg-icons'
import {FontAwesomeIcon} from '@fortawesome/vue-fontawesome'
import Vue from 'vue'

library.add(faSignInAlt)
library.add(faUserCircle)
library.add(faSignOutAlt)
Vue.component('font-awesome-icon', FontAwesomeIcon)
