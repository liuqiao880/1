package com.taskflow.app.data.repository;

import com.taskflow.app.data.local.dao.ChatDao;
import com.taskflow.app.domain.repository.PreferencesRepository;
import com.taskflow.app.domain.service.AiService;
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
public final class ChatRepositoryImpl_Factory implements Factory<ChatRepositoryImpl> {
  private final Provider<ChatDao> chatDaoProvider;

  private final Provider<AiService> aiServiceProvider;

  private final Provider<PreferencesRepository> preferencesRepositoryProvider;

  public ChatRepositoryImpl_Factory(Provider<ChatDao> chatDaoProvider,
      Provider<AiService> aiServiceProvider,
      Provider<PreferencesRepository> preferencesRepositoryProvider) {
    this.chatDaoProvider = chatDaoProvider;
    this.aiServiceProvider = aiServiceProvider;
    this.preferencesRepositoryProvider = preferencesRepositoryProvider;
  }

  @Override
  public ChatRepositoryImpl get() {
    return newInstance(chatDaoProvider.get(), aiServiceProvider.get(), preferencesRepositoryProvider.get());
  }

  public static ChatRepositoryImpl_Factory create(Provider<ChatDao> chatDaoProvider,
      Provider<AiService> aiServiceProvider,
      Provider<PreferencesRepository> preferencesRepositoryProvider) {
    return new ChatRepositoryImpl_Factory(chatDaoProvider, aiServiceProvider, preferencesRepositoryProvider);
  }

  public static ChatRepositoryImpl newInstance(ChatDao chatDao, AiService aiService,
      PreferencesRepository preferencesRepository) {
    return new ChatRepositoryImpl(chatDao, aiService, preferencesRepository);
  }
}
