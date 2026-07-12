package com.taskflow.app.ui.screen.chat;

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

  public ChatDetailViewModel_Factory(
      Provider<GetChatMessagesUseCase> getChatMessagesUseCaseProvider,
      Provider<SendChatMessageUseCase> sendChatMessageUseCaseProvider,
      Provider<CreateChatUseCase> createChatUseCaseProvider) {
    this.getChatMessagesUseCaseProvider = getChatMessagesUseCaseProvider;
    this.sendChatMessageUseCaseProvider = sendChatMessageUseCaseProvider;
    this.createChatUseCaseProvider = createChatUseCaseProvider;
  }

  @Override
  public ChatDetailViewModel get() {
    return newInstance(getChatMessagesUseCaseProvider.get(), sendChatMessageUseCaseProvider.get(), createChatUseCaseProvider.get());
  }

  public static ChatDetailViewModel_Factory create(
      Provider<GetChatMessagesUseCase> getChatMessagesUseCaseProvider,
      Provider<SendChatMessageUseCase> sendChatMessageUseCaseProvider,
      Provider<CreateChatUseCase> createChatUseCaseProvider) {
    return new ChatDetailViewModel_Factory(getChatMessagesUseCaseProvider, sendChatMessageUseCaseProvider, createChatUseCaseProvider);
  }

  public static ChatDetailViewModel newInstance(GetChatMessagesUseCase getChatMessagesUseCase,
      SendChatMessageUseCase sendChatMessageUseCase, CreateChatUseCase createChatUseCase) {
    return new ChatDetailViewModel(getChatMessagesUseCase, sendChatMessageUseCase, createChatUseCase);
  }
}
