import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/login/index.vue'),
      meta: { public: true }
    },
    {
      path: '/',
      name: 'Layout',
      component: () => import('@/layouts/MainLayout.vue'),
      redirect: '/dashboard',
      children: [
        {
          path: 'dashboard',
          name: 'Dashboard',
          component: () => import('@/views/dashboard/index.vue'),
          meta: { title: '工作台', icon: 'Odometer' }
        },
        {
          path: 'projects',
          name: 'Projects',
          component: () => import('@/views/projects/index.vue'),
          meta: { title: '项目管理', icon: 'Folder' }
        },
        {
          path: 'projects/:id',
          name: 'ProjectDetail',
          component: () => import('@/views/projects/detail.vue'),
          meta: { title: '项目详情', hidden: true }
        },
        {
          path: 'requirements',
          name: 'Requirements',
          component: () => import('@/views/requirements/index.vue'),
          meta: { title: '需求管理', icon: 'Document' }
        },
        {
          path: 'tasks',
          name: 'Tasks',
          component: () => import('@/views/tasks/index.vue'),
          meta: { title: '任务管理', icon: 'List' }
        },
        {
          path: 'bugs',
          name: 'Bugs',
          component: () => import('@/views/bugs/index.vue'),
          meta: { title: '缺陷管理', icon: 'Warning' }
        },
        {
          path: 'iterations',
          name: 'Iterations',
          component: () => import('@/views/iterations/index.vue'),
          meta: { title: '迭代管理', icon: 'Calendar' }
        },
        {
          path: 'kanban',
          name: 'Kanban',
          component: () => import('@/views/kanban/index.vue'),
          meta: { title: '看板', icon: 'Grid' }
        },
        {
          path: 'statistics',
          name: 'Statistics',
          component: () => import('@/views/statistics/index.vue'),
          meta: { title: '统计报表', icon: 'TrendCharts' }
        },
        {
          path: 'users',
          name: 'Users',
          component: () => import('@/views/users/index.vue'),
          meta: { title: '用户管理', icon: 'User', admin: true }
        },
        {
          path: 'profile',
          name: 'Profile',
          component: () => import('@/views/profile/index.vue'),
          meta: { title: '个人设置', hidden: true }
        }
      ]
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/error/404.vue')
    }
  ]
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  if (!to.meta.public && !userStore.token) {
    next('/login')
  } else if (to.meta.admin && userStore.userInfo?.role !== 'SUPER_ADMIN') {
    next('/')
  } else {
    next()
  }
})

export default router
