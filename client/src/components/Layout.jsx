import React, { useState } from 'react'
import { Layout as AntLayout, Menu, Button } from 'antd'
import { useNavigate, useLocation } from 'react-router-dom'
import { HomeOutlined, ShoppingOutlined, UserOutlined, LoginOutlined, LogoutOutlined } from '@ant-design/icons'
import { isAuthenticated, removeToken } from '../utils/auth'
import './Layout.css'

const { Header, Content, Footer } = AntLayout

const Layout = ({ children }) => {
  const navigate = useNavigate()
  const location = useLocation()
  const authenticated = isAuthenticated()

  const handleLogout = () => {
    removeToken()
    navigate('/')
    window.location.reload()
  }

  const menuItems = [
    {
      key: '/',
      icon: <HomeOutlined />,
      label: '首页'
    },
    {
      key: '/products',
      icon: <ShoppingOutlined />,
      label: '产品'
    },
    ...(authenticated
      ? [
          {
            key: '/admin',
            icon: <UserOutlined />,
            label: '管理后台'
          }
        ]
      : []),
    ...(authenticated
      ? []
      : [
          {
            key: '/login',
            icon: <LoginOutlined />,
            label: '登录'
          }
        ])
  ]

  return (
    <AntLayout className="layout">
      <Header className="header">
        <div className="logo">FusionOrder</div>
        <Menu
          theme="dark"
          mode="horizontal"
          selectedKeys={[location.pathname]}
          items={menuItems}
          onClick={({ key }) => navigate(key)}
          style={{ flex: 1, minWidth: 0 }}
        />
        {authenticated && (
          <Button type="text" icon={<LogoutOutlined />} onClick={handleLogout} style={{ color: 'white' }}>
            退出
          </Button>
        )}
      </Header>
      <Content className="content">
        {children}
      </Content>
      <Footer className="footer">
        FusionOrder ©2024 订单管理系统
      </Footer>
    </AntLayout>
  )
}

export default Layout

