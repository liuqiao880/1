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
    <div className="px-5 py-3 overflow-x-auto scrollbar-hide">
      <div className="flex gap-2">
        {tabs.map((tab) => {
          const active = filter === tab.key;
          return (
            <button
              key={tab.key}
              onClick={() => setFilter(tab.key)}
              className={`px-5 h-9 rounded-full text-sm font-medium whitespace-nowrap transition-all duration-300 active:scale-95 ${
                active
                  ? 'bg-green-600 text-white shadow-lg shadow-green-500/25'
                  : 'bg-gray-100 dark:bg-gray-800 text-gray-600 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-700'
              }`}
            >
              {tab.label}
            </button>
          );
        })}
      </div>
    </div>
  );
}
