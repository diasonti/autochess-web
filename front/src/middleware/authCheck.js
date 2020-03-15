import store from '../core/store'

export default function authCheck({router, from, to, next}) {

    const isAuthenticated = store.getters.isAuthenticatedGetter

    if (to.meta.anonymous && isAuthenticated) {
        console.warn('Route is anonymous, but user is authenticated. Redirecting to "home"')
        router.push({name: 'home'})
    } else if (to.meta.authenticated && !isAuthenticated) {
        console.warn('Route is authenticated, but user is anonymous. Redirecting to "index"')
        router.push({name: 'index'})
    }

    return next();
}
