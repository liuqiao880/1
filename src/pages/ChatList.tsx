import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, MessageSquare, Trash2, ArrowLeft, Settings, Sparkles } from 'lucide-react';
import { useChatStore } from '@/store/useChatStore';
import { Chat } from '@/types/task';

export default function ChatList() {
  const navigate = useNavigate();
  const { chats, createChat, deleteChat } = useChatStore();

  const handleNewChat = () => {
    const chat = createChat();
    navigate(`/chat/${chat.id}`);
  };

  const formatTime = (ts: number) => {
    const date = new Date(ts);
    const now = new Date();
    const sameDay = date.toDateString() === now.toDateString();
    if (sameDay) {
      return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
    }
    return `${date.getMonth() + 1}/${date.getDate()}`;
  };

  const formatDate = (ts: number) => {
    const date = new Date(ts);
    return `${date.getMonth() + 1}月${date.getDate()}日`;
  };

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
          <div className="h-full overflow-y-auto pt-7 scrollbar-hide">
            <ChatListContent
              chats={chats}
              onNewChat={handleNewChat}
              onDelete={deleteChat}
              onOpen={(id) => navigate(`/chat/${id}`)}
              onBack={() => navigate('/')}
              onSettings={() => navigate('/chat/settings')}
              formatTime={formatTime}
              formatDate={formatDate}
            />
          </div>
        </div>
      </div>

      <div className="sm:hidden flex-1 flex flex-col">
        <ChatListContent
          chats={chats}
          onNewChat={handleNewChat}
          onDelete={deleteChat}
          onOpen={(id) => navigate(`/chat/${id}`)}
          onBack={() => navigate('/')}
          onSettings={() => navigate('/chat/settings')}
          formatTime={formatTime}
          formatDate={formatDate}
        />
      </div>
    </div>
  );
}

function ChatListContent({
  chats,
  onNewChat,
  onDelete,
  onOpen,
  onBack,
  onSettings,
  formatTime,
  formatDate,
}: {
  chats: Chat[];
  onNewChat: () => void;
  onDelete: (id: string) => void;
  onOpen: (id: string) => void;
  onBack: () => void;
  onSettings: () => void;
  formatTime: (ts: number) => string;
  formatDate: (ts: number) => string;
}) {
  return (
    <>
      <div className="flex items-center justify-between px-5 py-3 border-b border-line-separator dark:border-gray-800">
        <div className="flex items-center gap-2.5">
          <button
            onClick={onBack}
            className="w-9 h-9 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
          >
            <ArrowLeft size={18} className="text-ink-gray dark:text-gray-400" />
          </button>
          <div className="flex items-center gap-2">
            <div className="w-7 h-7 bg-newspaper-red flex items-center justify-center text-white">
              <Sparkles size={14} />
            </div>
            <div>
              <h1 className="font-serif text-base font-semibold text-ink-black dark:text-white leading-none">AI 对话</h1>
              <div className="newspaper-accent-line mt-0.5" style={{ width: '16px', height: '2px' }} />
            </div>
          </div>
        </div>
        <div className="flex items-center gap-0.5">
          <button
            onClick={onSettings}
            className="w-9 h-9 flex items-center justify-center hover:bg-black/5 dark:hover:bg-white/5 transition-colors"
          >
            <Settings size={18} className="text-ink-light dark:text-gray-500" />
          </button>
          <button
            onClick={onNewChat}
            className="w-9 h-9 flex items-center justify-center bg-ink-black dark:bg-gray-700 hover:bg-newspaper-red text-white transition-all active:scale-95"
          >
            <Plus size={18} />
          </button>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto">
        {chats.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 px-6">
            <div className="w-16 h-16 border border-line-separator dark:border-gray-700 flex items-center justify-center mb-4">
              <MessageSquare size={28} className="text-ink-light" />
            </div>
            <h3 className="font-serif text-base font-semibold text-ink-gray dark:text-gray-300 mb-2">开始新对话</h3>
            <p className="text-xs text-ink-light dark:text-gray-500 text-center mb-6 font-sans">
              和 AI 聊聊你的目标，让它帮你拆解成可执行的任务
            </p>
            <button
              onClick={onNewChat}
              className="px-5 py-2 bg-ink-black dark:bg-gray-700 hover:bg-newspaper-red text-paper-white text-sm font-medium transition-all active:scale-95"
            >
              开始对话
            </button>
          </div>
        ) : (
          <div>
            {chats.map((chat, idx) => (
              <ChatItem
                key={chat.id}
                chat={chat}
                onOpen={onOpen}
                onDelete={onDelete}
                formatTime={formatTime}
                formatDate={formatDate}
                showBorder={idx < chats.length - 1}
              />
            ))}
          </div>
        )}
      </div>
    </>
  );
}

