# 编程规范

## 一、项目结构规范

### 1.1 目录结构

```
FusionOrder/
├── server/              # SpringBoot 后端项目
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/fusion/
│   │   │   │       ├── controller/    # 控制器层
│   │   │   │       ├── service/       # 服务层
│   │   │   │       ├── repository/    # 数据访问层
│   │   │   │       ├── entity/        # 实体类
│   │   │   │       ├── dto/           # 数据传输对象
│   │   │   │       ├── config/        # 配置类
│   │   │   │       ├── exception/     # 异常处理
│   │   │   │       └── util/          # 工具类
│   │   │   └── resources/
│   │   │       ├── application.yml    # 应用配置
│   │   │       └── application-dev.yml
│   │   └── test/                      # 测试代码
│   └── pom.xml                        # Maven配置
│
└── client/             # React + Vite 前端项目
    ├── src/
    │   ├── pages/      # 页面组件
    │   │   ├── Home.jsx        # 首页
    │   │   ├── Products.jsx   # 产品列表页
    │   │   ├── ProductDetail.jsx # 产品详情页
    │   │   ├── Login.jsx      # 登录页
    │   │   ├── Register.jsx   # 注册页
    │   │   └── Admin.jsx      # 管理页
    │   ├── components/ # 公共组件
    │   │   └── Layout.jsx     # 布局组件
    │   ├── utils/      # 工具类
    │   │   ├── api.js         # API请求封装
    │   │   └── auth.js        # 认证工具
    │   ├── App.jsx     # 根组件
    │   └── main.jsx    # 入口文件
    ├── index.html      # HTML模板
    ├── vite.config.js  # Vite配置
    ├── package.json    # 依赖配置
    └── tsconfig.json   # TypeScript配置（可选）
```

### 1.2 模块划分

- **用户管理模块** (`user`): 注册、登录、增删改查、权限管理
- **产品管理模块** (`product`): 产品信息增删改查、分类管理、图片上传
- **表单/订单模块** (`order`): 订货信息收集、订单管理

## 二、代码风格规范

### 2.1 SpringBoot 后端规范

#### 2.1.1 命名规范

- **类名**: 大驼峰命名，如 `UserController`、`ProductService`
- **方法名**: 小驼峰命名，如 `getUserById`、`createProduct`
- **变量名**: 小驼峰命名，如 `userId`、`productName`
- **常量**: 全大写下划线分隔，如 `MAX_PAGE_SIZE`
- **包名**: 全小写，如 `com.fusion.controller`

#### 2.1.2 Controller 层规范

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        // 实现逻辑
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserCreateDTO dto) {
        // 实现逻辑
    }
}
```

- 所有 Controller 使用 `@RestController`
- 统一使用 `/api` 作为接口前缀
- 使用 `ResponseEntity` 返回响应
- 使用 `@Valid` 进行参数校验
- 使用 RESTful 风格的 URL 设计

#### 2.1.3 Service 层规范

```java
@Service
@Transactional
public class UserService {
    
    public UserDTO getUserById(Long id) {
        // 业务逻辑
    }
    
    public void deleteUser(Long id) {
        // 业务逻辑
    }
}
```

- Service 接口和实现分离
- 使用 `@Transactional` 管理事务
- 方法命名清晰表达业务意图

#### 2.1.4 Repository 层规范

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByEmail(String email);
}
```

- 继承 `JpaRepository` 或使用自定义查询
- 方法名遵循 JPA 命名约定

#### 2.1.5 Entity 规范

```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
```

- 使用 Lombok 简化代码（`@Data`、`@Entity` 等）
- 字段添加适当的约束注解
- 统一使用时间戳字段：`createdAt`、`updatedAt`

### 2.2 React + Vite 前端规范

#### 2.2.1 命名规范

- **组件文件名**: 大驼峰命名，如 `ProductList.jsx`、`UserProfile.jsx`
- **工具文件名**: 小驼峰命名，如 `api.js`、`auth.js`
- **组件名**: 大驼峰命名，与文件名一致，如 `ProductList`、`UserProfile`
- **变量名**: 小驼峰命名，如 `userId`、`productList`
- **常量**: 全大写下划线分隔，如 `MAX_PAGE_SIZE`、`API_BASE_URL`
- **CSS文件名**: 与组件文件名一致，如 `ProductList.css`

#### 2.2.2 组件规范

```jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

/**
 * 产品列表组件
 * 展示产品列表，支持搜索和筛选
 */
const ProductList = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const response = await axios.get('/api/products');
      setProducts(response.data.data);
    } catch (error) {
      console.error('获取产品列表失败:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="product-list">
      {/* 组件内容 */}
    </div>
  );
};

export default ProductList;
```

- 使用函数式组件和 Hooks
- 组件顶部添加注释说明组件功能
- 使用语义化的 className
- 错误处理和加载状态要明确

#### 2.2.3 API 请求规范

```javascript
import axios from 'axios';

// API基础配置
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器：添加Token
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 响应拦截器：统一处理响应
api.interceptors.response.use(
  (response) => {
    // 统一响应格式处理
    if (response.data.code === 200) {
      return response.data.data;
    }
    throw new Error(response.data.message || '请求失败');
  },
  (error) => {
    // 统一错误处理
    if (error.response?.status === 401) {
      // 未授权，跳转登录页
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

- 使用 axios 进行 HTTP 请求
- 配置请求和响应拦截器
- 统一处理 Token 和错误
- API 响应格式统一处理

#### 2.2.4 路由规范

```jsx
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Home from './pages/Home';
import Products from './pages/Products';
import ProductDetail from './pages/ProductDetail';
import Login from './pages/Login';
import Register from './pages/Register';
import Admin from './pages/Admin';
import Layout from './components/Layout';

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Home />} />
          <Route path="products" element={<Products />} />
          <Route path="products/:id" element={<ProductDetail />} />
          <Route path="login" element={<Login />} />
          <Route path="register" element={<Register />} />
          <Route path="admin" element={<Admin />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
```

- 使用 React Router 进行路由管理
- 路由路径清晰、语义化
- 需要权限的页面添加路由守卫

#### 2.2.5 状态管理规范

- 简单状态使用 `useState`
- 复杂状态或跨组件共享使用 Context API 或状态管理库（如 Redux）
- API 数据统一通过自定义 Hook 管理

## 三、数据库规范

### 3.1 表命名规范

- 表名: 小写下划线，复数形式，如 `users`、`products`、`orders`
- 字段名: 小写下划线，如 `user_id`、`product_name`、`created_at`

### 3.2 字段规范

- 主键: 统一使用 `id`，类型为 `BIGINT`，自增
- 时间字段: `created_at`、`updated_at`，类型为 `DATETIME`
- 删除标记: 使用软删除，字段名为 `deleted_at`，类型为 `DATETIME`，默认 `NULL`
- 外键: 命名格式为 `关联表名_id`，如 `user_id`、`product_id`

### 3.3 索引规范

- 主键自动创建索引
- 外键字段创建索引
- 经常查询的字段创建索引
- 唯一约束字段自动创建唯一索引

### 3.4 示例表结构

```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_email (email),
    INDEX idx_role (role)
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    INDEX idx_category (category)
);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    product_id BIGINT,
    quantity INT NOT NULL,
    contact_info VARCHAR(100) NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME DEFAULT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status)
);
```

## 四、API 设计规范

### 4.1 RESTful 设计

- **GET**: 查询资源，如 `GET /api/users`、`GET /api/users/{id}`
- **POST**: 创建资源，如 `POST /api/users`
- **PUT**: 完整更新资源，如 `PUT /api/users/{id}`
- **PATCH**: 部分更新资源，如 `PATCH /api/users/{id}`
- **DELETE**: 删除资源，如 `DELETE /api/users/{id}`

### 4.2 统一响应格式

```json
{
  "code": 200,
  "message": "success",
  "data": {},
  "timestamp": "2024-01-01T00:00:00"
}
```

- 成功: `code: 200`
- 客户端错误: `code: 400`
- 未授权: `code: 401`
- 禁止访问: `code: 403`
- 资源不存在: `code: 404`
- 服务器错误: `code: 500`

### 4.3 分页规范

```json
{
  "code": 200,
  "data": {
    "content": [],
    "total": 100,
    "page": 1,
    "size": 10,
    "totalPages": 10
  }
}
```

- 默认每页 10 条
- 查询参数: `page`（页码，从1开始）、`size`（每页数量）

## 五、权限管理规范

### 5.1 角色定义

- **ADMIN**: 管理员，拥有所有权限
- **USER**: 普通用户，可以查看产品、提交订单
- **MANAGER**: 产品管理员，可以管理产品

### 5.2 权限控制

- 使用 JWT Token 进行身份认证
- 使用 `@PreAuthorize` 或自定义注解进行权限验证
- 敏感操作需要记录操作日志

### 5.3 安全规范

- 密码必须加密存储（BCrypt）
- 敏感信息不返回前端
- 所有接口需要防 SQL 注入、XSS 攻击
- 文件上传需要校验文件类型和大小

## 六、异常处理规范

### 6.1 统一异常处理

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException e) {
        // 处理逻辑
    }
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException e) {
        // 处理逻辑
    }
}
```

