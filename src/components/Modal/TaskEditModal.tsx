import { useState, useEffect } from 'react';
import { X, Calendar, Flag, Sparkles } from 'lucide-react';
import { Task, TaskPriority } from '@/types/task';

interface TaskEditModalProps {
  task: Task | null;
  isOpen: boolean;
  onClose: () => void;
  onSave: (task: Task) => void;
}

const priorityOptions: { value: TaskPriority; label: string; color: string; textColor: string }[] = [
  { value: 1, label: '紧急', color: 'bg-priority-high', textColor: 'text-priority-high' },
  { value: 2, label: '普通', color: 'bg-priority-medium', textColor: 'text-priority-medium' },
  { value: 3, label: '低优', color: 'bg-priority-low', textColor: 'text-priority-low' },
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
        className="absolute inset-0 bg-black/30 backdrop-blur-sm animate-fadeIn"
        onClick={onClose}
      />

      <div className="relative w-full sm:max-w-md bg-paper-cream dark:bg-gray-900 sm:rounded-sm shadow-2xl animate-slideUp max-h-[85vh] flex flex-col border border-line-separator dark:border-gray-800">
        <div className="flex items-center justify-between px-6 pt-5 pb-3 border-b border-line-separator dark:border-gray-800">
          <div className="flex items-center gap-2">
            <div className="newspaper-accent-line" />
            <h2 className="font-serif text-lg font-semibold text-ink-black dark:text-white">编辑任务</h2>
          </div>
          <button
            onClick={onClose}
            className="w-9 h-9 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors active:scale-90"
          >
            <X size={18} className="text-ink-light" />
          </button>
        </div>

        <div className="flex-1 overflow-y-auto p-6 space-y-5">
          <div>
            <label className="block text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
              任务标题
            </label>
            <input
              type="text"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-3 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white placeholder-ink-light outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all font-sans text-sm"
            />
          </div>

          <div>
            <label className="block text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
              描述
            </label>
            <textarea
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              rows={3}
              className="w-full px-4 py-3 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white placeholder-ink-light outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all resize-none font-sans text-sm"
            />
          </div>

          <div>
            <label className="flex items-center gap-1 text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
              <Calendar size={14} />
              截止日期
            </label>
            <input
              type="date"
              value={dueDate}
              onChange={(e) => setDueDate(e.target.value)}
              className="w-full px-4 py-3 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 transition-all font-sans text-sm"
            />
          </div>

          <div>
            <label className="flex items-center gap-1 text-sm font-serif font-medium text-ink-gray dark:text-gray-300 mb-2">
              <Flag size={14} />
              优先级
            </label>
            <div className="flex gap-2">
              {priorityOptions.map((opt) => (
                <button
                  key={opt.value}
                  onClick={() => setPriority(opt.value)}
                  className={`flex-1 py-2.5 text-sm font-medium font-sans transition-all border ${
                    priority === opt.value
                      ? `${opt.color} text-paper-white border-transparent`
                      : 'bg-transparent text-ink-gray dark:text-gray-400 border-line-separator dark:border-gray-700 hover:bg-black/5 dark:hover:bg-white/5'
                  }`}
                >
                  {opt.label}
                </button>
              ))}
            </div>
          </div>

          {task.aiGenerated && (
            <div className="flex items-center gap-2 px-4 py-3 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5">
              <Sparkles size={16} className="text-newspaper-red dark:text-newspaper-red-light" />
              <span className="text-sm text-newspaper-red dark:text-newspaper-red-light font-sans">
                此任务由 AI 生成
              </span>
            </div>
          )}
        </div>

        <div className="px-6 py-4 border-t border-line-separator dark:border-gray-800">
          <div className="flex gap-3">
            <button
              onClick={onClose}
              className="flex-1 h-11 font-serif font-semibold text-ink-gray dark:text-gray-300 bg-transparent border border-line-separator dark:border-gray-700 hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-[0.98] text-sm"
            >
              取消
            </button>
            <button
              onClick={handleSave}
              disabled={!title.trim()}
              className={`flex-1 h-11 font-serif font-semibold text-paper-white transition-all active:scale-[0.98] text-sm ${
                title.trim()
                  ? 'bg-ink-black hover:bg-newspaper-red'
                  : 'bg-line-separator dark:bg-gray-700 cursor-not-allowed'
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
