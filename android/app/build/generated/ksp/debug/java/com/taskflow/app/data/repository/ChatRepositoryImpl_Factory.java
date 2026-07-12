package com.taskflow.app.data.repository;

import com.taskflow.app.data.local.dao.ChatDao;
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

  public ChatRepositoryImpl_Factory(Provider<ChatDao> chatDaoProvider,
      Provider<AiService> aiServiceProvider) {
    this.chatDaoProvider = chatDaoProvider;
    this.aiServiceProvider = aiServiceProvider;
  }

  @Override
  public ChatRepositoryImpl get() {
    return newInstance(chatDaoProvider.get(), aiServiceProvider.get());
  }

  public static ChatRepositoryImpl_Factory create(Provider<ChatDao> chatDaoProvider,
      Provider<AiService> aiServiceProvider) {
    return new ChatRepositoryImpl_Factory(chatDaoProvider, aiServiceProvider);
  }

  public static ChatRepositoryImpl newInstance(ChatDao chatDao, AiService aiService) {
    return new ChatRepositoryImpl(chatDao, aiService);
  }
}
