interface ProgressBarProps {
  percent: number;
  size?: 'sm' | 'md';
}

export default function ProgressBar({ percent, size = 'sm' }: ProgressBarProps) {
  const heights = {
    sm: 'h-1.5',
    md: 'h-2',
  };

  return (
    <div className={`w-16 ${heights[size]} bg-gray-200 dark:bg-gray-700 rounded-full overflow-hidden`}>
      <div
        className="h-full bg-gradient-to-r from-green-500 to-green-600 rounded-full transition-all duration-500 ease-out"
        style={{ width: `${Math.min(100, Math.max(0, percent))}%` }}
      />
    </div>
  );
}
