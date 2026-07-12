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
public final class GetAllChatsUseCase_Factory implements Factory<GetAllChatsUseCase> {
  private final Provider<ChatRepository> repositoryProvider;

  public GetAllChatsUseCase_Factory(Provider<ChatRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllChatsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAllChatsUseCase_Factory create(Provider<ChatRepository> repositoryProvider) {
    return new GetAllChatsUseCase_Factory(repositoryProvider);
  }

  public static GetAllChatsUseCase newInstance(ChatRepository repository) {
    return new GetAllChatsUseCase(repository);
  }
}
