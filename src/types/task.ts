export type TaskStatus = 'todo' | 'completed';
export type TaskPriority = 1 | 2 | 3;
export type FilterType = 'all' | 'today' | 'tomorrow' | 'week';
export type ThemeType = 'light' | 'dark';

export interface Task {
  id: number;
  title: string;
  description?: string;
  status: TaskStatus;
  priority: TaskPriority;
  dueDate?: number;
  parentId?: number | null;
  order: number;
  aiGenerated: boolean;
  createTime: number;
  pomodoroCount: number;
  children?: Task[];
}

export interface TaskGroup {
  key: string;
  label: string;
  tasks: Task[];
}
