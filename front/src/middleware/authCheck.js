import store from '../core/store'

export default function authCheck({router, from, to, next}) {

    let isAuthenticated = store.getters.isAuthenticatedGetter || isAuthenticatedByCookie()

    if (to.meta.anonymous && isAuthenticated) {
        console.warn('Route is anonymous, but user is authenticated. Redirecting to "player"')
        router.push({name: 'player'})
    } else if (to.meta.authenticated && !isAuthenticated) {
        console.warn('Route is authenticated, but user is anonymous. Redirecting to "index"')
        router.push({name: 'index'})
    }

    return next();
}

function getCookie(name) {
    const cName = name + '='
    const ca = document.cookie.split(';')
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i]
        while (c.charAt(0) === ' ') {
            c = c.substring(1)
        }
        if (c.indexOf(name) === 0) {
            return c.substring(cName.length, c.length)
        }
    }
    return ''
}

function isAuthenticatedByCookie() {
    const cookie = getCookie('authenticated')
    return cookie === 'true'
}
