import { useEffect } from 'react';
import TopNavbar from '@/components/TopBar/TopNavbar';
import FilterTabs from '@/components/TopBar/FilterTabs';
import TaskList from '@/components/TaskList/TaskList';
import FloatingActionButton from '@/components/FAB/FloatingActionButton';
import AiPlanModal from '@/components/Modal/AiPlanModal';
import { useTaskStore } from '@/store/useTaskStore';

export default function Home() {
  const { theme } = useTaskStore();

  useEffect(() => {
    if (theme === 'dark') {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  }, [theme]);

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

          <div className="h-full overflow-y-auto pt-7 scrollbar-hide">
            <TopNavbar />
            <FilterTabs />
            <TaskList />
            <FloatingActionButton />
            <AiPlanModal />
          </div>
        </div>
      </div>

      {/* 移动端全屏 */}
      <div className="sm:hidden min-h-screen flex flex-col">
        <TopNavbar />
        <FilterTabs />
        <div className="flex-1 overflow-y-auto">
          <TaskList />
        </div>
        <FloatingActionButton />
        <AiPlanModal />
      </div>
    </div>
  );
}
