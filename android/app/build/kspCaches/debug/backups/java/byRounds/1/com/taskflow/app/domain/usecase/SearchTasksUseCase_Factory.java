package com.taskflow.app.domain.usecase;

import com.taskflow.app.domain.repository.TaskRepository;
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
public final class SearchTasksUseCase_Factory implements Factory<SearchTasksUseCase> {
  private final Provider<TaskRepository> repositoryProvider;

  public SearchTasksUseCase_Factory(Provider<TaskRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchTasksUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SearchTasksUseCase_Factory create(Provider<TaskRepository> repositoryProvider) {
    return new SearchTasksUseCase_Factory(repositoryProvider);
  }

  public static SearchTasksUseCase newInstance(TaskRepository repository) {
    return new SearchTasksUseCase(repository);
  }
}
