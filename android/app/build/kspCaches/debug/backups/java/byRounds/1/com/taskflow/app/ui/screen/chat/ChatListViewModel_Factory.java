package com.taskflow.app.ui.screen.chat;

import com.taskflow.app.domain.usecase.CreateChatUseCase;
import com.taskflow.app.domain.usecase.DeleteChatUseCase;
import com.taskflow.app.domain.usecase.GetAllChatsUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ChatListViewModel_Factory implements Factory<ChatListViewModel> {
  private final Provider<GetAllChatsUseCase> getAllChatsUseCaseProvider;

  private final Provider<CreateChatUseCase> createChatUseCaseProvider;

  private final Provider<DeleteChatUseCase> deleteChatUseCaseProvider;

  public ChatListViewModel_Factory(Provider<GetAllChatsUseCase> getAllChatsUseCaseProvider,
      Provider<CreateChatUseCase> createChatUseCaseProvider,
      Provider<DeleteChatUseCase> deleteChatUseCaseProvider) {
    this.getAllChatsUseCaseProvider = getAllChatsUseCaseProvider;
    this.createChatUseCaseProvider = createChatUseCaseProvider;
    this.deleteChatUseCaseProvider = deleteChatUseCaseProvider;
  }

  @Override
  public ChatListViewModel get() {
    return newInstance(getAllChatsUseCaseProvider.get(), createChatUseCaseProvider.get(), deleteChatUseCaseProvider.get());
  }

  public static ChatListViewModel_Factory create(
      Provider<GetAllChatsUseCase> getAllChatsUseCaseProvider,
      Provider<CreateChatUseCase> createChatUseCaseProvider,
      Provider<DeleteChatUseCase> deleteChatUseCaseProvider) {
    return new ChatListViewModel_Factory(getAllChatsUseCaseProvider, createChatUseCaseProvider, deleteChatUseCaseProvider);
  }

  public static ChatListViewModel newInstance(GetAllChatsUseCase getAllChatsUseCase,
      CreateChatUseCase createChatUseCase, DeleteChatUseCase deleteChatUseCase) {
    return new ChatListViewModel(getAllChatsUseCase, createChatUseCase, deleteChatUseCase);
  }
}
