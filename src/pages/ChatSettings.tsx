import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Key, Bot, Save, RotateCcw, Sparkles } from 'lucide-react';
import { useChatStore } from '@/store/useChatStore';
import { aiService } from '@/services/aiService';

export default function ChatSettings() {
  const navigate = useNavigate();
  const { aiConfig, updateAiConfig } = useChatStore();
  const [localConfig, setLocalConfig] = useState(aiConfig);
  const [savedTip, setSavedTip] = useState(false);

  const handleSave = () => {
    updateAiConfig(localConfig);
    setSavedTip(true);
    setTimeout(() => setSavedTip(false), 2000);
  };

  const handleResetPrompt = () => {
    setLocalConfig({ ...localConfig, systemPrompt: aiService.defaultSystemPrompt });
  };

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-950 flex flex-col">
      <div className="hidden sm:flex min-h-screen items-center justify-center py-8 px-4">
        <div className="relative w-full max-w-md h-[85vh] bg-white dark:bg-gray-900 rounded-[40px] shadow-2xl overflow-hidden border-8 border-gray-900 dark:border-gray-800">
          <div className="absolute top-0 left-0 right-0 h-7 bg-white dark:bg-gray-900 z-40 flex items-center justify-between px-6">
            <span className="text-xs font-semibold text-gray-900 dark:text-white">9:41</span>
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-28 h-5 bg-gray-900 rounded-b-2xl" />
            <div className="flex gap-1 items-center">
              <div className="w-4 h-2.5 border border-gray-900 dark:border-white rounded-sm relative">
                <div className="absolute inset-0.5 bg-gray-900 dark:bg-white rounded-sm" />
              </div>
            </div>
          </div>
          <div className="h-full flex flex-col pt-7">
            <SettingsContent
              config={localConfig}
              setConfig={setLocalConfig}
              onSave={handleSave}
              onResetPrompt={handleResetPrompt}
              onBack={() => navigate('/chat')}
              savedTip={savedTip}
            />
          </div>
        </div>
      </div>

      <div className="sm:hidden flex-1 flex flex-col">
        <SettingsContent
          config={localConfig}
          setConfig={setLocalConfig}
          onSave={handleSave}
          onResetPrompt={handleResetPrompt}
          onBack={() => navigate('/chat')}
          savedTip={savedTip}
        />
      </div>
    </div>
  );
}

function SettingsContent({
  config,
  setConfig,
  onSave,
  onResetPrompt,
  onBack,
  savedTip,
}: {
  config: any;
  setConfig: (c: any) => void;
  onSave: () => void;
  onResetPrompt: () => void;
  onBack: () => void;
  savedTip: boolean;
}) {
  return (
    <>
      <div className="flex items-center justify-between px-4 py-3 border-b border-gray-100 dark:border-gray-800 flex-shrink-0">
        <div className="flex items-center gap-2">
          <button
            onClick={onBack}
            className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
          >
            <ArrowLeft size={18} className="text-gray-600 dark:text-gray-300" />
          </button>
          <h1 className="font-bold text-lg text-gray-900 dark:text-white">AI 设置</h1>
        </div>
        <button
          onClick={onSave}
          className="flex items-center gap-1.5 px-3 py-1.5 bg-purple-500 hover:bg-purple-600 text-white rounded-lg text-sm font-medium transition-colors"
        >
          <Save size={16} />
          保存
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-6">
        {savedTip && (
          <div className="flex items-center gap-2 p-3 bg-green-50 dark:bg-green-950/30 text-green-700 dark:text-green-400 rounded-xl text-sm">
            <span>✓ 配置已保存</span>
          </div>
        )}

        {/* API 配置 */}
        <div>
          <div className="flex items-center gap-2 mb-3">
            <div className="w-8 h-8 rounded-lg bg-purple-100 dark:bg-purple-900/30 flex items-center justify-center">
              <Key size={16} className="text-purple-600 dark:text-purple-400" />
            </div>
            <h3 className="font-semibold text-gray-900 dark:text-white">API 配置</h3>
          </div>
          <div className="bg-white dark:bg-gray-800/50 rounded-2xl p-4 space-y-4">
            <div>
              <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1.5">Provider</label>
              <select
                value={config.provider}
                onChange={(e) => setConfig({ ...config, provider: e.target.value })}
                className="w-full px-3 py-2.5 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
              >
                <option value="openai">OpenAI 兼容</option>
                <option value="gemini">Google Gemini</option>
                <option value="anthropic">Anthropic Claude</option>
                <option value="ollama">Ollama (本地)</option>
              </select>
            </div>
            <div>
              <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1.5">Base URL</label>
              <input
                type="text"
                value={config.baseUrl}
                onChange={(e) => setConfig({ ...config, baseUrl: e.target.value })}
                className="w-full px-3 py-2.5 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
              />
            </div>
            <div>
              <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1.5">API Key</label>
              <input
                type="password"
                value={config.apiKey}
                onChange={(e) => setConfig({ ...config, apiKey: e.target.value })}
                placeholder="sk-..."
                className="w-full px-3 py-2.5 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
              />
            </div>
            <div>
              <label className="block text-xs text-gray-500 dark:text-gray-400 mb-1.5">Model</label>
              <input
                type="text"
                value={config.model}
                onChange={(e) => setConfig({ ...config, model: e.target.value })}
                className="w-full px-3 py-2.5 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none"
              />
            </div>
          </div>
        </div>

        {/* 系统提示词 */}
        <div>
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 rounded-lg bg-blue-100 dark:bg-blue-900/30 flex items-center justify-center">
                <Bot size={16} className="text-blue-600 dark:text-blue-400" />
              </div>
              <h3 className="font-semibold text-gray-900 dark:text-white">系统提示词</h3>
            </div>
            <button
              onClick={onResetPrompt}
              className="flex items-center gap-1 px-2.5 py-1.5 text-xs text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 hover:bg-gray-100 dark:hover:bg-gray-800 rounded-lg transition-colors"
            >
              <RotateCcw size={12} />
              重置
            </button>
          </div>
          <div className="bg-white dark:bg-gray-800/50 rounded-2xl p-4">
            <p className="text-xs text-gray-500 dark:text-gray-400 mb-3">
              定义 AI 助手的角色和行为，每次新对话时会使用此提示词
            </p>
            <textarea
              value={config.systemPrompt}
              onChange={(e) => setConfig({ ...config, systemPrompt: e.target.value })}
              rows={12}
              className="w-full px-3 py-2.5 rounded-xl bg-gray-50 dark:bg-gray-800 border border-gray-200 dark:border-gray-700 text-sm text-gray-900 dark:text-white outline-none resize-none font-mono"
            />
          </div>
        </div>

        {/* 关于 */}
        <div className="flex items-center gap-3 px-4 py-3 bg-white dark:bg-gray-800/50 rounded-2xl">
          <div className="w-10 h-10 rounded-xl bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center">
            <Sparkles size={20} className="text-white" />
          </div>
          <div className="flex-1">
            <p className="font-medium text-gray-900 dark:text-white">AI 对话</p>
            <p className="text-xs text-gray-500 dark:text-gray-400">智能任务拆分助手</p>
          </div>
        </div>
      </div>
    </>
  );
}
