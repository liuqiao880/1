import { useState } from 'react';
import { X, Sparkles, Calendar, Check, RefreshCw } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';

export default function AiPlanModal() {
  const { showAiModal, setShowAiModal } = useTaskStore();
  const [step, setStep] = useState<'form' | 'loading' | 'preview'>('form');
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [generatedTasks, setGeneratedTasks] = useState<
    { id: number; title: string; selected: boolean; daysFromNow: number }[]
  >([]);

  const close = () => {
    setShowAiModal(false);
    setTimeout(() => {
      setStep('form');
      setTitle('');
      setDescription('');
      setDueDate('');
      setGeneratedTasks([]);
    }, 300);
  };

  const handleGenerate = () => {
    if (!title.trim()) return;
    setStep('loading');

    setTimeout(() => {
      const mockSubtasks = [
        { id: 1, title: '明确目标与范围', selected: true, daysFromNow: 0 },
        { id: 2, title: '拆解关键步骤', selected: true, daysFromNow: 1 },
        { id: 3, title: '制定执行计划', selected: true, daysFromNow: 2 },
        { id: 4, title: '分配资源与时间', selected: true, daysFromNow: 3 },
        { id: 5, title: '执行并定期复盘', selected: false, daysFromNow: 5 },
      ];
      setGeneratedTasks(mockSubtasks);
      setStep('preview');
    }, 2000);
  };

  const toggleTask = (id: number) => {
    setGeneratedTasks((prev) =>
      prev.map((t) => (t.id === id ? { ...t, selected: !t.selected } : t))
    );
  };

  const handleConfirm = () => {
    alert('已保存 AI 生成的任务（演示）');
    close();
  };

  if (!showAiModal) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center">
      {/* 遮罩 */}
      <div
        className="absolute inset-0 bg-black/30 backdrop-blur-sm animate-fadeIn"
        onClick={close}
      />

      {/* 弹窗内容 */}
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

              {/* 骨架屏 */}
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

          {step === 'preview' && (
            <div>
              <div className="flex items-center justify-between mb-4">
                <h3 className="font-serif font-semibold text-ink-black dark:text-white text-sm">
                  已生成 {generatedTasks.length} 个子任务
                </h3>
                <button
                  onClick={handleGenerate}
                  className="flex items-center gap-1 text-xs text-newspaper-red dark:text-newspaper-red-light hover:underline font-sans"
                >
                  <RefreshCw size={12} />
                  重新生成
                </button>
              </div>

              <div className="space-y-2">
                {generatedTasks.map((task, index) => (
                  <div
                    key={task.id}
                    onClick={() => toggleTask(task.id)}
                    className={`flex items-center gap-3 p-3 border transition-all cursor-pointer ${
                      task.selected
                        ? 'bg-newspaper-red/5 dark:bg-newspaper-red/10 border-newspaper-red/20 dark:border-newspaper-red/30'
                        : 'bg-paper-white dark:bg-gray-800/50 border-line-separator dark:border-gray-700 opacity-60'
                    }`}
                  >
                    <div
                      className={`w-5 h-5 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all ${
                        task.selected
                          ? 'bg-ink-black border-ink-black'
                          : 'border-ink-light dark:border-gray-500'
                      }`}
                    >
                      <Check
                        size={11}
                        className={`text-paper-white transition-all ${
                          task.selected ? 'opacity-100' : 'opacity-0'
                        }`}
                      />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p
                        className={`text-sm font-sans ${
                          task.selected
                            ? 'text-ink-black dark:text-white'
                            : 'text-ink-light dark:text-gray-400 line-through'
                        }`}
                      >
                        {task.title}
                      </p>
                      <p className="text-xs text-ink-light dark:text-gray-500 mt-0.5 font-sans">
                        第 {index + 1} 天
                      </p>
                    </div>
                    <span className="text-[10px] text-newspaper-red dark:text-newspaper-red-light px-1.5 py-0.5 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5 font-sans">
                      AI
                    </span>
                  </div>
                ))}
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
                disabled={generatedTasks.filter((t) => t.selected).length === 0}
                className={`flex-1 h-11 font-serif font-semibold text-paper-white transition-all active:scale-[0.98] text-sm ${
                  generatedTasks.filter((t) => t.selected).length > 0
                    ? 'bg-ink-black hover:bg-newspaper-red'
                    : 'bg-line-separator dark:bg-gray-700 cursor-not-allowed'
                }`}
              >
                确认保存
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
