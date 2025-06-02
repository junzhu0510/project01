import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '../router'

const service = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      
      // token 失效或未登录
      if (res.code === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  error => {
    console.error('响应错误：', error)
    
    // 处理 401 错误
    if (error.response && error.response.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      router.push('/login')
    }
    
    ElMessage.error(error.response?.data?.message || '请求失败')
    return Promise.reject(error)
  }
)

export default service