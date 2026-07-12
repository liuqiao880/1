package com.taskflow.app.ui.screen.home;

import com.taskflow.app.domain.repository.PreferencesRepository;
import com.taskflow.app.domain.usecase.AddTaskUseCase;
import com.taskflow.app.domain.usecase.AddTasksUseCase;
import com.taskflow.app.domain.usecase.DeleteTaskUseCase;
import com.taskflow.app.domain.usecase.GetGroupedTasksUseCase;
import com.taskflow.app.domain.usecase.GetTaskByIdUseCase;
import com.taskflow.app.domain.usecase.IncrementPomodoroUseCase;
import com.taskflow.app.domain.usecase.SearchTasksUseCase;
import com.taskflow.app.domain.usecase.ToggleTaskUseCase;
import com.taskflow.app.domain.usecase.UpdateTaskUseCase;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<GetGroupedTasksUseCase> getGroupedTasksUseCaseProvider;

  private final Provider<ToggleTaskUseCase> toggleTaskUseCaseProvider;

  private final Provider<AddTaskUseCase> addTaskUseCaseProvider;

  private final Provider<AddTasksUseCase> addTasksUseCaseProvider;

  private final Provider<UpdateTaskUseCase> updateTaskUseCaseProvider;

  private final Provider<DeleteTaskUseCase> deleteTaskUseCaseProvider;

  private final Provider<SearchTasksUseCase> searchTasksUseCaseProvider;

  private final Provider<GetTaskByIdUseCase> getTaskByIdUseCaseProvider;

  private final Provider<IncrementPomodoroUseCase> incrementPomodoroUseCaseProvider;

  private final Provider<PreferencesRepository> preferencesRepositoryProvider;

  public HomeViewModel_Factory(Provider<GetGroupedTasksUseCase> getGroupedTasksUseCaseProvider,
      Provider<ToggleTaskUseCase> toggleTaskUseCaseProvider,
      Provider<AddTaskUseCase> addTaskUseCaseProvider,
      Provider<AddTasksUseCase> addTasksUseCaseProvider,
      Provider<UpdateTaskUseCase> updateTaskUseCaseProvider,
      Provider<DeleteTaskUseCase> deleteTaskUseCaseProvider,
      Provider<SearchTasksUseCase> searchTasksUseCaseProvider,
      Provider<GetTaskByIdUseCase> getTaskByIdUseCaseProvider,
      Provider<IncrementPomodoroUseCase> incrementPomodoroUseCaseProvider,
      Provider<PreferencesRepository> preferencesRepositoryProvider) {
    this.getGroupedTasksUseCaseProvider = getGroupedTasksUseCaseProvider;
    this.toggleTaskUseCaseProvider = toggleTaskUseCaseProvider;
    this.addTaskUseCaseProvider = addTaskUseCaseProvider;
    this.addTasksUseCaseProvider = addTasksUseCaseProvider;
    this.updateTaskUseCaseProvider = updateTaskUseCaseProvider;
    this.deleteTaskUseCaseProvider = deleteTaskUseCaseProvider;
    this.searchTasksUseCaseProvider = searchTasksUseCaseProvider;
    this.getTaskByIdUseCaseProvider = getTaskByIdUseCaseProvider;
    this.incrementPomodoroUseCaseProvider = incrementPomodoroUseCaseProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(getGroupedTasksUseCaseProvider.get(), toggleTaskUseCaseProvider.get(), addTaskUseCaseProvider.get(), addTasksUseCaseProvider.get(), updateTaskUseCaseProvider.get(), deleteTaskUseCaseProvider.get(), searchTasksUseCaseProvider.get(), getTaskByIdUseCaseProvider.get(), incrementPomodoroUseCaseProvider.get(), preferencesRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<GetGroupedTasksUseCase> getGroupedTasksUseCaseProvider,
      Provider<ToggleTaskUseCase> toggleTaskUseCaseProvider,
      Provider<AddTaskUseCase> addTaskUseCaseProvider,
      Provider<AddTasksUseCase> addTasksUseCaseProvider,
      Provider<UpdateTaskUseCase> updateTaskUseCaseProvider,
      Provider<DeleteTaskUseCase> deleteTaskUseCaseProvider,
      Provider<SearchTasksUseCase> searchTasksUseCaseProvider,
      Provider<GetTaskByIdUseCase> getTaskByIdUseCaseProvider,
      Provider<IncrementPomodoroUseCase> incrementPomodoroUseCaseProvider,
      Provider<PreferencesRepository> preferencesRepositoryProvider) {
    return new HomeViewModel_Factory(getGroupedTasksUseCaseProvider, toggleTaskUseCaseProvider, addTaskUseCaseProvider, addTasksUseCaseProvider, updateTaskUseCaseProvider, deleteTaskUseCaseProvider, searchTasksUseCaseProvider, getTaskByIdUseCaseProvider, incrementPomodoroUseCaseProvider, preferencesRepositoryProvider);
  }

  public static HomeViewModel newInstance(GetGroupedTasksUseCase getGroupedTasksUseCase,
      ToggleTaskUseCase toggleTaskUseCase, AddTaskUseCase addTaskUseCase,
      AddTasksUseCase addTasksUseCase, UpdateTaskUseCase updateTaskUseCase,
      DeleteTaskUseCase deleteTaskUseCase, SearchTasksUseCase searchTasksUseCase,
      GetTaskByIdUseCase getTaskByIdUseCase, IncrementPomodoroUseCase incrementPomodoroUseCase,
      PreferencesRepository preferencesRepository) {
    return new HomeViewModel(getGroupedTasksUseCase, toggleTaskUseCase, addTaskUseCase, addTasksUseCase, updateTaskUseCase, deleteTaskUseCase, searchTasksUseCase, getTaskByIdUseCase, incrementPomodoroUseCase, preferencesRepository);
  }
}
