package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvidePersistenceFactory implements Factory<Persistence> {
  private final RxCacheModule module;

  private final Provider<Disk> diskProvider;

  public RxCacheModule_ProvidePersistenceFactory(
      RxCacheModule module, Provider<Disk> diskProvider) {
    assert module != null;
    this.module = module;
    assert diskProvider != null;
    this.diskProvider = diskProvider;
  }

  @Override
  public Persistence get() {
    return Preconditions.checkNotNull(
        module.providePersistence(diskProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Persistence> create(RxCacheModule module, Provider<Disk> diskProvider) {
    return new RxCacheModule_ProvidePersistenceFactory(module, diskProvider);
  }

  /** Proxies {@link RxCacheModule#providePersistence(Disk)}. */
  public static Persistence proxyProvidePersistence(RxCacheModule instance, Disk disk) {
    return instance.providePersistence(disk);
  }
}
