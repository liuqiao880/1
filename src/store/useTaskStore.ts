import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { Task, FilterType, ThemeType } from '@/types/task';
import { mockTasks } from '@/data/mockTasks';

interface AppState {
  tasks: Task[];
  filter: FilterType;
  theme: ThemeType;
  expandedParents: number[];
  searchQuery: string;
  showAiModal: boolean;
  showSearch: boolean;

  toggleTask: (id: number) => void;
  toggleExpand: (id: number) => void;
  setFilter: (filter: FilterType) => void;
  toggleTheme: () => void;
  setSearchQuery: (q: string) => void;
  setShowAiModal: (show: boolean) => void;
  setShowSearch: (show: boolean) => void;
  addTask: (task: Task) => void;
  deleteTask: (id: number) => void;
}

function toggleTaskInTree(tasks: Task[], id: number): Task[] {
  return tasks.map((t) => {
    if (t.id === id) {
      return { ...t, status: t.status === 'completed' ? 'todo' : 'completed' };
    }
    if (t.children && t.children.length > 0) {
      return { ...t, children: toggleTaskInTree(t.children, id) };
    }
    return t;
  });
}

export const useTaskStore = create<AppState>()(
  persist(
    (set, get) => ({
      tasks: mockTasks,
      filter: 'all',
      theme: 'light',
      expandedParents: [2, 4],
      searchQuery: '',
      showAiModal: false,
      showSearch: false,

      toggleTask: (id: number) => {
        set({ tasks: toggleTaskInTree(get().tasks, id) });
      },

      toggleExpand: (id: number) => {
        const { expandedParents } = get();
        if (expandedParents.includes(id)) {
          set({ expandedParents: expandedParents.filter((x) => x !== id) });
        } else {
          set({ expandedParents: [...expandedParents, id] });
        }
      },

      setFilter: (filter: FilterType) => set({ filter }),

      toggleTheme: () => {
        const { theme } = get();
        const next = theme === 'light' ? 'dark' : 'light';
        set({ theme: next });
        if (typeof document !== 'undefined') {
          if (next === 'dark') {
            document.documentElement.classList.add('dark');
          } else {
            document.documentElement.classList.remove('dark');
          }
        }
      },

      setSearchQuery: (q: string) => set({ searchQuery: q }),

      setShowAiModal: (show: boolean) => set({ showAiModal: show }),

      setShowSearch: (show: boolean) => set({ showSearch: show }),

      addTask: (task: Task) => {
        set({ tasks: [task, ...get().tasks] });
      },

      deleteTask: (id: number) => {
        const filterFn = (items: Task[]): Task[] =>
          items
            .filter((t) => t.id !== id)
            .map((t) => (t.children ? { ...t, children: filterFn(t.children) } : t));
        set({ tasks: filterFn(get().tasks) });
      },
    }),
    {
      name: 'taskflow-storage',
    }
  )
);
