import React from 'react'
import { Form, Input, Button, Card, message } from 'antd'
import { UserOutlined, LockOutlined, MailOutlined, PhoneOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import api from '../utils/api'
import './Auth.css'

const Register = () => {
  const navigate = useNavigate()

  const handleSubmit = async (values) => {
    try {
      await api.post('/auth/register', values)
      message.success('注册成功，请登录')
      navigate('/login')
    } catch (error) {
      message.error(error.response?.data?.message || '注册失败，请稍后重试')
    }
  }

  return (
    <div className="auth-container">
      <Card title="用户注册" className="auth-card">
        <Form
          name="register"
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
            rules={[
              { required: true, message: '请输入密码' },
              { min: 6, message: '密码长度至少6位' }
            ]}
          >
            <Input.Password prefix={<LockOutlined />} placeholder="密码（至少6位）" />
          </Form.Item>

          <Form.Item
            name="email"
            rules={[
              { type: 'email', message: '请输入正确的邮箱地址' }
            ]}
          >
            <Input prefix={<MailOutlined />} placeholder="邮箱（可选）" />
          </Form.Item>

          <Form.Item
            name="phone"
            rules={[
              { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码' }
            ]}
          >
            <Input prefix={<PhoneOutlined />} placeholder="手机号（可选）" />
          </Form.Item>

          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              注册
            </Button>
          </Form.Item>

          <div style={{ textAlign: 'center' }}>
            <Button type="link" onClick={() => navigate('/login')}>
              已有账号？立即登录
            </Button>
          </div>
        </Form>
      </Card>
    </div>
  )
}

export default Register

