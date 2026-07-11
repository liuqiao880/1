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
        <div className="w-20 h-20 rounded-full bg-gray-100 dark:bg-gray-800 flex items-center justify-center mb-4">
          <Inbox size={32} className="text-gray-400" />
        </div>
        <h3 className="text-lg font-medium text-gray-700 dark:text-gray-300 mb-1">
          {searchQuery ? '没有找到相关任务' : '暂无任务'}
        </h3>
        <p className="text-sm text-gray-500 dark:text-gray-400 text-center">
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
