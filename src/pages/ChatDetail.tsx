import { useState, useRef, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  ArrowLeft,
  Send,
  Plus,
  Paperclip,
  Check,
  Sparkles,
  ChevronDown,
  ChevronUp,
  Download,
  ListTodo,
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
  const [expandedTasks, setExpandedTasks] = useState<Set<string>>(new Set());
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
  }, [chat?.messages, isLoading]);

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

  const toggleTasks = (msgId: string) => {
    setExpandedTasks((prev) => {
      const next = new Set(prev);
      if (next.has(msgId)) {
        next.delete(msgId);
      } else {
        next.add(msgId);
      }
      return next;
    });
  };

  const handleImportTasks = (tasks: Task[]) => {
    const importTask = (t: Task): Task => ({
      ...t,
      id: Date.now() + Math.floor(Math.random() * 1000),
      children: t.children?.map(importTask),
    });

    tasks.forEach((t) => {
      addTask(importTask(t));
    });
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
              expandedTasks={expandedTasks}
              onToggleTasks={toggleTasks}
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
          </div>
        </div>
      </div>

      <div className="sm:hidden flex-1 flex flex-col">
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
          expandedTasks={expandedTasks}
          onToggleTasks={toggleTasks}
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
        className="w-9 h-9 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
      >
        <Plus size={18} className="text-gray-600 dark:text-gray-300" />
      </button>
    </div>
  );
}

