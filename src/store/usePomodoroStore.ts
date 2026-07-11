import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export type PomodoroPhase = 'focus' | 'shortBreak' | 'longBreak';
export type PomodoroStatus = 'idle' | 'running' | 'paused';

interface PomodoroState {
  taskId: number | null;
  taskTitle: string;
  phase: PomodoroPhase;
  status: PomodoroStatus;
  timeLeft: number;
  completedPomodoros: number;
  showPanel: boolean;

  focusDuration: number;
  shortBreakDuration: number;
  longBreakDuration: number;
  longBreakInterval: number;

  startPomodoro: (taskId: number, taskTitle: string) => void;
  pause: () => void;
  resume: () => void;
  reset: () => void;
  skip: () => void;
  tick: () => void;
  setShowPanel: (show: boolean) => void;
  completePhase: () => void;
}

export const usePomodoroStore = create<PomodoroState>()(
  persist(
    (set, get) => ({
      taskId: null,
      taskTitle: '',
      phase: 'focus',
      status: 'idle',
      timeLeft: 25 * 60,
      completedPomodoros: 0,
      showPanel: false,

      focusDuration: 25 * 60,
      shortBreakDuration: 5 * 60,
      longBreakDuration: 15 * 60,
      longBreakInterval: 4,

      startPomodoro: (taskId: number, taskTitle: string) => {
        set({
          taskId,
          taskTitle,
          phase: 'focus',
          status: 'running',
          timeLeft: get().focusDuration,
          showPanel: true,
        });
      },

      pause: () => {
        if (get().status === 'running') {
          set({ status: 'paused' });
        }
      },

      resume: () => {
        if (get().status === 'paused') {
          set({ status: 'running' });
        }
      },

      reset: () => {
        const { phase, focusDuration, shortBreakDuration, longBreakDuration } = get();
        let timeLeft = focusDuration;
        if (phase === 'shortBreak') timeLeft = shortBreakDuration;
        if (phase === 'longBreak') timeLeft = longBreakDuration;
        set({ status: 'idle', timeLeft });
      },

      skip: () => {
        get().completePhase();
      },

      tick: () => {
        const { status, timeLeft } = get();
        if (status !== 'running') return;
        if (timeLeft > 0) {
          set({ timeLeft: timeLeft - 1 });
        } else {
          get().completePhase();
        }
      },

      completePhase: () => {
        const { phase, completedPomodoros, longBreakInterval, focusDuration, shortBreakDuration, longBreakDuration } = get();
        
        if (phase === 'focus') {
          const newCount = completedPomodoros + 1;
          const nextPhase: PomodoroPhase = 
            newCount % longBreakInterval === 0 ? 'longBreak' : 'shortBreak';
          const nextTime = nextPhase === 'longBreak' ? longBreakDuration : shortBreakDuration;
          
          set({
            phase: nextPhase,
            status: 'idle',
            timeLeft: nextTime,
            completedPomodoros: newCount,
          });

          if ('Notification' in window && Notification.permission === 'granted') {
            new Notification('🍅 专注完成！', {
              body: nextPhase === 'longBreak' ? '休息一下吧，长休息 15 分钟' : '休息 5 分钟',
            });
          }
        } else {
          set({
            phase: 'focus',
            status: 'idle',
            timeLeft: focusDuration,
          });

          if ('Notification' in window && Notification.permission === 'granted') {
            new Notification('☕ 休息结束', {
              body: '准备好开始下一个番茄钟了吗？',
            });
          }
        }
      },

      setShowPanel: (show: boolean) => set({ showPanel: show }),
    }),
    {
      name: 'taskflow-pomodoro',
      partialize: (state) => ({
        completedPomodoros: state.completedPomodoros,
        focusDuration: state.focusDuration,
        shortBreakDuration: state.shortBreakDuration,
        longBreakDuration: state.longBreakDuration,
        longBreakInterval: state.longBreakInterval,
      }),
    }
  )
);
