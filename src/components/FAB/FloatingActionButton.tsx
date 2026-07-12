import { useState } from 'react';
import { Plus, Sparkles, PenLine } from 'lucide-react';
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
        className={`absolute bottom-16 right-0 flex flex-col gap-2.5 items-end transition-all duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
          isOpen ? 'opacity-100 translate-y-0 pointer-events-auto' : 'opacity-0 translate-y-4 pointer-events-none'
        }`}
      >
        <button
          onClick={handleAiPlan}
          className="flex items-center gap-2.5 px-4 py-2.5 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 shadow-sm hover:shadow-md transition-all active:scale-95 group"
          style={{ transitionDelay: isOpen ? '50ms' : '0ms' }}
        >
          <div className="w-7 h-7 bg-newspaper-red flex items-center justify-center text-white">
            <Sparkles size={14} />
          </div>
          <div className="text-left">
            <p className="text-xs font-semibold text-ink-black dark:text-white font-sans">AI 规划</p>
            <p className="text-[10px] text-ink-light dark:text-gray-400 font-sans">智能拆解任务</p>
          </div>
        </button>

        <button
          onClick={handleQuickAdd}
          className="flex items-center gap-2.5 px-4 py-2.5 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 shadow-sm hover:shadow-md transition-all active:scale-95"
          style={{ transitionDelay: isOpen ? '0ms' : '50ms' }}
        >
          <div className="w-7 h-7 bg-ink-black dark:bg-gray-600 flex items-center justify-center text-white">
            <PenLine size={14} />
          </div>
          <div className="text-left">
            <p className="text-xs font-semibold text-ink-black dark:text-white font-sans">普通任务</p>
            <p className="text-[10px] text-ink-light dark:text-gray-400 font-sans">手动添加任务</p>
          </div>
        </button>
      </div>

      {/* 主按钮 */}
      <button
        onClick={toggle}
        className={`w-14 h-14 shadow-lg flex items-center justify-center text-white transition-all duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] active:scale-90 ${
          isOpen
            ? 'bg-newspaper-red rotate-[135deg]'
            : 'bg-ink-black dark:bg-gray-700 hover:bg-newspaper-red'
        }`}
      >
        <Plus size={24} />
      </button>
    </div>
  );
}
