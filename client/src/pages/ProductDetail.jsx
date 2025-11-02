import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { Card, Image, Typography, Button, Form, Input, InputNumber, message, Spin, Divider } from 'antd'
import api from '../utils/api'
import './ProductDetail.css'

const { Title, Text, Paragraph } = Typography
const { TextArea } = Input

const ProductDetail = () => {
  const { id } = useParams()
  const navigate = useNavigate()
  const [product, setProduct] = useState(null)
  const [loading, setLoading] = useState(false)
  const [form] = Form.useForm()

  useEffect(() => {
    fetchProduct()
  }, [id])

  const fetchProduct = async () => {
    setLoading(true)
    try {
      const data = await api.get(`/products/${id}`)
      setProduct(data)
    } catch (error) {
      message.error('加载产品详情失败')
      navigate('/products')
    } finally {
      setLoading(false)
    }
  }

  const handleSubmit = async (values) => {
    try {
      await api.post('/orders', {
        product: { id: parseInt(id) },
        quantity: values.quantity,
        contactName: values.contactName,
        contactPhone: values.contactPhone,
        contactEmail: values.contactEmail,
        requirements: values.requirements
      })
      message.success('订单提交成功！我们会尽快联系您')
      form.resetFields()
    } catch (error) {
      message.error('订单提交失败，请稍后重试')
    }
  }

  if (loading) {
    return <Spin size="large" style={{ display: 'block', textAlign: 'center', marginTop: 100 }} />
  }

  if (!product) {
    return null
  }

  return (
    <div className="product-detail-page">
      <div className="product-detail-container">
        <Card>
          <div className="product-info">
            <div className="product-image-section">
              {product.imageUrl ? (
                <Image
                  src={`/api${product.imageUrl}`}
                  alt={product.name}
                  fallback="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMIAAADDCAYAAADQvc6UAAABRWlDQ1BJQ0MgUHJvZmlsZQAAKJFjYGASSSwoyGFhYGDIzSspCnJ3UoiIjFJgf8LAwSDCIMogwMCcmFxc4BgQ4ANUwgCjUcG3awyMIPqyLsis7PPOq3QdDFcvjV3jOD1boQVTPQrgSkktTgbSf4A4LbmgqISBgTEFyFYuLykAsTuAbJEioKOA7DkgdjqEvQHEToKwj4DVhAQ5A9k3gGyB5IxEoBmML4BsnSQk8XQkNtReEOBxcfXxUQg1Mjc0dyHgXNJBSWpFCYh2zi+oLMpMzyhRcASGUqqCZ16yno6CkYGRAQMDKMwhXjHHAPGdkHSTyF7STALAoYwBTIfAhJPwQjK9XYGDY0taCwOADKNvZ8BwT1yBnBsHCJwLCfnWL4H/39DygwMbLcODDy+vBgb6lBkZ9QLxCbTvZGBgaP9L//8/zGBgYr8HA8A4H1Glv4MDAH8ZAwD1F34jDxXhXgAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAAwqADAAQAAAABAAAAwwAAAAD9b/HnAAAHlklEQVR4Ae3dP3Ik1RnG4W+FgYxN"
                  style={{ width: '100%', maxWidth: 500 }}
                />
              ) : (
                <div style={{ width: 500, height: 400, background: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                  暂无图片
                </div>
              )}
            </div>
            <div className="product-details-section">
              <Title level={2}>{product.name}</Title>
              <Divider />
              <div className="product-meta">
                <Text strong>分类：</Text>
                <Text>{product.category}</Text>
              </div>
              <div className="product-price">
                <Text strong style={{ fontSize: '24px' }}>价格：</Text>
                <Text style={{ fontSize: '28px', color: '#ff4d4f', fontWeight: 'bold' }}>
                  ¥{product.price}
                </Text>
              </div>
              {product.description && (
                <div className="product-description">
                  <Title level={4}>产品描述</Title>
                  <Paragraph>{product.description}</Paragraph>
                </div>
              )}
            </div>
          </div>

          <Divider>填写订货信息</Divider>

          <Form
            form={form}
            layout="vertical"
            onFinish={handleSubmit}
            className="order-form"
          >
            <Form.Item
              name="quantity"
              label="订货量"
              rules={[{ required: true, message: '请输入订货量' }]}
            >
              <InputNumber min={1} style={{ width: '100%' }} placeholder="请输入订货数量" />
            </Form.Item>

            <Form.Item
              name="contactName"
              label="联系人姓名"
              rules={[{ required: true, message: '请输入联系人姓名' }]}
            >
              <Input placeholder="请输入您的姓名" />
            </Form.Item>

            <Form.Item
              name="contactPhone"
              label="联系电话"
              rules={[
                { required: true, message: '请输入联系电话' },
                { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码' }
              ]}
            >
              <Input placeholder="请输入手机号码" />
            </Form.Item>

            <Form.Item
              name="contactEmail"
              label="联系邮箱"
              rules={[
                { type: 'email', message: '请输入正确的邮箱地址' }
              ]}
            >
              <Input placeholder="请输入邮箱地址（可选）" />
            </Form.Item>

            <Form.Item
              name="requirements"
              label="其他需求"
            >
              <TextArea rows={4} placeholder="请描述您的具体需求（可选）" />
            </Form.Item>

            <Form.Item>
              <Button type="primary" htmlType="submit" size="large" block>
                提交订单
              </Button>
            </Form.Item>
          </Form>
        </Card>
      </div>
    </div>
  )
}

export default ProductDetail

