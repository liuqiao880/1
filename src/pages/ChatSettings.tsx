import { useState, useMemo, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft, Key, Bot, Save, RotateCcw, Sparkles, Check, Wifi } from 'lucide-react';
import { useChatStore } from '@/store/useChatStore';
import { aiService } from '@/services/aiService';

export default function ChatSettings() {
  const navigate = useNavigate();
  const { aiConfig, updateAiConfig } = useChatStore();
  const [localConfig, setLocalConfig] = useState(aiConfig);
  const [savedTip, setSavedTip] = useState(false);
  const [testing, setTesting] = useState(false);
  const [testResult, setTestResult] = useState<{ success: boolean; message: string } | null>(null);

  const isDirty = useMemo(() => {
    return (
      localConfig.provider !== aiConfig.provider ||
      localConfig.baseUrl !== aiConfig.baseUrl ||
      localConfig.apiKey !== aiConfig.apiKey ||
      localConfig.model !== aiConfig.model ||
      localConfig.systemPrompt !== aiConfig.systemPrompt
    );
  }, [localConfig, aiConfig]);

  const handleBack = () => {
    if (isDirty) {
      if (confirm('有未保存的修改，确定要离开吗？')) {
        navigate('/chat');
      }
    } else {
      navigate('/chat');
    }
  };

  useEffect(() => {
    const handleBeforeUnload = (e: BeforeUnloadEvent) => {
      if (isDirty) {
        e.preventDefault();
        e.returnValue = '';
      }
    };
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, [isDirty]);

  const handleSave = () => {
    updateAiConfig(localConfig);
    setSavedTip(true);
    setTimeout(() => setSavedTip(false), 2000);
  };

  const handleResetPrompt = () => {
    if (confirm('确定要重置系统提示词为默认值吗？')) {
      setLocalConfig({ ...localConfig, systemPrompt: aiService.defaultSystemPrompt });
    }
  };

  const handleProviderChange = (provider: string) => {
    const preset = aiService.providerPresets[provider];
    if (preset) {
      setLocalConfig({
        ...localConfig,
        provider,
        baseUrl: preset.baseUrl,
        model: preset.model,
      });
    } else {
      setLocalConfig({ ...localConfig, provider });
    }
  };

  const handleTestConnection = async () => {
    if (!localConfig.apiKey) {
      setTestResult({ success: false, message: '请先填写 API Key' });
      return;
    }
    setTesting(true);
    setTestResult(null);
    try {
      const result = await aiService.chat(
        [{ role: 'user', content: 'Hi' }],
        { ...localConfig, systemPrompt: 'You are a helpful assistant.' }
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

  return (
    <div className="min-h-screen bg-paper-white dark:bg-gray-950 flex flex-col">
      <div className="hidden sm:flex min-h-screen items-center justify-center py-8 px-4">
        <div className="relative w-full max-w-md h-[85vh] bg-paper-cream dark:bg-gray-900 shadow-2xl overflow-hidden border-2 border-ink-black dark:border-gray-800">
          <div className="absolute top-0 left-0 right-0 h-7 bg-paper-cream dark:bg-gray-900 z-40 flex items-center justify-between px-6">
            <span className="text-xs font-semibold text-ink-black dark:text-white">9:41</span>
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-24 h-4 bg-ink-black dark:bg-gray-700" />
            <div className="flex gap-1 items-center">
              <div className="w-3.5 h-2 border border-ink-black dark:border-gray-400 relative">
                <div className="absolute inset-0.5 bg-ink-black dark:bg-gray-400" />
              </div>
            </div>
          </div>
          <div className="h-full flex flex-col pt-7">
            <SettingsContent
              config={localConfig}
              setConfig={setLocalConfig}
              onSave={handleSave}
              onResetPrompt={handleResetPrompt}
              onProviderChange={handleProviderChange}
              onBack={handleBack}
              savedTip={savedTip}
              testing={testing}
              testResult={testResult}
              onTestConnection={handleTestConnection}
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
          onProviderChange={handleProviderChange}
          onBack={handleBack}
          savedTip={savedTip}
          testing={testing}
          testResult={testResult}
          onTestConnection={handleTestConnection}
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
  onProviderChange,
  onBack,
  savedTip,
  testing,
  testResult,
  onTestConnection,
}: {
  config: any;
  setConfig: (c: any) => void;
  onSave: () => void;
  onResetPrompt: () => void;
  onProviderChange: (provider: string) => void;
  onBack: () => void;
  savedTip: boolean;
  testing: boolean;
  testResult: { success: boolean; message: string } | null;
  onTestConnection: () => void;
}) {
  return (
    <>
      <div className="flex items-center justify-between px-4 py-3 border-b border-line-separator dark:border-gray-800 flex-shrink-0">
        <div className="flex items-center gap-2">
          <button
            onClick={onBack}
            className="w-9 h-9 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
          >
            <ArrowLeft size={18} className="text-ink-gray dark:text-gray-300" />
          </button>
          <div className="flex items-center gap-2">
            <div className="newspaper-accent-line" />
            <h1 className="font-serif font-bold text-lg text-ink-black dark:text-white">AI 设置</h1>
          </div>
        </div>
        <button
          onClick={onSave}
          className="flex items-center gap-1.5 px-3 py-1.5 bg-ink-black hover:bg-newspaper-red text-paper-white text-sm font-serif font-semibold transition-colors"
        >
          <Save size={16} />
          保存
        </button>
      </div>

      <div className="flex-1 overflow-y-auto p-4 space-y-6">
        {savedTip && (
          <div className="flex items-center gap-2 p-3 border border-newspaper-red/20 dark:border-newspaper-red/30 bg-newspaper-red/5 text-newspaper-red dark:text-newspaper-red-light text-sm font-sans">
            <Check size={16} />
            <span>配置已保存</span>
          </div>
        )}

        {/* API 配置 */}
        <div>
          <div className="flex items-center gap-2 mb-3">
            <div className="w-8 h-8 bg-newspaper-red/10 dark:bg-newspaper-red/20 flex items-center justify-center">
              <Key size={16} className="text-newspaper-red dark:text-newspaper-red-light" />
            </div>
            <h3 className="font-serif font-semibold text-ink-black dark:text-white">API 配置</h3>
          </div>
          <div className="bg-paper-white dark:bg-gray-800/50 border border-line-separator dark:border-gray-700 p-4 space-y-4">
            <div>
              <label className="block text-xs text-ink-light dark:text-gray-400 mb-1.5 font-sans">Provider</label>
              <select
                value={config.provider}
                onChange={(e) => onProviderChange(e.target.value)}
                className="w-full px-3 py-2.5 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
              >
                <option value="openai">OpenAI 兼容</option>
                <option value="gemini">Google Gemini</option>
                <option value="anthropic">Anthropic Claude</option>
                <option value="ollama">Ollama (本地)</option>
              </select>
            </div>
            <div>
              <label className="block text-xs text-ink-light dark:text-gray-400 mb-1.5 font-sans">Base URL</label>
              <input
                type="text"
                value={config.baseUrl}
                onChange={(e) => setConfig({ ...config, baseUrl: e.target.value })}
                className="w-full px-3 py-2.5 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
              />
            </div>
            <div>
              <label className="block text-xs text-ink-light dark:text-gray-400 mb-1.5 font-sans">API Key</label>
              <input
                type="password"
                value={config.apiKey}
                onChange={(e) => setConfig({ ...config, apiKey: e.target.value })}
                placeholder="sk-..."
                className="w-full px-3 py-2.5 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
              />
            </div>
            <div>
              <label className="block text-xs text-ink-light dark:text-gray-400 mb-1.5 font-sans">Model</label>
              <input
                type="text"
                value={config.model}
                onChange={(e) => setConfig({ ...config, model: e.target.value })}
                className="w-full px-3 py-2.5 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 font-sans"
              />
            </div>

            <button
              onClick={onTestConnection}
              disabled={testing || !config.apiKey}
              className={`w-full flex items-center justify-center gap-2 py-2.5 text-xs font-medium transition-all ${
                testing || !config.apiKey
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
                {testResult.success ? <Check size={14} /> : <Key size={14} />}
                <span className="flex-1">{testResult.message}</span>
              </div>
            )}
          </div>
        </div>

        {/* 系统提示词 */}
        <div>
          <div className="flex items-center justify-between mb-3">
            <div className="flex items-center gap-2">
              <div className="w-8 h-8 bg-ink-black/5 dark:bg-white/10 flex items-center justify-center">
                <Bot size={16} className="text-ink-gray dark:text-gray-300" />
              </div>
              <h3 className="font-serif font-semibold text-ink-black dark:text-white">系统提示词</h3>
            </div>
            <button
              onClick={onResetPrompt}
              className="flex items-center gap-1 px-2.5 py-1.5 text-xs text-ink-light hover:text-ink-black dark:text-gray-400 dark:hover:text-gray-200 hover:bg-black/5 dark:hover:bg-white/5 transition-colors font-sans"
            >
              <RotateCcw size={12} />
              重置
            </button>
          </div>
          <div className="bg-paper-white dark:bg-gray-800/50 border border-line-separator dark:border-gray-700 p-4">
            <p className="text-xs text-ink-light dark:text-gray-400 mb-3 font-sans">
              定义 AI 助手的角色和行为，每次新对话时会使用此提示词
            </p>
            <textarea
              value={config.systemPrompt}
              onChange={(e) => setConfig({ ...config, systemPrompt: e.target.value })}
              rows={12}
              className="w-full px-3 py-2.5 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white outline-none focus:border-newspaper-red/40 resize-none font-mono"
            />
          </div>
        </div>

        {/* 关于 */}
        <div className="flex items-center gap-3 px-4 py-3 bg-paper-white dark:bg-gray-800/50 border border-line-separator dark:border-gray-700">
          <div className="w-10 h-10 bg-newspaper-red flex items-center justify-center">
            <Sparkles size={20} className="text-paper-white" />
          </div>
          <div className="flex-1">
            <p className="font-serif font-medium text-ink-black dark:text-white">AI 对话</p>
            <p className="text-xs text-ink-light dark:text-gray-400 font-sans">智能任务拆分助手</p>
          </div>
        </div>
      </div>
    </>
  );
}
