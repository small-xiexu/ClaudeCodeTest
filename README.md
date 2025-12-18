# 📚 图书管理系统 (Library Management System)

一个基于 Spring Boot + MySQL + MyBatis-Plus 的现代化图书管理系统，提供完整的 RESTful API 接口。

## ✨ 功能特性

- 📖 **图书管理**: 图书的增删改查、分类管理、库存管理、关键词搜索
- 👥 **读者管理**: 读者注册、信息管理、借阅限额控制
- 🔄 **借还管理**: 图书借阅、归还、逾期计算、罚金管理
- 📊 **记录查询**: 借阅历史、活跃借阅记录、逾期记录查询
- 🛡️ **业务校验**: 库存检查、借阅限额验证、参数校验、全局异常处理
- 📝 **API 文档**: 集成 Swagger UI，提供交互式 API 文档

## 🛠️ 技术栈

### 后端技术
- **框架**: Spring Boot 2.7.18
- **语言**: Java 8
- **构建工具**: Maven
- **数据库**: MySQL 8.0 (Docker)
- **ORM**: MyBatis-Plus 3.5.3.1
- **API 文档**: SpringDoc OpenAPI 1.6.15
- **工具库**: Hutool 5.8.25

### 架构特点
- RESTful API 设计
- 统一响应格式
- 全局异常处理
- 参数校验
- CORS 跨域支持
- 自动时间戳填充

## 📁 项目结构

```
ClaudeCodeTest/
├── src/
│   ├── main/
│   │   ├── java/com/xbk/
│   │   │   ├── LibraryApplication.java    # Spring Boot 启动类
│   │   │   ├── entity/                    # 实体类 (MyBatis-Plus)
│   │   │   │   ├── Book.java
│   │   │   │   ├── Reader.java
│   │   │   │   ├── BorrowRecord.java
│   │   │   │   └── Category.java
│   │   │   ├── mapper/                    # MyBatis-Plus Mapper 接口
│   │   │   │   ├── BookMapper.java
│   │   │   │   ├── ReaderMapper.java
│   │   │   │   ├── BorrowRecordMapper.java
│   │   │   │   └── CategoryMapper.java
│   │   │   ├── service/                   # 业务逻辑层接口
│   │   │   │   └── impl/                  # Service 实现类
│   │   │   ├── controller/                # REST API 控制器
│   │   │   │   ├── BookController.java
│   │   │   │   ├── ReaderController.java
│   │   │   │   ├── BorrowController.java
│   │   │   │   └── CategoryController.java
│   │   │   ├── dto/                       # 数据传输对象
│   │   │   │   ├── request/               # 请求 DTO
│   │   │   │   └── response/              # 响应 DTO
│   │   │   ├── vo/                        # 视图对象
│   │   │   │   └── ApiResponse.java       # 统一响应格式
│   │   │   ├── exception/                 # 异常处理
│   │   │   │   ├── BusinessException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   └── config/                    # 配置类
│   │   │       ├── MyMetaObjectHandler.java
│   │   │       ├── CorsConfig.java
│   │   │       └── SwaggerConfig.java
│   │   └── resources/
│   │       ├── application.yml            # 主配置文件
│   │       ├── application-dev.yml        # 开发环境配置
│   │       ├── application-test.yml       # 测试环境配置
│   │       ├── mapper/                    # MyBatis XML 映射文件
│   │       │   ├── BookMapper.xml
│   │       │   ├── ReaderMapper.xml
│   │       │   ├── BorrowRecordMapper.xml
│   │       │   └── CategoryMapper.xml
│   │       └── db/
│   │           └── schema.sql             # MySQL 数据库初始化脚本
│   └── test/
│       └── java/com/xbk/                  # 单元测试
├── pom.xml                                 # Maven 配置文件
└── README.md
```

## 🗄️ 数据库设计

### 核心表结构

- **category** - 图书分类表
- **book** - 图书信息表（外键关联 category）
- **reader** - 读者信息表
- **borrow_record** - 借阅记录表（外键关联 book、reader）

### 关键字段说明

**图书表 (book)**
- `isbn`: 国际标准书号 (唯一索引)
- `total_quantity`: 总藏书量
- `available_quantity`: 可借数量（带约束：0 ≤ available_quantity ≤ total_quantity）
- `location`: 图书馆位置
- `created_at`, `updated_at`: 自动时间戳

**借阅记录表 (borrow_record)**
- `status`: 借阅状态 (BORROWED/RETURNED)
- `due_date`: 应还日期
- `fine`: 罚金 (逾期计算，每天 1 元)
- `borrow_date`, `return_date`: 借还日期时间戳

