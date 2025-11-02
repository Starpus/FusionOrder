import React from 'react'
import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import api from '../utils/api'
import { setToken } from '../utils/auth'
import './Auth.css'

const Login = () => {
  const navigate = useNavigate()

  const handleSubmit = async (values) => {
    try {
      // api拦截器会自动提取ApiResponse的data字段
      const response = await api.post('/auth/login', values)
      
      // response 现在直接是 AuthResponse 对象 { token, username, role }
      if (response && response.token) {
        setToken(response.token)
        message.success(`登录成功！欢迎，${response.username || '用户'}`)
        
        // 根据角色跳转
        if (response.role === 'ADMIN') {
          navigate('/admin')
        } else {
          navigate('/products')
        }
      } else {
        message.error('登录响应格式错误')
        console.error('登录响应:', response)
      }
    } catch (error) {
      console.error('登录错误:', error)
      const errorMessage = error.message || error.response?.data?.message || '登录失败，请检查用户名和密码'
      message.error(errorMessage)
    }
  }

  return (
    <div className="auth-container">
      <Card title="用户登录" className="auth-card">
        <Form
          name="login"
          onFinish={handleSubmit}
          autoComplete="off"
        >
          <Form.Item
            name="username"
            rules={[{ required: true, message: '请输入用户名' }]}
          >
            <Input prefix={<UserOutlined />} placeholder="用户名" />
          </Form.Item>

          <Form.Item
            name="password"
            rules={[{ required: true, message: '请输入密码' }]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="密码" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              登录
            </Button>
          </Form.Item>

          <div style={{ textAlign: 'center' }}>
            <Button type="link" onClick={() => navigate('/register')}>
              还没有账号？立即注册
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  )
}

export default Login

