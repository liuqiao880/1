import { Search, Moon, Sun, Settings } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';

export default function TopNavbar() {
  const { theme, toggleTheme, setShowSearch, showSearch, searchQuery, setSearchQuery } = useTaskStore();

  return (
    <div className="sticky top-0 z-30 bg-white/80 dark:bg-gray-900/80 backdrop-blur-xl border-b border-gray-100 dark:border-gray-800">
      <div className="px-5 pt-6 pb-3">
        {!showSearch ? (
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-green-600 to-green-700 flex items-center justify-center text-white font-bold text-lg shadow-lg shadow-green-500/20">
                T
              </div>
              <h1 className="text-xl font-bold text-gray-900 dark:text-white tracking-tight">
                TaskFlow
              </h1>
            </div>
            <div className="flex items-center gap-1">
              <button
                onClick={() => setShowSearch(true)}
                className="w-11 h-11 flex items-center justify-center rounded-full text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 transition-all active:scale-95"
              >
                <Search size={20} />
              </button>
              <button
                onClick={toggleTheme}
                className="w-11 h-11 flex items-center justify-center rounded-full text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 transition-all active:scale-95"
              >
                {theme === 'light' ? <Moon size={20} /> : <Sun size={20} />}
              </button>
              <button className="w-11 h-11 flex items-center justify-center rounded-full text-gray-600 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-800 transition-all active:scale-95">
                <Settings size={20} />
              </button>
            </div>
          </div>
        ) : (
          <div className="flex items-center gap-2">
            <div className="flex-1 relative">
              <Search size={18} className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
              <input
                autoFocus
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="搜索任务..."
                className="w-full h-11 pl-11 pr-4 rounded-full bg-gray-100 dark:bg-gray-800 text-gray-900 dark:text-white placeholder-gray-400 outline-none focus:ring-2 focus:ring-green-500/30 transition-all"
              />
            </div>
            <button
              onClick={() => {
                setShowSearch(false);
                setSearchQuery('');
              }}
              className="px-4 h-11 text-green-600 dark:text-green-400 font-medium active:scale-95 transition-transform"
            >
              取消
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
