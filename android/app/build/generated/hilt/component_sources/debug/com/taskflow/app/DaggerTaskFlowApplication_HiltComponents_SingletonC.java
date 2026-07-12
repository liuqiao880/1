package com.taskflow.app;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.taskflow.app.data.local.TaskDatabase;
import com.taskflow.app.data.local.dao.ChatDao;
import com.taskflow.app.data.local.dao.TaskDao;
import com.taskflow.app.data.local.di.DatabaseModule_ProvideChatDaoFactory;
import com.taskflow.app.data.local.di.DatabaseModule_ProvideDatabaseFactory;
import com.taskflow.app.data.local.di.DatabaseModule_ProvideTaskDaoFactory;
import com.taskflow.app.data.repository.ChatRepositoryImpl;
import com.taskflow.app.data.repository.PreferencesRepositoryImpl;
import com.taskflow.app.data.repository.TaskRepositoryImpl;
import com.taskflow.app.di.RepositoryModule_Companion_ProvideAiServiceFactory;
import com.taskflow.app.domain.repository.ChatRepository;
import com.taskflow.app.domain.repository.PreferencesRepository;
import com.taskflow.app.domain.repository.TaskRepository;
import com.taskflow.app.domain.service.AiService;
import com.taskflow.app.domain.usecase.AddTaskUseCase;
import com.taskflow.app.domain.usecase.AddTasksUseCase;
import com.taskflow.app.domain.usecase.CreateChatUseCase;
import com.taskflow.app.domain.usecase.DeleteChatUseCase;
import com.taskflow.app.domain.usecase.DeleteTaskUseCase;
import com.taskflow.app.domain.usecase.GetAllChatsUseCase;
import com.taskflow.app.domain.usecase.GetChatMessagesUseCase;
import com.taskflow.app.domain.usecase.GetGroupedTasksUseCase;
import com.taskflow.app.domain.usecase.GetTaskByIdUseCase;
import com.taskflow.app.domain.usecase.IncrementPomodoroUseCase;
import com.taskflow.app.domain.usecase.SearchTasksUseCase;
import com.taskflow.app.domain.usecase.SendChatMessageUseCase;
import com.taskflow.app.domain.usecase.ToggleTaskUseCase;
import com.taskflow.app.domain.usecase.UpdateTaskUseCase;
import com.taskflow.app.ui.screen.chat.ChatDetailViewModel;
import com.taskflow.app.ui.screen.chat.ChatDetailViewModel_HiltModules;
import com.taskflow.app.ui.screen.chat.ChatListViewModel;
import com.taskflow.app.ui.screen.chat.ChatListViewModel_HiltModules;
import com.taskflow.app.ui.screen.home.HomeViewModel;
import com.taskflow.app.ui.screen.home.HomeViewModel_HiltModules;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerTaskFlowApplication_HiltComponents_SingletonC {
  private DaggerTaskFlowApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public TaskFlowApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements TaskFlowApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements TaskFlowApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements TaskFlowApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements TaskFlowApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements TaskFlowApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements TaskFlowApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements TaskFlowApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public TaskFlowApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends TaskFlowApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends TaskFlowApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends TaskFlowApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends TaskFlowApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(3).put(LazyClassKeyProvider.com_taskflow_app_ui_screen_chat_ChatDetailViewModel, ChatDetailViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_taskflow_app_ui_screen_chat_ChatListViewModel, ChatListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_taskflow_app_ui_screen_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @CanIgnoreReturnValue
    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesRepository(instance, singletonCImpl.bindPreferencesRepositoryProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_taskflow_app_ui_screen_chat_ChatListViewModel = "com.taskflow.app.ui.screen.chat.ChatListViewModel";

      static String com_taskflow_app_ui_screen_home_HomeViewModel = "com.taskflow.app.ui.screen.home.HomeViewModel";

      static String com_taskflow_app_ui_screen_chat_ChatDetailViewModel = "com.taskflow.app.ui.screen.chat.ChatDetailViewModel";

      @KeepFieldType
      ChatListViewModel com_taskflow_app_ui_screen_chat_ChatListViewModel2;

      @KeepFieldType
      HomeViewModel com_taskflow_app_ui_screen_home_HomeViewModel2;

      @KeepFieldType
      ChatDetailViewModel com_taskflow_app_ui_screen_chat_ChatDetailViewModel2;
    }
  }

  private static final class ViewModelCImpl extends TaskFlowApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<ChatDetailViewModel> chatDetailViewModelProvider;

    private Provider<ChatListViewModel> chatListViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private GetChatMessagesUseCase getChatMessagesUseCase() {
      return new GetChatMessagesUseCase(singletonCImpl.bindChatRepositoryProvider.get());
    }

    private SendChatMessageUseCase sendChatMessageUseCase() {
      return new SendChatMessageUseCase(singletonCImpl.bindChatRepositoryProvider.get());
    }

    private CreateChatUseCase createChatUseCase() {
      return new CreateChatUseCase(singletonCImpl.bindChatRepositoryProvider.get());
    }

    private GetAllChatsUseCase getAllChatsUseCase() {
      return new GetAllChatsUseCase(singletonCImpl.bindChatRepositoryProvider.get());
    }

    private DeleteChatUseCase deleteChatUseCase() {
      return new DeleteChatUseCase(singletonCImpl.bindChatRepositoryProvider.get());
    }

    private GetGroupedTasksUseCase getGroupedTasksUseCase() {
      return new GetGroupedTasksUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private ToggleTaskUseCase toggleTaskUseCase() {
      return new ToggleTaskUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private AddTaskUseCase addTaskUseCase() {
      return new AddTaskUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private AddTasksUseCase addTasksUseCase() {
      return new AddTasksUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private UpdateTaskUseCase updateTaskUseCase() {
      return new UpdateTaskUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private DeleteTaskUseCase deleteTaskUseCase() {
      return new DeleteTaskUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private SearchTasksUseCase searchTasksUseCase() {
      return new SearchTasksUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private GetTaskByIdUseCase getTaskByIdUseCase() {
      return new GetTaskByIdUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    private IncrementPomodoroUseCase incrementPomodoroUseCase() {
      return new IncrementPomodoroUseCase(singletonCImpl.bindTaskRepositoryProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.chatDetailViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.chatListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(3).put(LazyClassKeyProvider.com_taskflow_app_ui_screen_chat_ChatDetailViewModel, ((Provider) chatDetailViewModelProvider)).put(LazyClassKeyProvider.com_taskflow_app_ui_screen_chat_ChatListViewModel, ((Provider) chatListViewModelProvider)).put(LazyClassKeyProvider.com_taskflow_app_ui_screen_home_HomeViewModel, ((Provider) homeViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_taskflow_app_ui_screen_chat_ChatDetailViewModel = "com.taskflow.app.ui.screen.chat.ChatDetailViewModel";

      static String com_taskflow_app_ui_screen_home_HomeViewModel = "com.taskflow.app.ui.screen.home.HomeViewModel";

      static String com_taskflow_app_ui_screen_chat_ChatListViewModel = "com.taskflow.app.ui.screen.chat.ChatListViewModel";

      @KeepFieldType
      ChatDetailViewModel com_taskflow_app_ui_screen_chat_ChatDetailViewModel2;

      @KeepFieldType
      HomeViewModel com_taskflow_app_ui_screen_home_HomeViewModel2;

      @KeepFieldType
      ChatListViewModel com_taskflow_app_ui_screen_chat_ChatListViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.taskflow.app.ui.screen.chat.ChatDetailViewModel 
          return (T) new ChatDetailViewModel(viewModelCImpl.getChatMessagesUseCase(), viewModelCImpl.sendChatMessageUseCase(), viewModelCImpl.createChatUseCase());

          case 1: // com.taskflow.app.ui.screen.chat.ChatListViewModel 
          return (T) new ChatListViewModel(viewModelCImpl.getAllChatsUseCase(), viewModelCImpl.createChatUseCase(), viewModelCImpl.deleteChatUseCase());

          case 2: // com.taskflow.app.ui.screen.home.HomeViewModel 
          return (T) new HomeViewModel(viewModelCImpl.getGroupedTasksUseCase(), viewModelCImpl.toggleTaskUseCase(), viewModelCImpl.addTaskUseCase(), viewModelCImpl.addTasksUseCase(), viewModelCImpl.updateTaskUseCase(), viewModelCImpl.deleteTaskUseCase(), viewModelCImpl.searchTasksUseCase(), viewModelCImpl.getTaskByIdUseCase(), viewModelCImpl.incrementPomodoroUseCase(), singletonCImpl.bindPreferencesRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends TaskFlowApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends TaskFlowApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends TaskFlowApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<PreferencesRepositoryImpl> preferencesRepositoryImplProvider;

    private Provider<PreferencesRepository> bindPreferencesRepositoryProvider;

    private Provider<TaskDatabase> provideDatabaseProvider;

    private Provider<AiService> provideAiServiceProvider;

    private Provider<ChatRepositoryImpl> chatRepositoryImplProvider;

    private Provider<ChatRepository> bindChatRepositoryProvider;

    private Provider<TaskRepositoryImpl> taskRepositoryImplProvider;

    private Provider<TaskRepository> bindTaskRepositoryProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private ChatDao chatDao() {
      return DatabaseModule_ProvideChatDaoFactory.provideChatDao(provideDatabaseProvider.get());
    }

    private TaskDao taskDao() {
      return DatabaseModule_ProvideTaskDaoFactory.provideTaskDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.preferencesRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 0);
      this.bindPreferencesRepositoryProvider = DoubleCheck.provider((Provider) preferencesRepositoryImplProvider);
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<TaskDatabase>(singletonCImpl, 2));
      this.provideAiServiceProvider = DoubleCheck.provider(new SwitchingProvider<AiService>(singletonCImpl, 3));
      this.chatRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 1);
      this.bindChatRepositoryProvider = DoubleCheck.provider((Provider) chatRepositoryImplProvider);
      this.taskRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 4);
      this.bindTaskRepositoryProvider = DoubleCheck.provider((Provider) taskRepositoryImplProvider);
    }

    @Override
    public void injectTaskFlowApplication(TaskFlowApplication taskFlowApplication) {
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.taskflow.app.data.repository.PreferencesRepositoryImpl 
          return (T) new PreferencesRepositoryImpl(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 1: // com.taskflow.app.data.repository.ChatRepositoryImpl 
          return (T) new ChatRepositoryImpl(singletonCImpl.chatDao(), singletonCImpl.provideAiServiceProvider.get(), singletonCImpl.bindPreferencesRepositoryProvider.get());

          case 2: // com.taskflow.app.data.local.TaskDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.taskflow.app.domain.service.AiService 
          return (T) RepositoryModule_Companion_ProvideAiServiceFactory.provideAiService();

          case 4: // com.taskflow.app.data.repository.TaskRepositoryImpl 
          return (T) new TaskRepositoryImpl(singletonCImpl.taskDao());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
