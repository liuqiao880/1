import { TaskGroup } from '@/types/task';
import TaskItem from './TaskItem';

interface TaskGroupProps {
  group: TaskGroup;
}

export default function TaskGroupView({ group }: TaskGroupProps) {
  return (
    <div className="mb-5">
      <div className="flex items-center gap-2.5 mb-2.5 px-1">
        <div className="newspaper-accent-line" />
        <h2 className="font-serif text-xs font-semibold text-ink-gray dark:text-gray-400 uppercase tracking-wider">
          {group.label}
        </h2>
        <span className="text-xs text-ink-light dark:text-gray-500 font-sans tabular-nums">
          {group.tasks.length}
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
