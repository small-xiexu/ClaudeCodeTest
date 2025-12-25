#!/bin/bash
# MCP Server 启动脚本

# 获取脚本所在目录
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$DIR"

# 1. 编译 HelloMcpServer.java (如果还没编译或源文件更新)
if [ ! -f "HelloMcpServer.class" ] || [ "HelloMcpServer.java" -nt "HelloMcpServer.class" ]; then
    echo "[MCP] Compiling HelloMcpServer.java..." >&2

    # 获取 Maven classpath
    CP=$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout 2>/dev/null)

    if [ -z "$CP" ]; then
        echo "[MCP] Error: Failed to get Maven classpath" >&2
        exit 1
    fi

    # 编译
    javac -cp "$CP" HelloMcpServer.java

    if [ $? -ne 0 ]; then
        echo "[MCP] Error: Compilation failed" >&2
        exit 1
    fi

    echo "[MCP] Compilation successful" >&2
fi

# 2. 运行 MCP Server
CP=$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout 2>/dev/null)
java -cp ".:$CP" HelloMcpServer
