import { X, Moon, Sun, Bell, Palette, Key, Info, Shield, Check, Wifi } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';
import { useChatStore } from '@/store/useChatStore';
import { useState, useRef } from 'react';
import { AccentColor, AiConfig } from '@/types/task';
import { aiService } from '@/services/aiService';

const accentColors: { key: AccentColor; label: string; value: string }[] = [
  { key: 'red', label: '报纸红', value: '#C41E3A' },
  { key: 'ink', label: '墨黑', value: '#1A1A1A' },
  { key: 'gold', label: '暗金', value: '#B8860B' },
  { key: 'blue', label: '钢蓝', value: '#4682B4' },
];

export default function SettingsModal() {
  const {
    showSettings,
    setShowSettings,
    theme,
    toggleTheme,
    accentColor,
    setAccentColor,
    notificationEnabled,
    toggleNotification,
  } = useTaskStore();
  const { aiConfig, updateAiConfig } = useChatStore();
  const [savedTip, setSavedTip] = useState(false);
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState<{ success: boolean; message: string } | null>(null);
  const tipTimer = useRef<ReturnType<typeof setTimeout> | undefined>(undefined);

  const handleAiConfigChange = (field: keyof AiConfig, value: string) => {
    updateAiConfig({ [field]: value } as Partial<AiConfig>);
    setSavedTip(true);
    setTestResult(null);
    clearTimeout(tipTimer.current);
    tipTimer.current = setTimeout(() => setSavedTip(false), 1500);
  };

  const handleProviderChange = (provider: string) => {
    const preset = aiService.providerPresets[provider];
    if (preset) {
      updateAiConfig({
        provider,
        baseUrl: preset.baseUrl,
        model: preset.model,
      });
    } else {
      updateAiConfig({ provider });
    }
    setSavedTip(true);
    setTestResult(null);
    clearTimeout(tipTimer.current);
    tipTimer.current = setTimeout(() => setSavedTip(false), 1500);
  };

  const handleTestConnection = async () => {
    if (!aiConfig.apiKey) {
      setTestResult({ success: false, message: '请先填写 API Key' });
      return;
    }
    setTesting(true);
    setTestResult(null);
    try {
      const result = await aiService.chat(
        [{ role: 'user', content: 'Hi' }],
        { ...aiConfig, systemPrompt: 'You are a helpful assistant.' }
      );
      if (result.includes('演示模式')) {
        setTestResult({ success: false, message: 'API Key 为空，正在使用演示模式' });
      } else {
        setTestResult({ success: true, message: '连接成功！API 配置正常' });
      }
    } catch (error) {
      setTestResult({
        success: false,
        message: error instanceof Error ? error.message : '连接失败',
      });
    } finally {
      setTesting(false);
    }
  };

  if (!showSettings) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center">
      <div
        className="absolute inset-0 bg-black/30 backdrop-blur-sm animate-fadeIn"
        onClick={() => setShowSettings(false)}
      />

      <div className="relative w-full sm:max-w-md bg-paper-cream dark:bg-gray-900 sm:rounded-sm shadow-2xl animate-slideUp max-h-[90vh] flex flex-col border border-line-separator dark:border-gray-800">
        {/* 头部 */}
        <div className="flex items-center justify-between px-6 pt-5 pb-3 border-b border-line-separator dark:border-gray-800 flex-shrink-0">
          <div className="flex items-center gap-2">
            <div className="newspaper-accent-line" />
            <h2 className="font-serif text-lg font-semibold text-ink-black dark:text-white">设置</h2>
          </div>
          <button
            onClick={() => setShowSettings(false)}
            className="w-9 h-9 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors active:scale-90"
          >
            <X size={18} className="text-ink-light" />
          </button>
        </div>

        {/* 内容 */}
        <div className="flex-1 overflow-y-auto">
          {/* 外观 */}
          <div className="px-6 py-4">
            <h3 className="font-serif text-xs font-semibold text-ink-gray dark:text-gray-400 uppercase tracking-wider mb-3">
              外观
            </h3>
            <div className="border border-line-separator dark:border-gray-800">
              {/* 主题模式 */}
              <button
                onClick={toggleTheme}
                className="w-full flex items-center justify-between px-4 py-3.5 hover:bg-paper-white dark:hover:bg-gray-800/50 transition-colors"
              >
                <div className="flex items-center gap-3">
                  <div className="w-9 h-9 bg-newspaper-red/10 dark:bg-newspaper-red/20 flex items-center justify-center">
                    {theme === 'light' ? (
                      <Sun size={18} className="text-newspaper-red dark:text-newspaper-red-light" />
                    ) : (
                      <Moon size={18} className="text-newspaper-red dark:text-newspaper-red-light" />
                    )}
                  </div>
                  <div className="text-left">
                    <p className="font-serif font-medium text-ink-black dark:text-white text-sm">主题模式</p>
                    <p className="text-xs text-ink-light dark:text-gray-400 font-sans">
                      {theme === 'light' ? '浅色模式' : '深色模式'}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <div
                    className={`w-11 h-6 p-0.5 transition-colors ${
                      theme === 'dark' ? 'bg-ink-black' : 'bg-line-separator dark:bg-gray-600'
                    }`}
                  >
                    <div
                      className={`w-5 h-5 bg-paper-white shadow-sm transition-transform ${
                        theme === 'dark' ? 'translate-x-5' : ''
                      }`}
                    />
                  </div>
                </div>
              </button>

              <div className="h-px bg-line-separator dark:bg-gray-800 mx-4" />

              {/* 主题色 - 可点击切换 */}
              <div className="px-4 py-3.5">
                <div className="flex items-center gap-3 mb-3">
                  <div className="w-9 h-9 bg-newspaper-red/10 dark:bg-newspaper-red/20 flex items-center justify-center">
                    <Palette size={18} className="text-newspaper-red dark:text-newspaper-red-light" />
                  </div>
                  <div className="text-left">
                    <p className="font-serif font-medium text-ink-black dark:text-white text-sm">主题色</p>
                    <p className="text-xs text-ink-light dark:text-gray-400 font-sans">
                      {accentColors.find((c) => c.key === accentColor)?.label || '报纸红'}
                    </p>
                  </div>
                </div>
                <div className="flex gap-3 pl-12">
                  {accentColors.map((color) => (
                    <button
                      key={color.key}
                      onClick={() => setAccentColor(color.key)}
                      className={`w-8 h-8 border-2 transition-all active:scale-90 ${
                        accentColor === color.key
                          ? 'border-ink-black dark:border-white scale-110'
                          : 'border-transparent hover:scale-105'
                      }`}
                      style={{ backgroundColor: color.value }}
                      title={color.label}
                    />
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* AI 设置 */}
          <div className="px-6 py-4">
            <h3 className="font-serif text-xs font-semibold text-ink-gray dark:text-gray-400 uppercase tracking-wider mb-3">
              AI 智能规划
            </h3>
            <div className="border border-line-separator dark:border-gray-800">
              <div className="px-4 py-4">
                <div className="flex items-center gap-3 mb-4">
                  <div className="w-9 h-9 bg-newspaper-red/10 dark:bg-newspaper-red/20 flex items-center justify-center">
                    <Key size={18} className="text-newspaper-red dark:text-newspaper-red-light" />
                  </div>
                  <div className="text-left flex-1">
                    <p className="font-serif font-medium text-ink-black dark:text-white text-sm">API 配置</p>
                    <p className="text-xs text-ink-light dark:text-gray-400 font-sans">使用你自己的 AI API</p>
                  </div>
                </div>

                <div className="space-y-3">
                  <div>
                    <label className="block text-xs text-ink-light dark:text-gray-400 mb-1 font-sans">
                      Provider
                    </label>
                    <select
                      value={aiConfig.provider}
                      onChange={(e) => handleProviderChange(e.target.value)}
                      className="w-full px-3 py-2 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
                    >
                      <option value="openai">OpenAI 兼容</option>
                      <option value="gemini">Google Gemini</option>
                      <option value="anthropic">Anthropic Claude</option>
                      <option value="ollama">Ollama (本地)</option>
                    </select>
                  </div>

                  <div>
                    <label className="block text-xs text-ink-light dark:text-gray-400 mb-1 font-sans">
                      Base URL
                    </label>
                    <input
                      type="text"
                      value={aiConfig.baseUrl}
                      onChange={(e) => handleAiConfigChange('baseUrl', e.target.value)}
                      placeholder="https://api.openai.com/v1"
                      className="w-full px-3 py-2 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
                    />
                  </div>

                  <div>
                    <label className="block text-xs text-ink-light dark:text-gray-400 mb-1 font-sans">
                      API Key
                    </label>
                    <input
                      type="password"
                      value={aiConfig.apiKey}
                      onChange={(e) => handleAiConfigChange('apiKey', e.target.value)}
                      placeholder="sk-..."
                      className="w-full px-3 py-2 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
                    />
                  </div>

                  <div>
                    <label className="block text-xs text-ink-light dark:text-gray-400 mb-1 font-sans">
                      Model
                    </label>
                    <input
                      type="text"
                      value={aiConfig.model}
                      onChange={(e) => handleAiConfigChange('model', e.target.value)}
                      placeholder="gpt-4o-mini"
                      className="w-full px-3 py-2 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
                    />
                  </div>

                  <button
                    onClick={handleTestConnection}
                    disabled={testing || !aiConfig.apiKey}
                    className={`w-full flex items-center justify-center gap-2 py-2.5 text-xs font-medium transition-all ${
                      testing || !aiConfig.apiKey
                        ? 'bg-line-separator dark:bg-gray-700 text-ink-light cursor-not-allowed'
                        : 'bg-ink-black hover:bg-newspaper-red text-paper-white active:scale-[0.99]'
                    }`}
                  >
                    <Wifi size={14} className={testing ? 'animate-pulse' : ''} />
                    {testing ? '正在测试...' : '测试连接'}
                  </button>

                  {testResult && (
                    <div className={`flex items-center gap-2 p-2.5 border text-xs font-sans ${
                      testResult.success
                        ? 'border-green-500/30 bg-green-50 dark:bg-green-900/20 text-green-700 dark:text-green-400'
                        : 'border-newspaper-red/30 bg-newspaper-red/5 text-newspaper-red dark:text-newspaper-red-light'
                    }`}>
                      {testResult.success ? <Check size={14} /> : <Shield size={14} />}
                      <span className="flex-1">{testResult.message}</span>
                    </div>
                  )}
                </div>

                {savedTip && (
                  <div className="mt-3 flex items-center gap-2 p-2 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5">
                    <Check size={14} className="text-newspaper-red" />
                    <p className="text-xs text-newspaper-red dark:text-newspaper-red-light font-sans">
                      配置已保存
                    </p>
                  </div>
                )}

                <div className="mt-3 flex items-start gap-2 p-3 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5">
                  <Shield size={14} className="text-newspaper-red mt-0.5 flex-shrink-0" />
                  <p className="text-xs text-newspaper-red dark:text-newspaper-red-light font-sans">
                    API Key 仅存在当前会话内存中，刷新页面后需重新输入；Key 会发送到您配置的 AI 服务地址
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* 通知 - 可开关 */}
          <div className="px-6 py-4">
            <h3 className="font-serif text-xs font-semibold text-ink-gray dark:text-gray-400 uppercase tracking-wider mb-3">
              通知
            </h3>
            <div className="border border-line-separator dark:border-gray-800">
              <button
                onClick={toggleNotification}
                className="w-full flex items-center justify-between px-4 py-3.5 hover:bg-paper-white dark:hover:bg-gray-800/50 transition-colors"
              >
                <div className="flex items-center gap-3">
                  <div className={`w-9 h-9 flex items-center justify-center transition-colors ${
                    notificationEnabled
                      ? 'bg-newspaper-red/10 dark:bg-newspaper-red/20'
                      : 'bg-ink-black/5 dark:bg-white/10'
                  }`}>
                    <Bell size={18} className={notificationEnabled ? 'text-newspaper-red dark:text-newspaper-red-light' : 'text-ink-light'} />
                  </div>
                  <div className="text-left">
                    <p className="font-serif font-medium text-ink-black dark:text-white text-sm">每日提醒</p>
                    <p className="text-xs text-ink-light dark:text-gray-400 font-sans">
                      {notificationEnabled ? '早晨 8:00 推送今日待办' : '已关闭'}
                    </p>
                  </div>
                </div>
                <div
                  className={`w-11 h-6 p-0.5 transition-colors ${
                    notificationEnabled ? 'bg-ink-black' : 'bg-line-separator dark:bg-gray-600'
                  }`}
                >
                  <div
                    className={`w-5 h-5 bg-paper-white shadow-sm transition-transform ${
                      notificationEnabled ? 'translate-x-5' : ''
                    }`}
                  />
                </div>
              </button>
            </div>
          </div>

          {/* 关于 */}
          <div className="px-6 py-4 pb-8">
            <h3 className="font-serif text-xs font-semibold text-ink-gray dark:text-gray-400 uppercase tracking-wider mb-3">
              关于
            </h3>
            <div className="border border-line-separator dark:border-gray-800">
              <div className="flex items-center justify-between px-4 py-3.5">
                <div className="flex items-center gap-3">
                  <div className="w-9 h-9 bg-ink-black/5 dark:bg-white/10 flex items-center justify-center">
                    <Info size={18} className="text-ink-gray dark:text-gray-300" />
                  </div>
                  <div className="text-left">
                    <p className="font-serif font-medium text-ink-black dark:text-white text-sm">版本</p>
                    <p className="text-xs text-ink-light dark:text-gray-400 font-sans">TaskFlow v0.6.0</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
