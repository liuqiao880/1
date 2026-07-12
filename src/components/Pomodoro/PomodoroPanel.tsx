import { useEffect, useRef } from 'react';
import { X, Play, Pause, RotateCcw, SkipForward, Timer } from 'lucide-react';
import { usePomodoroStore } from '@/store/usePomodoroStore';
import { useTaskStore } from '@/store/useTaskStore';

export default function PomodoroPanel() {
  const {
    showPanel,
    phase,
    status,
    timeLeft,
    taskTitle,
    completedPomodoros,
    setShowPanel,
    pause,
    resume,
    reset,
    skip,
    tick,
    taskId,
  } = usePomodoroStore();

  const { incrementPomodoro } = useTaskStore();
  const prevPhaseRef = useRef(phase);
  const timerRef = useRef<number | null>(null);

  useEffect(() => {
    if (status === 'running') {
      timerRef.current = window.setInterval(() => {
        tick();
      }, 1000);
    } else {
      if (timerRef.current) {
        clearInterval(timerRef.current);
        timerRef.current = null;
      }
    }
    return () => {
      if (timerRef.current) {
        clearInterval(timerRef.current);
      }
    };
  }, [status, tick]);

  useEffect(() => {
    if (prevPhaseRef.current === 'focus' && phase !== 'focus' && taskId) {
      incrementPomodoro(taskId);
    }
    prevPhaseRef.current = phase;
  }, [phase, taskId, incrementPomodoro]);

  useEffect(() => {
    if (showPanel && 'Notification' in window && Notification.permission === 'default') {
      Notification.requestPermission();
    }
  }, [showPanel]);

  if (!showPanel) return null;

  const minutes = Math.floor(timeLeft / 60);
  const seconds = timeLeft % 60;
  const timeStr = `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;

  const phaseLabel = {
    focus: '专注中',
    shortBreak: '短休息',
    longBreak: '长休息',
  }[phase];

  const phaseColor = {
    focus: 'bg-newspaper-red',
    shortBreak: 'bg-ink-black',
    longBreak: 'bg-priority-low',
  }[phase];

  const progress = () => {
    const { focusDuration, shortBreakDuration, longBreakDuration } = usePomodoroStore.getState();
    const total = phase === 'focus' ? focusDuration : phase === 'shortBreak' ? shortBreakDuration : longBreakDuration;
    return ((total - timeLeft) / total) * 100;
  };

  const circumference = 2 * Math.PI * 120;
  const strokeDashoffset = circumference - (progress() / 100) * circumference;

  return (
    <div className="fixed inset-0 z-50 flex items-end justify-center">
      <div 
        className="absolute inset-0 bg-black/30 backdrop-blur-sm animate-fadeIn"
        onClick={() => setShowPanel(false)}
      />
      <div className="relative w-full max-w-md bg-paper-cream dark:bg-gray-900 shadow-2xl animate-slideUp pb-8 border-t border-line-separator dark:border-gray-800">
        <div className="flex items-center justify-between px-6 pt-5 pb-2">
          <div className="flex items-center gap-2">
            <Timer size={18} className="text-newspaper-red" />
            <span className="font-serif font-semibold text-ink-black dark:text-white">番茄钟</span>
          </div>
          <button
            onClick={() => setShowPanel(false)}
            className="w-8 h-8 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
          >
            <X size={16} className="text-ink-light" />
          </button>
        </div>

        <div className="px-6 pt-2 pb-6">
          <div className={`inline-block px-2.5 py-1 ${phaseColor} text-white text-[10px] font-medium mb-6`}>
            {phaseLabel}
          </div>

          <div className="flex flex-col items-center">
            <div className="relative w-64 h-64">
              <svg className="w-full h-full -rotate-90" viewBox="0 0 260 260">
                <circle
                  cx="130"
                  cy="130"
                  r="120"
                  stroke="currentColor"
                  strokeWidth="6"
                  fill="none"
                  className="text-line-separator dark:text-gray-800"
                />
                <circle
                  cx="130"
                  cy="130"
                  r="120"
                  stroke="currentColor"
                  strokeWidth="6"
                  fill="none"
                  strokeLinecap="round"
                  strokeDasharray={circumference}
                  strokeDashoffset={strokeDashoffset}
                  className={phase === 'focus' ? 'text-newspaper-red' : phase === 'shortBreak' ? 'text-ink-black' : 'text-priority-low'}
                  style={{ transition: 'stroke-dashoffset 1s linear' }}
                />
              </svg>
              <div className="absolute inset-0 flex flex-col items-center justify-center">
                <span className="text-5xl font-serif font-bold text-ink-black dark:text-white tabular-nums">
                  {timeStr}
                </span>
                <span className="text-xs text-ink-light dark:text-gray-400 mt-2 font-sans">
                  今日已完成 {completedPomodoros} 个番茄
                </span>
              </div>
            </div>

            {taskTitle && (
              <div className="mt-6 text-center">
                <p className="text-xs text-ink-light dark:text-gray-400 font-sans">当前任务</p>
                <p className="font-serif font-medium text-ink-black dark:text-white mt-1 line-clamp-2 max-w-xs text-sm">
                  {taskTitle}
                </p>
              </div>
            )}

            <div className="flex items-center gap-6 mt-8">
              <button
                onClick={reset}
                className="w-11 h-11 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
              >
                <RotateCcw size={18} className="text-ink-gray dark:text-gray-400" />
              </button>

              <button
                onClick={() => status === 'running' ? pause() : resume()}
                className={`w-14 h-14 flex items-center justify-center transition-all text-white shadow-lg ${
                  phase === 'focus'
                    ? 'bg-newspaper-red hover:bg-newspaper-red-dark'
                    : phase === 'shortBreak'
                    ? 'bg-ink-black hover:bg-ink-gray'
                    : 'bg-priority-low hover:bg-priority-low/80'
                }`}
              >
                {status === 'running' ? <Pause size={24} fill="currentColor" /> : <Play size={24} fill="currentColor" className="ml-1" />}
              </button>

              <button
                onClick={skip}
                className="w-11 h-11 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
              >
                <SkipForward size={18} className="text-ink-gray dark:text-gray-400" />
              </button>
            </div>

            <div className="flex gap-3 mt-6">
              {(['focus', 'shortBreak', 'longBreak'] as const).map((p) => (
                <button
                  key={p}
                  onClick={() => {
                    const store = usePomodoroStore.getState();
                    let duration = store.focusDuration;
                    if (p === 'shortBreak') duration = store.shortBreakDuration;
                    if (p === 'longBreak') duration = store.longBreakDuration;
                    usePomodoroStore.setState({ phase: p, timeLeft: duration, status: 'idle' });
                  }}
                  className={`px-3 py-1.5 text-xs font-medium font-sans transition-colors ${
                    phase === p
                      ? p === 'focus'
                        ? 'bg-newspaper-red/10 text-newspaper-red dark:bg-newspaper-red/20 dark:text-newspaper-red-light'
                        : p === 'shortBreak'
                        ? 'bg-ink-black/10 text-ink-black dark:bg-white/10 dark:text-gray-300'
                        : 'bg-priority-low/10 text-priority-low dark:bg-priority-low/20'
                      : 'text-ink-light dark:text-gray-500 hover:bg-black/5 dark:hover:bg-white/5'
                  }`}
                >
                  {p === 'focus' ? '专注' : p === 'shortBreak' ? '短休' : '长休'}
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
