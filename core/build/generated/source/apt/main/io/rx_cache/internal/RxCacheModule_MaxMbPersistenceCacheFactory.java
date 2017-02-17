package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_MaxMbPersistenceCacheFactory implements Factory<Integer> {
  private final RxCacheModule module;

  public RxCacheModule_MaxMbPersistenceCacheFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public Integer get() {
    return Preconditions.checkNotNull(
        module.maxMbPersistenceCache(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Integer> create(RxCacheModule module) {
    return new RxCacheModule_MaxMbPersistenceCacheFactory(module);
  }

  /** Proxies {@link RxCacheModule#maxMbPersistenceCache()}. */
  public static Integer proxyMaxMbPersistenceCache(RxCacheModule instance) {
    return instance.maxMbPersistenceCache();
  }
}
