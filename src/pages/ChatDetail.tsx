import { useState, useRef, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Send,
  Plus,
  Sparkles,
  ChevronDown,
  ChevronUp,
  Download,
  ListTodo,
  Copy,
  CheckCheck,
  RefreshCw,
  Mic,
} from 'lucide-react';
import { useChatStore } from '@/store/useChatStore';
import { useTaskStore } from '@/store/useTaskStore';
import { ChatMessage, Task } from '@/types/task';

export default function ChatDetail() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { chats, sendMessage, regenerateLastResponse, isLoading, createChat } = useChatStore();
  const { addTask } = useTaskStore();
  const [input, setInput] = useState('');
  const [copiedId, setCopiedId] = useState<string | null>(null);
  const [importToast, setImportToast] = useState<{ show: boolean; count: number }>({ show: false, count: 0 });
  const [isListening, setIsListening] = useState(false);
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const recognitionRef = useRef<any>(null);

  const chat = chats.find((c) => c.id === id);

  useEffect(() => {
    if (!chat && chats.length === 0) {
      const newChat = createChat();
      navigate(`/chat/${newChat.id}`, { replace: true });
    }
  }, [chat, chats.length, createChat, navigate]);

  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [chat?.messages.length, isLoading]);

  const handleSend = async () => {
    if (!input.trim() || isLoading || !id) return;
    const content = input;
    setInput('');
    await sendMessage(id, content);
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const handleRegenerate = async () => {
    if (!chat || !id || isLoading) return;
    await regenerateLastResponse(id);
  };

  const handleQuickQuestion = (question: string) => {
    setInput(question);
  };

  const toggleVoiceInput = () => {
    if (isListening) {
      recognitionRef.current?.stop();
      setIsListening(false);
      return;
    }

    const SpeechRecognition = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
    if (!SpeechRecognition) {
      alert('当前浏览器不支持语音输入，请使用 Chrome 或 Edge');
      return;
    }

    const recognition = new SpeechRecognition();
    recognition.lang = 'zh-CN';
    recognition.interimResults = true;
    recognition.continuous = false;

    recognition.onresult = (event: any) => {
      let transcript = '';
      for (let i = 0; i < event.results.length; i++) {
        transcript += event.results[i][0].transcript;
      }
      setInput(transcript);
    };

    recognition.onerror = (event: any) => {
      console.error('语音识别错误:', event.error);
      setIsListening(false);
    };

    recognition.onend = () => {
      setIsListening(false);
    };

    recognitionRef.current = recognition;
    recognition.start();
    setIsListening(true);
  };

  const handleCopy = async (msg: ChatMessage) => {
    const text = msg.content.replace(/```json[\s\S]*?```/g, '').trim();
    await navigator.clipboard.writeText(text);
    setCopiedId(msg.id);
    setTimeout(() => setCopiedId(null), 1500);
  };

  const handleImportTasks = (tasks: Task[], selectedIds: Set<number>) => {
    const flattenTasks = (list: Task[]): Task[] => {
      const result: Task[] = [];
      list.forEach((t) => {
        if (selectedIds.has(t.id)) {
          const newTask = {
            ...t,
            id: Date.now() + Math.floor(Math.random() * 10000),
            children: t.children ? flattenTasks(t.children).map((c, i) => ({
              ...c,
              id: Date.now() + Math.floor(Math.random() * 10000) + i + 1,
            })) : undefined,
          };
          result.push(newTask);
        } else if (t.children) {
          result.push(...flattenTasks(t.children));
        }
      });
      return result.flat();
    };

    const toImport = flattenTasks(tasks);
    toImport.forEach((t) => addTask(t));

    setImportToast({ show: true, count: toImport.length });
    setTimeout(() => setImportToast({ show: false, count: 0 }), 2500);
  };

  if (!chat) return null;

  return (
    <div className="min-h-screen bg-paper-white dark:bg-gray-950 flex flex-col">
      <div className="hidden sm:flex min-h-screen items-center justify-center py-8 px-4">
        <div className="relative w-full max-w-md h-[85vh] bg-paper-cream dark:bg-gray-900 shadow-2xl overflow-hidden border border-line-separator dark:border-gray-800">
          <div className="absolute top-0 left-0 right-0 h-7 bg-paper-cream dark:bg-gray-900 z-40 flex items-center justify-between px-6 border-b border-line-separator dark:border-gray-800">
            <span className="text-[10px] font-medium text-ink-gray dark:text-gray-400">9:41</span>
            <div className="absolute top-0 left-1/2 -translate-x-1/2 w-24 h-4 bg-ink-black dark:bg-gray-700 rounded-b-sm" />
            <div className="flex gap-1 items-center">
              <div className="w-3.5 h-2 border border-ink-black dark:border-gray-400 relative">
                <div className="absolute inset-0.5 bg-ink-black dark:bg-gray-400" />
              </div>
            </div>
          </div>
          <div className="h-full flex flex-col pt-7">
            <ChatHeader
              title={chat.title}
              onBack={() => navigate('/chat')}
              onNewChat={() => {
                const c = createChat();
                navigate(`/chat/${c.id}`);
              }}
            />
            <MessageList
              messages={chat.messages}
              isLoading={isLoading}
              onCopy={handleCopy}
              copiedId={copiedId}
              onRegenerate={handleRegenerate}
              onImport={handleImportTasks}
              onQuickQuestion={handleQuickQuestion}
              messagesEndRef={messagesEndRef}
            />
            <InputBar
              input={input}
              setInput={setInput}
              onSend={handleSend}
              onKeyDown={handleKeyDown}
              isLoading={isLoading}
              isListening={isListening}
              onVoiceInput={toggleVoiceInput}
            />
            {importToast.show && (
              <div className="absolute bottom-20 left-1/2 -translate-x-1/2 px-4 py-2 bg-ink-black/90 dark:bg-white/90 text-paper-white dark:text-ink-black text-xs font-medium shadow-lg border border-line-separator z-50 animate-[fadeInUp_0.3s_ease]">
                已导入 {importToast.count} 个任务
              </div>
            )}
          </div>
        </div>
      </div>

      <div className="sm:hidden flex-1 flex flex-col relative">
        <ChatHeader
          title={chat.title}
          onBack={() => navigate('/chat')}
          onNewChat={() => {
            const c = createChat();
            navigate(`/chat/${c.id}`);
          }}
        />
        <MessageList
          messages={chat.messages}
          isLoading={isLoading}
          onCopy={handleCopy}
          copiedId={copiedId}
          onRegenerate={handleRegenerate}
          onImport={handleImportTasks}
          onQuickQuestion={handleQuickQuestion}
          messagesEndRef={messagesEndRef}
        />
        <InputBar
          input={input}
          setInput={setInput}
          onSend={handleSend}
          onKeyDown={handleKeyDown}
          isLoading={isLoading}
          isListening={isListening}
          onVoiceInput={toggleVoiceInput}
        />
        {importToast.show && (
          <div className="absolute bottom-20 left-1/2 -translate-x-1/2 px-4 py-2 bg-ink-black/90 dark:bg-white/90 text-paper-white dark:text-ink-black text-xs font-medium shadow-lg border border-line-separator z-50 animate-[fadeInUp_0.3s_ease]">
            已导入 {importToast.count} 个任务
          </div>
        )}
      </div>
    </div>
  );
}

