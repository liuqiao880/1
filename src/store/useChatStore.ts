import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { Chat, ChatMessage, AiConfig } from '@/types/task';
import { aiService } from '@/services/aiService';

interface ChatState {
  chats: Chat[];
  activeChatId: string | null;
  aiConfig: AiConfig;
  isLoading: boolean;

  createChat: () => Chat;
  deleteChat: (id: string) => void;
  setActiveChat: (id: string) => void;
  sendMessage: (chatId: string, content: string) => Promise<void>;
  regenerateLastResponse: (chatId: string) => Promise<void>;
  updateAiConfig: (config: Partial<AiConfig>) => void;
}

function genId() {
  return Date.now().toString(36) + Math.random().toString(36).slice(2);
}

const defaultAiConfig: AiConfig = {
  provider: 'openai',
  baseUrl: 'https://api.openai.com/v1',
  apiKey: '',
  model: 'gpt-4o-mini',
  systemPrompt: aiService.defaultSystemPrompt,
};

export const useChatStore = create<ChatState>()(
  persist(
    (set, get) => ({
      chats: [],
      activeChatId: null,
      aiConfig: defaultAiConfig,
      isLoading: false,

      createChat: () => {
        const now = Date.now();
        const newChat: Chat = {
          id: genId(),
          title: '新对话',
          messages: [],
          createTime: now,
          updateTime: now,
        };
        set({
          chats: [newChat, ...get().chats],
          activeChatId: newChat.id,
        });
        return newChat;
      },

      deleteChat: (id: string) => {
        const { chats, activeChatId } = get();
        const newChats = chats.filter((c) => c.id !== id);
        set({
          chats: newChats,
          activeChatId: activeChatId === id ? (newChats[0]?.id ?? null) : activeChatId,
        });
      },

      setActiveChat: (id: string) => {
        set({ activeChatId: id });
      },

      sendMessage: async (chatId: string, content: string) => {
        if (!content.trim()) return;

        const userMsg: ChatMessage = {
          id: genId(),
          role: 'user',
          content: content.trim(),
          timestamp: Date.now(),
        };

        const chats = get().chats.map((c) => {
          if (c.id !== chatId) return c;
          const isFirst = c.messages.length === 0;
          return {
            ...c,
            messages: [...c.messages, userMsg],
            title: isFirst ? content.slice(0, 20) : c.title,
            updateTime: Date.now(),
          };
        });
        set({ chats, isLoading: true });

        try {
          const chat = chats.find((c) => c.id === chatId);
          if (!chat) throw new Error('对话不存在');

          const msgHistory = chat.messages.map((m) => ({
            role: m.role,
            content: m.content,
          }));

          const { aiConfig } = get();
          const response = await aiService.chat(msgHistory, aiConfig);
          const suggestedTasks = aiService.parseTasksFromResponse(response);

          const assistantMsg: ChatMessage = {
            id: genId(),
            role: 'assistant',
            content: response,
            timestamp: Date.now(),
            suggestedTasks: suggestedTasks.length > 0 ? suggestedTasks : undefined,
          };

          set({
            chats: get().chats.map((c) =>
              c.id === chatId
                ? { ...c, messages: [...c.messages, assistantMsg], updateTime: Date.now() }
                : c
            ),
            isLoading: false,
          });
        } catch (error) {
          console.error('发送消息失败:', error);
          const errorMsg: ChatMessage = {
            id: genId(),
            role: 'assistant',
            content: `抱歉，请求失败了：${error instanceof Error ? error.message : '未知错误'}\n\n请检查 AI 设置中的 API 配置。`,
            timestamp: Date.now(),
          };
          set({
            chats: get().chats.map((c) =>
              c.id === chatId
                ? { ...c, messages: [...c.messages, errorMsg], updateTime: Date.now() }
                : c
            ),
            isLoading: false,
          });
        }
      },

      regenerateLastResponse: async (chatId: string) => {
        const chat = get().chats.find((c) => c.id === chatId);
        if (!chat) return;

        const lastUserIdx = [...chat.messages].reverse().findIndex((m) => m.role === 'user');
        if (lastUserIdx === -1) return;

        const userMsgIdx = chat.messages.length - 1 - lastUserIdx;
        const truncatedMessages = chat.messages.slice(0, userMsgIdx + 1);

        set({
          chats: get().chats.map((c) =>
            c.id === chatId
              ? { ...c, messages: truncatedMessages, updateTime: Date.now() }
              : c
          ),
          isLoading: true,
        });

        try {
          const msgHistory = truncatedMessages.map((m) => ({
            role: m.role,
            content: m.content,
          }));

          const { aiConfig } = get();
          const response = await aiService.chat(msgHistory, aiConfig);
          const suggestedTasks = aiService.parseTasksFromResponse(response);

          const assistantMsg: ChatMessage = {
            id: genId(),
            role: 'assistant',
            content: response,
            timestamp: Date.now(),
            suggestedTasks: suggestedTasks.length > 0 ? suggestedTasks : undefined,
          };

          set({
            chats: get().chats.map((c) =>
              c.id === chatId
                ? { ...c, messages: [...c.messages, assistantMsg], updateTime: Date.now() }
                : c
            ),
            isLoading: false,
          });
        } catch (error) {
          console.error('重新生成失败:', error);
          const errorMsg: ChatMessage = {
            id: genId(),
            role: 'assistant',
            content: `抱歉，重新生成失败：${error instanceof Error ? error.message : '未知错误'}`,
            timestamp: Date.now(),
          };
          set({
            chats: get().chats.map((c) =>
              c.id === chatId
                ? { ...c, messages: [...c.messages, errorMsg], updateTime: Date.now() }
                : c
            ),
            isLoading: false,
          });
        }
      },

      updateAiConfig: (config: Partial<AiConfig>) => {
        set({ aiConfig: { ...get().aiConfig, ...config } });
      },
    }),
    {
      name: 'taskflow-chat',
      partialize: (state) => ({
        chats: state.chats,
        aiConfig: {
          ...state.aiConfig,
          apiKey: '', // 不持久化 API Key 到 localStorage
        },
      }),
    }
  )
);