### 数据库特性
- InnoDB 引擎，支持事务和外键
- UTF-8MB4 字符集，支持全部 Unicode 字符
- 复合索引优化查询性能
- CHECK 约束保证数据完整性

## 🚀 快速开始

### 环境要求

- JDK 8 或更高版本
- Maven 3.x
- Docker (用于运行 MySQL)

### 安装步骤

1. **克隆项目**
```bash
git clone https://github.com/small-xiexu/ClaudeCodeTest.git
cd ClaudeCodeTest
```

2. **启动 MySQL 数据库**
```bash
# 使用 Docker 启动 MySQL 8.0
docker run -d \
  --name mysql \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=library_db \
  mysql:8.0

# 等待数据库启动完成（约10秒）
sleep 10

# 执行数据库初始化脚本
docker exec -i mysql mysql -uroot -proot library_db < src/main/resources/db/schema.sql
```

3. **配置数据库连接**

编辑 `src/main/resources/application.yml`，确认数据库密码：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root  # 如果你的密码不同，请修改这里
```

4. **编译和运行**
```bash
# 编译项目
mvn clean install

# 启动 Spring Boot 应用
mvn spring-boot:run
```

应用启动成功后会显示：
```
========================================
图书管理系统启动成功!
API 文档地址: http://localhost:8080/api/swagger-ui.html
数据库类型: MySQL 8.0
========================================
```

5. **访问 API 文档**

打开浏览器访问：http://localhost:8080/api/swagger-ui.html

## 📡 API 接口

### 图书管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/books` | 获取所有图书 |
| GET | `/api/books/{id}` | 根据 ID 获取图书 |
| GET | `/api/books/isbn/{isbn}` | 根据 ISBN 获取图书 |
| GET | `/api/books/category/{categoryId}` | 根据分类获取图书 |
| GET | `/api/books/search` | 关键词搜索图书 |
| POST | `/api/books` | 添加图书 |
| PUT | `/api/books/{id}` | 更新图书信息 |
| DELETE | `/api/books/{id}` | 删除图书 |

### 读者管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/readers` | 获取所有读者 |
| GET | `/api/readers/{id}` | 根据 ID 获取读者 |
| GET | `/api/readers/card/{cardNumber}` | 根据借书证号获取读者 |
| POST | `/api/readers` | 注册读者 |
| PUT | `/api/readers/{id}` | 更新读者信息 |
| DELETE | `/api/readers/{id}` | 删除读者 |

### 借阅管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/borrows` | 借书 |
| POST | `/api/borrows/{id}/return` | 还书 |
| GET | `/api/borrows/reader/{readerId}/history` | 查询读者借阅历史 |
| GET | `/api/borrows/reader/{readerId}/active` | 查询读者当前借阅 |
| GET | `/api/borrows/overdue` | 查询逾期记录 |
| GET | `/api/borrows/{id}/fine` | 计算罚金 |

### 分类管理 API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/categories` | 获取所有分类 |
| GET | `/api/categories/{id}` | 根据 ID 获取分类 |
| POST | `/api/categories` | 添加分类 |
| PUT | `/api/categories/{id}` | 更新分类 |
| DELETE | `/api/categories/{id}` | 删除分类 |

## 📝 API 使用示例

### 示例 1: 添加图书

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "isbn": "978-7-115-54321-0",
    "title": "Java编程思想",
    "author": "Bruce Eckel",
    "publisher": "人民邮电出版社",
    "publishDate": "2020-01-01",
    "categoryId": 2,
    "totalQuantity": 3,
    "location": "A区-1层-001"
  }'
```

### 示例 2: 注册读者

```bash
curl -X POST http://localhost:8080/api/readers \
  -H "Content-Type: application/json" \
  -d '{
    "cardNumber": "R20251001",
    "name": "张三",
    "gender": "男",
    "phone": "13800138001",
    "email": "zhangsan@example.com",
    "address": "北京市朝阳区"
  }'
```

### 示例 3: 借阅图书

```bash
curl -X POST http://localhost:8080/api/borrows \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "readerId": 1,
    "borrowDays": 30
  }'
