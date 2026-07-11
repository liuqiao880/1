import { useEffect } from 'react';
import { X, Trash2 } from 'lucide-react';
import TopNavbar from '@/components/TopBar/TopNavbar';
import FilterTabs from '@/components/TopBar/FilterTabs';
import TaskList from '@/components/TaskList/TaskList';
import FloatingActionButton from '@/components/FAB/FloatingActionButton';
import AiPlanModal from '@/components/Modal/AiPlanModal';
import TaskEditModal from '@/components/Modal/TaskEditModal';
import SettingsModal from '@/components/Modal/SettingsModal';
import { useTaskStore } from '@/store/useTaskStore';

export default function Home() {
  const {
    theme,
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

  const appContent = (
    <>
      <TopNavbar />
      <FilterTabs />
      <TaskList />
      <FloatingActionButton />
      <AiPlanModal />
      <TaskEditModalWrapper />
      <SettingsModal />

      {/* 多选模式顶部工具栏 */}
      {multiSelectMode && (
        <div className="absolute top-0 left-0 right-0 z-40 bg-white/90 dark:bg-gray-900/90 backdrop-blur-xl border-b border-gray-100 dark:border-gray-800 animate-fadeIn">
          <div className="flex items-center justify-between px-5 py-4">
            <div className="flex items-center gap-3">
              <button
                onClick={clearSelection}
                className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
              >
                <X size={20} className="text-gray-600 dark:text-gray-300" />
              </button>
              <span className="font-semibold text-gray-900 dark:text-white">
                已选 {selectedTasks.length} 项
              </span>
            </div>
            <div className="flex items-center gap-2">
              <button
                onClick={deleteSelected}
                disabled={selectedTasks.length === 0}
                className="w-10 h-10 flex items-center justify-center rounded-full bg-red-50 dark:bg-red-950/30 text-red-500 hover:bg-red-100 dark:hover:bg-red-950/50 transition-colors disabled:opacity-50"
              >
                <Trash2 size={18} />
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 撤销 Snackbar */}
      {undoBuffer && (
        <div className="fixed bottom-24 left-1/2 -translate-x-1/2 z-50 animate-slideUp">
          <div className="flex items-center gap-3 px-5 py-3 bg-gray-900 dark:bg-gray-700 text-white rounded-xl shadow-2xl">
            <span className="text-sm">{undoBuffer.message}</span>
            <button
              onClick={undo}
              className="text-green-400 font-medium text-sm hover:text-green-300 transition-colors"
            >
              撤销
            </button>
          </div>
        </div>
      )}
    </>
  );

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 transition-colors duration-300">
      {/* 桌面端外框（模拟手机效果） */}
      <div className="hidden sm:flex min-h-screen items-center justify-center py-8 px-4">
        <div className="relative w-full max-w-md h-[85vh] bg-white dark:bg-gray-900 rounded-[40px] shadow-2xl overflow-hidden border-8 border-gray-900 dark:border-gray-800">
          {/* 状态栏 */}
          <div className="absolute top-0 left-0 right-0 h-7 bg-white dark:bg-gray-900 z-40 flex items-center justify-between px-6">
            <span className="text-xs font-semibold text-gray-900 dark:text-white">9:41</span>
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-28 h-5 bg-gray-900 rounded-b-2xl" />
            <div className="flex gap-1 items-center">
              <div className="w-4 h-2.5 border border-gray-900 dark:border-white rounded-sm relative">
                <div className="absolute inset-0.5 bg-gray-900 dark:bg-white rounded-sm" />
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
