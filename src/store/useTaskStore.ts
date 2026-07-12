import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { Task, FilterType, ThemeType, AccentColor } from '@/types/task';
import { mockTasks } from '@/data/mockTasks';

interface AppState {
  tasks: Task[];
  filter: FilterType;
  theme: ThemeType;
  accentColor: AccentColor;
  notificationEnabled: boolean;
  expandedParents: number[];
  searchQuery: string;
  showAiModal: boolean;
  showSearch: boolean;
  showSettings: boolean;
  showEditModal: boolean;
  editingTask: Task | null;
  selectedTasks: number[];
  multiSelectMode: boolean;
  undoBuffer: { tasks: Task[]; message: string } | null;

  toggleTask: (id: number) => void;
  toggleExpand: (id: number) => void;
  setFilter: (filter: FilterType) => void;
  toggleTheme: () => void;
  setAccentColor: (color: AccentColor) => void;
  toggleNotification: () => void;
  setSearchQuery: (q: string) => void;
  setShowAiModal: (show: boolean) => void;
  setShowSearch: (show: boolean) => void;
  setShowSettings: (show: boolean) => void;
  openEditModal: (task: Task) => void;
  closeEditModal: () => void;
  updateTask: (task: Task) => void;
  addTask: (task: Task) => void;
  deleteTask: (id: number) => void;
  toggleMultiSelect: () => void;
  toggleSelected: (id: number) => void;
  clearSelection: () => void;
  deleteSelected: () => void;
  undo: () => void;
  incrementPomodoro: (taskId: number) => void;
}

function mapTaskInTree(tasks: Task[], id: number, updater: (t: Task) => Task): Task[] {
  return tasks.map((t) => {
    if (t.id === id) {
      return updater(t);
    }
    if (t.children && t.children.length > 0) {
      return { ...t, children: mapTaskInTree(t.children, id, updater) };
    }
    return t;
  });
}

function toggleTaskInTree(tasks: Task[], id: number): Task[] {
  return mapTaskInTree(tasks, id, (t) => ({
    ...t,
    status: t.status === 'completed' ? 'todo' : 'completed',
  }));
}

function deepCloneTasks(tasks: Task[]): Task[] {
  return JSON.parse(JSON.stringify(tasks));
}

export const useTaskStore = create<AppState>()(
  persist(
    (set, get) => ({
      tasks: mockTasks,
      filter: 'all',
      theme: 'light',
      accentColor: 'red',
      notificationEnabled: true,
      expandedParents: [2, 4],
      searchQuery: '',
      showAiModal: false,
      showSearch: false,
      showSettings: false,
      showEditModal: false,
      editingTask: null,
      selectedTasks: [],
      multiSelectMode: false,
      undoBuffer: null,

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

      setAccentColor: (color: AccentColor) => {
        set({ accentColor: color });
        if (typeof document !== 'undefined') {
          document.documentElement.setAttribute('data-accent', color);
        }
      },

      toggleNotification: () => {
        set({ notificationEnabled: !get().notificationEnabled });
      },

      setSearchQuery: (q: string) => set({ searchQuery: q }),

      setShowAiModal: (show: boolean) => set({ showAiModal: show }),

      setShowSearch: (show: boolean) => set({ showSearch: show }),

      setShowSettings: (show: boolean) => set({ showSettings: show }),

      openEditModal: (task: Task) => {
        set({ editingTask: task, showEditModal: true });
      },

      closeEditModal: () => {
        set({ showEditModal: false, editingTask: null });
      },

      updateTask: (updated: Task) => {
        set({
          tasks: mapTaskInTree(get().tasks, updated.id, () => ({
            ...updated,
            updateTime: Date.now(),
          })),
        });
      },

      addTask: (task: Task) => {
        set({ tasks: [task, ...get().tasks] });
      },

      deleteTask: (id: number) => {
        const currentTasks = deepCloneTasks(get().tasks);
        const filterFn = (items: Task[]): Task[] =>
          items
            .filter((t) => t.id !== id)
            .map((t) => (t.children ? { ...t, children: filterFn(t.children) } : t));
        const newTasks = filterFn(currentTasks);
        set({
          tasks: newTasks,
          undoBuffer: { tasks: currentTasks, message: '任务已删除' },
        });

        setTimeout(() => {
          set({ undoBuffer: null });
        }, 3000);
      },

      toggleMultiSelect: () => {
        const { multiSelectMode } = get();
        set({
          multiSelectMode: !multiSelectMode,
          selectedTasks: multiSelectMode ? [] : get().selectedTasks,
        });
      },

      toggleSelected: (id: number) => {
        const { selectedTasks } = get();
        if (selectedTasks.includes(id)) {
          set({ selectedTasks: selectedTasks.filter((x) => x !== id) });
        } else {
          set({ selectedTasks: [...selectedTasks, id] });
        }
      },

      clearSelection: () => {
        set({ selectedTasks: [], multiSelectMode: false });
      },

      deleteSelected: () => {
        const { selectedTasks, tasks } = get();
        const currentTasks = deepCloneTasks(tasks);
        const filterFn = (items: Task[]): Task[] =>
          items
            .filter((t) => !selectedTasks.includes(t.id))
            .map((t) => (t.children ? { ...t, children: filterFn(t.children) } : t));
        set({
          tasks: filterFn(currentTasks),
          selectedTasks: [],
          multiSelectMode: false,
          undoBuffer: { tasks: currentTasks, message: `已删除 ${selectedTasks.length} 个任务` },
        });

        setTimeout(() => {
          set({ undoBuffer: null });
        }, 3000);
      },

      undo: () => {
        const { undoBuffer } = get();
        if (undoBuffer) {
          set({ tasks: undoBuffer.tasks, undoBuffer: null });
        }
      },

      incrementPomodoro: (taskId: number) => {
        set({
          tasks: mapTaskInTree(get().tasks, taskId, (t) => ({
            ...t,
            pomodoroCount: (t.pomodoroCount || 0) + 1,
          })),
        });
      },
    }),
    {
      name: 'taskflow-storage',
      partialize: (state) => ({
        tasks: state.tasks,
        theme: state.theme,
        accentColor: state.accentColor,
        notificationEnabled: state.notificationEnabled,
        expandedParents: state.expandedParents,
        filter: state.filter,
      }),
    }
  )
);
