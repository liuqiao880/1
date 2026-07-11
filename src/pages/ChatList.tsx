import { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { Plus, MessageSquare, Trash2, ArrowLeft, Settings, Sparkles, Pin } from 'lucide-react';
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
      <div className="flex items-center justify-between px-5 py-4 border-b border-gray-100 dark:border-gray-800">
        <div className="flex items-center gap-3">
          <button
            onClick={onBack}
            className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
          >
            <ArrowLeft size={20} className="text-gray-600 dark:text-gray-300" />
          </button>
          <div className="flex items-center gap-2">
            <div className="w-9 h-9 rounded-xl bg-gradient-to-br from-purple-500 to-pink-500 flex items-center justify-center">
              <Sparkles size={18} className="text-white" />
            </div>
            <h1 className="font-bold text-lg text-gray-900 dark:text-white">AI 对话</h1>
          </div>
        </div>
        <div className="flex items-center gap-1">
          <button
            onClick={onSettings}
            className="w-10 h-10 flex items-center justify-center rounded-full hover:bg-gray-100 dark:hover:bg-gray-800 transition-colors"
          >
            <Settings size={20} className="text-gray-500" />
          </button>
          <button
            onClick={onNewChat}
            className="w-10 h-10 flex items-center justify-center rounded-full bg-purple-500 hover:bg-purple-600 text-white transition-all active:scale-95"
          >
            <Plus size={20} />
          </button>
        </div>
      </div>

      <div className="flex-1 overflow-y-auto">
        {chats.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 px-6">
            <div className="w-20 h-20 rounded-2xl bg-gradient-to-br from-purple-100 to-pink-100 dark:from-purple-900/30 dark:to-pink-900/30 flex items-center justify-center mb-4">
              <MessageSquare size={36} className="text-purple-500" />
            </div>
            <h3 className="font-semibold text-gray-900 dark:text-white mb-2">开始新对话</h3>
            <p className="text-sm text-gray-500 dark:text-gray-400 text-center mb-6">
              和 AI 聊聊你的目标，让它帮你拆解成可执行的任务
            </p>
            <button
              onClick={onNewChat}
              className="px-6 py-2.5 bg-purple-500 hover:bg-purple-600 text-white rounded-xl font-medium transition-all active:scale-95"
            >
              开始对话
            </button>
          </div>
        ) : (
          <div className="p-3 space-y-1">
            {chats.map((chat) => (
              <ChatItem
                key={chat.id}
                chat={chat}
                onOpen={onOpen}
                onDelete={onDelete}
                formatTime={formatTime}
                formatDate={formatDate}
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
}: {
  chat: Chat;
  onOpen: (id: string) => void;
  onDelete: (id: string) => void;
  formatTime: (ts: number) => string;
  formatDate: (ts: number) => string;
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
    <div className="relative overflow-hidden rounded-xl">
      <div
        className="absolute right-0 top-0 bottom-0 w-20 bg-red-500 flex items-center justify-center text-white z-0"
        onClick={handleDelete}
      >
        <div className="flex flex-col items-center gap-1">
          <Trash2 size={16} />
          <span className="text-xs font-medium">删除</span>
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
        className="relative flex items-center gap-3 p-3 bg-white dark:bg-gray-900 hover:bg-gray-50 dark:hover:bg-gray-800/50 transition-colors z-10 cursor-pointer active:scale-[0.99]"
      >
        <div className="w-11 h-11 rounded-xl bg-gradient-to-br from-purple-100 to-pink-100 dark:from-purple-900/40 dark:to-pink-900/40 flex items-center justify-center flex-shrink-0">
          <MessageSquare size={20} className="text-purple-600 dark:text-purple-400" />
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center justify-between gap-2">
            <p className="font-medium text-gray-900 dark:text-white truncate">
              {chat.title}
            </p>
            <span className="text-xs text-gray-400 flex-shrink-0">{formatTime(chat.updateTime)}</span>
          </div>
          <p className="text-xs text-gray-500 dark:text-gray-400 truncate mt-1">
            {preview || '暂无消息'}
          </p>
        </div>
      </div>
    </div>
  );
}
