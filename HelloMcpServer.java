import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.Scanner;

/**
 * MCP Server Java 实现 (纯净版)
 * 核心逻辑：监听标准输入 (stdin) -> 解析 JSON-RPC -> 执行业务逻辑 -> 输出标准输出 (stdout)
 */
public class HelloMcpServer {

    // Jackson ObjectMapper，用于处理 JSON 数据的序列化与反序列化
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        // 使用 Scanner 监听 System.in (标准输入流)，用于接收 MCP 客户端发送的指令
        try (Scanner scanner = new Scanner(System.in)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

                try {
                    // 1. 解析客户端发来的 JSON 请求
                    JsonNode request = mapper.readTree(line);

                    // 2. 基础校验：必须包含 JSON-RPC 的 method 字段
                    if (!request.has("method")) continue;

                    String method = request.get("method").asText();
                    JsonNode id = request.get("id");

                    // 3. 路由分发：根据 method 字段决定调用哪个处理函数
                    switch (method) {
                        case "initialize":
                            handleInitialize(id);
                            break;
                        case "tools/list":
                            handleToolsList(id);
                            break;
                        case "tools/call":
                            handleToolCall(id, request.get("params"));
                            break;
                        case "notifications/initialized":
                            // 客户端通知初始化完成，服务端无需响应，保持静默即可
                            break;
                        default:
                            // 遇到未定义的方法，返回标准 JSON-RPC 错误
                            sendError(id, -32601, "Method not found");
                    }
                } catch (Exception e) {
                    // 错误日志必须输出到 System.err，避免污染 System.out 的通信管道
                    System.err.println("Error processing request: " + e.getMessage());
                }
            }
        }
    }

    // === 核心处理逻辑 ===

    // 处理 initialize 请求：向客户端返回服务器元数据和支持的能力
    private static void handleInitialize(JsonNode id) {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");

        // 声明能力：告诉客户端本服务器支持 "tools" (工具调用)
        ObjectNode capabilities = mapper.createObjectNode();
        capabilities.putObject("tools");
        result.set("capabilities", capabilities);

        // 服务器信息
        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name", "hello-java-server");
        serverInfo.put("version", "1.0.0");
        result.set("serverInfo", serverInfo);

        sendResult(id, result);
    }

    // 处理 tools/list 请求：列出所有可用工具的定义 (Schema)
    private static void handleToolsList(JsonNode id) {
        ObjectNode result = mapper.createObjectNode();
        ArrayNode tools = result.putArray("tools");

        // --- 定义 "greet" 工具 ---
        ObjectNode greetTool = mapper.createObjectNode();
        greetTool.put("name", "greet");
        greetTool.put("description", "A simple tool that returns a greeting.");

        // 定义输入参数结构 (基于 JSON Schema 标准)
        ObjectNode inputSchema = mapper.createObjectNode();
        inputSchema.put("type", "object");
        ObjectNode properties = inputSchema.putObject("properties");

        // 参数: name
        ObjectNode nameProp = properties.putObject("name");
        nameProp.put("type", "string");
        nameProp.put("description", "The name of the person to greet.");

        // 设置必填字段
        ArrayNode required = inputSchema.putArray("required");
        required.add("name");

        greetTool.set("inputSchema", inputSchema);
        tools.add(greetTool);

        sendResult(id, result);
    }

    // 处理 tools/call 请求：执行具体的业务逻辑
    private static void handleToolCall(JsonNode id, JsonNode params) {
        String toolName = params.get("name").asText();

        if (!"greet".equals(toolName)) {
            sendError(id, -32601, "Tool not found");
            return;
        }

        // 解析工具参数
        JsonNode arguments = params.get("arguments");
        String name = arguments.has("name") ? arguments.get("name").asText() : "World";

        // === 这里的代码是真正的业务逻辑实现 ===
        String greeting = "Hello, " + name + "! Welcome to the world of MCP in Java.";

        // 构造执行结果
        ObjectNode result = mapper.createObjectNode();
        ArrayNode content = result.putArray("content");

        ObjectNode textContent = content.addObject();
        textContent.put("type", "text");
        textContent.put("text", greeting);

        sendResult(id, result);
    }

    // === 辅助方法 ===

    // 封装并发送成功响应 (JSON-RPC 2.0)
    private static void sendResult(JsonNode id, ObjectNode result) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", id);
        response.set("result", result);
        sendJson(response);
    }

    // 封装并发送错误响应
    private static void sendError(JsonNode id, int code, String message) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.set("id", id);

        ObjectNode error = response.putObject("error");
        error.put("code", code);
        error.put("message", message);

        sendJson(response);
    }

    // 底层输出方法：将 JSON 对象序列化并写入 System.out
    private static void sendJson(ObjectNode response) {
        try {
            String jsonStr = mapper.writeValueAsString(response);
            // MCP 协议强制要求：每条消息占一行，并以换行符结束
            System.out.println(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}