import Vue from 'vue'
import Router from 'vue-router'
import Index from './views/Index'
import ChessBoard from './components/ChessBoard'
import Login from './components/Login'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: [
    {
      path: '/',
      name: 'index',
      component: Index
    },
    {
      path: '/board',
      name: 'board',
      component: ChessBoard
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
  ],
  scrollBehavior (to, from, savedPosition) {
    return savedPosition || { x: 0, y: 0 }
  },
  linkActiveClass: 'is-active'
});

export default router