function ChatHeader({
  title,
  onBack,
  onNewChat,
}: {
  title: string;
  onBack: () => void;
  onNewChat: () => void;
}) {
  return (
    <div className="flex items-center justify-between px-4 py-2.5 border-b border-line-separator dark:border-gray-800 flex-shrink-0">
      <div className="flex items-center gap-2">
        <button
          onClick={onBack}
          className="w-8 h-8 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
        >
          <ArrowLeft size={16} className="text-ink-gray dark:text-gray-400" />
        </button>
        <div className="w-6 h-6 bg-newspaper-red flex items-center justify-center text-white">
          <Sparkles size={12} />
        </div>
      </div>
      <span className="font-serif font-semibold text-ink-black dark:text-white text-sm truncate max-w-[180px]">
        {title}
      </span>
      <button
        onClick={onNewChat}
        className="w-8 h-8 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-all active:scale-95"
      >
        <Plus size={16} className="text-ink-gray dark:text-gray-400" />
      </button>
    </div>
  );
}

function MessageList({
  messages,
  isLoading,
  onCopy,
  copiedId,
  onRegenerate,
  onImport,
  onQuickQuestion,
  messagesEndRef,
}: {
  messages: ChatMessage[];
  isLoading: boolean;
  onCopy: (msg: ChatMessage) => void;
  copiedId: string | null;
  onRegenerate: () => void;
  onImport: (tasks: Task[], selected: Set<number>) => void;
  onQuickQuestion: (question: string) => void;
  messagesEndRef: React.RefObject<HTMLDivElement>;
}) {
  if (messages.length === 0) {
    return (
      <div className="flex-1 flex flex-col items-center justify-center px-6 py-10">
        <div className="w-14 h-14 border border-line-separator dark:border-gray-700 flex items-center justify-center mb-4 animate-[float_3s_ease-in-out_infinite]">
          <Sparkles size={24} className="text-newspaper-red" />
        </div>
        <h3 className="font-serif font-semibold text-ink-black dark:text-white mb-2">AI 任务助手</h3>
        <p className="text-xs text-ink-light dark:text-gray-400 text-center mb-6 font-sans">
          告诉我你的目标，我来帮你拆解成可执行的任务清单
        </p>
        <div className="w-full space-y-1.5">
          {['我要学习一门新技能', '帮我规划一个项目', '制定健身计划'].map((q, i) => (
            <button
              key={q}
              onClick={() => onQuickQuestion(q)}
              className="w-full text-left px-3 py-2.5 border border-line-separator dark:border-gray-700 hover:bg-paper-cream dark:hover:bg-gray-800 hover:border-newspaper-red/30 text-xs text-ink-gray dark:text-gray-300 transition-all active:scale-[0.99] font-sans"
              style={{ animation: `fadeInUp 0.5s ease ${i * 0.1}s both` }}
            >
              {q}
            </button>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className="flex-1 overflow-y-auto p-4 space-y-4">
      {messages.map((msg, idx) => (
        <MessageBubble
          key={msg.id}
          message={msg}
          isLast={idx === messages.length - 1}
          isCopied={copiedId === msg.id}
          onCopy={() => onCopy(msg)}
          onRegenerate={onRegenerate}
          onImport={onImport}
          index={idx}
        />
      ))}
      {isLoading && (
        <div className="flex gap-3 animate-[fadeIn_0.3s_ease]">
          <div className="w-7 h-7 bg-newspaper-red flex items-center justify-center flex-shrink-0">
            <Sparkles size={12} className="text-white" />
          </div>
          <div className="bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 px-3 py-2.5">
            <div className="flex gap-1.5">
              <span className="w-1.5 h-1.5 bg-newspaper-red rounded-full animate-bounce" style={{ animationDelay: '0ms' }} />
              <span className="w-1.5 h-1.5 bg-newspaper-red rounded-full animate-bounce" style={{ animationDelay: '150ms' }} />
              <span className="w-1.5 h-1.5 bg-newspaper-red rounded-full animate-bounce" style={{ animationDelay: '300ms' }} />
            </div>
          </div>
        </div>
      )}
      <div ref={messagesEndRef} />
    </div>
  );
}

function MessageBubble({
  message,
  isLast,
  isCopied,
  onCopy,
  onRegenerate,
  onImport,
  index,
}: {
  message: ChatMessage;
  isLast: boolean;
  isCopied: boolean;
  onCopy: () => void;
  onRegenerate: () => void;
  onImport: (tasks: Task[], selected: Set<number>) => void;
  index: number;
}) {
  const isUser = message.role === 'user';
  const hasTasks = message.suggestedTasks && message.suggestedTasks.length > 0;
  const [showTasks, setShowTasks] = useState(false);
  const [selectedTaskIds, setSelectedTaskIds] = useState<Set<number>>(() => {
    const ids = new Set<number>();
    const collectIds = (tasks: Task[]) => {
      tasks.forEach((t) => {
        ids.add(t.id);
        if (t.children) collectIds(t.children);
      });
    };
    if (message.suggestedTasks) collectIds(message.suggestedTasks);
    return ids;
  });

  const renderedContent = useMemo(() => {
    return renderMarkdown(message.content);
  }, [message.content]);

  const totalTaskCount = useMemo(() => {
    if (!message.suggestedTasks) return 0;
    let count = 0;
    const countTasks = (tasks: Task[]) => {
      tasks.forEach((t) => {
        count++;
        if (t.children) countTasks(t.children);
      });
    };
    countTasks(message.suggestedTasks);
    return count;
  }, [message.suggestedTasks]);

  const selectedCount = selectedTaskIds.size;

  const toggleTask = (id: number) => {
    setSelectedTaskIds((prev) => {
      const next = new Set(prev);
      if (next.has(id)) {
        next.delete(id);
      } else {
        next.add(id);
      }
      return next;
    });
  };

  const toggleAll = () => {
    if (!message.suggestedTasks) return;
    if (selectedCount === totalTaskCount) {
      setSelectedTaskIds(new Set());
    } else {
      const ids = new Set<number>();
      const collectIds = (tasks: Task[]) => {
        tasks.forEach((t) => {
          ids.add(t.id);
          if (t.children) collectIds(t.children);
        });
      };
      collectIds(message.suggestedTasks);
      setSelectedTaskIds(ids);
    }
  };

  return (
    <div
      className={`flex gap-3 ${isUser ? 'flex-row-reverse' : ''}`}
      style={{
        animation: `fadeInUp 0.4s cubic-bezier(0.34, 1.56, 0.64, 1) ${index * 0.05}s both`,
      }}
    >
      <div
        className={`w-7 h-7 flex items-center justify-center flex-shrink-0 ${
          isUser
            ? 'bg-ink-black'
            : 'bg-newspaper-red'
        }`}
      >
        {isUser ? (
          <span className="text-white text-[10px] font-bold font-sans">我</span>
        ) : (
          <Sparkles size={12} className="text-white" />
        )}
      </div>
      <div className={`max-w-[82%] ${isUser ? 'items-end' : 'items-start'}`}>
        <div className="group relative">
          <div
            className={`px-3.5 py-2.5 text-sm leading-relaxed break-words font-sans ${
              isUser
                ? 'bg-ink-black text-paper-white'
                : 'bg-paper-cream dark:bg-gray-800 text-ink-black dark:text-gray-200 border border-line-separator dark:border-gray-700'
            }`}
            dangerouslySetInnerHTML={{ __html: renderedContent }}
          />

          {!isUser && isLast && (
            <div className="flex items-center gap-1 mt-1.5 opacity-0 group-hover:opacity-100 transition-opacity">
              <button
                onClick={onCopy}
                className="flex items-center gap-1 px-2 py-1 text-[10px] text-ink-light dark:text-gray-500 hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
              >
                {isCopied ? (
                  <>
                    <CheckCheck size={10} className="text-newspaper-red" />
                    <span className="text-newspaper-red">已复制</span>
                  </>
                ) : (
                  <>
                    <Copy size={10} />
                    <span>复制</span>
                  </>
                )}
              </button>
              <button
                onClick={onRegenerate}
                className="flex items-center gap-1 px-2 py-1 text-[10px] text-ink-light dark:text-gray-500 hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
              >
                <RefreshCw size={10} />
                <span>重新生成</span>
              </button>
            </div>
          )}
        </div>

        {hasTasks && (
          <div className="mt-2">
            <button
              onClick={() => setShowTasks(!showTasks)}
              className={`flex items-center gap-2 px-3 py-2 text-[10px] font-medium transition-all w-full border ${
                isUser
                  ? 'bg-ink-black/5 text-ink-black dark:bg-white/5 dark:text-gray-300 border-line-separator dark:border-gray-700'
                  : 'bg-newspaper-red/5 text-newspaper-red dark:bg-newspaper-red/10 dark:text-newspaper-red-light border-newspaper-red/20 dark:border-newspaper-red/30'
              } hover:shadow-sm active:scale-[0.99]`}
            >
              <ListTodo size={12} />
              <span>任务清单 ({message.suggestedTasks!.length} 组 / 共 {totalTaskCount} 项</span>
              {showTasks ? <ChevronUp size={12} className="ml-auto" /> : <ChevronDown size={12} className="ml-auto" />}
            </button>

            {showTasks && (
              <div className="mt-2 bg-paper-white dark:bg-gray-800 border border-line-separator dark:border-gray-700 overflow-hidden animate-[fadeIn_0.25s_ease]">
                <div className="flex items-center justify-between px-3 py-2 border-b border-line-separator dark:border-gray-700 bg-paper-cream dark:bg-gray-750">
                  <label className="flex items-center gap-2 text-[10px] text-ink-gray dark:text-gray-300 font-sans">
                    <input
                      type="checkbox"
                      checked={selectedCount === totalTaskCount}
                      onChange={toggleAll}
                      className="w-3.5 h-3.5 border-ink-light text-newspaper-red focus:ring-newspaper-red"
                    />
                    全选 ({selectedCount}/{totalTaskCount}
                  </label>
                </div>
                <TaskPreview
                  tasks={message.suggestedTasks!}
                  selectedIds={selectedTaskIds}
                  onToggle={toggleTask}
                />
                <div className="p-3 border-t border-line-separator dark:border-gray-700">
                  <button
                    onClick={() => onImport(message.suggestedTasks!, selectedTaskIds)}
                    disabled={selectedCount === 0}
                    className={`w-full flex items-center justify-center gap-2 py-2 text-xs font-medium transition-all ${
                      selectedCount > 0
                        ? 'bg-ink-black hover:bg-newspaper-red text-paper-white active:scale-[0.99]'
                        : 'bg-line-separator dark:bg-gray-700 text-ink-light cursor-not-allowed'
                    }`}
                  >
                    <Download size={14} />
                    导入 {selectedCount} 个任务
                  </button>
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

function TaskPreview({
  tasks,
  selectedIds,
  onToggle,
  depth = 0,
}: {
  tasks: Task[];
  selectedIds: Set<number>;
  onToggle: (id: number) => void;
  depth?: number;
}) {
  const priorityColors: Record<number, string> = {
    1: 'bg-priority-high',
    2: 'bg-priority-medium',
    3: 'bg-priority-low',
  };

  return (
    <div className="divide-y divide-line-thin dark:divide-gray-700">
      {tasks.map((task) => (
        <div key={task.id}>
          <label
            className={`flex items-start gap-3 px-3 py-2 hover:bg-paper-cream dark:hover:bg-gray-700/50 cursor-pointer transition-colors ${
              depth > 0 ? 'pl-8' : ''
            }`}
          >
            <input
              type="checkbox"
              checked={selectedIds.has(task.id)}
              onChange={() => onToggle(task.id)}
              className="mt-0.5 w-3.5 h-3.5 border-ink-light text-newspaper-red focus:ring-newspaper-red flex-shrink-0"
            />
            <div className={`mt-1 w-0.5 h-3.5 ${priorityColors[task.priority]} flex-shrink-0`} />
            <div className="flex-1 min-w-0">
              <p className="text-xs font-medium text-ink-black dark:text-white font-sans">
                {task.title}
              </p>
              {task.description && (
                <p className="text-[10px] text-ink-light dark:text-gray-400 mt-0.5 font-sans">
                  {task.description}
                </p>
              )}
            </div>
          </label>
          {task.children && task.children.length > 0 && (
            <TaskPreview
              tasks={task.children}
              selectedIds={selectedIds}
              onToggle={onToggle}
              depth={depth + 1}
            />
          )}
        </div>
      ))}
    </div>
  );
}

function InputBar({
  input,
  setInput,
  onSend,
  onKeyDown,
  isLoading,
  isListening,
  onVoiceInput,
}: {
  input: string;
  setInput: (v: string) => void;
  onSend: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
  isLoading: boolean;
  isListening: boolean;
  onVoiceInput: () => void;
}) {
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
      textareaRef.current.style.height = Math.min(textareaRef.current.scrollHeight, 128) + 'px';
    }
  }, [input]);

  return (
    <div className="border-t border-line-separator dark:border-gray-800 p-3 flex-shrink-0 bg-paper-white dark:bg-gray-900">
      <div className="flex items-end gap-2">
        <button
          onClick={onVoiceInput}
          className={`w-9 h-9 flex items-center justify-center transition-all active:scale-95 flex-shrink-0 ${
            isListening
              ? 'bg-newspaper-red text-paper-white animate-pulse'
              : 'hover:bg-black/5 dark:hover:bg-white/5'
          }`}
        >
          <Mic size={18} className={isListening ? 'text-paper-white' : 'text-ink-light'} />
        </button>
        <div className="flex-1 relative">
          <textarea
            ref={textareaRef}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={onKeyDown}
            placeholder={isListening ? '正在聆听...' : '说说你的目标...'}
            rows={1}
            className="w-full px-3 py-2 bg-paper-cream dark:bg-gray-800 border border-line-separator dark:border-gray-700 text-sm text-ink-black dark:text-white placeholder-ink-light outline-none focus:border-newspaper-red/40 focus:ring-1 focus:ring-newspaper-red/20 resize-none max-h-32 font-sans"
            style={{ minHeight: '38px' }}
          />
        </div>
        {input.trim() ? (
          <button
            onClick={onSend}
            disabled={isLoading}
            className="w-9 h-9 flex items-center justify-center bg-newspaper-red hover:bg-newspaper-red-dark text-paper-white transition-all active:scale-95 flex-shrink-0 shadow-sm"
          >
            <Send size={16} />
          </button>
        ) : (
          <button
            onClick={onVoiceInput}
            className={`w-9 h-9 flex items-center justify-center transition-all active:scale-95 flex-shrink-0 ${
              isListening
                ? 'bg-newspaper-red text-paper-white animate-pulse'
                : 'hover:bg-black/5 dark:hover:bg-white/5'
            }`}
          >
            <Mic size={18} className={isListening ? 'text-paper-white' : 'text-ink-light'} />
          </button>
        )}
      </div>
    </div>
  );
}

function renderMarkdown(text: string): string {
  let html = text
    .replace(/```json[\s\S]*?```/g, '')
    .replace(/```([\s\S]*?)```/g, (_, code) => {
      return `<pre class="mt-2 p-3 bg-ink-black dark:bg-gray-950 text-xs text-gray-300 overflow-x-auto border border-line-separator"><code>${escapeHtml(code.trim())}</code></pre>`;
    })
    .replace(/`([^`]+)`/g, '<code class="px-1 py-0.5 bg-paper-cream dark:bg-gray-700 text-newspaper-red text-xs font-mono border border-line-separator">$1</code>');

  html = html
    .replace(/^### (.+)$/gm, '<h3 class="font-serif font-semibold text-sm mt-3 mb-2">$1</h3>')
    .replace(/^## (.+)$/gm, '<h2 class="font-serif font-semibold text-base mt-4 mb-2">$1</h2>')
    .replace(/^# (.+)$/gm, '<h1 class="font-serif font-semibold text-lg mt-4 mb-2">$1</h1>');

  html = html
    .replace(/\*\*(.+?)\*\*/g, '<strong class="font-semibold">$1</strong>')
    .replace(/\*(.+?)\*/g, '<em>$1</em>');

  html = html
    .replace(/^- (.+)$/gm, '<li class="ml-4 list-disc">$1</li>')
    .replace(/^\d+\. (.+)$/gm, '<li class="ml-4 list-decimal">$1</li>');

  html = html.replace(/\n/g, '<br />');

  return html;
}

function escapeHtml(text: string): string {
  const div = document.createElement('div');
  div.appendChild(document.createTextNode(text));
  return div.innerHTML;
}
