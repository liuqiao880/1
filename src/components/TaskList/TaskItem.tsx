import { useState, useRef } from 'react';
import { Check, ChevronDown, Sparkles, Calendar, Flag, Trash2, Timer } from 'lucide-react';
import { Task } from '@/types/task';
import { useTaskStore } from '@/store/useTaskStore';
import { usePomodoroStore } from '@/store/usePomodoroStore';
import { formatDate, calcProgress } from '@/utils/dateUtils';
import ProgressBar from './ProgressBar';

interface TaskItemProps {
  task: Task;
}

const priorityColors: Record<number, string> = {
  1: 'bg-priority-high',
  2: 'bg-priority-medium',
  3: 'bg-priority-low',
};

const priorityTextColors: Record<number, string> = {
  1: 'text-priority-high',
  2: 'text-priority-medium',
  3: 'text-priority-low',
};

const priorityLabels: Record<number, string> = {
  1: '紧急',
  2: '普通',
  3: '低优',
};

export default function TaskItem({ task }: TaskItemProps) {
  const {
    toggleTask,
    toggleExpand,
    expandedParents,
    multiSelectMode,
    toggleSelected,
    selectedTasks,
    openEditModal,
    deleteTask,
  } = useTaskStore();

  const { startPomodoro } = usePomodoroStore();

  const hasChildren = task.children && task.children.length > 0;
  const isExpanded = expandedParents.includes(task.id);
  const progress = calcProgress(task);
  const isSelected = selectedTasks.includes(task.id);

  const [translateX, setTranslateX] = useState(0);
  const [startX, setStartX] = useState(0);
  const [isDragging, setIsDragging] = useState(false);
  const longPressTimer = useRef<ReturnType<typeof setTimeout> | null>(null);

  const actionWidth = 88;
  const maxSwipe = actionWidth * 2;

  const handleCheckboxClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (multiSelectMode) {
      toggleSelected(task.id);
    } else {
      toggleTask(task.id);
      if (navigator.vibrate) navigator.vibrate(10);
    }
  };

  const handleRowClick = () => {
    if (multiSelectMode) {
      toggleSelected(task.id);
      return;
    }
    if (hasChildren) {
      toggleExpand(task.id);
    } else {
      openEditModal(task);
    }
  };

  const handleLongPress = () => {
    if (!multiSelectMode) {
      toggleSelected(task.id);
      if (navigator.vibrate) navigator.vibrate(20);
    }
  };

  const handleTouchStart = (e: React.TouchEvent | React.MouseEvent) => {
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    setStartX(clientX - translateX);
    setIsDragging(false);

    longPressTimer.current = setTimeout(() => {
      handleLongPress();
    }, 500);
  };

  const handleTouchMove = (e: React.TouchEvent | React.MouseEvent) => {
    if (longPressTimer.current) {
      clearTimeout(longPressTimer.current);
      longPressTimer.current = null;
    }
    if (multiSelectMode) return;

    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    let diff = clientX - startX;
    diff = Math.max(-maxSwipe, Math.min(actionWidth, diff));
    if (Math.abs(diff) > 5) setIsDragging(true);
    setTranslateX(diff);
  };

  const handleTouchEnd = () => {
    if (longPressTimer.current) {
      clearTimeout(longPressTimer.current);
      longPressTimer.current = null;
    }
    if (multiSelectMode) return;

    if (translateX < -actionWidth * 0.6) {
      setTranslateX(-actionWidth);
    } else if (translateX > actionWidth * 0.4) {
      setTranslateX(actionWidth);
      setTimeout(() => {
        toggleTask(task.id);
        setTranslateX(0);
      }, 200);
    } else {
      setTranslateX(0);
    }
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.stopPropagation();
    deleteTask(task.id);
    setTranslateX(0);
  };

  const handleEdit = (e: React.MouseEvent) => {
    e.stopPropagation();
    openEditModal(task);
    setTranslateX(0);
  };

  return (
    <div>
      <div className="relative overflow-hidden">
        {/* 左侧 - 完成操作 */}
        <div
          className={`absolute left-0 top-0 bottom-0 w-24 bg-ink-black flex items-center justify-center text-white transition-opacity z-0 ${
            translateX > 10 ? 'opacity-100' : 'opacity-0'
          }`}
          onClick={(e) => {
            e.stopPropagation();
            toggleTask(task.id);
            setTranslateX(0);
          }}
        >
          <div className="flex flex-col items-center gap-1">
            <Check size={16} />
            <span className="text-xs font-medium">完成</span>
          </div>
        </div>

        {/* 右侧 - 编辑 + 删除操作 */}
        <div
          className="absolute right-0 top-0 bottom-0 flex z-0"
          style={{ transform: `translateX(${Math.min(0, translateX + actionWidth)}px)` }}
        >
          <button
            onClick={handleEdit}
            className="w-16 bg-ink-gray flex flex-col items-center justify-center text-white"
          >
            <Calendar size={14} />
            <span className="text-xs font-medium mt-1">编辑</span>
          </button>
          <button
            onClick={handleDelete}
            className="w-12 bg-newspaper-red flex flex-col items-center justify-center text-white"
          >
            <Trash2 size={14} />
            <span className="text-xs font-medium mt-1">删除</span>
          </button>
        </div>

        {/* 任务项 - 报纸风格 */}
        <div
          onClick={handleRowClick}
          onTouchStart={handleTouchStart}
          onTouchMove={handleTouchMove}
          onTouchEnd={handleTouchEnd}
          onMouseDown={handleTouchStart}
          onMouseMove={handleTouchMove}
          onMouseUp={handleTouchEnd}
          onMouseLeave={handleTouchEnd}
          style={{
            transform: `translateX(${translateX}px)`,
            transition: isDragging ? 'none' : 'transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)',
          }}
          className={`relative bg-paper-white dark:bg-gray-900 border-b border-line-separator dark:border-gray-800 transition-all duration-200 z-10 ${
            isSelected
              ? 'bg-newspaper-red/5 dark:bg-newspaper-red/10'
              : 'hover:bg-paper-cream dark:hover:bg-gray-800/50'
          } ${hasChildren ? 'cursor-pointer' : 'cursor-default'} active:scale-[0.995]`}
        >
          {/* 优先级色条 */}
          <div className={`absolute left-0 top-0 bottom-0 w-0.5 ${priorityColors[task.priority]}`} />

          <div className="flex items-start gap-3 px-5 py-3.5 pl-5">
            {/* 复选框 - 描边风格 */}
            <button
              onClick={handleCheckboxClick}
              className={`mt-0.5 w-5 h-5 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all duration-300 ${
                multiSelectMode
                  ? isSelected
                    ? 'bg-ink-black border-ink-black'
                    : 'border-ink-light dark:border-gray-600'
                  : task.status === 'completed'
                  ? 'bg-ink-black border-ink-black'
                  : 'border-ink-light dark:border-gray-600 hover:border-ink-black dark:hover:border-gray-400'
              }`}
            >
              <Check
                size={12}
                className={`text-white transition-all duration-300 ${
                  multiSelectMode
                    ? isSelected
                      ? 'opacity-100 scale-100'
                      : 'opacity-0 scale-50'
                    : task.status === 'completed'
                    ? 'opacity-100 scale-100'
                    : 'opacity-0 scale-50'
                }`}
              />
            </button>

            {/* 内容区 */}
            <div className="flex-1 min-w-0">
              <div className="flex items-start justify-between gap-2">
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 flex-wrap">
                    <h3
                      className={`font-serif text-sm font-medium transition-all duration-300 ${
                        task.status === 'completed' && !multiSelectMode
                          ? 'text-ink-light dark:text-gray-600 line-through'
                          : 'text-ink-black dark:text-white'
                      }`}
                    >
                      {task.title}
                    </h3>
                    {task.aiGenerated && (
                      <span className="flex items-center gap-0.5 px-1.5 py-0.5 border border-newspaper-red/20 dark:border-newspaper-red/30 text-newspaper-red dark:text-newspaper-red-light text-[10px] font-medium flex-shrink-0">
                        <Sparkles size={9} />
                        AI
                      </span>
                    )}
                  </div>

                  {task.description && (
                    <p
                      className={`mt-0.5 text-xs line-clamp-1 transition-all duration-300 font-sans ${
                        task.status === 'completed'
                          ? 'text-ink-light/50 dark:text-gray-700'
                          : 'text-ink-gray dark:text-gray-400'
                      }`}
                    >
                      {task.description}
                    </p>
                  )}

                  <div className="mt-1.5 flex items-center gap-3 flex-wrap">
                    {task.dueDate && (
                      <div className="flex items-center gap-1 text-xs text-ink-light dark:text-gray-500 font-sans">
                        <Calendar size={11} />
                        <span>{formatDate(task.dueDate)}</span>
                      </div>
                    )}
                    <div
                      className={`flex items-center gap-1 text-[10px] font-medium font-sans ${priorityTextColors[task.priority]}`}
                    >
                      <Flag size={9} />
                      <span>{priorityLabels[task.priority]}</span>
                    </div>
                    {hasChildren && (
                      <div className="flex items-center gap-1.5">
                        <ProgressBar percent={progress.percent} />
                        <span className="text-[10px] text-ink-light dark:text-gray-500 tabular-nums font-sans">
                          {progress.percent}%
                        </span>
                      </div>
                    )}
                    {(task.pomodoroCount > 0 || !multiSelectMode) && (
                      <button
                        onClick={(e) => {
                          e.stopPropagation();
                          if (!multiSelectMode) {
                            startPomodoro(task.id, task.title);
                          }
                        }}
                        className={`flex items-center gap-1 text-[10px] font-medium font-sans transition-colors ${
                          task.pomodoroCount > 0
                            ? 'text-newspaper-red dark:text-newspaper-red-light'
                            : 'text-ink-light dark:text-gray-500 hover:text-newspaper-red dark:hover:text-newspaper-red-light'
                        } ${multiSelectMode ? 'pointer-events-none opacity-60' : ''}`}
                      >
                        <Timer size={9} />
                        <span>{task.pomodoroCount || '开始'}</span>
                      </button>
                    )}
                  </div>
                </div>

                {/* 展开箭头 */}
                {hasChildren && (
                  <div className="flex-shrink-0 mt-0.5">
                    <ChevronDown
                      size={16}
                      className={`text-ink-light transition-transform duration-300 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
                        isExpanded ? 'rotate-180' : ''
                      }`}
                    />
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* 子任务 */}
      {hasChildren && (
        <div
          className={`overflow-hidden transition-all duration-500 ease-[cubic-bezier(0.34,1.56,0.64,1)] ${
            isExpanded ? 'max-h-[2000px] opacity-100' : 'max-h-0 opacity-0'
          }`}
        >
          <div className="ml-8 pl-4 border-l border-line-separator dark:border-gray-800">
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
  const { toggleTask, openEditModal, multiSelectMode, toggleSelected, selectedTasks, deleteTask } =
    useTaskStore();
  const [translateX, setTranslateX] = useState(0);
  const [startX, setStartX] = useState(0);
  const [isDragging, setIsDragging] = useState(false);

  const actionWidth = 70;

  const handleClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (multiSelectMode) {
      toggleSelected(task.id);
    } else {
      openEditModal(task);
    }
  };

  const handleCheckboxClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (multiSelectMode) {
      toggleSelected(task.id);
    } else {
      toggleTask(task.id);
      if (navigator.vibrate) navigator.vibrate(8);
    }
  };

  const handleTouchStart = (e: React.TouchEvent | React.MouseEvent) => {
    if (multiSelectMode) return;
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    setStartX(clientX - translateX);
    setIsDragging(false);
  };

  const handleTouchMove = (e: React.TouchEvent | React.MouseEvent) => {
    if (multiSelectMode) return;
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    let diff = clientX - startX;
    diff = Math.max(-actionWidth, Math.min(0, diff));
    if (Math.abs(diff) > 5) setIsDragging(true);
    setTranslateX(diff);
  };

  const handleTouchEnd = () => {
    if (multiSelectMode) return;
    if (translateX < -actionWidth * 0.6) {
      setTranslateX(-actionWidth);
    } else {
      setTranslateX(0);
    }
  };

  const isSelected = selectedTasks.includes(task.id);

  return (
    <div className="relative overflow-hidden">
      <div
        className="absolute right-0 top-0 bottom-0 w-16 bg-newspaper-red flex items-center justify-center text-white z-0"
        onClick={(e) => {
          e.stopPropagation();
          deleteTask(task.id);
        }}
      >
        <Trash2 size={14} />
      </div>

      <div
        onClick={handleClick}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
        onMouseDown={handleTouchStart}
        onMouseMove={handleTouchMove}
        onMouseUp={handleTouchEnd}
        onMouseLeave={handleTouchEnd}
        style={{
          transform: `translateX(${translateX}px)`,
          transition: isDragging ? 'none' : 'transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)',
        }}
        className={`relative flex items-center gap-3 py-2.5 pl-1 pr-3 group z-10 bg-transparent border-b border-line-thin dark:border-gray-800/50 ${
          isSelected ? 'bg-newspaper-red/5 dark:bg-newspaper-red/10' : ''
        }`}
      >
        <button
          onClick={handleCheckboxClick}
          className={`w-4 h-4 rounded-full border-2 flex-shrink-0 flex items-center justify-center transition-all duration-200 ${
            multiSelectMode
              ? isSelected
                ? 'bg-ink-black border-ink-black'
                : 'border-ink-light dark:border-gray-600'
              : task.status === 'completed'
              ? 'bg-ink-black border-ink-black'
              : 'border-ink-light dark:border-gray-600 group-hover:border-ink-gray dark:group-hover:border-gray-400'
          }`}
        >
          <Check
            size={10}
            className={`text-white transition-all duration-200 ${
              multiSelectMode
                ? isSelected
                  ? 'opacity-100 scale-100'
                  : 'opacity-0 scale-50'
                : task.status === 'completed'
                ? 'opacity-100 scale-100'
                : 'opacity-0 scale-50'
            }`}
          />
        </button>

        <div className="flex-1 min-w-0">
          <p
            className={`text-xs font-sans transition-all duration-300 ${
              task.status === 'completed' && !multiSelectMode
                ? 'text-ink-light dark:text-gray-600 line-through'
                : 'text-ink-gray dark:text-gray-300'
            }`}
          >
            {task.title}
          </p>
          {task.dueDate && (
            <p className="text-[10px] text-ink-light dark:text-gray-500 mt-0.5 flex items-center gap-1 font-sans">
              <Calendar size={9} />
              {formatDate(task.dueDate)}
              {task.aiGenerated && (
                <span className="ml-1 flex items-center gap-0.5 text-newspaper-red dark:text-newspaper-red-light">
                  <Sparkles size={9} /> AI
                </span>
              )}
            </p>
          )}
        </div>

        <div className={`w-1 h-1 rounded-full ${priorityColors[task.priority]}`} />
      </div>
    </div>
  );
}
