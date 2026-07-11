import { TaskGroup } from '@/types/task';
import TaskItem from './TaskItem';

interface TaskGroupProps {
  group: TaskGroup;
}

export default function TaskGroupView({ group }: TaskGroupProps) {
  return (
    <div className="mb-6">
      <div className="flex items-center justify-between mb-3 px-1">
        <h2 className="text-sm font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider">
          {group.label}
        </h2>
        <span className="text-xs text-gray-400 dark:text-gray-500 bg-gray-100 dark:bg-gray-800 px-2 py-0.5 rounded-full">
          {group.tasks.length} 项
        </span>
      </div>
      <div>
        {group.tasks.map((task) => (
          <TaskItem key={task.id} task={task} />
        ))}
      </div>
    </div>
  );
}
