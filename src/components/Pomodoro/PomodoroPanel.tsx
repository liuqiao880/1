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
    focus: 'bg-red-500',
    shortBreak: 'bg-green-500',
    longBreak: 'bg-blue-500',
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
        className="absolute inset-0 bg-black/40 backdrop-blur-sm animate-fadeIn"
        onClick={() => setShowPanel(false)}
      />
      <div className="relative w-full max-w-md bg-white dark:bg-gray-900 rounded-t-3xl shadow-2xl animate-slideUp pb-8">
        <div className="flex items-center justify-between px-6 pt-5 pb-2">
          <div className="flex items-center gap-2">
            <Timer size={20} className="text-red-500" />
            <span className="font-semibold text-gray-900 dark:text-white">番茄钟</span>
          </div>
          <button
            onClick={() => setShowPanel(false)}
            className="w-8 h-8 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
          >
            <X size={18} className="text-gray-500" />
          </button>
        </div>

        <div className="px-6 pt-2 pb-6">
          <div className={`inline-block px-3 py-1 rounded-full ${phaseColor} text-white text-xs font-medium mb-6`}>
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
                  strokeWidth="8"
                  fill="none"
                  className="text-gray-100 dark:text-gray-800"
                />
                <circle
                  cx="130"
                  cy="130"
                  r="120"
                  stroke="currentColor"
                  strokeWidth="8"
                  fill="none"
                  strokeLinecap="round"
                  strokeDasharray={circumference}
                  strokeDashoffset={strokeDashoffset}
                  className={phase === 'focus' ? 'text-red-500' : phase === 'shortBreak' ? 'text-green-500' : 'text-blue-500'}
                  style={{ transition: 'stroke-dashoffset 1s linear' }}
                />
              </svg>
              <div className="absolute inset-0 flex flex-col items-center justify-center">
                <span className="text-5xl font-bold text-gray-900 dark:text-white tabular-nums">
                  {timeStr}
                </span>
                <span className="text-sm text-gray-500 dark:text-gray-400 mt-2">
                  今日已完成 {completedPomodoros} 个番茄
                </span>
              </div>
            </div>

            {taskTitle && (
              <div className="mt-6 text-center">
                <p className="text-sm text-gray-500 dark:text-gray-400">当前任务</p>
                <p className="font-medium text-gray-900 dark:text-white mt-1 line-clamp-2 max-w-xs">
                  {taskTitle}
                </p>
              </div>
            )}

            <div className="flex items-center gap-6 mt-8">
              <button
                onClick={reset}
                className="w-12 h-12 flex items-center justify-center rounded-full bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors"
              >
                <RotateCcw size={20} className="text-gray-600 dark:text-gray-300" />
              </button>

              <button
                onClick={() => status === 'running' ? pause() : resume()}
                className={`w-16 h-16 flex items-center justify-center rounded-full transition-all ${
                  phase === 'focus'
                    ? 'bg-red-500 hover:bg-red-600'
                    : phase === 'shortBreak'
                    ? 'bg-green-500 hover:bg-green-600'
                    : 'bg-blue-500 hover:bg-blue-600'
                } text-white shadow-lg`}
              >
                {status === 'running' ? <Pause size={28} fill="currentColor" /> : <Play size={28} fill="currentColor" className="ml-1" />}
              </button>

              <button
                onClick={skip}
                className="w-12 h-12 flex items-center justify-center rounded-full bg-gray-100 dark:bg-gray-800 hover:bg-gray-200 dark:hover:bg-gray-700 transition-colors"
              >
                <SkipForward size={20} className="text-gray-600 dark:text-gray-300" />
              </button>
            </div>

            <div className="flex gap-4 mt-6">
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
                  className={`px-4 py-2 rounded-full text-sm font-medium transition-colors ${
                    phase === p
                      ? p === 'focus'
                        ? 'bg-red-100 text-red-600 dark:bg-red-950/50 dark:text-red-400'
                        : p === 'shortBreak'
                        ? 'bg-green-100 text-green-600 dark:bg-green-950/50 dark:text-green-400'
                        : 'bg-blue-100 text-blue-600 dark:bg-blue-950/50 dark:text-blue-400'
                      : 'bg-gray-100 dark:bg-gray-800 text-gray-600 dark:text-gray-300'
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