```

### 示例 4: 归还图书

```bash
curl -X POST http://localhost:8080/api/borrows/1/return
```

### 示例 5: 搜索图书

```bash
# 搜索书名或作者包含 "Java" 的图书
curl http://localhost:8080/api/books/search?keyword=Java
```

## 🏗️ 核心设计

### 分层架构

1. **Controller 层 (表现层)**
   - 处理 HTTP 请求和响应
   - 参数校验和转换
   - 调用 Service 层完成业务逻辑

2. **Service 层 (业务逻辑层)**
   - 实现业务逻辑和规则验证
   - 声明式事务管理（@Transactional）
   - 调用 Mapper 层完成数据操作

3. **Mapper 层 (数据访问层)**
   - MyBatis-Plus BaseMapper 提供基础 CRUD
   - 自定义复杂查询（XML 映射）
   - 自动填充时间戳字段

4. **Entity 层 (实体层)**
   - 对应数据库表结构的 Java 对象
   - MyBatis-Plus 注解映射
   - 实现 Serializable 接口

### 关键特性

- ✅ **声明式事务**: 使用 @Transactional 注解管理事务
- ✅ **统一响应**: ApiResponse 封装所有 API 响应
- ✅ **全局异常处理**: GlobalExceptionHandler 统一处理异常
- ✅ **参数校验**: 使用 JSR-303 Bean Validation
- ✅ **自动时间戳**: MetaObjectHandler 自动填充 created_at/updated_at
- ✅ **API 文档**: Swagger UI 提供可视化接口文档
- ✅ **CORS 支持**: 前后端分离开发友好

### 统一响应格式

所有 API 返回统一的 JSON 格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... },
  "timestamp": 1766076916341
}
```

### 异常处理

系统定义了多种业务异常：
- `BusinessException`: 业务逻辑异常（如库存不足）
- `ResourceNotFoundException`: 资源不存在异常
- 参数校验异常自动转换为 400 错误

## 📦 依赖说明

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-web | 2.7.18 | Spring Boot Web 框架 |
| spring-boot-starter-validation | 2.7.18 | 参数校验 |
| mysql-connector-java | 8.0.33 | MySQL 驱动 |
| mybatis-plus-boot-starter | 3.5.3.1 | MyBatis-Plus ORM |
| springdoc-openapi-ui | 1.6.15 | Swagger API 文档 |
| hutool-all | 5.8.25 | Java 工具库 |

## 🔧 配置说明

### 数据源配置

位于 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### MyBatis-Plus 配置

```yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.xbk.entity
  global-config:
    db-config:
      id-type: auto  # 主键自增
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # SQL 日志
```

### 应用配置

```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  profiles:
    active: dev  # 激活开发环境配置
```

## 🎯 功能演示

系统启动后可以通过 Swagger UI 测试以下完整流程：

1. ✓ 查看图书分类（预置 5 个分类）
2. ✓ 添加图书
3. ✓ 注册读者
4. ✓ 借阅图书（自动检查库存和限额）
5. ✓ 查看借阅记录
6. ✓ 归还图书（自动计算罚金）
7. ✓ 查询逾期记录

## 🔍 开发指南

### 添加新的 API 接口

1. 在 Controller 中添加新方法
2. 使用 Swagger 注解描述接口
3. 在 Service 中实现业务逻辑
4. 如需自定义查询，在 Mapper 和 XML 中添加

示例：
```java
@Operation(summary = "根据作者查询图书", description = "支持模糊匹配")
@GetMapping("/author/{author}")
public ApiResponse<List<BookResponse>> getBooksByAuthor(
    @Parameter(description = "作者名称") @PathVariable String author) {
    List<Book> books = bookService.findByAuthor(author);
    return ApiResponse.success(/* ... */);
}
```

### 运行测试

```bash
mvn test
```

### 打包部署

```bash
# 打包成 JAR
mvn clean package

# 运行 JAR
java -jar target/ClaudeCodeTest-1.0-SNAPSHOT.jar
```

## 🐛 常见问题

### Q: 启动时报错 "Access denied for user 'root'"
**A:** 检查 application.yml 中的数据库密码是否正确。

### Q: 启动时报错 "Communications link failure"
**A:** 确认 MySQL 容器已启动，端口 3306 未被占用。

### Q: API 返回 404
**A:** 确认 URL 路径包含 `/api` 前缀，例如 `http://localhost:8080/api/books`。

### Q: Swagger UI 无法访问
**A:** 访问 http://localhost:8080/api/swagger-ui.html （注意 `/api` 前缀）。

## 📄 License

MIT License

## 👤 作者

- **xiexu**
- GitHub: [@small-xiexu](https://github.com/small-xiexu)

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

### 技术改进建议
- [ ] 添加 Redis 缓存
- [ ] 集成 Spring Security 认证授权
- [ ] 添加日志系统（Logback）
- [ ] 完善单元测试和集成测试
- [ ] 添加 Vue 3 前端项目

---

⭐ 如果这个项目对你有帮助，欢迎 Star！
