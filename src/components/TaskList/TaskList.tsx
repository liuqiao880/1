import { useTaskStore } from '@/store/useTaskStore';
import { groupTasksByDate, filterTasks } from '@/utils/dateUtils';
import TaskGroup from './TaskGroup';
import { Inbox } from 'lucide-react';

export default function TaskList() {
  const { tasks, filter, searchQuery } = useTaskStore();

  const filteredTasks = filterTasks(tasks, filter, searchQuery);
  const groups = groupTasksByDate(filteredTasks);

  if (groups.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-20 px-6">
        <div className="w-16 h-16 border border-line-separator dark:border-gray-700 flex items-center justify-center mb-4">
          <Inbox size={28} className="text-ink-light" />
        </div>
        <h3 className="font-serif text-base font-semibold text-ink-gray dark:text-gray-300 mb-1">
          {searchQuery ? '没有找到相关任务' : '暂无任务'}
        </h3>
        <p className="text-xs text-ink-light dark:text-gray-500 text-center font-sans">
          {searchQuery ? '换个关键词试试吧' : '点击右下角 + 号添加你的第一个任务'}
        </p>
      </div>
    );
  }

  return (
    <div className="px-5 pb-32 pt-2">
      {groups.map((group) => (
        <TaskGroup key={group.key} group={group} />
      ))}
    </div>
  );
}
