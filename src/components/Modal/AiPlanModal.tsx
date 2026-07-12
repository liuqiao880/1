import { useState } from 'react';
import { X, Sparkles, Calendar, Check, RefreshCw, AlertCircle } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';
import { useChatStore } from '@/store/useChatStore';
import { aiService } from '@/services/aiService';
import { Task } from '@/types/task';

export default function AiPlanModal() {
  const { showAiModal, setShowAiModal, addTask } = useTaskStore();
  const { aiConfig } = useChatStore();
  const [step, setStep] = useState<'form' | 'loading' | 'preview' | 'error'>('form');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [generatedTasks, setGeneratedTasks] = useState<Task[]>([]);
  const [selectedIds, setSelectedIds] = useState<Set<number>>(new Set());
  const [errorMsg, setErrorMsg] = useState('');

  const close = () => {
    setShowAiModal(false);
    setTimeout(() => {
      setStep('form');
      setTitle('');
      setDescription('');
      setDueDate('');
      setGeneratedTasks([]);
      setSelectedIds(new Set());
      setErrorMsg('');
    }, 300);
  };

  const handleGenerate = async () => {
    if (!title.trim()) return;
    setStep('loading');
    setErrorMsg('');

    const prompt = `请帮我拆解以下任务目标：
标题：${title}
${description ? `描述：${description}\n` : ''}
${dueDate ? `截止日期：${dueDate}\n` : ''}
请将目标拆解为可执行的子任务清单。`;

    try {
      const response = await aiService.chat(
        [{ role: 'user', content: prompt }],
        aiConfig
      );
      const tasks = aiService.parseTasksFromResponse(response);
      if (tasks.length === 0) {
        setErrorMsg('AI 未返回有效任务，请重试或修改描述');
        setStep('error');
        return;
      }
      setGeneratedTasks(tasks);
      const allIds = new Set<number>();
      const collectIds = (list: Task[]) => {
        list.forEach((t) => {
          allIds.add(t.id);
          if (t.children) collectIds(t.children);
        });
      };
      collectIds(tasks);
      setSelectedIds(allIds);
      setStep('preview');
    } catch (error) {
      setErrorMsg(error instanceof Error ? error.message : 'AI 请求失败，请检查配置');
      setStep('error');
    }
  };

  const toggleTask = (id: number) => {
    setSelectedIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  };

  const toggleAll = () => {
    const allIds = new Set<number>();
    const collectIds = (list: Task[]) => {
      list.forEach((t) => {
        allIds.add(t.id);
        if (t.children) collectIds(t.children);
      });
    };
    collectIds(generatedTasks);
    if (selectedIds.size === allIds.size) {
      setSelectedIds(new Set());
    } else {
      setSelectedIds(allIds);
    }
  };

  const handleConfirm = () => {
    const isDemo = !aiConfig.apiKey;
    const flattenSelected = (list: Task[]): Task[] => {
      const result: Task[] = [];
      list.forEach((t) => {
        if (selectedIds.has(t.id)) {
          const newId = Date.now() + Math.floor(Math.random() * 100000);
          const newTask: Task = {
            ...t,
            id: newId,
            children: t.children ? flattenSelected(t.children).map((c, i) => ({
              ...c,
              id: newId + i + 1,
              parentId: newId,
            })) : undefined,
          };
          result.push(newTask);
        } else if (t.children) {
          result.push(...flattenSelected(t.children));
        }
      });
      return result;
    };

    const toImport = flattenSelected(generatedTasks);
    toImport.forEach((t) => addTask(t));

    setShowAiModal(false);
    setTimeout(() => {
      setStep('form');
      setTitle('');
      setDescription('');
      setDueDate('');
      setGeneratedTasks([]);
      setSelectedIds(new Set());
    }, 300);
  };

  if (!showAiModal) return null;

  const isDemoMode = !aiConfig.apiKey;
  const selectedCount = selectedIds.size;
  const totalCount = generatedTasks.reduce((acc, t) => {
    let count = 0;
    const countAll = (task: Task) => {
      count++;
      if (task.children) task.children.forEach(countAll);
    };
    countAll(t);
    return acc + count;
  }, 0);

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center">
      <div
        className="absolute inset-0 bg-black/30 backdrop-blur-sm animate-fadeIn"
        onClick={close}
      />

      <div className="relative w-full sm:max-w-md bg-paper-cream dark:bg-gray-900 sm:rounded-sm shadow-2xl animate-slideUp max-h-[85vh] flex flex-col border border-line-separator dark:border-gray-800">
        {/* 头部 */}
        <div className="flex items-center justify-between px-6 pt-5 pb-3 border-b border-line-separator dark:border-gray-800">
          <div className="flex items-center gap-2.5">
            <div className="w-8 h-8 bg-newspaper-red flex items-center justify-center text-white">
              <Sparkles size={16} />
            </div>
            <div>
              <h2 className="font-serif text-lg font-semibold text-ink-black dark:text-white leading-none">AI 智能规划</h2>
              <p className="text-xs text-ink-light dark:text-gray-400 font-sans mt-1">输入目标，AI 为你拆解</p>
            </div>
          </div>
          <button
            onClick={close}
            className="w-9 h-9 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors active:scale-90"
          >
            <X size={18} className="text-ink-light" />
          </button>
        </div>

        {/* 演示模式提示 */}
        {isDemoMode && step === 'form' && (
          <div className="mx-6 mt-4 flex items-start gap-2 p-3 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5">
            <AlertCircle size={14} className="text-newspaper-red mt-0.5 flex-shrink-0" />
            <p className="text-xs text-newspaper-red dark:text-newspaper-red-light font-sans">
              未配置 API Key，当前为演示模式（使用模拟数据）。请在设置中配置 AI API 以使用真实规划。
            </p>
          </div>
        )}

        {/* 内容区 */}
        <div className="flex-1 overflow-y-auto p-6">
          {step === 'form' && (
            <div className="space-y-5">
              <div>
                <label className="block text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
                  任务目标
                </label>
                <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="例如：完成毕业论文"
                  className="w-full px-4 py-3 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white placeholder-ink-light outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all font-sans text-sm"
                />
              </div>

              <div>
                <label className="block text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
                  补充说明（选填）
                </label>
                <textarea
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="描述更多细节，让 AI 规划更精准"
                  rows={3}
                  className="w-full px-4 py-3 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white placeholder-ink-light outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all resize-none font-sans text-sm"
                />
              </div>

              <div>
                <label className="block text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
                  <span className="flex items-center gap-1">
                    <Calendar size={14} />
                    截止日期（选填）
                  </span>
                </label>
                <input
                  type="date"
                  value={dueDate}
                  onChange={(e) => setDueDate(e.target.value)}
                  className="w-full px-4 py-3 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all font-sans text-sm"
                />
              </div>
            </div>
          )}

          {step === 'loading' && (
            <div className="py-10 space-y-4">
              <div className="flex items-center justify-center mb-6">
                <div className="relative w-16 h-16">
                  <div className="absolute inset-0 border-4 border-newspaper-red/10" />
                  <div className="absolute inset-0 border-4 border-transparent border-t-newspaper-red animate-spin" />
                  <Sparkles
                    size={24}
                    className="absolute inset-0 m-auto text-newspaper-red animate-pulse"
                  />
                </div>
              </div>
              <p className="text-center text-ink-gray dark:text-gray-300 font-serif font-medium">
                AI 正在为你规划任务...
              </p>
              <p className="text-center text-sm text-ink-light dark:text-gray-500 font-sans">
                预计需要几秒钟
              </p>

              <div className="mt-6 space-y-3">
                {[1, 2, 3, 4].map((i) => (
                  <div
                    key={i}
                    className="h-12 bg-paper-white dark:bg-gray-800 shimmer border border-line-separator dark:border-gray-800"
                    style={{ animationDelay: `${i * 100}ms` }}
                  />
                ))}
              </div>
            </div>
          )}

          {step === 'error' && (
            <div className="py-10 text-center">
              <div className="w-14 h-14 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5 flex items-center justify-center mx-auto mb-4">
                <AlertCircle size={24} className="text-newspaper-red" />
              </div>
              <p className="font-serif font-medium text-ink-black dark:text-white mb-2">规划失败</p>
              <p className="text-xs text-ink-light dark:text-gray-400 mb-6 font-sans">{errorMsg}</p>
              <div className="flex gap-3">
                <button
                  onClick={() => setStep('form')}
                  className="flex-1 h-11 font-serif font-semibold text-ink-gray dark:text-gray-300 bg-transparent border border-line-separator dark:border-gray-700 hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-[0.98] text-sm"
                >
                  返回修改
                </button>
                <button
                  onClick={handleGenerate}
                  className="flex-1 h-11 font-serif font-semibold text-paper-white bg-ink-black hover:bg-newspaper-red transition-all active:scale-[0.98] text-sm"
                >
                  重试
                </button>
              </div>
            </div>
          )}

          {step === 'preview' && (
            <div>
              <div className="flex items-center justify-between mb-4">
                <h3 className="font-serif font-semibold text-ink-black dark:text-white text-sm">
                  已生成 {totalCount} 个任务
                </h3>
                <div className="flex items-center gap-3">
                  <button
                    onClick={toggleAll}
                    className="text-xs text-ink-gray dark:text-gray-400 hover:text-ink-black dark:hover:text-white font-sans"
                  >
                    {selectedCount === totalCount ? '取消全选' : '全选'}
                  </button>
                  <button
                    onClick={handleGenerate}
                    className="flex items-center gap-1 text-xs text-newspaper-red dark:text-newspaper-red-light hover:underline font-sans"
                  >
                    <RefreshCw size={12} />
                    重新生成
                  </button>
                </div>
              </div>

              <div className="space-y-2">
                <PreviewTaskList
                  tasks={generatedTasks}
                  selectedIds={selectedIds}
                  onToggle={toggleTask}
                  depth={0}
                />
              </div>

              <p className="mt-4 text-xs text-ink-light dark:text-gray-500 text-center font-sans">
                点击任务可取消勾选，确认后将保存选中的任务
              </p>
            </div>
          )}
        </div>

        {/* 底部按钮 */}
        <div className="px-6 py-4 border-t border-line-separator dark:border-gray-800">
          {step === 'form' && (
            <button
              onClick={handleGenerate}
              disabled={!title.trim()}
              className={`w-full h-11 font-serif font-semibold text-paper-white transition-all active:scale-[0.98] text-sm ${
                title.trim()
                  ? 'bg-ink-black hover:bg-newspaper-red'
                  : 'bg-line-separator dark:bg-gray-700 cursor-not-allowed'
              }`}
            >
              开始规划
            </button>
          )}

          {step === 'loading' && (
            <button
              disabled
              className="w-full h-11 font-serif font-semibold text-paper-white bg-line-separator dark:bg-gray-700 cursor-not-allowed flex items-center justify-center gap-2 text-sm"
            >
              <div className="w-4 h-4 border-2 border-paper-white/30 border-t-paper-white rounded-full animate-spin" />
              规划中...
            </button>
          )}

          {step === 'preview' && (
            <div className="flex gap-3">
              <button
                onClick={() => setStep('form')}
                className="flex-1 h-11 font-serif font-semibold text-ink-gray dark:text-gray-300 bg-transparent border border-line-separator dark:border-gray-700 hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-[0.98] text-sm"
              >
                返回修改
              </button>
              <button
                onClick={handleConfirm}
                disabled={selectedCount === 0}
                className={`flex-1 h-11 font-serif font-semibold text-paper-white transition-all active:scale-[0.98] text-sm ${
                  selectedCount > 0
                    ? 'bg-ink-black hover:bg-newspaper-red'
                    : 'bg-line-separator dark:bg-gray-700 cursor-not-allowed'
                }`}
              >
                确认保存 ({selectedCount})
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

function PreviewTaskList({
  tasks,
  selectedIds,
  onToggle,
  depth,
}: {
  tasks: Task[];
  selectedIds: Set<number>;
  onToggle: (id: number) => void;
  depth: number;
}) {
  const priorityColors: Record<number, string> = {
    1: 'bg-priority-high',
    2: 'bg-priority-medium',
    3: 'bg-priority-low',
  };

  return (
    <>
      {tasks.map((task) => (
        <div key={task.id}>
          <div
            onClick={() => onToggle(task.id)}
            className={`flex items-center gap-3 p-3 border transition-all cursor-pointer ${
              depth > 0 ? 'ml-6' : ''
            } ${
              selectedIds.has(task.id)
                ? 'bg-newspaper-red/5 dark:bg-newspaper-red/10 border-newspaper-red/20 dark:border-newspaper-red/30'
                : 'bg-paper-white dark:bg-gray-800/50 border-line-separator dark:border-gray-700 opacity-60'
            }`}
          >
            <div
              className={`w-5 h-5 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all ${
                selectedIds.has(task.id)
                  ? 'bg-ink-black border-ink-black'
                  : 'border-ink-light dark:border-gray-500'
              }`}
            >
              <Check
                size={11}
                className={`text-paper-white transition-all ${
                  selectedIds.has(task.id) ? 'opacity-100' : 'opacity-0'
                }`}
              />
            </div>
            <div className={`mt-1 w-0.5 h-3.5 ${priorityColors[task.priority]} flex-shrink-0`} />
            <div className="flex-1 min-w-0">
              <p className={`text-sm font-sans ${selectedIds.has(task.id) ? 'text-ink-black dark:text-white' : 'text-ink-light dark:text-gray-400 line-through'}`}>
                {task.title}
              </p>
              {task.description && (
                <p className="text-[10px] text-ink-light dark:text-gray-400 mt-0.5 font-sans">{task.description}</p>
              )}
            </div>
            <span className="text-[10px] text-newspaper-red dark:text-newspaper-red-light px-1.5 py-0.5 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5 font-sans">
              AI
            </span>
          </div>
          {task.children && task.children.length > 0 && (
            <PreviewTaskList
              tasks={task.children}
              selectedIds={selectedIds}
              onToggle={onToggle}
              depth={depth + 1}
            />
          )}
        </div>
      ))}
    </>
  );
}
