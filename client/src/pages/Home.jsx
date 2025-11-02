import React from 'react'
import { Button, Typography, Card } from 'antd'
import { ShoppingOutlined } from '@ant-design/icons'
import { useNavigate } from 'react-router-dom'
import './Home.css'

const { Title, Paragraph } = Typography

const Home = () => {
  const navigate = useNavigate()

  return (
    <div className="home">
      <div className="hero">
        <Title level={1}>欢迎使用 FusionOrder</Title>
        <Paragraph style={{ fontSize: '18px', marginBottom: '30px' }}>
          专业的订单管理系统，轻松管理您的产品和订单
        </Paragraph>
        <Button
          type="primary"
          size="large"
          icon={<ShoppingOutlined />}
          onClick={() => navigate('/products')}
        >
          浏览产品
        </Button>
      </div>
      
      <div className="features">
        <Card title="产品管理" bordered={false} className="feature-card">
          <p>完善的产品信息管理，支持分类、价格、图片等</p>
        </Card>
        <Card title="订单管理" bordered={false} className="feature-card">
          <p>便捷的订单提交和管理流程</p>
        </Card>
        <Card title="用户权限" bordered={false} className="feature-card">
          <p>灵活的用户权限管理系统</p>
        </Card>
      </div>
    </div>
  )
}

export default Home

