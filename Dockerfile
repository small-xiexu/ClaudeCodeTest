# 使用多阶段构建优化镜像大小
# 阶段1: 构建应用
FROM maven:3.8.6-openjdk-8-slim AS builder

WORKDIR /app

# 先复制 pom.xml 并下载依赖（利用 Docker 缓存）
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 复制源代码并构建
COPY src ./src
RUN mvn clean package -DskipTests -B

# 阶段2: 运行应用
FROM amazoncorretto:8

WORKDIR /app

# 复制构建好的 jar 包
COPY --from=builder /app/target/ClaudeCodeTest-1.0-SNAPSHOT.jar app.jar

# 暴露端口
EXPOSE 8080

# 设置 JVM 参数
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
