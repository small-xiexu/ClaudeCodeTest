---
description: 智能生成 Commit Message。若未暂存代码，会自动检测工作区变更并发出提醒。
allowed-tools: Bash(git diff*, git status*, git ls-files*)
---

你是一位资深的 Git 专家。你的任务是为我生成一条 Commit Message。

请分析以下三个维度的 Git 状态数据：

**1. [优先级高] 暂存区变更 (Staged Changes):**
> 这些是用户明确准备提交的内容。
!`git diff --staged --name-status`
!`git diff --staged`

**2. [优先级低] 工作区变更 (Unstaged Changes):**
> 这些是用户修改了但忘记 `git add` 的内容。
!`git diff --name-status`
!`git diff`

**3. [新文件] 未跟踪文件 (Untracked Files):**
!`git ls-files --others --exclude-standard`

---

**🧠 智能处理逻辑 (必须严格执行):**

**情况 A: 暂存区 (Staged) 不为空**
* **动作**: 忽略工作区变更，**只**根据暂存区的内容生成 Commit Message。
* **输出**: 直接输出符合 Conventional Commits 规范的消息。

**情况 B: 暂存区为空，但工作区/未跟踪区有内容**
* **动作**: 这是用户可能忘记了 `git add`。
* **输出**:
    1.  先输出一行中文警告：`⚠️ **警告**: 检测到变更，但你还没有暂存 (git add) 任何内容！`
    2.  列出检测到的修改文件。
    3.  基于这些**未暂存**的内容，生成一条“预览版” Commit Message，并用中文提示：“如果你打算提交这些文件，可以先 `git add .`，参考消息如下：”

**情况 C: 所有区域都为空**
* **动作**: 用户的目录是干净的。
* **输出**: `✨ 工作区是干净的 (Working tree clean)，没有需要提交的内容。`

---

**Commit Message 规范:**
- 格式: `<type>(<scope>): <subject>`
- 语言: **简体中文 (Simplified Chinese)**
- 示例: `fix(order): 将金额计算逻辑重构为 BigDecimal 以解决精度丢失问题`
