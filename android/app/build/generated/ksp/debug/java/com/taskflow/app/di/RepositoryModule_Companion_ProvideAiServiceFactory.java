package com.taskflow.app.di;

import com.taskflow.app.domain.service.AiService;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RepositoryModule_Companion_ProvideAiServiceFactory implements Factory<AiService> {
  @Override
  public AiService get() {
    return provideAiService();
  }

  public static RepositoryModule_Companion_ProvideAiServiceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static AiService provideAiService() {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.Companion.provideAiService());
  }

  private static final class InstanceHolder {
    private static final RepositoryModule_Companion_ProvideAiServiceFactory INSTANCE = new RepositoryModule_Companion_ProvideAiServiceFactory();
  }
}
