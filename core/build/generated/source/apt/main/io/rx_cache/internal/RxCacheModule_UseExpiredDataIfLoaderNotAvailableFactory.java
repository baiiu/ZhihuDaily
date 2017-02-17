package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_UseExpiredDataIfLoaderNotAvailableFactory
    implements Factory<Boolean> {
  private final RxCacheModule module;

  public RxCacheModule_UseExpiredDataIfLoaderNotAvailableFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public Boolean get() {
    return Preconditions.checkNotNull(
        module.useExpiredDataIfLoaderNotAvailable(),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Boolean> create(RxCacheModule module) {
    return new RxCacheModule_UseExpiredDataIfLoaderNotAvailableFactory(module);
  }

  /** Proxies {@link RxCacheModule#useExpiredDataIfLoaderNotAvailable()}. */
  public static Boolean proxyUseExpiredDataIfLoaderNotAvailable(RxCacheModule instance) {
    return instance.useExpiredDataIfLoaderNotAvailable();
  }
}
