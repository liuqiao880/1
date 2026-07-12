import { FilterType } from '@/types/task';
import { useTaskStore } from '@/store/useTaskStore';

const tabs: { key: FilterType; label: string }[] = [
  { key: 'today', label: '今日' },
  { key: 'tomorrow', label: '明日' },
  { key: 'week', label: '本周' },
  { key: 'all', label: '全部' },
];

export default function FilterTabs() {
  const { filter, setFilter } = useTaskStore();

  return (
    <div className="px-5 py-2 overflow-x-auto scrollbar-hide">
      <div className="flex gap-1">
        {tabs.map((tab) => {
          const active = filter === tab.key;
          return (
            <button
              key={tab.key}
              onClick={() => setFilter(tab.key)}
              className={`px-4 h-8 text-sm font-medium whitespace-nowrap transition-all duration-200 active:scale-95 ${
                active
                  ? 'bg-ink-black dark:bg-white text-white dark:text-ink-black'
                  : 'bg-transparent text-ink-gray dark:text-gray-400 hover:bg-black/5 dark:hover:bg-white/5'
              }`}
            >
              {tab.label}
            </button>
          );
        })}
      </div>
      <div className="mt-2 h-px bg-line-separator dark:bg-gray-800" />
    </div>
  );
}
