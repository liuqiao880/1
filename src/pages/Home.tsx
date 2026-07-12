import { useEffect } from 'react';
import { X, Trash2 } from 'lucide-react';
import TopNavbar from '@/components/TopBar/TopNavbar';
import FilterTabs from '@/components/TopBar/FilterTabs';
import TaskList from '@/components/TaskList/TaskList';
import FloatingActionButton from '@/components/FAB/FloatingActionButton';
import AiPlanModal from '@/components/Modal/AiPlanModal';
import TaskEditModal from '@/components/Modal/TaskEditModal';
import SettingsModal from '@/components/Modal/SettingsModal';
import PomodoroPanel from '@/components/Pomodoro/PomodoroPanel';
import { useTaskStore } from '@/store/useTaskStore';

export default function Home() {
  const {
    theme,
    accentColor,
    multiSelectMode,
    selectedTasks,
    clearSelection,
    deleteSelected,
    undoBuffer,
    undo,
  } = useTaskStore();

  useEffect(() => {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [theme]);

  useEffect(() => {
    document.documentElement.setAttribute('data-accent', accentColor);
  }, [accentColor]);

  const appContent = (
    <>
      <TopNavbar />
      <FilterTabs />
      <TaskList />
      <FloatingActionButton />
      <AiPlanModal />
      <TaskEditModalWrapper />
      <SettingsModal />
      <PomodoroPanel />

      {/* 多选模式顶部工具栏 */}
      {multiSelectMode && (
        <div className="absolute top-0 left-0 right-0 z-40 bg-paper-white/95 dark:bg-gray-900/95 backdrop-blur-sm border-b border-line-separator dark:border-gray-800 animate-fadeIn">
          <div className="flex items-center justify-between px-5 py-3">
            <div className="flex items-center gap-3">
              <button
                onClick={clearSelection}
                className="w-10 h-10 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
              >
                <X size={18} className="text-ink-gray dark:text-gray-300" />
              </button>
              <span className="font-serif font-semibold text-ink-black dark:text-white">
                已选 {selectedTasks.length} 项
              </span>
            </div>
            <div className="flex items-center gap-2">
              <button
                onClick={deleteSelected}
                disabled={selectedTasks.length === 0}
                className="w-10 h-10 flex items-center justify-center bg-newspaper-red/5 dark:bg-newspaper-red/10 text-newspaper-red hover:bg-newspaper-red/10 dark:hover:bg-newspaper-red/20 transition-colors disabled:opacity-50"
              >
                <Trash2 size={16} />
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 撤销 Snackbar */}
      {undoBuffer && (
        <div className="fixed bottom-24 left-1/2 -translate-x-1/2 z-50 animate-slideUp">
          <div className="flex items-center gap-3 px-4 py-2.5 bg-ink-black dark:bg-gray-700 text-paper-white text-sm shadow-lg border border-line-separator dark:border-gray-600">
            <span className="font-sans text-xs">{undoBuffer.message}</span>
            <button
              onClick={undo}
              className="text-newspaper-red-light font-medium text-xs hover:text-newspaper-red transition-colors"
            >
              撤销
            </button>
          </div>
        </div>
      )}
    </>
  );

  return (
    <div className="min-h-screen bg-paper-white dark:bg-gray-950 transition-colors duration-300">
      {/* 桌面端外框（模拟报纸版面） */}
      <div className="hidden sm:flex min-h-screen items-center justify-center py-8 px-4">
        <div className="relative w-full max-w-md h-[85vh] bg-paper-cream dark:bg-gray-900 shadow-2xl overflow-hidden border border-line-separator dark:border-gray-800">
          {/* 状态栏 - 报纸风格 */}
          <div className="absolute top-0 left-0 right-0 h-7 bg-paper-cream dark:bg-gray-900 z-40 flex items-center justify-between px-6 border-b border-line-separator dark:border-gray-800">
            <span className="text-[10px] font-medium text-ink-gray dark:text-gray-400">9:41</span>
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-24 h-4 bg-ink-black dark:bg-gray-700 rounded-b-sm" />
            <div className="flex gap-1 items-center">
              <div className="w-3.5 h-2 border border-ink-black dark:border-gray-400 relative">
                <div className="absolute inset-0.5 bg-ink-black dark:bg-gray-400" />
              </div>
            </div>
          </div>

          <div className="h-full overflow-y-auto pt-7 scrollbar-hide relative">
            {appContent}
          </div>
        </div>
      </div>

      {/* 移动端全屏 */}
      <div className="sm:hidden min-h-screen flex flex-col relative">
        {appContent}
      </div>
    </div>
  );
}

function TaskEditModalWrapper() {
  const { showEditModal, editingTask, closeEditModal, updateTask } = useTaskStore();
  return (
    <TaskEditModal
      task={editingTask}
      isOpen={showEditModal}
      onClose={closeEditModal}
      onSave={(task) => updateTask(task)}
    />
  );
}
