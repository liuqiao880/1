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
public final class GetGroupedTasksUseCase_Factory implements Factory<GetGroupedTasksUseCase> {
  private final Provider<TaskRepository> repositoryProvider;

  public GetGroupedTasksUseCase_Factory(Provider<TaskRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetGroupedTasksUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetGroupedTasksUseCase_Factory create(Provider<TaskRepository> repositoryProvider) {
    return new GetGroupedTasksUseCase_Factory(repositoryProvider);
  }

  public static GetGroupedTasksUseCase newInstance(TaskRepository repository) {
    return new GetGroupedTasksUseCase(repository);
  }
}
