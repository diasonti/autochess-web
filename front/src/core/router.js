import Vue from 'vue'
import Router from 'vue-router'
import Index from '../views/Index'
import Login from '../views/Login'
import Player from '../views/Player'
import logRoute from '../middleware/logRoute'
import authCheck from '../middleware/authCheck'
import Registration from '../views/Registration'

Vue.use(Router)

const defaultMiddleware = [logRoute, authCheck]

const router = new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'index',
            component: Index,
            meta: {
                middleware: [...defaultMiddleware],
            },
        },
        {
            path: '/login',
            name: 'login',
            component: Login,
            props: (route) => ({query: route.query.back}),
            meta: {
                middleware: [...defaultMiddleware],
                anonymous: true,
            },
        },
        {
            path: '/registration',
            name: 'registration',
            component: Registration,
            props: (route) => ({confirmationEmail: route.query.confirmationEmail, token: route.query.token}),
            meta: {
                middleware: [...defaultMiddleware],
                anonymous: true,
            },
        },
        {
            path: '/player',
            name: 'player',
            component: Player,
            meta: {
                middleware: [...defaultMiddleware],
                authenticated: true,
            },
        },
    ],
    scrollBehavior(to, from, savedPosition) {
        return savedPosition || {x: 0, y: 0}
    },
    linkActiveClass: 'active',
})

function nextFactory(context, middleware, index) {
    const subsequentMiddleware = middleware[index]
    if (!subsequentMiddleware)
        return context.next
    return (...parameters) => {
        context.next(...parameters)
        const nextMiddleware = nextFactory(context, middleware, index + 1)
        subsequentMiddleware({...context, next: nextMiddleware})
    }
}

router.beforeEach((to, from, next) => {
    if (to.meta.middleware) {
        const middleware = Array.isArray(to.meta.middleware) ? to.meta.middleware : [to.meta.middleware]
        const context = {router, from, to, next}
        const nextMiddleware = nextFactory(context, middleware, 1)
        return middleware[0]({...context, next: nextMiddleware})
    }
    return next()
})

export default router
