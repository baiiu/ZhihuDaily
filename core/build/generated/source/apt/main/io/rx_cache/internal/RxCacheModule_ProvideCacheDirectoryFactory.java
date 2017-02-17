package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.io.File;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideCacheDirectoryFactory implements Factory<File> {
  private final RxCacheModule module;

  public RxCacheModule_ProvideCacheDirectoryFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public File get() {
    return Preconditions.checkNotNull(
        module.provideCacheDirectory(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<File> create(RxCacheModule module) {
    return new RxCacheModule_ProvideCacheDirectoryFactory(module);
  }

  /** Proxies {@link RxCacheModule#provideCacheDirectory()}. */
  public static File proxyProvideCacheDirectory(RxCacheModule instance) {
    return instance.provideCacheDirectory();
  }
}
