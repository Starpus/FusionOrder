import React, { useState, useEffect } from 'react'
import { Tabs, Table, Button, Modal, Form, Input, InputNumber, Upload, message, Select, Tag } from 'antd'
import { PlusOutlined, EditOutlined, DeleteOutlined } from '@ant-design/icons'
import api from '../utils/api'
import { isAuthenticated } from '../utils/auth'
import { useNavigate } from 'react-router-dom'
import './Admin.css'

const { TabPane } = Tabs
const { TextArea } = Input
const { Option } = Select

const Admin = () => {
  const navigate = useNavigate()
  const [users, setUsers] = useState([])
  const [products, setProducts] = useState([])
  const [orders, setOrders] = useState([])
  const [loading, setLoading] = useState(false)
  const [userModalVisible, setUserModalVisible] = useState(false)
  const [productModalVisible, setProductModalVisible] = useState(false)
  const [editingUser, setEditingUser] = useState(null)
  const [editingProduct, setEditingProduct] = useState(null)
  const [userForm] = Form.useForm()
  const [productForm] = Form.useForm()

  useEffect(() => {
    if (!isAuthenticated()) {
      navigate('/login')
      return
    }
    fetchUsers()
    fetchProducts()
    fetchOrders()
  }, [])

  const fetchUsers = async () => {
    try {
      const data = await api.get('/admin/users')
      setUsers(data)
    } catch (error) {
      message.error('加载用户列表失败')
    }
  }

  const fetchProducts = async () => {
    setLoading(true)
    try {
      const data = await api.get('/products')
      setProducts(data)
    } catch (error) {
      message.error('加载产品列表失败')
    } finally {
      setLoading(false)
    }
  }

  const fetchOrders = async () => {
    try {
      const data = await api.get('/orders')
      setOrders(data)
    } catch (error) {
      message.error('加载订单列表失败')
    }
  }

  const handleUserSubmit = async (values) => {
    try {
      if (editingUser) {
        await api.put(`/admin/users/${editingUser.id}`, values)
        message.success('用户更新成功')
      } else {
        await api.post('/auth/register', values)
        message.success('用户创建成功')
      }
      setUserModalVisible(false)
      userForm.resetFields()
      setEditingUser(null)
      fetchUsers()
    } catch (error) {
      message.error(error.response?.data?.message || '操作失败')
    }
  }

  const handleProductSubmit = async (values) => {
    try {
      if (editingProduct) {
        await api.put(`/products/${editingProduct.id}`, values)
        message.success('产品更新成功')
      } else {
        await api.post('/products', values)
        message.success('产品创建成功')
      }
      setProductModalVisible(false)
      productForm.resetFields()
      setEditingProduct(null)
      fetchProducts()
    } catch (error) {
      message.error('操作失败')
    }
  }

  const handleDeleteUser = (id) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除这个用户吗？',
      onOk: async () => {
        try {
          await api.delete(`/admin/users/${id}`)
          message.success('删除成功')
          fetchUsers()
        } catch (error) {
          message.error('删除失败')
        }
      }
    })
  }

  const handleDeleteProduct = (id) => {
    Modal.confirm({
      title: '确认删除',
      content: '确定要删除这个产品吗？',
      onOk: async () => {
        try {
          await api.delete(`/products/${id}`)
          message.success('删除成功')
          fetchProducts()
        } catch (error) {
          message.error('删除失败')
        }
      }
    })
  }

  const handleEditUser = (user) => {
    setEditingUser(user)
    userForm.setFieldsValue(user)
    setUserModalVisible(true)
  }

  const handleEditProduct = (product) => {
    setEditingProduct(product)
    productForm.setFieldsValue(product)
    setProductModalVisible(true)
  }

  const handleUpdateOrderStatus = async (id, status) => {
    try {
      await api.put(`/orders/${id}/status`, null, { params: { status } })
      message.success('订单状态更新成功')
      fetchOrders()
    } catch (error) {
      message.error('更新失败')
    }
  }

  const userColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: '用户名', dataIndex: 'username', key: 'username' },
    { title: '邮箱', dataIndex: 'email', key: 'email' },
    { title: '手机', dataIndex: 'phone', key: 'phone' },
    {
      title: '角色',
      dataIndex: 'role',
      key: 'role',
      render: (role) => <Tag color={role === 'ADMIN' ? 'red' : 'blue'}>{role}</Tag>
    },
    {
      title: '状态',
      dataIndex: 'enabled',
      key: 'enabled',
      render: (enabled) => <Tag color={enabled ? 'green' : 'red'}>{enabled ? '启用' : '禁用'}</Tag>
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEditUser(record)}>
            编辑
          </Button>
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDeleteUser(record.id)}>
            删除
          </Button>
        </>
      )
    }
  ]

  const productColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: '名称', dataIndex: 'name', key: 'name' },
    { title: '分类', dataIndex: 'category', key: 'category' },
    {
      title: '价格',
      dataIndex: 'price',
      key: 'price',
      render: (price) => `¥${price}`
    },
    {
      title: '状态',
      dataIndex: 'available',
      key: 'available',
      render: (available) => <Tag color={available ? 'green' : 'red'}>{available ? '在售' : '下架'}</Tag>
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <>
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEditProduct(record)}>
            编辑
          </Button>
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDeleteProduct(record.id)}>
            删除
          </Button>
        </>
      )
    }
  ]

  const orderColumns = [
    { title: 'ID', dataIndex: 'id', key: 'id' },
    { title: '产品名称', dataIndex: 'productName', key: 'productName' },
    { title: '订货量', dataIndex: 'quantity', key: 'quantity' },
    { title: '联系人', dataIndex: 'contactName', key: 'contactName' },
    { title: '联系电话', dataIndex: 'contactPhone', key: 'contactPhone' },
    { title: '联系邮箱', dataIndex: 'contactEmail', key: 'contactEmail' },
    {
      title: '状态',
      dataIndex: 'status',
      key: 'status',
      render: (status) => {
        const statusMap = {
          PENDING: { color: 'orange', text: '待处理' },
          CONFIRMED: { color: 'blue', text: '已确认' },
          PROCESSING: { color: 'cyan', text: '处理中' },
          COMPLETED: { color: 'green', text: '已完成' },
          CANCELLED: { color: 'red', text: '已取消' }
        }
        const s = statusMap[status] || { color: 'default', text: status }
        return <Tag color={s.color}>{s.text}</Tag>
      }
    },
    {
      title: '操作',
      key: 'action',
      render: (_, record) => (
        <Select
          value={record.status}
          onChange={(value) => handleUpdateOrderStatus(record.id, value)}
          style={{ width: 120 }}
        >
          <Option value="PENDING">待处理</Option>
          <Option value="CONFIRMED">已确认</Option>
          <Option value="PROCESSING">处理中</Option>
          <Option value="COMPLETED">已完成</Option>
          <Option value="CANCELLED">已取消</Option>
        </Select>
      )
    }
  ]

  return (
    <div className="admin-page">
      <Tabs defaultActiveKey="products">
        <TabPane tab="产品管理" key="products">
          <div style={{ marginBottom: 16 }}>
            <Button type="primary" icon={<PlusOutlined />} onClick={() => {
              setEditingProduct(null)
              productForm.resetFields()
              setProductModalVisible(true)
            }}>
              添加产品
            </Button>
          </div>
          <Table
            columns={productColumns}
            dataSource={products}
            rowKey="id"
            loading={loading}
          />
        </TabPane>
        <TabPane tab="用户管理" key="users">
          <div style={{ marginBottom: 16 }}>
            <Button type="primary" icon={<PlusOutlined />} onClick={() => {
              setEditingUser(null)
              userForm.resetFields()
              setUserModalVisible(true)
            }}>
              添加用户
            </Button>
          </div>
          <Table
            columns={userColumns}
            dataSource={users}
            rowKey="id"
          />
        </TabPane>
        <TabPane tab="订单管理" key="orders">
          <Table
            columns={orderColumns}
            dataSource={orders}
            rowKey="id"
          />
        </TabPane>
      </Tabs>

      <Modal
        title={editingUser ? '编辑用户' : '添加用户'}
        open={userModalVisible}
        onCancel={() => {
          setUserModalVisible(false)
          userForm.resetFields()
          setEditingUser(null)
        }}
        footer={null}
      >
        <Form form={userForm} onFinish={handleUserSubmit} layout="vertical">
          <Form.Item name="username" label="用户名" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          {!editingUser && (
            <Form.Item name="password" label="密码" rules={[{ required: true, min: 6 }]}>
              <Input.Password />
            </Form.Item>
          )}
          <Form.Item name="email" label="邮箱">
            <Input />
          </Form.Item>
          <Form.Item name="phone" label="手机">
            <Input />
          </Form.Item>
          <Form.Item name="role" label="角色" rules={[{ required: true }]}>
            <Select>
              <Option value="USER">用户</Option>
              <Option value="ADMIN">管理员</Option>
            </Select>
          </Form.Item>
          {editingUser && (
            <Form.Item name="enabled" label="状态">
              <Select>
                <Option value={true}>启用</Option>
                <Option value={false}>禁用</Option>
              </Select>
            </Form.Item>
          )}
          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              {editingUser ? '更新' : '创建'}
            </Button>
          </Form.Item>
        </Form>
      </Modal>

      <Modal
        title={editingProduct ? '编辑产品' : '添加产品'}
        open={productModalVisible}
        onCancel={() => {
          setProductModalVisible(false)
          productForm.resetFields()
          setEditingProduct(null)
        }}
        footer={null}
      >
        <Form form={productForm} onFinish={handleProductSubmit} layout="vertical">
          <Form.Item name="name" label="产品名称" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="category" label="分类" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="price" label="价格" rules={[{ required: true }]}>
            <InputNumber min={0} precision={2} style={{ width: '100%' }} />
          </Form.Item>
          <Form.Item name="description" label="描述">
            <TextArea rows={4} />
          </Form.Item>
          <Form.Item name="imageUrl" label="图片URL">
            <Input placeholder="请输入图片URL" />
          </Form.Item>
          <Form.Item name="available" label="状态" initialValue={true}>
            <Select>
              <Option value={true}>在售</Option>
              <Option value={false}>下架</Option>
            </Select>
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit" block>
              {editingProduct ? '更新' : '创建'}
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </div>
  )
}

export default Admin

