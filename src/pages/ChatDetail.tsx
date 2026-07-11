import { useState, useRef, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Send,
  Plus,
  Paperclip,
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
  const { chats, sendMessage, isLoading, createChat } = useChatStore();
  const { addTask } = useTaskStore();
  const [input, setInput] = useState('');
  const [copiedId, setCopiedId] = useState<string | null>(null);
  const [importToast, setImportToast] = useState<{ show: boolean; count: number }>({ show: false, count: 0 });
  const messagesEndRef = useRef<HTMLDivElement>(null);

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
    if (!chat || chat.messages.length < 2) return;
    const lastUserIndex = [...chat.messages].reverse().findIndex((m) => m.role === 'user');
    if (lastUserIndex === -1) return;
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
              messagesEndRef={messagesEndRef}
            />
            <InputBar
              input={input}
              setInput={setInput}
              onSend={handleSend}
              onKeyDown={handleKeyDown}
              isLoading={isLoading}
            />
            {importToast.show && (
              <div className="absolute bottom-20 left-1/2 -translate-x-1/2 px-4 py-2.5 bg-gray-900/90 dark:bg-white/90 text-white dark:text-gray-900 rounded-xl text-sm font-medium shadow-lg backdrop-blur-xl z-50 animate-[fadeInUp_0.3s_ease]">
                ✓ 已导入 {importToast.count} 个任务
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
          messagesEndRef={messagesEndRef}
        />
        <InputBar
          input={input}
          setInput={setInput}
          onSend={handleSend}
          onKeyDown={handleKeyDown}
          isLoading={isLoading}
        />
        {importToast.show && (
          <div className="absolute bottom-20 left-1/2 -translate-x-1/2 px-4 py-2.5 bg-gray-900/90 dark:bg-white/90 text-white dark:text-gray-900 rounded-xl text-sm font-medium shadow-lg backdrop-blur-xl z-50 animate-[fadeInUp_0.3s_ease]">
            ✓ 已导入 {importToast.count} 个任务
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
    <div className="flex items-center justify-between px-4 py-3 border-b border-gray-100 dark:border-gray-800 flex-shrink-0">
      <div className="flex items-center gap-2">
        <button
          onClick={onBack}
          className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
        >
          <ArrowLeft size={18} className="text-gray-600 dark:text-gray-300" />
        </button>
        <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center">
          <Sparkles size={16} className="text-white" />
        </div>
      </div>
      <span className="font-semibold text-gray-900 dark:text-white text-sm truncate max-w-[180px]">
        {title}
      </span>
      <button
        onClick={onNewChat}
        className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-all active:scale-95"
      >
        <Plus size={18} className="text-gray-600 dark:text-gray-300" />
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
  messagesEndRef,
}: {
  messages: ChatMessage[];
  isLoading: boolean;
  onCopy: (msg: ChatMessage) => void;
  copiedId: string | null;
  onRegenerate: () => void;
  onImport: (tasks: Task[], selected: Set<number>) => void;
  messagesEndRef: React.RefObject<HTMLDivElement>;
}) {
  if (messages.length === 0) {
    return (
      <div className="flex-1 flex flex-col items-center justify-center px-6 py-10">
        <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-purple-100 to-pink-100 dark:from-purple-900/30 dark:to-pink-900/30 flex items-center justify-center mb-4 animate-[float_3s_ease-in-out_infinite]">
          <Sparkles size={28} className="text-purple-500" />
        </div>
        <h3 className="font-semibold text-gray-900 dark:text-white mb-2">AI 任务助手</h3>
        <p className="text-sm text-gray-500 dark:text-gray-400 text-center mb-6">
          告诉我你的目标，我来帮你拆解成可执行的任务清单
        </p>
        <div className="w-full space-y-2">
          {['我要学习一门新技能', '帮我规划一个项目', '制定健身计划'].map((q, i) => (
            <button
              key={q}
              className="w-full text-left px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 text-sm text-gray-700 dark:text-gray-300 transition-all active:scale-[0.98]"
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
          <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center flex-shrink-0">
            <Sparkles size={14} className="text-white" />
          </div>
          <div className="bg-white dark:bg-gray-800 rounded-2xl rounded-tl-md px-4 py-3 shadow-sm">
            <div className="flex gap-1.5">
              <span className="w-2 h-2 bg-purple-400 rounded-full animate-bounce" style={{ animationDelay: '0ms' }} />
              <span className="w-2 h-2 bg-purple-400 rounded-full animate-bounce" style={{ animationDelay: '150ms' }} />
              <span className="w-2 h-2 bg-purple-400 rounded-full animate-bounce" style={{ animationDelay: '300ms' }} />
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
        className={`w-8 h-8 rounded-lg flex items-center justify-center flex-shrink-0 ${
          isUser
            ? 'bg-blue-500'
            : 'bg-gradient-to-br from-purple-500 to-pink-500'
        }`}
      >
        {isUser ? (
          <span className="text-white text-xs font-bold">我</span>
        ) : (
          <Sparkles size={14} className="text-white" />
        )}
      </div>
      <div className={`max-w-[82%] ${isUser ? 'items-end' : 'items-start'}`}>
        <div className="group relative">
          <div
            className={`px-4 py-2.5 rounded-2xl text-sm leading-relaxed break-words ${
              isUser
                ? 'bg-gradient-to-br from-blue-500 to-blue-600 text-white rounded-tr-md shadow-sm'
                : 'bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200 rounded-tl-md shadow-sm'
            }`}
            dangerouslySetInnerHTML={{ __html: renderedContent }}
          />

          {!isUser && isLast && (
            <div className="flex items-center gap-1 mt-1.5 opacity-0 group-hover:opacity-100 transition-opacity">
              <button
                onClick={onCopy}
                className="flex items-center gap-1 px-2 py-1 rounded-lg text-xs text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
              >
                {isCopied ? (
                  <>
                    <CheckCheck size={12} className="text-green-500" />
                    <span className="text-green-500">已复制</span>
                  </>
                ) : (
                  <>
                    <Copy size={12} />
                    <span>复制</span>
                  </>
                )}
              </button>
              <button
                onClick={onRegenerate}
                className="flex items-center gap-1 px-2 py-1 rounded-lg text-xs text-gray-500 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors"
              >
                <RefreshCw size={12} />
                <span>重新生成</span>
              </button>
            </div>
          )}
        </div>

        {hasTasks && (
          <div className="mt-2">
            <button
              onClick={() => setShowTasks(!showTasks)}
              className={`flex items-center gap-2 px-3 py-2 rounded-xl text-xs font-medium transition-all w-full ${
                isUser
                  ? 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300'
                  : 'bg-purple-50 text-purple-700 dark:bg-purple-950/30 dark:text-purple-300'
              } hover:shadow-sm active:scale-[0.98]`}
            >
              <ListTodo size={14} />
              <span>任务清单 ({message.suggestedTasks!.length} 组 / 共 {totalTaskCount} 项</span>
              {showTasks ? <ChevronUp size={14} className="ml-auto" /> : <ChevronDown size={14} className="ml-auto" />}
            </button>

            {showTasks && (
              <div className="mt-2 bg-white dark:bg-gray-800 rounded-xl shadow-md overflow-hidden border border-gray-100 dark:border-gray-700 animate-[fadeIn_0.25s_ease]">
                <div className="flex items-center justify-between px-3 py-2 border-b border-gray-100 dark:border-gray-700 bg-gray-50 dark:bg-gray-750">
                  <label className="flex items-center gap-2 text-xs text-gray-600 dark:text-gray-300">
                    <input
                      type="checkbox"
                      checked={selectedCount === totalTaskCount}
                      onChange={toggleAll}
                      className="w-4 h-4 rounded border-gray-300 text-purple-500 focus:ring-purple-500"
                    />
                    全选 ({selectedCount}/{totalTaskCount}
                  </label>
                </div>
                <TaskPreview
                  tasks={message.suggestedTasks!}
                  selectedIds={selectedTaskIds}
                  onToggle={toggleTask}
                />
                <div className="p-3 border-t border-gray-100 dark:border-gray-700">
                  <button
                    onClick={() => onImport(message.suggestedTasks!, selectedTaskIds)}
                    disabled={selectedCount === 0}
                    className={`w-full flex items-center justify-center gap-2 py-2.5 rounded-lg text-sm font-medium transition-all ${
                      selectedCount > 0
                        ? 'bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white active:scale-[0.98]'
                        : 'bg-gray-100 dark:bg-gray-700 text-gray-400 cursor-not-allowed'
                    }`}
                  >
                    <Download size={16} />
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
  const priorityColors = {
    1: 'bg-red-500',
    2: 'bg-yellow-500',
    3: 'bg-blue-500',
  };

  return (
    <div className="divide-y divide-gray-100 dark:divide-gray-700">
      {tasks.map((task) => (
        <div key={task.id}>
          <label
            className={`flex items-start gap-3 px-3 py-2.5 hover:bg-gray-50 dark:hover:bg-gray-700/50 cursor-pointer transition-colors ${
          depth > 0 ? 'pl-10' : ''
        }`}
          >
            <input
              type="checkbox"
              checked={selectedIds.has(task.id)}
              onChange={() => onToggle(task.id)}
              className="mt-0.5 w-4 h-4 rounded border-gray-300 text-purple-500 focus:ring-purple-500 flex-shrink-0"
            />
            <div className={`mt-1 w-1 h-4 rounded-full ${priorityColors[task.priority]} flex-shrink-0`} />
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 dark:text-white">
                {task.title}
              </p>
              {task.description && (
                <p className="text-xs text-gray-500 dark:text-gray-400 mt-0.5">
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
}: {
  input: string;
  setInput: (v: string) => void;
  onSend: () => void;
  onKeyDown: (e: React.KeyboardEvent) => void;
  isLoading: boolean;
}) {
  const textareaRef = useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    if (textareaRef.current) {
      textareaRef.current.style.height = 'auto';
      textareaRef.current.style.height = Math.min(textareaRef.current.scrollHeight, 128) + 'px';
    }
  }, [input]);

  return (
    <div className="border-t border-gray-100 dark:border-gray-800 p-3 flex-shrink-0 bg-white dark:bg-gray-900">
      <div className="flex items-end gap-2">
        <button className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-all active:scale-95 flex-shrink-0">
          <Paperclip size={20} className="text-gray-500" />
        </button>
        <div className="flex-1 relative">
          <textarea
            ref={textareaRef}
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={onKeyDown}
            placeholder="说说你的目标..."
            rows={1}
            className="w-full px-4 py-2.5 bg-gray-100 dark:bg-gray-800 rounded-2xl text-sm text-gray-900 dark:text-white placeholder-gray-500 outline-none resize-none max-h-32"
            style={{ minHeight: '42px' }}
          />
        </div>
        {input.trim() ? (
          <button
            onClick={onSend}
            disabled={isLoading}
            className="w-10 h-10 flex items-center justify-center rounded-full bg-gradient-to-br from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white transition-all active:scale-95 flex-shrink-0 shadow-md"
          >
            <Send size={18} />
          </button>
        ) : (
          <button className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-all active:scale-95 flex-shrink-0">
            <Mic size={20} className="text-gray-500" />
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
      return `<pre class="mt-2 p-3 rounded-lg bg-gray-900 dark:bg-gray-950 text-xs text-gray-300 overflow-x-auto"><code>${escapeHtml(code.trim())}</code></pre>`;
    })
    .replace(/`([^`]+)`/g, '<code class="px-1.5 py-0.5 rounded bg-gray-100 dark:bg-gray-700 text-pink-500 text-xs font-mono">$1</code>');

  html = html
    .replace(/^### (.+)$/gm, '<h3 class="font-bold text-base mt-3 mb-2">$1</h3>')
    .replace(/^## (.+)$/gm, '<h2 class="font-bold text-lg mt-4 mb-2">$1</h2>')
    .replace(/^# (.+)$/gm, '<h1 class="font-bold text-xl mt-4 mb-2">$1</h1>');

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
