---
description: 按照项目宪法实现新功能 (Plan -> Check -> Code)
argument-hint: <功能描述>
---

# 任务：实现新功能 "$ARGUMENTS"

## 上下文引用
请读取以下核心文件以确保合规：
- `constitution.md` (项目宪法 - 最高优先级)
- `spec.md` (如果有)

## 强制工作流 (Workflow)

你必须严格按照以下步骤执行，**严禁跳步**：

1.  **制定计划 (Plan)**:
    - 分析用户需求: "$ARGUMENTS"
    - 创建或更新 `plan.md`。
    - 列出受影响的模块。

2.  **合宪性审查 (Constitution Check)**:
    - 在 `plan.md` 中显式包含一个 Checkbox 列表。
    - **检查项**:
        - [ ] JDK 8 兼容性 (无 `var`, 无 `List.of`) ?
        - [ ] Lombok 限制 (无 `@Data`, 使用 `@Value`/`@Builder`) ?
        - [ ] 测试先行 (Test-First) ?
        - [ ] 依赖合规 (javax.*, 无 Jakarta) ?

3.  **等待批准**:
    - 输出 "计划已生成，请审查 `plan.md`。输入 'continue' 开始编写测试。" 并暂停。

4.  **实施 (Implementation)**:
    - **Step 1**: 编写失败的单元测试 (Red)。
    - **Step 2**: 编写业务代码 (Green)。
    - **Step 3**: 重构并优化 (Refactor)。

---
现在，请开始执行 **第1步：制定计划**。