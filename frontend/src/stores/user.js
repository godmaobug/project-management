import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import request from '@/utils/request'
import router from '@/router'

export const useUserStore = defineStore('user', () => {
  // State
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  // Getters
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value?.role === 'SUPER_ADMIN')
  const displayName = computed(() => userInfo.value?.realName || userInfo.value?.username || '')

  // Actions
  const setToken = (newToken) => {
    token.value = newToken
    localStorage.setItem('token', newToken)
  }

  const setUserInfo = (info) => {
    userInfo.value = info
    localStorage.setItem('userInfo', JSON.stringify(info))
  }

  const login = async (credentials) => {
    const data = await request.post('/auth/login', credentials)
    setToken(data.token)
    setUserInfo({
      id: data.id,
      username: data.username,
      realName: data.realName,
      avatar: data.avatar,
      role: data.role
    })
    return data
  }

  const logout = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    router.push('/login')
  }

  const fetchUserInfo = async () => {
    const data = await request.get('/auth/me')
    setUserInfo(data)
    return data
  }

  return {
    token,
    userInfo,
    isLoggedIn,
    isAdmin,
    displayName,
    login,
    logout,
    fetchUserInfo,
    setUserInfo
  }
})