function MessageList({
  messages,
  isLoading,
  expandedTasks,
  onToggleTasks,
  onImport,
  messagesEndRef,
}: {
  messages: ChatMessage[];
  isLoading: boolean;
  expandedTasks: Set<string>;
  onToggleTasks: (id: string) => void;
  onImport: (tasks: Task[]) => void;
  messagesEndRef: React.RefObject<HTMLDivElement>;
}) {
  if (messages.length === 0) {
    return (
      <div className="flex-1 flex flex-col items-center justify-center px-6 py-10">
        <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-purple-100 to-pink-100 dark:from-purple-900/30 dark:to-pink-900/30 flex items-center justify-center mb-4">
          <Sparkles size={28} className="text-purple-500" />
        </div>
        <h3 className="font-semibold text-gray-900 dark:text-white mb-2">AI 任务助手</h3>
        <p className="text-sm text-gray-500 dark:text-gray-400 text-center mb-6">
          告诉我你的目标，我来帮你拆解成可执行的任务清单
        </p>
        <div className="w-full space-y-2">
          {['我要学习一门新技能', '帮我规划一个项目', '制定健身计划'].map((q) => (
            <button
              key={q}
              className="w-full text-left px-4 py-3 rounded-xl bg-gray-50 dark:bg-gray-800/50 hover:bg-gray-100 dark:hover:bg-gray-800 text-sm text-gray-700 dark:text-gray-300 transition-colors"
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
      {messages.map((msg) => (
        <MessageBubble
          key={msg.id}
          message={msg}
          expanded={expandedTasks.has(msg.id)}
          onToggleTasks={() => onToggleTasks(msg.id)}
          onImport={onImport}
        />
      ))}
      {isLoading && (
        <div className="flex gap-3">
          <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center flex-shrink-0">
            <Sparkles size={14} className="text-white" />
          </div>
          <div className="bg-white dark:bg-gray-800 rounded-2xl rounded-tl-md px-4 py-3 shadow-sm">
            <div className="flex gap-1.5">
              <span className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '0ms' }} />
              <span className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '150ms' }} />
              <span className="w-2 h-2 bg-gray-400 rounded-full animate-bounce" style={{ animationDelay: '300ms' }} />
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
  expanded,
  onToggleTasks,
  onImport,
}: {
  message: ChatMessage;
  expanded: boolean;
  onToggleTasks: () => void;
  onImport: (tasks: Task[]) => void;
}) {
  const isUser = message.role === 'user';
  const hasTasks = message.suggestedTasks && message.suggestedTasks.length > 0;

  return (
    <div className={`flex gap-3 ${isUser ? 'flex-row-reverse' : ''}`}>
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
      <div className={`max-w-[80%] ${isUser ? 'items-end' : 'items-start'}`}>
        <div
          className={`px-4 py-2.5 rounded-2xl text-sm leading-relaxed whitespace-pre-wrap break-words ${
            isUser
              ? 'bg-blue-500 text-white rounded-tr-md'
              : 'bg-white dark:bg-gray-800 text-gray-800 dark:text-gray-200 rounded-tl-md shadow-sm'
          }`}
        >
          {message.content.replace(/```json[\s\S]*?```/g, '').trim() || '查看下方任务列表'}
        </div>

        {hasTasks && (
          <div className="mt-2">
            <button
              onClick={onToggleTasks}
              className={`flex items-center gap-2 px-3 py-2 rounded-xl text-xs font-medium transition-colors w-full ${
                isUser
                  ? 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300'
                  : 'bg-purple-50 text-purple-700 dark:bg-purple-950/30 dark:text-purple-300'
              }`}
            >
              <ListTodo size={14} />
              <span>任务清单 ({message.suggestedTasks!.length})</span>
              {expanded ? <ChevronUp size={14} className="ml-auto" /> : <ChevronDown size={14} className="ml-auto" />}
            </button>

            {expanded && (
              <div className="mt-2 bg-white dark:bg-gray-800 rounded-xl shadow-sm overflow-hidden border border-gray-100 dark:border-gray-700">
                <TaskPreview tasks={message.suggestedTasks!} />
                <div className="p-3 border-t border-gray-100 dark:border-gray-700">
                  <button
                    onClick={() => onImport(message.suggestedTasks!)}
                    className="w-full flex items-center justify-center gap-2 py-2 bg-purple-500 hover:bg-purple-600 text-white rounded-lg text-sm font-medium transition-colors"
                  >
                    <Download size={16} />
                    导入到任务列表
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

function TaskPreview({ tasks, depth = 0 }: { tasks: Task[]; depth?: number }) {
  const priorityColors = {
    1: 'bg-red-500',
    2: 'bg-yellow-500',
    3: 'bg-blue-500',
  };

  return (
    <div className="divide-y divide-gray-100 dark:divide-gray-700">
      {tasks.map((task, idx) => (
        <div key={task.id}>
          <div
            className={`flex items-start gap-3 px-3 py-2.5 hover:bg-gray-50 dark:hover:bg-gray-700/50 ${
              depth > 0 ? 'pl-10' : ''
            }`}
          >
            <div className={`mt-1 w-1 h-4 rounded-full ${priorityColors[task.priority]} flex-shrink-0`} />
            <div className="flex-1 min-w-0">
              <p className="text-sm font-medium text-gray-900 dark:text-white truncate">
                {task.title}
              </p>
              {task.description && (
                <p className="text-xs text-gray-500 dark:text-gray-400 truncate mt-0.5">
                  {task.description}
                </p>
              )}
            </div>
            <span className="text-xs text-gray-400">#{idx + 1}</span>
          </div>
          {task.children && task.children.length > 0 && (
            <TaskPreview tasks={task.children} depth={depth + 1} />
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
  return (
    <div className="border-t border-gray-100 dark:border-gray-800 p-3 flex-shrink-0 bg-white dark:bg-gray-900">
      <div className="flex items-end gap-2">
        <button className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors flex-shrink-0">
          <Paperclip size={20} className="text-gray-500" />
        </button>
        <div className="flex-1 relative">
          <textarea
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={onKeyDown}
            placeholder="说说你的目标..."
            rows={1}
            className="w-full px-4 py-2.5 bg-gray-100 dark:bg-gray-800 rounded-2xl text-sm text-gray-900 dark:text-white placeholder-gray-500 outline-none resize-none max-h-32"
            style={{ minHeight: '42px' }}
          />
        </div>
        <button
          onClick={onSend}
          disabled={!input.trim() || isLoading}
          className={`w-10 h-10 flex items-center justify-center rounded-full transition-all flex-shrink-0 ${
            input.trim() && !isLoading
              ? 'bg-purple-500 hover:bg-purple-600 text-white'
              : 'bg-gray-200 dark:bg-gray-700 text-gray-400'
          }`}
        >
          <Send size={18} />
        </button>
      </div>
    </div>
  );
}
