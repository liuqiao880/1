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
        className="absolute inset-0 bg-black/50 backdrop-blur-sm animate-fadeIn"
        onClick={close}
      />

      {/* 弹窗内容 */}
      <div className="relative w-full sm:max-w-md bg-white dark:bg-gray-900 sm:rounded-3xl rounded-t-3xl shadow-2xl animate-slideUp max-h-[85vh] flex flex-col">
        {/* 头部 */}
        <div className="flex items-center justify-between px-6 pt-6 pb-4 border-b border-gray-100 dark:border-gray-800">
          <div className="flex items-center gap-2">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-purple-500 to-purple-600 flex items-center justify-center text-white">
              <Sparkles size={18} />
            </div>
            <div>
              <h2 className="text-lg font-bold text-gray-900 dark:text-white">AI 智能规划</h2>
              <p className="text-xs text-gray-500 dark:text-gray-400">输入目标，AI 为你拆解</p>
            </div>
          </div>
          <button
            onClick={close}
            className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors active:scale-90"
          >
            <X size={20} className="text-gray-500" />
          </button>
        </div>

        {/* 内容区 */}
        <div className="flex-1 overflow-y-auto p-6">
          {step === 'form' && (
            <div className="space-y-5">
              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  任务目标
                </label>
                <input
                  type="text"
                  value={title}
                  onChange={(e) => setTitle(e.target.value)}
                  placeholder="例如：完成毕业论文"
                  className="w-full px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-gray-900 dark:text-white placeholder-gray-400 outline-none focus:ring-2 focus:ring-purple-500/30 focus:border-purple-500 transition-all"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  补充说明（选填）
                </label>
                <textarea
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                  placeholder="描述更多细节，让 AI 规划更精准"
                  rows={3}
                  className="w-full px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-gray-900 dark:text-white placeholder-gray-400 outline-none focus:ring-2 focus:ring-purple-500/30 focus:border-purple-500 transition-all resize-none"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
                  <span className="flex items-center gap-1">
                    <Calendar size={14} />
                    截止日期（选填）
                  </span>
                </label>
                <input
                  type="date"
                  value={dueDate}
                  onChange={(e) => setDueDate(e.target.value)}
                  className="w-full px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-gray-900 dark:text-white outline-none focus:ring-2 focus:ring-purple-500/30 focus:border-purple-500 transition-all"
                />
              </div>
            </div>
          )}

          {step === 'loading' && (
            <div className="py-10 space-y-4">
              <div className="flex items-center justify-center mb-6">
                <div className="relative w-16 h-16">
                  <div className="absolute inset-0 rounded-full border-4 border-purple-100 dark:border-purple-900" />
                  <div className="absolute inset-0 rounded-full border-4 border-transparent border-t-purple-500 animate-spin" />
                  <Sparkles
                    size={24}
                    className="absolute inset-0 m-auto text-purple-500 animate-pulse"
                  />
                </div>
              </div>
              <p className="text-center text-gray-600 dark:text-gray-300 font-medium">
                AI 正在为你规划任务...
              </p>
              <p className="text-center text-sm text-gray-400 dark:text-gray-500">
                预计需要几秒钟
              </p>

              {/* 骨架屏 */}
              <div className="mt-6 space-y-3">
                {[1, 2, 3, 4].map((i) => (
                  <div
                    key={i}
                    className="h-14 rounded-xl bg-gray-100 dark:bg-gray-800 shimmer"
                    style={{ animationDelay: `${i * 100}ms` }}
                  />
                ))}
              </div>
            </div>
          )}

          {step === 'preview' && (
            <div>
              <div className="flex items-center justify-between mb-4">
                <h3 className="font-semibold text-gray-900 dark:text-white">
                  已生成 {generatedTasks.length} 个子任务
                </h3>
                <button
                  onClick={handleGenerate}
                  className="flex items-center gap-1 text-sm text-purple-600 dark:text-purple-400 hover:underline"
                >
                  <RefreshCw size={14} />
                  重新生成
                </button>
              </div>

              <div className="space-y-2">
                {generatedTasks.map((task, index) => (
                  <div
                    key={task.id}
                    onClick={() => toggleTask(task.id)}
                    className={`flex items-center gap-3 p-3 rounded-xl border transition-all cursor-pointer ${
                      task.selected
                        ? 'bg-purple-50 dark:bg-purple-950/30 border-purple-200 dark:border-purple-800'
                        : 'bg-gray-50 dark:bg-gray-800/50 border-gray-100 dark:border-gray-700 opacity-60'
                    }`}
                  >
                    <div
                      className={`w-5 h-5 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all ${
                        task.selected
                          ? 'bg-purple-500 border-purple-500'
                          : 'border-gray-300 dark:border-gray-500'
                      }`}
                    >
                      <Check
                        size={11}
                        className={`text-white transition-all ${
                          task.selected ? 'opacity-100' : 'opacity-0'
                        }`}
                      />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p
                        className={`text-sm font-medium ${
                          task.selected
                            ? 'text-gray-900 dark:text-white'
                            : 'text-gray-500 dark:text-gray-400 line-through'
                        }`}
                      >
                        {task.title}
                      </p>
                      <p className="text-xs text-gray-400 dark:text-gray-500 mt-0.5">
                        第 {index + 1} 天
                      </p>
                    </div>
                    <span className="text-xs text-purple-500 dark:text-purple-400 px-2 py-1 rounded-md bg-purple-100 dark:bg-purple-900/30">
                      AI
                    </span>
                  </div>
                ))}
              </div>

              <p className="mt-4 text-xs text-gray-400 dark:text-gray-500 text-center">
                点击任务可取消勾选，确认后将保存选中的任务
              </p>
            </div>
          )}
        </div>

        {/* 底部按钮 */}
        <div className="px-6 py-4 border-t border-gray-100 dark:border-gray-800">
          {step === 'form' && (
            <button
              onClick={handleGenerate}
              disabled={!title.trim()}
              className={`w-full h-12 rounded-xl font-semibold text-white transition-all active:scale-[0.98] ${
                title.trim()
                  ? 'bg-gradient-to-r from-purple-500 to-purple-600 shadow-lg shadow-purple-500/25 hover:shadow-xl hover:shadow-purple-500/30'
                  : 'bg-gray-300 dark:bg-gray-700 cursor-not-allowed'
              }`}
            >
              ✨ 开始规划
            </button>
          )}

          {step === 'loading' && (
            <button
              disabled
              className="w-full h-12 rounded-xl font-semibold text-white bg-gray-300 dark:bg-gray-700 cursor-not-allowed flex items-center justify-center gap-2"
            >
              <div className="w-5 h-5 border-2 border-white/30 border-t-white rounded-full animate-spin" />
              规划中...
            </button>
          )}

          {step === 'preview' && (
            <div className="flex gap-3">
              <button
                onClick={() => setStep('form')}
                className="flex-1 h-12 rounded-xl font-semibold text-gray-600 dark:text-gray-300 bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 transition-all active:scale-[0.98]"
              >
                返回修改
              </button>
              <button
                onClick={handleConfirm}
                disabled={generatedTasks.filter((t) => t.selected).length === 0}
                className={`flex-1 h-12 rounded-xl font-semibold text-white transition-all active:scale-[0.98] ${
                  generatedTasks.filter((t) => t.selected).length > 0
                    ? 'bg-gradient-to-r from-green-500 to-green-600 shadow-lg shadow-green-500/25 hover:shadow-xl'
                    : 'bg-gray-300 dark:bg-gray-700 cursor-not-allowed'
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
