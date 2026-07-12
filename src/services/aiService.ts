import { Task } from '@/types/task';

const DEFAULT_SYSTEM_PROMPT = `你是一位专业的任务拆分师，擅长将复杂的目标拆解为可执行的具体任务。

你的职责：
1. 深入理解用户的目标和需求
2. 将大目标拆解为层级清晰的任务清单（支持父子任务）
3. 为每个任务分配合适的优先级（1紧急/2普通/3低优）
4. 预估每个任务的大致时间节点
5. 给出合理的执行顺序建议

输出格式要求：
请以 JSON 格式输出任务列表，格式如下：
{
  "tasks": [
    {
      "title": "任务标题",
      "description": "任务描述（可选）",
      "priority": 1,
      "dueDaysFromNow": 0,
      "children": [...]
    }
  ]
}

重要：
- 先回应用户，用自然语言简要分析和说明
- 然后在回复末尾用 \`\`\`json 代码块包裹任务 JSON
- 优先级 1=紧急，2=普通，3=低优
- dueDaysFromNow 表示从今天起的天数偏移
- 支持嵌套子任务，最多 2 层`;

let taskIdCounter = 0;
function genTaskId(): number {
  return Date.now() * 1000 + (taskIdCounter++ % 1000);
}

export const aiService = {
  defaultSystemPrompt: DEFAULT_SYSTEM_PROMPT,

  async chat(
    messages: { role: string; content: string }[],
    config: { apiKey: string; baseUrl: string; model: string; systemPrompt?: string }
  ): Promise<string> {
    let { apiKey, baseUrl, model, systemPrompt = DEFAULT_SYSTEM_PROMPT } = config;

    // 规范化 Base URL：去除末尾斜杠和 /chat/completions 后缀
    baseUrl = baseUrl.trim().replace(/\/+$/, '');
    if (baseUrl.endsWith('/chat/completions')) {
      baseUrl = baseUrl.slice(0, -'/chat/completions'.length);
    }
    // 如果没有 http 前缀，自动加上 https
    if (baseUrl && !/^https?:\/\//.test(baseUrl)) {
      baseUrl = 'https://' + baseUrl;
    }

    if (!apiKey) {
      const mock = this.mockResponse(messages);
      return `> ⚠️ **演示模式**：未配置 API Key，以下为模拟数据。请在设置中配置 AI API 以使用真实规划。\n\n${mock}`;
    }

    if (!baseUrl) {
      throw new Error('请先配置 Base URL');
    }

    if (!model) {
      throw new Error('请先配置 Model 名称');
    }

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 30000);

    try {
      const systemMsg = { role: 'system', content: systemPrompt };
      const allMessages = [systemMsg, ...messages];

      const response = await fetch(`${baseUrl}/chat/completions`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${apiKey}`,
        },
        body: JSON.stringify({
          model,
          messages: allMessages,
          temperature: 0.5,
        }),
        signal: controller.signal,
      });

      if (!response.ok) {
        const errorText = await response.text().catch(() => '');
        throw new Error(`API 请求失败 (${response.status}): ${errorText.slice(0, 100)}`);
      }

      const contentType = response.headers.get('content-type') || '';
      if (!contentType.includes('application/json')) {
        const text = await response.text().catch(() => '');
        throw new Error(`服务器返回非 JSON 响应（可能 Base URL 错误）: ${text.slice(0, 80)}`);
      }

      const data = await response.json();
      const content = data.choices?.[0]?.message?.content;
      if (typeof content === 'string') return content;
      if (Array.isArray(content)) return content.map((c: any) => c.text || '').join('');
      return '';
    } catch (error) {
      if (error instanceof DOMException && error.name === 'AbortError') {
        throw new Error('请求超时（30秒），请检查网络或稍后重试');
      }
      if (error instanceof TypeError && error.message.includes('fetch')) {
        throw new Error('网络请求失败，请检查 Base URL 是否正确或网络连接');
      }
      throw error;
    } finally {
      clearTimeout(timeoutId);
    }
  },

  mockResponse(messages: { role: string; content: string }[]): string {
    const lastUserMsg = [...messages].reverse().find((m) => m.role === 'user')?.content || '';

    const hasKeyword = (keywords: string[]) =>
      keywords.some((k) => lastUserMsg.includes(k));

    let tasks: any[] = [];
    let analysis = '';

    if (hasKeyword(['学习', '课程', '考试', '复习', '读书', '阅读'])) {
      analysis = '好的！我来帮你规划这个学习任务。根据你的目标，我拆分为以下几个阶段：';
      tasks = [
        { title: '制定学习计划', description: '明确学习目标和时间安排', priority: 2, dueDaysFromNow: 0, children: [
          { title: '梳理知识大纲', priority: 2, dueDaysFromNow: 0 },
          { title: '分配每日学习时间', priority: 2, dueDaysFromNow: 1 },
        ]},
        { title: '基础概念学习', description: '系统学习核心知识点', priority: 1, dueDaysFromNow: 2, children: [
          { title: '阅读教材第1-3章', priority: 1, dueDaysFromNow: 2 },
          { title: '做章节配套练习', priority: 1, dueDaysFromNow: 3 },
          { title: '整理笔记和重点', priority: 2, dueDaysFromNow: 4 },
        ]},
        { title: '实战练习', description: '通过习题巩固知识', priority: 1, dueDaysFromNow: 5, children: [
          { title: '完成模拟题一', priority: 1, dueDaysFromNow: 5 },
          { title: '错题整理与复习', priority: 1, dueDaysFromNow: 6 },
        ]},
        { title: '总复习与总结', priority: 2, dueDaysFromNow: 7 },
      ];
    } else if (hasKeyword(['项目', '开发', '产品', '上线', '需求', '设计'])) {
      analysis = '好的！这是一个项目型任务，我按照标准项目流程为你拆解：';
      tasks = [
        { title: '需求分析与规划', description: '明确项目目标和范围', priority: 1, dueDaysFromNow: 0, children: [
          { title: '收集用户需求', priority: 1, dueDaysFromNow: 0 },
          { title: '确定功能优先级', priority: 2, dueDaysFromNow: 1 },
          { title: '制定项目排期', priority: 2, dueDaysFromNow: 1 },
        ]},
        { title: '设计阶段', description: '产品设计和技术方案', priority: 1, dueDaysFromNow: 2, children: [
          { title: '原型设计', priority: 1, dueDaysFromNow: 2 },
          { title: '技术方案评审', priority: 1, dueDaysFromNow: 3 },
          { title: 'UI 设计稿', priority: 2, dueDaysFromNow: 4 },
        ]},
        { title: '开发实现', description: '核心功能开发', priority: 1, dueDaysFromNow: 5, children: [
          { title: '搭建项目架构', priority: 1, dueDaysFromNow: 5 },
          { title: '开发核心功能模块', priority: 1, dueDaysFromNow: 8 },
          { title: '联调与测试', priority: 2, dueDaysFromNow: 12 },
        ]},
        { title: '上线发布', priority: 2, dueDaysFromNow: 14 },
      ];
    } else if (hasKeyword(['健身', '减肥', '运动', '锻炼', '跑步'])) {
      analysis = '好的！我来为你规划健身计划，循序渐进才能坚持下去：';
      tasks = [
        { title: '体能测试与目标设定', priority: 2, dueDaysFromNow: 0, children: [
          { title: '测量身体基础数据', priority: 2, dueDaysFromNow: 0 },
          { title: '设定阶段性目标', priority: 2, dueDaysFromNow: 0 },
        ]},
        { title: '第一阶段：适应期（2周）', description: '培养运动习惯', priority: 1, dueDaysFromNow: 1, children: [
          { title: '每周3次轻度有氧运动', priority: 1, dueDaysFromNow: 1 },
          { title: '学习正确的动作姿势', priority: 2, dueDaysFromNow: 3 },
          { title: '调整饮食结构', priority: 2, dueDaysFromNow: 5 },
        ]},
        { title: '第二阶段：提升期（4周）', description: '增加训练强度', priority: 1, dueDaysFromNow: 14, children: [
          { title: '加入力量训练', priority: 1, dueDaysFromNow: 14 },
          { title: '提高有氧强度', priority: 1, dueDaysFromNow: 21 },
        ]},
      ];
    } else {
      analysis = '好的！我来帮你拆解这个任务。根据你的描述，我建议这样安排：';
      tasks = [
        { title: '明确目标与范围', priority: 1, dueDaysFromNow: 0, children: [
          { title: '梳理具体需求', priority: 1, dueDaysFromNow: 0 },
          { title: '确定优先级排序', priority: 2, dueDaysFromNow: 1 },
        ]},
        { title: '制定执行计划', priority: 1, dueDaysFromNow: 2, children: [
          { title: '拆分关键步骤', priority: 1, dueDaysFromNow: 2 },
          { title: '预估每个步骤耗时', priority: 2, dueDaysFromNow: 3 },
        ]},
        { title: '执行并定期复盘', priority: 1, dueDaysFromNow: 4 },
      ];
    }

    const jsonStr = JSON.stringify({ tasks }, null, 2);
    return `${analysis}\n\n\`\`\`json\n${jsonStr}\n\`\`\``;
  },

  parseTasksFromResponse(content: string): Task[] {
    // 1. 尝试匹配 ```json 或 ``` 代码块
    const codeBlockMatch = content.match(/```(?:json|JSON)?\s*([\s\S]*?)\s*```/);
    let jsonStr: string | null = null;

    if (codeBlockMatch) {
      jsonStr = codeBlockMatch[1];
    } else {
      // 2. 尝试直接提取 JSON 对象
      const objStart = content.indexOf('{');
      const objEnd = content.lastIndexOf('}');
      if (objStart !== -1 && objEnd !== -1 && objEnd > objStart) {
        jsonStr = content.slice(objStart, objEnd + 1);
      }
    }

    if (!jsonStr) return [];

    try {
      const parsed = JSON.parse(jsonStr);
      const rawTasks = Array.isArray(parsed) ? parsed : (parsed.tasks || []);
      if (!Array.isArray(rawTasks)) return [];
      return this.convertToTasks(rawTasks);
    } catch {
      return [];
    }
  },

  convertToTasks(raw: any[], parentId: number | null = null): Task[] {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const todayTs = today.getTime();
    const dayMs = 24 * 60 * 60 * 1000;

    const convert = (items: any[], pid: number | null): Task[] => {
      return items.map((item) => {
        const id = genTaskId();
        const dueDays = Number(item.dueDaysFromNow);
        const dueDate = Number.isFinite(dueDays) && dueDays >= 0
          ? todayTs + dueDays * dayMs
          : undefined;

        const validPriority = [1, 2, 3].includes(Number(item.priority)) ? Number(item.priority) : 2;
        const title = typeof item.title === 'string' && item.title.trim() ? item.title.trim() : '未命名任务';

        const task: Task = {
          id,
          title,
          description: typeof item.description === 'string' ? item.description : undefined,
          status: 'todo',
          priority: validPriority as 1 | 2 | 3,
          dueDate,
          parentId: pid,
          order: 0,
          aiGenerated: true,
          createTime: Date.now(),
          pomodoroCount: 0,
        };

        if (Array.isArray(item.children) && item.children.length > 0) {
          task.children = convert(item.children, id);
        }

        return task;
      });
    };

    return convert(raw, parentId);
  },

  /** 统一的任务扁平化工具：正确处理父子关系和 ID 生成 */
  flattenSelectedTasks(tasks: Task[], selectedIds: Set<number>): Task[] {
    const result: Task[] = [];

    const flatten = (list: Task[], forcedParentId: number | null): void => {
      list.forEach((t) => {
        if (selectedIds.has(t.id)) {
          const newId = genTaskId();
          const newTask: Task = {
            ...t,
            id: newId,
            parentId: forcedParentId,
            children: undefined,
          };
          result.push(newTask);
          if (t.children) {
            flatten(t.children, newId);
          }
        } else if (t.children) {
          // 父未选中但子可能选中，子提升到当前层级
          flatten(t.children, forcedParentId);
        }
      });
    };

    flatten(tasks, null);
    return result;
  },
};
