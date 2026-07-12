import { Search, Moon, Sun, Settings, Sparkles } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';
import { useNavigate } from 'react-router-dom';

export default function TopNavbar() {
  const {
    theme,
    toggleTheme,
    setShowSearch,
    showSearch,
    searchQuery,
    setSearchQuery,
    setShowSettings,
  } = useTaskStore();
  const navigate = useNavigate();

  return (
    <div className="sticky top-0 z-30 bg-paper-white/95 dark:bg-gray-900/95 backdrop-blur-sm border-b border-line-separator dark:border-gray-800">
      <div className="px-5 pt-5 pb-3">
        {!showSearch ? (
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2.5">
              <div className="w-8 h-8 bg-newspaper-red flex items-center justify-center text-white font-serif font-bold text-sm">
                T
              </div>
              <div>
                <h1 className="font-serif text-xl font-semibold text-ink-black dark:text-white tracking-tight leading-none">
                  TaskFlow
                </h1>
                <div className="newspaper-accent-line mt-1" />
              </div>
            </div>
            <div className="flex items-center gap-0.5">
              <button
                onClick={() => navigate('/chat')}
                className="w-10 h-10 flex items-center justify-center rounded-full text-newspaper-red dark:text-newspaper-red-light hover:bg-newspaper-red/5 dark:hover:bg-newspaper-red/10 transition-all active:scale-95"
              >
                <Sparkles size={19} />
              </button>
              <button
                onClick={() => setShowSearch(true)}
                className="w-10 h-10 flex items-center justify-center rounded-full text-ink-gray dark:text-gray-400 hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-95"
              >
                <Search size={19} />
              </button>
              <button
                onClick={toggleTheme}
                className="w-10 h-10 flex items-center justify-center rounded-full text-ink-gray dark:text-gray-400 hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-95"
              >
                {theme === 'light' ? <Moon size={19} /> : <Sun size={19} />}
              </button>
              <button
                onClick={() => setShowSettings(true)}
                className="w-10 h-10 flex items-center justify-center rounded-full text-ink-gray dark:text-gray-400 hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-95"
              >
                <Settings size={19} />
              </button>
            </div>
          </div>
        ) : (
          <div className="flex items-center gap-2">
            <div className="flex-1 relative">
              <Search size={16} className="absolute left-3.5 top-1/2 -translate-y-1/2 text-ink-light" />
              <input
                autoFocus
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                placeholder="搜索任务..."
                className="w-full h-10 pl-10 pr-4 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white placeholder-ink-light outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all text-sm"
              />
            </div>
            <button
              onClick={() => {
                setShowSearch(false);
                setSearchQuery('');
              }}
              className="px-3 h-10 text-newspaper-red dark:text-newspaper-red-light font-medium text-sm active:scale-95 transition-transform"
            >
              取消
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
