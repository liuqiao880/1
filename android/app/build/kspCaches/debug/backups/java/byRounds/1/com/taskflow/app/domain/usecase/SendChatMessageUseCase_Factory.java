package com.taskflow.app.domain.usecase;

import com.taskflow.app.domain.repository.ChatRepository;
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
public final class SendChatMessageUseCase_Factory implements Factory<SendChatMessageUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  public SendChatMessageUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SendChatMessageUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SendChatMessageUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new SendChatMessageUseCase_Factory(repositoryProvider);
  }

  public static SendChatMessageUseCase newInstance(ChatRepository repository) {
    return new SendChatMessageUseCase(repository);
  }
}
