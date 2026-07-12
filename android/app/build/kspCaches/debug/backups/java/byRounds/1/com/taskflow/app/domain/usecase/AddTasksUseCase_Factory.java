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
public final class AddTasksUseCase_Factory implements Factory<AddTasksUseCase> {
  private final Provider<TaskRepository> repositoryProvider;

  public AddTasksUseCase_Factory(Provider<TaskRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public AddTasksUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static AddTasksUseCase_Factory create(Provider<TaskRepository> repositoryProvider) {
    return new AddTasksUseCase_Factory(repositoryProvider);
  }

  public static AddTasksUseCase newInstance(TaskRepository repository) {
    return new AddTasksUseCase(repository);
  }
}
