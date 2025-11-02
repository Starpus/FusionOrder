import React, { useState, useEffect } from 'react'
import { Card, Row, Col, Input, Select, Spin, message, Image } from 'antd'
import { useNavigate } from 'react-router-dom'
import api from '../utils/api'
import './Products.css'

const { Search } = Input
const { Option } = Select

const Products = () => {
  const [products, setProducts] = useState([])
  const [loading, setLoading] = useState(false)
  const [categories, setCategories] = useState([])
  const [selectedCategory, setSelectedCategory] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    fetchProducts()
  }, [selectedCategory])

  const fetchProducts = async () => {
    setLoading(true)
    try {
      const params = {}
      if (selectedCategory) {
        params.category = selectedCategory
      } else {
        params.available = true
      }
      const data = await api.get('/products', { params })
      setProducts(data)
      
      const uniqueCategories = [...new Set(data.map(p => p.category))]
      setCategories(uniqueCategories)
    } catch (error) {
      console.error('加载产品失败:', error)
      message.error(error.message || '加载产品失败')
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = async (value) => {
    if (!value) {
      fetchProducts()
      return
    }
    setLoading(true)
    try {
      const data = await api.get('/products', { params: { keyword: value } })
      setProducts(data)
    } catch (error) {
      console.error('搜索失败:', error)
      message.error(error.message || '搜索失败')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="products-page">
      <div className="products-header">
        <h1>产品列表</h1>
        <div className="products-filters">
          <Search
            placeholder="搜索产品名称"
            onSearch={handleSearch}
            style={{ width: 300, marginRight: 16 }}
          />
          <Select
            placeholder="选择分类"
            style={{ width: 200 }}
            value={selectedCategory}
            onChange={setSelectedCategory}
            allowClear
          >
            {categories.map(cat => (
              <Option key={cat} value={cat}>{cat}</Option>
            ))}
          </Select>
        </div>
      </div>

      <Spin spinning={loading}>
        <Row gutter={[16, 16]}>
          {products.map(product => (
            <Col xs={24} sm={12} md={8} lg={6} key={product.id}>
              <Card
                hoverable
                cover={
                  product.imageUrl ? (
                    <Image
                      alt={product.name}
                      src={`/api${product.imageUrl}`}
                      fallback="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMIAAADDCAYAAADQvc6UAAABRWlDQ1BJQ0MgUHJvZmlsZQAAKJFjYGASSSwoyGFhYGDIzSspCnJ3UoiIjFJgf8LAwSDCIMogwMCcmFxc4BgQ4ANUwgCjUcG3awyMIPqyLsis7PPOq3QdDFcvjV3jOD1boQVTPQrgSkktTgbSf4A4LbmgqISBgTEFyFYuLykAsTuAbJEioKOA7DkgdjqEvQHEToKwj4DVhAQ5A9k3gGyB5IxEoBmML4BsnSQk8XQkNtReEOBxcfXxUQg1Mjc0dyHgXNJBSWpFCYh2zi+oLMpMzyhRcASGUqqCZ16yno6CkYGRAQMDKMwhXjHHAPGdkHSTyF7STALAoYwBTIfAhJPwQjK9XYGDY0taCwOADKNvZ8BwT1yBnBsHCJwLCfnWL4H/39DygwMbLcODDy+vBgb6lBkZ9QLxCbTvZGBgaP9L//8/zGBgYr8HA8A4H1Glv4MDAH8ZAwD1F34jDxXhXgAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAAwqADAAQAAAABAAAAwwAAAAD9b/HnAAAHlklEQVR4Ae3dP3Ik1RnG4W+FgYxN"
                      preview={false}
                      style={{ height: 200, objectFit: 'cover' }}
                    />
                  ) : (
                    <div style={{ height: 200, background: '#f0f0f0', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                      暂无图片
                    </div>
                  )
                }
                onClick={() => navigate(`/products/${product.id}`)}
              >
                <Card.Meta
                  title={product.name}
                  description={
                    <div>
                      <div>分类: {product.category}</div>
                      <div style={{ marginTop: 8, color: '#ff4d4f', fontSize: '18px', fontWeight: 'bold' }}>
                        ¥{product.price}
                      </div>
                    </div>
                  }
                />
              </Card>
            </Col>
          ))}
        </Row>
      </Spin>
    </div>
  )
}

export default Products

