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
public final class CreateChatUseCase_Factory implements Factory<CreateChatUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  public CreateChatUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CreateChatUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static CreateChatUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new CreateChatUseCase_Factory(repositoryProvider);
  }

  public static CreateChatUseCase newInstance(ChatRepository repository) {
    return new CreateChatUseCase(repository);
  }
}
