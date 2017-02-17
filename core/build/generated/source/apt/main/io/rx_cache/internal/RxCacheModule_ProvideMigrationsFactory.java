package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.rx_cache.MigrationCache;
import java.util.List;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideMigrationsFactory implements Factory<List<MigrationCache>> {
  private final RxCacheModule module;

  public RxCacheModule_ProvideMigrationsFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public List<MigrationCache> get() {
    return Preconditions.checkNotNull(
        module.provideMigrations(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<List<MigrationCache>> create(RxCacheModule module) {
    return new RxCacheModule_ProvideMigrationsFactory(module);
  }

  /** Proxies {@link RxCacheModule#provideMigrations()}. */
  public static List<MigrationCache> proxyProvideMigrations(RxCacheModule instance) {
    return instance.provideMigrations();
  }
}
