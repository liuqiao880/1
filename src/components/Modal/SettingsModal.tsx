import { X, Moon, Sun, Bell, Palette, Key, Info, ChevronRight, Shield } from 'lucide-react';
import { useTaskStore } from '@/store/useTaskStore';
import { useState } from 'react';

export default function SettingsModal() {
  const { showSettings, setShowSettings, theme, toggleTheme } = useTaskStore();
  const [aiConfig, setAiConfig] = useState({
    provider: 'openai',
    baseUrl: 'https://api.openai.com/v1',
    apiKey: '',
    model: 'gpt-4o-mini',
  });

  if (!showSettings) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-end sm:items-center justify-center">
      <div
        className="absolute inset-0 bg-black/50 backdrop-blur-sm animate-fadeIn"
        onClick={() => setShowSettings(false)}
      />

      <div className="relative w-full sm:max-w-md bg-white dark:bg-gray-900 sm:rounded-3xl rounded-t-3xl shadow-2xl animate-slideUp max-h-[90vh] flex flex-col">
        {/* 头部 */}
        <div className="flex items-center justify-between px-6 pt-6 pb-4 border-b border-gray-100 dark:border-gray-800 flex-shrink-0">
          <h2 className="text-lg font-bold text-gray-900 dark:text-white">设置</h2>
          <button
            onClick={() => setShowSettings(false)}
            className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors active:scale-90"
          >
            <X size={20} className="text-gray-500" />
          </button>
        </div>

        {/* 内容 */}
        <div className="flex-1 overflow-y-auto">
          {/* 外观 */}
          <div className="px-6 py-4">
            <h3 className="text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-3">
              外观
            </h3>
            <div className="bg-gray-50 dark:bg-gray-800/50 rounded-2xl overflow-hidden">
              <button
                onClick={toggleTheme}
                className="w-full flex items-center justify-between px-4 py-4 hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
              >
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-xl bg-green-100 dark:bg-green-900/30 flex items-center justify-center">
                    {theme === 'light' ? (
                      <Sun size={20} className="text-green-600 dark:text-green-400" />
                    ) : (
                      <Moon size={20} className="text-green-600 dark:text-green-400" />
                    )}
                  </div>
                  <div className="text-left">
                    <p className="font-medium text-gray-900 dark:text-white">主题模式</p>
                    <p className="text-xs text-gray-500 dark:text-gray-400">
                      {theme === 'light' ? '浅色模式' : '深色模式'}
                    </p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <div
                    className={`w-12 h-7 rounded-full p-1 transition-colors ${
                      theme === 'dark' ? 'bg-green-500' : 'bg-gray-300 dark:bg-gray-600'
                    }`}
                  >
                    <div
                      className={`w-5 h-5 rounded-full bg-white shadow-md transition-transform ${
                        theme === 'dark' ? 'translate-x-5' : ''
                      }`}
                    />
                  </div>
                </div>
              </button>

              <div className="h-px bg-gray-200 dark:bg-gray-700 mx-4" />

              <div className="flex items-center justify-between px-4 py-4">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-xl bg-purple-100 dark:bg-purple-900/30 flex items-center justify-center">
                    <Palette size={20} className="text-purple-600 dark:text-purple-400" />
                  </div>
                  <div className="text-left">
                    <p className="font-medium text-gray-900 dark:text-white">主题色</p>
                    <p className="text-xs text-gray-500 dark:text-gray-400">墨绿</p>
                  </div>
                </div>
                <div className="flex gap-2">
                  {['#2e7d32', '#1976d2', '#7b1fa2', '#d32f2f'].map((color) => (
                    <div
                      key={color}
                      className={`w-6 h-6 rounded-full border-2 ${
                        color === '#2e7d32' ? 'border-gray-900 dark:border-white' : 'border-transparent'
                      }`}
                      style={{ backgroundColor: color }}
                    />
                  ))}
                </div>
              </div>
            </div>
          </div>

          {/* AI 设置 */}
          <div className="px-6 py-4">
            <h3 className="text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-3">
              AI 智能规划
            </h3>
            <div className="bg-gray-50 dark:bg-gray-800/50 rounded-2xl overflow-hidden">
              <div className="px-4 py-4">
                <div className="flex items-center gap-3 mb-4">
                  <div className="w-10 h-10 rounded-xl bg-purple-100 dark:bg-purple-900/30 flex items-center justify-center">
                    <Key size={20} className="text-purple-600 dark:text-purple-400" />
                  </div>
                  <div className="text-left flex-1">
                    <p className="font-medium text-gray-900 dark:text-white">API 配置</p>
                    <p className="text-xs text-gray-500 dark:text-gray-400">使用你自己的 AI API</p>
                  </div>
                </div>

                <div className="space-y-3">
                  <div>
                    <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1">
                      Provider
                    </label>
                    <select
                      value={aiConfig.provider}
                      onChange={(e) => setAiConfig({ ...aiConfig, provider: e.target.value })}
                      className="w-full px-3 py-2 rounded-lg bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
                    >
                      <option value="openai">OpenAI 兼容</option>
                      <option value="gemini">Google Gemini</option>
                      <option value="anthropic">Anthropic Claude</option>
                      <option value="ollama">Ollama (本地)</option>
                    </select>
                  </div>

                  <div>
                    <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1">
                      Base URL
                    </label>
                    <input
                      type="text"
                      value={aiConfig.baseUrl}
                      onChange={(e) => setAiConfig({ ...aiConfig, baseUrl: e.target.value })}
                      className="w-full px-3 py-2 rounded-lg bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
                    />
                  </div>

                  <div>
                    <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1">
                      API Key
                    </label>
                    <input
                      type="password"
                      value={aiConfig.apiKey}
                      onChange={(e) => setAiConfig({ ...aiConfig, apiKey: e.target.value })}
                      placeholder="sk-..."
                      className="w-full px-3 py-2 rounded-lg bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
                    />
                  </div>

                  <div>
                    <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1">
                      Model
                    </label>
                    <input
                      type="text"
                      value={aiConfig.model}
                      onChange={(e) => setAiConfig({ ...aiConfig, model: e.target.value })}
                      className="w-full px-3 py-2 rounded-lg bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
                    />
                  </div>
                </div>

                <div className="mt-3 flex items-start gap-2 p-3 rounded-lg bg-blue-50 dark:bg-blue-950/30">
                  <Shield size={14} className="text-blue-500 mt-0.5 flex-shrink-0" />
                  <p className="text-xs text-blue-700 dark:text-blue-300">
                    API Key 仅保存在本地，不会上传到任何服务器
                  </p>
                </div>
              </div>
            </div>
          </div>

          {/* 通知 */}
          <div className="px-6 py-4">
            <h3 className="text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-3">
              通知
            </h3>
            <div className="bg-gray-50 dark:bg-gray-800/50 rounded-2xl overflow-hidden">
              <div className="flex items-center justify-between px-4 py-4">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-xl bg-orange-100 dark:bg-orange-900/30 flex items-center justify-center">
                    <Bell size={20} className="text-orange-600 dark:text-orange-400" />
                  </div>
                  <div className="text-left">
                    <p className="font-medium text-gray-900 dark:text-white">每日提醒</p>
                    <p className="text-xs text-gray-500 dark:text-gray-400">早晨 8:00 推送今日待办</p>
                  </div>
                </div>
                <div className="w-12 h-7 rounded-full p-1 bg-green-500">
                  <div className="w-5 h-5 rounded-full bg-white shadow-md translate-x-5" />
                </div>
              </div>
            </div>
          </div>

          {/* 关于 */}
          <div className="px-6 py-4 pb-8">
            <h3 className="text-xs font-semibold text-gray-500 dark:text-gray-400 uppercase tracking-wider mb-3">
              关于
            </h3>
            <div className="bg-gray-50 dark:bg-gray-800/50 rounded-2xl overflow-hidden">
              <div className="flex items-center justify-between px-4 py-4">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-xl bg-gray-100 dark:bg-gray-700 flex items-center justify-center">
                    <Info size={20} className="text-gray-500" />
                  </div>
                  <div className="text-left">
                    <p className="font-medium text-gray-900 dark:text-white">版本</p>
                    <p className="text-xs text-gray-500 dark:text-gray-400">TaskFlow v1.3.0</p>
                  </div>
                </div>
                <ChevronRight size={18} className="text-gray-400" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
