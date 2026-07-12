package com.taskflow.app.ui.screen.chat;

import com.taskflow.app.domain.usecase.AddTasksUseCase;
import com.taskflow.app.domain.usecase.CreateChatUseCase;
import com.taskflow.app.domain.usecase.GetChatMessagesUseCase;
import com.taskflow.app.domain.usecase.SendChatMessageUseCase;
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
public final class ChatDetailViewModel_Factory implements Factory<ChatDetailViewModel> {
  private final Provider<GetChatMessagesUseCase> getChatMessagesUseCaseProvider;

  private final Provider<SendChatMessageUseCase> sendChatMessageUseCaseProvider;

  private final Provider<CreateChatUseCase> createChatUseCaseProvider;

  private final Provider<AddTasksUseCase> addTasksUseCaseProvider;

  public ChatDetailViewModel_Factory(
      Provider<GetChatMessagesUseCase> getChatMessagesUseCaseProvider,
      Provider<SendChatMessageUseCase> sendChatMessageUseCaseProvider,
      Provider<CreateChatUseCase> createChatUseCaseProvider,
      Provider<AddTasksUseCase> addTasksUseCaseProvider) {
    this.getChatMessagesUseCaseProvider = getChatMessagesUseCaseProvider;
    this.sendChatMessageUseCaseProvider = sendChatMessageUseCaseProvider;
    this.createChatUseCaseProvider = createChatUseCaseProvider;
    this.addTasksUseCaseProvider = addTasksUseCaseProvider;
  }

  @Override
  public ChatDetailViewModel get() {
    return newInstance(getChatMessagesUseCaseProvider.get(), sendChatMessageUseCaseProvider.get(), createChatUseCaseProvider.get(), addTasksUseCaseProvider.get());
  }

  public static ChatDetailViewModel_Factory create(
      Provider<GetChatMessagesUseCase> getChatMessagesUseCaseProvider,
      Provider<SendChatMessageUseCase> sendChatMessageUseCaseProvider,
      Provider<CreateChatUseCase> createChatUseCaseProvider,
      Provider<AddTasksUseCase> addTasksUseCaseProvider) {
    return new ChatDetailViewModel_Factory(getChatMessagesUseCaseProvider, sendChatMessageUseCaseProvider, createChatUseCaseProvider, addTasksUseCaseProvider);
  }

  public static ChatDetailViewModel newInstance(GetChatMessagesUseCase getChatMessagesUseCase,
      SendChatMessageUseCase sendChatMessageUseCase, CreateChatUseCase createChatUseCase,
      AddTasksUseCase addTasksUseCase) {
    return new ChatDetailViewModel(getChatMessagesUseCase, sendChatMessageUseCase, createChatUseCase, addTasksUseCase);
  }
}
