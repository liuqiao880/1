import { Task, FilterType, TaskGroup } from '@/types/task';

const dayMs = 24 * 60 * 60 * 1000;

export function startOfDay(ts: number): number {
  const d = new Date(ts);
  d.setHours(0, 0, 0, 0);
  return d.getTime();
}

export function isSameDay(a: number, b: number): boolean {
  return startOfDay(a) === startOfDay(b);
}

export function isToday(ts: number): boolean {
  return isSameDay(ts, Date.now());
}

export function isTomorrow(ts: number): boolean {
  return isSameDay(ts, Date.now() + dayMs);
}

export function isThisWeek(ts: number): boolean {
  const now = new Date();
  const day = now.getDay();
  const diff = day === 0 ? 6 : day - 1;
  const monday = startOfDay(now.getTime() - diff * dayMs);
  const nextMonday = monday + 7 * dayMs;
  return ts >= monday && ts < nextMonday;
}

export function formatDate(ts?: number): string {
  if (!ts) return '';
  const d = new Date(ts);
  const now = new Date();
  const today = startOfDay(now.getTime());
  const target = startOfDay(ts);
  const diff = Math.round((target - today) / dayMs);

  if (diff === 0) return '今天';
  if (diff === 1) return '明天';
  if (diff === -1) return '昨天';
  if (diff > 1 && diff < 7) {
    const weekdays = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
    return weekdays[d.getDay()];
  }
  return `${d.getMonth() + 1}月${d.getDate()}日`;
}

export function formatRelativeTime(ts: number): string {
  const diff = Date.now() - ts;
  const minutes = Math.floor(diff / 60000);
  const hours = Math.floor(diff / 3600000);
  const days = Math.floor(diff / dayMs);

  if (minutes < 1) return '刚刚';
  if (minutes < 60) return `${minutes} 分钟前`;
  if (hours < 24) return `${hours} 小时前`;
  if (days < 7) return `${days} 天前`;
  return formatDate(ts);
}

export function groupTasksByDate(tasks: Task[]): TaskGroup[] {
  const now = Date.now();
  const today = startOfDay(now);
  const tomorrow = today + dayMs;
  const dayOfWeek = new Date(today).getDay();
  const diffToMonday = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
  const weekStart = today - diffToMonday * dayMs;
  const weekEnd = weekStart + 7 * dayMs;

  const todayTasks: Task[] = [];
  const tomorrowTasks: Task[] = [];
  const weekTasks: Task[] = [];
  const laterTasks: Task[] = [];

  for (const task of tasks) {
    if (task.parentId) continue;
    if (!task.dueDate) {
      laterTasks.push(task);
      continue;
    }
    const due = startOfDay(task.dueDate);
    if (due === today) {
      todayTasks.push(task);
    } else if (due === tomorrow) {
      tomorrowTasks.push(task);
    } else if (due >= weekStart && due < weekEnd) {
      weekTasks.push(task);
    } else {
      laterTasks.push(task);
    }
  }

  const groups: TaskGroup[] = [];
  if (todayTasks.length > 0) {
    groups.push({ key: 'today', label: '今天', tasks: todayTasks.sort((a, b) => a.order - b.order) });
  }
  if (tomorrowTasks.length > 0) {
    groups.push({ key: 'tomorrow', label: '明天', tasks: tomorrowTasks.sort((a, b) => a.order - b.order) });
  }
  if (weekTasks.length > 0) {
    groups.push({ key: 'week', label: '本周', tasks: weekTasks.sort((a, b) => (a.dueDate || 0) - (b.dueDate || 0)) });
  }
  if (laterTasks.length > 0) {
    groups.push({ key: 'later', label: '更晚', tasks: laterTasks.sort((a, b) => (a.dueDate || Infinity) - (b.dueDate || Infinity)) });
  }

  return groups;
}

export function filterTasks(tasks: Task[], filter: FilterType, searchQuery: string): Task[] {
  let result = tasks;

  if (searchQuery.trim()) {
    const q = searchQuery.toLowerCase();
    result = result.filter(
      (t) =>
        t.title.toLowerCase().includes(q) ||
        (t.description && t.description.toLowerCase().includes(q)) ||
        (t.children && t.children.some((c) => c.title.toLowerCase().includes(q)))
    );
  }

  if (filter === 'all') return result;

  const now = Date.now();
  const today = startOfDay(now);
  const tomorrow = today + dayMs;
  const dayOfWeek = new Date(today).getDay();
  const diffToMonday = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
  const weekStart = today - diffToMonday * dayMs;
  const weekEnd = weekStart + 7 * dayMs;

  return result.filter((task) => {
    if (!task.dueDate) return false;
    const due = startOfDay(task.dueDate);
    if (filter === 'today') return due === today;
    if (filter === 'tomorrow') return due === tomorrow;
    if (filter === 'week') return due >= weekStart && due < weekEnd;
    return true;
  });
}

export function calcProgress(task: Task): { completed: number; total: number; percent: number } {
  if (!task.children || task.children.length === 0) {
    const completed = task.status === 'completed' ? 1 : 0;
    return { completed, total: 1, percent: completed * 100 };
  }
  const total = task.children.length;
  const completed = task.children.filter((c) => c.status === 'completed').length;
  return { completed, total, percent: total > 0 ? Math.round((completed / total) * 100) : 0 };
}
