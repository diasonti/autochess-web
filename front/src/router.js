import Vue from 'vue'
import Router from 'vue-router'
import Index from './views/Index'
import Pieces from './views/Pieces'

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
      path: '/pieces',
      name: 'pieces',
      component: Pieces
    },
  ],
  scrollBehavior (to, from, savedPosition) {
    return savedPosition || { x: 0, y: 0 }
  },
  linkActiveClass: 'is-active'
});

export default router
