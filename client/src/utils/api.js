import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    // 后端返回的统一响应格式 ApiResponse { code, message, data, timestamp }
    const apiResponse = response.data
    
    // 检查响应格式
    if (apiResponse && typeof apiResponse === 'object' && 'data' in apiResponse) {
      // 如果是错误响应（code !== 200），抛出错误
      if (apiResponse.code !== 200 && apiResponse.code !== undefined) {
        return Promise.reject(new Error(apiResponse.message || '请求失败'))
      }
      // 成功响应，返回 data 字段
      return apiResponse.data
    }
    
    // 兼容非统一格式的响应（直接返回原数据）
    return apiResponse
  },
  (error) => {
    // 处理 HTTP 错误响应
    if (error.response) {
      const apiResponse = error.response.data
      
      // 如果是统一格式的错误响应
      if (apiResponse && typeof apiResponse === 'object' && 'code' in apiResponse) {
        const errorMessage = apiResponse.message || '请求失败'
        
        if (apiResponse.code === 401) {
          localStorage.removeItem('token')
          window.location.href = '/login'
        }
        
        return Promise.reject(new Error(errorMessage))
      }
      
      // 处理标准 HTTP 错误
      if (error.response.status === 401) {
        localStorage.removeItem('token')
        window.location.href = '/login'
      }
    }
    
    return Promise.reject(error)
  }
)

export default api

