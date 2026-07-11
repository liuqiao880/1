import { useState, useEffect, useRef } from 'react';
import { Trash2, Check, X } from 'lucide-react';
import { Task } from '@/types/task';
import { useTaskStore } from '@/store/useTaskStore';

interface SwipeableTaskProps {
  task: Task;
  children: React.ReactNode;
  isSubTask?: boolean;
}

export default function SwipeableTask({ task, children, isSubTask = false }: SwipeableTaskProps) {
  const { deleteTask, toggleTask } = useTaskStore();
  const [translateX, setTranslateX] = useState(0);
  const [startX, setStartX] = useState(0);
  const [isDragging, setIsDragging] = useState(false);
  const [showUndo, setShowUndo] = useState(false);
  const containerRef = useRef<HTMLDivElement>(null);

  const actionWidth = isSubTask ? 70 : 80;
  const maxSwipe = actionWidth * 2;

  const handleTouchStart = (e: React.TouchEvent | React.MouseEvent) => {
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    setStartX(clientX - translateX);
    setIsDragging(true);
  };

  const handleTouchMove = (e: React.TouchEvent | React.MouseEvent) => {
    if (!isDragging) return;
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    let diff = clientX - startX;
    diff = Math.max(-maxSwipe, Math.min(actionWidth, diff));
    setTranslateX(diff);
  };

  const handleTouchEnd = () => {
    setIsDragging(false);
    if (translateX < -actionWidth) {
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

  const handleDelete = () => {
    deleteTask(task.id);
    setShowUndo(true);
    setTimeout(() => setShowUndo(false), 3000);
  };

  useEffect(() => {
    const handleGlobalClick = () => {
      if (translateX < 0) {
        setTranslateX(0);
      }
    };
    document.addEventListener('click', handleGlobalClick);
    return () => document.removeEventListener('click', handleGlobalClick);
  }, [translateX]);

  return (
    <div className="relative overflow-hidden" ref={containerRef}>
      {/* 左侧 - 完成操作 */}
      <div
        className={`absolute left-0 top-0 bottom-0 w-[${actionWidth}px] bg-green-500 flex items-center justify-center text-white transition-opacity ${
          translateX > 10 ? 'opacity-100' : 'opacity-0'
        }`}
        style={{ width: actionWidth }}
        onClick={(e) => {
          e.stopPropagation();
          toggleTask(task.id);
          setTranslateX(0);
        }}
      >
        <Check size={20} />
      </div>

      {/* 右侧 - 删除操作 */}
      <div
        className="absolute right-0 top-0 bottom-0 bg-red-500 flex items-center justify-center text-white cursor-pointer"
        style={{ width: Math.abs(Math.min(0, translateX)) || actionWidth }}
        onClick={(e) => {
          e.stopPropagation();
          handleDelete();
        }}
      >
        <div className="flex items-center gap-1 px-4">
          <Trash2 size={18} />
          <span className="text-sm font-medium">删除</span>
        </div>
      </div>

      {/* 任务内容 */}
      <div
        style={{
          transform: `translateX(${translateX}px)`,
          transition: isDragging ? 'none' : 'transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)',
        }}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
        onMouseDown={handleTouchStart}
        onMouseMove={handleTouchMove}
        onMouseUp={handleTouchEnd}
        onMouseLeave={handleTouchEnd}
        onClick={(e) => {
          if (translateX < 0) {
            e.stopPropagation();
            setTranslateX(0);
          }
        }}
      >
        {children}
      </div>

      {/* 撤销提示 */}
      {showUndo && (
        <div className="fixed bottom-24 left-1/2 -translate-x-1/2 z-50 animate-slideUp">
          <div className="flex items-center gap-3 px-5 py-3 bg-gray-900 dark:bg-gray-700 text-white rounded-xl shadow-2xl">
            <span className="text-sm">任务已删除</span>
            <button
              onClick={() => {
                // 撤销删除 - 简单实现：刷新恢复（实际应用需要更复杂的逻辑）
                window.location.reload();
              }}
              className="text-green-400 font-medium text-sm hover:text-green-300"
            >
              撤销
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
