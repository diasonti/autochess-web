import Vue from 'vue'
import Router from 'vue-router'
import Index from './views/Index'
import ChessBoard from './components/ChessBoard'
import Login from './components/Login'
import Home from './components/Home'

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
      path: '/login',
      name: 'login',
      component: Login,
      props: (route) => ({ query: route.query.back })
    },
    {
      path: '/home',
      name: 'home',
      component: Home
    },
    {
      path: '/game/:gameId',
      name: 'board',
      component: ChessBoard,
      props: true
    },
  ],
  scrollBehavior (to, from, savedPosition) {
    return savedPosition || { x: 0, y: 0 }
  },
  linkActiveClass: 'active'
});

export default router
