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
public final class GetChatMessagesUseCase_Factory implements Factory<GetChatMessagesUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  public GetChatMessagesUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetChatMessagesUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetChatMessagesUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new GetChatMessagesUseCase_Factory(repositoryProvider);
  }

  public static GetChatMessagesUseCase newInstance(ChatRepository repository) {
    return new GetChatMessagesUseCase(repository);
  }
}
