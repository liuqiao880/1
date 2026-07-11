import { Check, ChevronDown, Sparkles, Calendar, Flag } from 'lucide-react';
import { Task } from '@/types/task';
import { useTaskStore } from '@/store/useTaskStore';
import { formatDate, calcProgress } from '@/utils/dateUtils';
import ProgressBar from './ProgressBar';

interface TaskItemProps {
  task: Task;
}

const priorityColors = {
  1: 'bg-red-500',
  2: 'bg-yellow-500',
  3: 'bg-blue-500',
};

const priorityBgColors = {
  1: 'bg-red-50 dark:bg-red-950/30 text-red-600 dark:text-red-400',
  2: 'bg-yellow-50 dark:bg-yellow-950/30 text-yellow-700 dark:text-yellow-400',
  3: 'bg-blue-50 dark:bg-blue-950/30 text-blue-600 dark:text-blue-400',
};

const priorityLabels = {
  1: '紧急',
  2: '普通',
  3: '低优',
};

export default function TaskItem({ task }: TaskItemProps) {
  const { toggleTask, toggleExpand, expandedParents, theme } = useTaskStore();
  const hasChildren = task.children && task.children.length > 0;
  const isExpanded = expandedParents.includes(task.id);
  const progress = calcProgress(task);

  const handleCheckboxClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    toggleTask(task.id);
    if (navigator.vibrate) {
      navigator.vibrate(10);
    }
  };

  const handleRowClick = () => {
    if (hasChildren) {
      toggleExpand(task.id);
    }
  };

  return (
    <div className="mb-3">
      <div
        onClick={handleRowClick}
        className={`relative overflow-hidden bg-white dark:bg-gray-800 rounded-2xl shadow-sm border border-gray-100 dark:border-gray-700 transition-all duration-300 hover:shadow-md active:scale-[0.99] cursor-pointer ${
          hasChildren ? '' : 'cursor-default'
        }`}
      >
        <div className={`absolute left-0 top-0 bottom-0 w-1 ${priorityColors[task.priority]}`} />

        <div className="flex items-start gap-3 px-4 py-3.5 pl-5">
          <button
            onClick={handleCheckboxClick}
            className={`mt-0.5 w-6 h-6 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all duration-300 ${
              task.status === 'completed'
                ? 'bg-green-600 border-green-600 scale-100'
                : 'border-gray-300 dark:border-gray-500 hover:border-green-500'
            }`}
          >
            <Check
              size={14}
              className={`text-white transition-all duration-300 ${
                task.status === 'completed' ? 'opacity-100 scale-100' : 'opacity-0 scale-50'
              }`}
            />
          </button>

          <div className="flex-1 min-w-0">
            <div className="flex items-start justify-between gap-2">
              <div className="flex-1 min-w-0">
                <div className="flex items-center gap-2">
                  <h3
                    className={`font-medium text-[15px] transition-all duration-300 ${
                      task.status === 'completed'
                        ? 'text-gray-400 dark:text-gray-500 line-through'
                        : 'text-gray-900 dark:text-white'
                    }`}
                  >
                    {task.title}
                  </h3>
                  {task.aiGenerated && (
                    <span className="flex items-center gap-0.5 px-1.5 py-0.5 rounded-md bg-purple-50 dark:bg-purple-950/30 text-purple-600 dark:text-purple-400 text-xs font-medium flex-shrink-0">
                      <Sparkles size={10} />
                      AI
                    </span>
                  )}
                </div>

                {task.description && (
                  <p
                    className={`mt-1 text-sm line-clamp-1 transition-all duration-300 ${
                      task.status === 'completed'
                        ? 'text-gray-300 dark:text-gray-600'
                        : 'text-gray-500 dark:text-gray-400'
                    }`}
                  >
                    {task.description}
                  </p>
                )}

                <div className="mt-2 flex items-center gap-3 flex-wrap">
                  {task.dueDate && (
                    <div className="flex items-center gap-1 text-xs text-gray-500 dark:text-gray-400">
                      <Calendar size={12} />
                      <span>{formatDate(task.dueDate)}</span>
                    </div>
                  )}
                  <div
                    className={`flex items-center gap-1 px-2 py-0.5 rounded-full text-xs font-medium ${priorityBgColors[task.priority]}`}
                  >
                    <Flag size={10} />
                    <span>{priorityLabels[task.priority]}</span>
                  </div>
                  {hasChildren && (
                    <div className="flex items-center gap-2">
                      <ProgressBar percent={progress.percent} />
                      <span className="text-xs text-gray-500 dark:text-gray-400">
                        {progress.percent}%
                      </span>
                    </div>
                  )}
                </div>
              </div>

              {hasChildren && (
                <div className="flex-shrink-0 mt-1">
                  <ChevronDown
                    size={18}
                    className={`text-gray-400 transition-transform duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
                      isExpanded ? 'rotate-180' : ''
                    }`}
                  />
                </div>
              )}
            </div>
          </div>
        </div>
      </div>

      {hasChildren && (
        <div
          className={`overflow-hidden transition-all duration-500 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
            isExpanded ? 'max-h-[2000px] opacity-100 mt-2' : 'max-h-0 opacity-0'
          }`}
        >
          <div className="ml-6 pl-6 border-l-2 border-gray-100 dark:border-gray-700 space-y-2">
            {task.children!.map((child) => (
              <SubTaskItem key={child.id} task={child} />
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

function SubTaskItem({ task }: { task: Task }) {
  const { toggleTask } = useTaskStore();

  const handleClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    toggleTask(task.id);
    if (navigator.vibrate) {
      navigator.vibrate(8);
    }
  };

  return (
    <div className="flex items-center gap-3 py-2 group">
      <button
        onClick={handleClick}
        className={`w-5 h-5 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all duration-200 ${
          task.status === 'completed'
            ? 'bg-green-600 border-green-600'
            : 'border-gray-300 dark:border-gray-500 group-hover:border-green-500'
        }`}
      >
        <Check
          size={11}
          className={`text-white transition-all duration-200 ${
            task.status === 'completed' ? 'opacity-100 scale-100' : 'opacity-0 scale-50'
          }`}
        />
      </button>

      <div className="flex-1 min-w-0">
        <p
          className={`text-sm transition-all duration-300 ${
            task.status === 'completed'
              ? 'text-gray-400 dark:text-gray-500 line-through'
              : 'text-gray-700 dark:text-gray-200'
          }`}
        >
          {task.title}
        </p>
        {task.dueDate && (
          <p className="text-xs text-gray-400 dark:text-gray-500 mt-0.5 flex items-center gap-1">
            <Calendar size={10} />
            {formatDate(task.dueDate)}
            {task.aiGenerated && (
              <span className="ml-1 flex items-center gap-0.5 text-purple-500 dark:text-purple-400">
                <Sparkles size={10} /> AI生成
              </span>
            )}
          </p>
        )}
      </div>

      <div className={`w-1 h-1 rounded-full ${priorityColors[task.priority]}`} />
    </div>
  );
}
