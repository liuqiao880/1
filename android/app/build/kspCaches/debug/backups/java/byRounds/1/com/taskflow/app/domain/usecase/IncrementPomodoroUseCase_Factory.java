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
public final class IncrementPomodoroUseCase_Factory implements Factory<IncrementPomodoroUseCase> {
  private final Provider<TaskRepository> repositoryProvider;

  public IncrementPomodoroUseCase_Factory(Provider<TaskRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public IncrementPomodoroUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static IncrementPomodoroUseCase_Factory create(
      Provider<TaskRepository> repositoryProvider) {
    return new IncrementPomodoroUseCase_Factory(repositoryProvider);
  }

  public static IncrementPomodoroUseCase newInstance(TaskRepository repository) {
    return new IncrementPomodoroUseCase(repository);
  }
}
