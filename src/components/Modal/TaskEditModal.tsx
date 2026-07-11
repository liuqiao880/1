import { useState, useEffect } from 'react';
import { X, Calendar, Flag, Sparkles } from 'lucide-react';
import { Task, TaskPriority } from '@/types/task';

interface TaskEditModalProps {
  task: Task | null;
  isOpen: boolean;
  onClose: () => void;
  onSave: (task: Task) => void;
}

const priorityOptions: { value: TaskPriority; label: string; color: string }[] = [
  { value: 1, label: '紧急', color: 'bg-red-500' },
  { value: 2, label: '普通', color: 'bg-yellow-500' },
  { value: 3, label: '低优', color: 'bg-blue-500' },
];

export default function TaskEditModal({ task, isOpen, onClose, onSave }: TaskEditModalProps) {
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [priority, setPriority] = useState<TaskPriority>(2);
  const [dueDate, setDueDate] = useState('');

  useEffect(() => {
    if (task) {
      setTitle(task.title);
      setDescription(task.description || '');
      setPriority(task.priority);
      setDueDate(
        task.dueDate ? new Date(task.dueDate).toISOString().split('T')[0] : ''
      );
    }
  }, [task]);

  const handleSave = () => {
    if (!title.trim() || !task) return;
    onSave({
      ...task,
      title: title.trim(),
      description: description.trim() || undefined,
      priority,
      dueDate: dueDate ? new Date(dueDate).getTime() : undefined,
    });
    onClose();
  };

  if (!isOpen || !task) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center">
      <div
        className="absolute inset-0 bg-black/50 backdrop-blur-sm animate-fadeIn"
        onClick={onClose}
      />

      <div className="relative w-full sm:max-w-md bg-white dark:bg-gray-900 sm:rounded-3xl rounded-t-3xl shadow-2xl animate-slideUp max-h-[85vh] flex flex-col">
        <div className="flex items-center justify-between px-6 pt-6 pb-4 border-b border-gray-100 dark:border-gray-800">
          <h2 className="text-lg font-bold text-gray-900 dark:text-white">编辑任务</h2>
          <button
            onClick={onClose}
            className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors active:scale-90"
          >
            <X size={20} className="text-gray-500" />
          </button>
        </div>

        <div className="flex-1 overflow-y-auto p-6 space-y-5">
          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              任务标题
            </label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-gray-900 dark:text-white placeholder-gray-400 outline-none focus:ring-2 focus:ring-green-500/30 focus:border-green-500 transition-all"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              描述
            </label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={3}
              className="w-full px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-gray-900 dark:text-white placeholder-gray-400 outline-none focus:ring-2 focus:ring-green-500/30 focus:border-green-500 transition-all resize-none"
            />
          </div>

          <div>
            <label className="flex items-center gap-1 text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              <Calendar size={14} />
              截止日期
            </label>
            <input
              type="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
              className="w-full px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-gray-900 dark:text-white outline-none focus:ring-2 focus:ring-green-500/30 focus:border-green-500 transition-all"
            />
          </div>

          <div>
            <label className="flex items-center gap-1 text-sm font-medium text-gray-700 dark:text-gray-300 mb-2">
              <Flag size={14} />
              优先级
            </label>
            <div className="flex gap-2">
              {priorityOptions.map((opt) => (
                <button
                  key={opt.value}
                  onClick={() => setPriority(opt.value)}
                  className={`flex-1 py-2.5 rounded-xl text-sm font-medium transition-all ${
                    priority === opt.value
                      ? `${opt.color} text-white shadow-md`
                      : 'bg-gray-100 dark:bg-gray-800 text-gray-600 dark:text-gray-300'
                  }`}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>

          {task.aiGenerated && (
            <div className="flex items-center gap-2 px-4 py-3 rounded-xl bg-purple-50 dark:bg-purple-950/30">
              <Sparkles size={16} className="text-purple-500" />
              <span className="text-sm text-purple-700 dark:text-purple-300">
                此任务由 AI 生成
              </span>
            </div>
          )}
        </div>

        <div className="px-6 py-4 border-t border-gray-100 dark:border-gray-800">
          <div className="flex gap-3">
            <button
              onClick={onClose}
              className="flex-1 h-12 rounded-xl font-semibold text-gray-600 dark:text-gray-300 bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 transition-all active:scale-[0.98]"
            >
              取消
            </button>
            <button
              onClick={handleSave}
              disabled={!title.trim()}
              className={`flex-1 h-12 rounded-xl font-semibold text-white transition-all active:scale-[0.98] ${
                title.trim()
                  ? 'bg-gradient-to-r from-green-500 to-green-600 shadow-lg shadow-green-500/25 hover:shadow-xl'
                  : 'bg-gray-300 dark:bg-gray-700 cursor-not-allowed'
              }`}
            >
              保存
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