function ChatItem({
  chat,
  onOpen,
  onDelete,
  formatTime,
  formatDate,
  showBorder,
}: {
  chat: Chat;
  onOpen: (id: string) => void;
  onDelete: (id: string) => void;
  formatTime: (ts: number) => string;
  formatDate: (ts: number) => string;
  showBorder: boolean;
}) {
  const [translateX, setTranslateX] = useState(0);
  const [startX, setStartX] = useState(0);
  const [isDragging, setIsDragging] = useState(false);
  const actionWidth = 72;

  const handleTouchStart = (e: React.TouchEvent | React.MouseEvent) => {
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    setStartX(clientX - translateX);
    setIsDragging(false);
  };

  const handleTouchMove = (e: React.TouchEvent | React.MouseEvent) => {
    const clientX = 'touches' in e ? e.touches[0].clientX : e.clientX;
    let diff = clientX - startX;
    diff = Math.max(-actionWidth, Math.min(0, diff));
    if (Math.abs(diff) > 5) setIsDragging(true);
    setTranslateX(diff);
  };

  const handleTouchEnd = () => {
    if (translateX < -actionWidth * 0.5) {
      setTranslateX(-actionWidth);
    } else {
      setTranslateX(0);
    }
  };

  const handleClick = () => {
    if (isDragging || Math.abs(translateX) > 5) return;
    onOpen(chat.id);
  };

  const handleDelete = (e: React.MouseEvent) => {
    e.stopPropagation();
    onDelete(chat.id);
  };

  const lastMsg = chat.messages[chat.messages.length - 1];
  const preview = lastMsg
    ? lastMsg.content.replace(/```json[\s\S]*?```/g, '').replace(/[#*`]/g, '').trim().slice(0, 50)
    : '暂无消息';

  return (
    <div className={`relative overflow-hidden ${showBorder ? 'border-b border-line-separator dark:border-gray-800' : ''}`}>
      <div
        className="absolute right-0 top-0 bottom-0 w-20 bg-newspaper-red flex items-center justify-center text-white z-0"
        onClick={handleDelete}
      >
        <div className="flex flex-col items-center gap-1">
          <Trash2 size={14} />
          <span className="text-[10px] font-medium">删除</span>
        </div>
      </div>

      <div
        onClick={handleClick}
        onTouchStart={handleTouchStart}
        onTouchMove={handleTouchMove}
        onTouchEnd={handleTouchEnd}
        onMouseDown={handleTouchStart}
        onMouseMove={handleTouchMove}
        onMouseUp={handleTouchEnd}
        onMouseLeave={handleTouchEnd}
        style={{
          transform: `translateX(${translateX}px)`,
          transition: isDragging ? 'none' : 'transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1)',
        }}
        className="relative flex items-center gap-3 px-5 py-3.5 bg-paper-white dark:bg-gray-900 hover:bg-paper-cream dark:hover:bg-gray-800/50 transition-colors z-10 cursor-pointer active:scale-[0.995]"
      >
        <div className="w-9 h-9 bg-newspaper-red/10 dark:bg-newspaper-red/20 flex items-center justify-center flex-shrink-0">
          <MessageSquare size={16} className="text-newspaper-red dark:text-newspaper-red-light" />
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center justify-between gap-2">
            <p className="font-serif text-sm font-medium text-ink-black dark:text-white truncate">
              {chat.title}
            </p>
            <span className="text-[10px] text-ink-light flex-shrink-0 tabular-nums font-sans">{formatTime(chat.updateTime)}</span>
          </div>
          <p className="text-xs text-ink-light dark:text-gray-500 truncate mt-0.5 font-sans">
            {preview || '暂无消息'}
          </p>
        </div>
      </div>
    </div>
  );
}
