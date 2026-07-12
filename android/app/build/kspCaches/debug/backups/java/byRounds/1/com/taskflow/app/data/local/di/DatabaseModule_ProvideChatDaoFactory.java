package com.taskflow.app.data.local.di;

import com.taskflow.app.data.local.TaskDatabase;
import com.taskflow.app.data.local.dao.ChatDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideChatDaoFactory implements Factory<ChatDao> {
  private final Provider<TaskDatabase> databaseProvider;

  public DatabaseModule_ProvideChatDaoFactory(Provider<TaskDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public ChatDao get() {
    return provideChatDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideChatDaoFactory create(
      Provider<TaskDatabase> databaseProvider) {
    return new DatabaseModule_ProvideChatDaoFactory(databaseProvider);
  }

  public static ChatDao provideChatDao(TaskDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideChatDao(database));
  }
}
