# FusionOrder 订单管理系统

基于 SpringBoot + React 的订单管理系统

## 项目结构

```
FusionOrder/
├── server/          # SpringBoot 后端项目
│   ├── src/
│   │   └── main/
│   │       ├── java/com/fusionorder/
│   │       │   ├── config/       # 配置类（Security等）
│   │       │   ├── controller/   # 控制器
│   │       │   ├── dto/          # 数据传输对象
│   │       │   ├── entity/       # 实体类
│   │       │   ├── filter/       # 过滤器
│   │       │   ├── repository/   # 数据访问层
│   │       │   ├── service/      # 业务逻辑层
│   │       │   └── util/         # 工具类
│   │       └── resources/
│   │           └── application.yml
│   └── pom.xml
├── client/          # React 前端项目
│   ├── src/
│   │   ├── components/   # 组件
│   │   ├── pages/        # 页面
│   │   └── utils/        # 工具类
│   ├── package.json
│   └── vite.config.js
└── design.md       # 设计文档
```

## 技术栈

### 后端
- Spring Boot 3.2.0
- Spring Security (JWT认证)
- Spring Data JPA
- MySQL 数据库
- Maven

### 前端
- React 18
- Ant Design 5
- React Router
- Axios
- Vite

## 功能模块

### 1. 用户管理模块
- 用户注册
- 用户登录（JWT认证）
- 用户增删改查
- 权限管理（USER/ADMIN角色）
- 用户状态管理（启用/禁用）

### 2. 产品管理模块
- 产品信息管理（名称、分类、价格、描述、图片）
- 产品增删改查
- 产品分类筛选
- 产品搜索
- 产品状态管理（在售/下架）

### 3. 订单表单模块
- 前端填写订货信息
- 订货量管理
- 联系方式管理（姓名、电话、邮箱）
- 需求描述
- 订单状态管理（待处理、已确认、处理中、已完成、已取消）

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- Maven 3.6+

### 后端启动

1. 进入 server 目录
```bash
cd server
```

2. 编译项目
```bash
mvn clean install
```

3. 运行项目
```bash
mvn spring-boot:run
```

后端服务将在 `http://localhost:8080/api` 启动

### 前端启动

1. 进入 client 目录
```bash
cd client
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

前端应用将在 `http://localhost:3000` 启动

## 数据库

项目使用 MySQL 数据库，需要预先创建数据库和用户。

### 数据库配置
- 数据库名：`fusion`
- 用户名：`fusion`
- 密码：`741852`
- 主机：`localhost`
- 端口：`3306`

### 创建数据库（如未创建）

```sql
CREATE DATABASE fusion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'fusion'@'localhost' IDENTIFIED BY '741852';
GRANT ALL PRIVILEGES ON fusion.* TO 'fusion'@'localhost';
FLUSH PRIVILEGES;
```

首次启动时会自动创建表结构（Hibernate ddl-auto: update）。

## API 接口

### 认证接口
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录

### 产品接口
- `GET /api/products` - 获取产品列表（支持分类、关键词筛选）
- `GET /api/products/{id}` - 获取产品详情
- `POST /api/products` - 创建产品（需认证）
- `PUT /api/products/{id}` - 更新产品（需认证）
- `DELETE /api/products/{id}` - 删除产品（需认证）

### 订单接口
- `POST /api/orders` - 创建订单
- `GET /api/orders` - 获取订单列表（支持产品ID、状态筛选）
- `GET /api/orders/{id}` - 获取订单详情
- `PUT /api/orders/{id}/status` - 更新订单状态（需认证）
- `DELETE /api/orders/{id}` - 删除订单（需认证）

### 用户管理接口（需ADMIN权限）
- `GET /api/admin/users` - 获取用户列表
- `GET /api/admin/users/{id}` - 获取用户详情
- `PUT /api/admin/users/{id}` - 更新用户信息
- `DELETE /api/admin/users/{id}` - 删除用户

## 使用说明

1. **首次使用**
   - 访问前端页面，注册一个账号
   - 首次注册的用户默认是 USER 角色
   - 需要在数据库中将某个用户设置为 ADMIN 角色才能访问管理后台

2. **产品管理**
   - 在首页或产品页面浏览产品
   - 点击产品查看详情并填写订单信息

3. **管理后台**
   - 使用 ADMIN 角色账号登录
   - 在管理后台可以进行产品、用户、订单的管理

## 注意事项

1. JWT Token 有效期为 24 小时
2. 图片上传功能需要配置文件存储路径（当前版本支持图片URL）
3. 生产环境请修改 `application.yml` 中的 JWT Secret 和数据库密码
4. 确保 MySQL 服务已启动，数据库和用户已创建
5. 数据库连接信息在 `server/src/main/resources/application.yml` 中配置

## 开发说明

### 后端
- 主启动类：`com.fusionorder.FusionOrderApplication`
- 配置文件：`server/src/main/resources/application.yml`

### 前端
- 入口文件：`client/src/main.jsx`
- 路由配置：`client/src/App.jsx`
- API 配置：`client/src/utils/api.js`

## 许可证

MIT License

