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
public final class DeleteChatUseCase_Factory implements Factory<DeleteChatUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  public DeleteChatUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public DeleteChatUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static DeleteChatUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new DeleteChatUseCase_Factory(repositoryProvider);
  }

  public static DeleteChatUseCase newInstance(ChatRepository repository) {
    return new DeleteChatUseCase(repository);
  }
}