### 6.2 异常分类

- **业务异常**: 如资源不存在、权限不足
- **验证异常**: 参数校验失败
- **系统异常**: 数据库连接失败、系统错误

## 七、日志规范

### 7.1 日志级别

- **ERROR**: 系统错误、异常信息
- **WARN**: 警告信息、潜在问题
- **INFO**: 关键业务流程、重要操作
- **DEBUG**: 调试信息（仅开发环境）

### 7.2 日志格式

```java
log.info("用户登录成功, userId: {}, username: {}", userId, username);
log.error("删除用户失败, userId: {}, error: {}", userId, e.getMessage(), e);
```

## 八、注释和文档规范

### 8.1 代码注释

- 类注释: 说明类的职责和用途
- 方法注释: 说明方法功能、参数、返回值
- 复杂逻辑: 添加行内注释说明

```java
/**
 * 用户服务类
 * 负责用户相关的业务逻辑处理
 */
@Service
public class UserService {
    
    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户DTO对象
     * @throws ResourceNotFoundException 用户不存在时抛出
     */
    public UserDTO getUserById(Long id) {
        // 实现逻辑
    }
}
```

### 8.2 API 文档

- **必须使用 Swagger/OpenAPI 配置交互式接口文档**
- 所有接口需要添加 Swagger 注解说明
- 接口注释清晰，包含参数说明、返回值说明、示例

#### 8.2.1 Swagger 配置规范

```java
@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FusionOrder API 文档")
                        .version("1.0.0")
                        .description("FusionOrder 订单管理系统 API 接口文档"))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
```

#### 8.2.2 Controller 中 Swagger 注解示例

```java
@RestController
@RequestMapping("/api/products")
@Tag(name = "产品管理", description = "产品相关的增删改查接口")
public class ProductController {
    
    @GetMapping
    @Operation(summary = "获取产品列表", description = "支持按分类、关键词搜索产品")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "查询成功"),
        @ApiResponse(responseCode = "400", description = "参数错误")
    })
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProducts(
            @Parameter(description = "产品分类") @RequestParam(required = false) String category,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword) {
        // 实现逻辑
    }
    
    @PostMapping
    @Operation(summary = "创建产品", description = "需要管理员或产品管理员权限")
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "产品信息")
            @Valid @RequestBody Product product) {
        // 实现逻辑
    }
}
```

#### 8.2.3 Swagger 访问地址

- 开发环境: `http://localhost:8080/api/swagger-ui.html`
- 或: `http://localhost:8080/api/swagger-ui/index.html`
- JSON 格式: `http://localhost:8080/api/v3/api-docs`

## 九、Swagger API 文档规范

### 9.1 Swagger 依赖配置

在 `pom.xml` 中添加 Swagger 依赖：

```xml
<!-- Swagger/OpenAPI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

### 9.2 Swagger 配置类

创建 `SwaggerConfig.java` 配置类，配置 API 文档的基本信息和安全认证。

### 9.3 接口注解规范

- 所有 Controller 类添加 `@Tag` 注解
- 所有接口方法添加 `@Operation` 注解
- 请求参数添加 `@Parameter` 注解
- 请求体添加 `@RequestBody` 注解说明
- 返回值添加 `@ApiResponses` 注解说明

### 9.4 Swagger UI 访问

- 开发环境必须可访问 Swagger UI
- 访问地址: `http://localhost:8080/api/swagger-ui/index.html`
- 文档必须包含所有接口的完整说明

## 十、配置规范

### 10.1 数据库配置

**数据库配置信息**（根据 design.md 要求）：
- **数据库类型**: MySQL
- **数据库名**: `fusion`
- **用户名**: `fusion`
- **密码**: `741852`
- **主机**: `localhost:3306`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fusion?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: fusion
    password: 741852
    driver-class-name: com.mysql.cj.jdbc.Driver
```

#### 9.1.1 数据库初始化

在项目启动前，确保已创建数据库：

```sql
CREATE DATABASE IF NOT EXISTS fusion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 9.1.2 JPA 配置

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # 开发环境使用 update，生产环境使用 validate
    show-sql: true      # 开发环境显示SQL，生产环境设为false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true
