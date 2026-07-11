import { useState } from 'react';
import { Plus, Sparkles, PenLine, X } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';

export default function FloatingActionButton() {
  const [isOpen, setIsOpen] = useState(false);
  const { setShowAiModal } = useTaskStore();

  const toggle = () => setIsOpen(!isOpen);

  const handleAiPlan = () => {
    setShowAiModal(true);
    setIsOpen(false);
  };

  const handleQuickAdd = () => {
    alert('快速添加任务（演示）');
    setIsOpen(false);
  };

  return (
    <div className="fixed bottom-6 right-6 z-40">
      {/* 展开的菜单 */}
      <div
        className={`absolute bottom-16 right-0 flex flex-col gap-3 items-end transition-all duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
          isOpen ? 'opacity-100 translate-y-0 pointer-events-auto' : 'opacity-0 translate-y-4 pointer-events-none'
        }`}
      >
        <button
          onClick={handleAiPlan}
          className="flex items-center gap-2 px-4 py-3 bg-white dark:bg-gray-800 rounded-2xl shadow-lg border border-gray-100 dark:border-gray-700 hover:shadow-xl transition-all active:scale-95 group"
          style={{ transitionDelay: isOpen ? '50ms' : '0ms' }}
        >
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-purple-500 to-purple-600 flex items-center justify-center text-white">
            <Sparkles size={18} />
          </div>
          <div className="text-left">
            <p className="text-sm font-semibold text-gray-900 dark:text-white">AI 规划</p>
            <p className="text-xs text-gray-500 dark:text-gray-400">智能拆解任务</p>
          </div>
        </button>

        <button
          onClick={handleQuickAdd}
          className="flex items-center gap-2 px-4 py-3 bg-white dark:bg-gray-800 rounded-2xl shadow-lg border border-gray-100 dark:border-gray-700 hover:shadow-xl transition-all active:scale-95"
          style={{ transitionDelay: isOpen ? '0ms' : '50ms' }}
        >
          <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-gray-600 to-gray-700 flex items-center justify-center text-white">
            <PenLine size={18} />
          </div>
          <div className="text-left">
            <p className="text-sm font-semibold text-gray-900 dark:text-white">普通任务</p>
            <p className="text-xs text-gray-500 dark:text-gray-400">手动添加任务</p>
          </div>
        </button>
      </div>

      {/* 主按钮 */}
      <button
        onClick={toggle}
        className={`w-16 h-16 rounded-full shadow-xl flex items-center justify-center text-white transition-all duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] active:scale-90 ${
          isOpen
            ? 'bg-gradient-to-br from-red-500 to-red-600 shadow-red-500/30'
            : 'bg-gradient-to-br from-green-600 to-green-700 shadow-green-500/30 hover:shadow-green-500/50'
        }`}
      >
        <Plus
          size={28}
          className={`transition-transform duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
            isOpen ? 'rotate-[135deg]' : ''
          }`}
        />
      </button>
    </div>
  );
}