```

### 10.2 环境配置

- 开发环境: `application-dev.yml`
- 生产环境: `application-prod.yml`
- 敏感信息使用环境变量或配置中心

### 10.3 前端配置

前端使用 Vite，配置文件 `vite.config.js`：

```javascript
export default defineConfig({
  plugins: [react()],
  server: {
    host: '0.0.0.0',
    port: 3000,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  }
});
```

**前端运行命令**：
- 开发环境: `npm run dev`
- 生产构建: `npm run build`
- 预览构建: `npm run preview`

## 十一、测试规范

### 11.1 单元测试

- Service 层必须有单元测试
- 测试覆盖率目标: 80% 以上
- 使用 JUnit 5 + Mockito

### 11.2 集成测试

- 关键业务流程需要集成测试
- API 接口需要集成测试

### 11.3 前端测试

- 关键组件需要有单元测试
- 使用 React Testing Library 进行组件测试
- API 请求需要 Mock 数据测试

## 十二、版本控制规范

### 12.1 Git 提交规范

- 提交信息格式: `类型: 简短描述`
- 类型: `feat`（新功能）、`fix`（修复）、`docs`（文档）、`refactor`（重构）、`test`（测试）、`chore`（构建/工具）、`style`（格式）

**提交示例**：
```
feat: 添加用户注册功能
fix: 修复产品列表分页问题
docs: 更新API文档
refactor: 重构订单服务代码
test: 添加用户服务单元测试
```

### 12.2 分支规范

- `main`: 主分支，稳定版本，仅接受来自 develop 的合并
- `develop`: 开发分支，日常开发在此分支进行
- `feature/xxx`: 功能分支，从 develop 创建，完成后合并回 develop
- `hotfix/xxx`: 热修复分支，从 main 创建，修复后合并回 main 和 develop
- `release/xxx`: 发布分支，用于发布前的准备工作

### 12.3 分支命名示例

```
feature/user-management
feature/product-crud
hotfix/login-bug
release/v1.0.0
```

## 十三、代码审查规范

- 所有代码提交前必须经过代码审查
- 审查重点: 代码规范、业务逻辑、性能、安全性
- 审查通过后方可合并到主分支
- PR（Pull Request）必须包含：功能说明、测试情况、影响范围

## 十四、项目启动规范

### 14.1 后端启动

```bash
# 切换到server目录
cd server

# Maven方式启动
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/fusion-order-server-1.0.0.jar
```

**启动前检查**：
1. Java 17 已安装并配置环境变量
2. MySQL 数据库已启动
3. 数据库 `fusion` 已创建
4. 端口 8080 未被占用

### 14.2 前端启动

```bash
# 切换到client目录
cd client

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev
```

**启动前检查**：
1. Node.js (>= 16) 已安装
2. 依赖已安装（`node_modules` 存在）
3. 端口 3000 未被占用

### 14.3 访问地址

- **后端API**: `http://localhost:8080/api`
- **Swagger文档**: `http://localhost:8080/api/swagger-ui/index.html`
- **前端应用**: `http://localhost:3000`

## 十五、注释规范补充

### 15.1 代码注释要求

- **所有代码文件必须包含详细注释**
- 类注释：说明类的职责、用途、作者
- 方法注释：说明方法功能、参数、返回值、异常
- 复杂逻辑：添加行内注释说明
- 字段注释：说明字段的用途和约束

### 15.2 注释示例

```java
/**
 * 用户服务类
 * 负责用户相关的业务逻辑处理，包括注册、登录、查询、更新、删除等功能
 * 
 * @author FusionOrder Team
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    /**
     * 用户注册
     * 验证用户名和邮箱的唯一性，加密密码后保存用户信息
     * 
     * @param user 用户实体对象，包含用户名、密码、邮箱等信息
     * @return 用户DTO对象，不包含敏感信息
     * @throws BusinessException 用户名或邮箱已存在时抛出
     */
    @Transactional
    public UserDTO register(User user) {
        // 实现逻辑
    }
}
```

---

**重要说明**：

1. **如果需要执行命令，请暂停创建文件，让我先执行命令。**
2. **为这个项目中的所有代码写上详细注释。**
3. **必须配置 Swagger 交互式接口文档。**
4. **前端使用 React + Vite，不是 NestJS。**
5. **数据库配置严格按照 design.md 要求：用户名 fusion，密码 741852，数据库名 fusion。**
6. **为这个项目中的所有代码写上详细注释。
**注意**: 本文档需要根据项目实际情况持续更新和完善。