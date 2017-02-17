package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideMemoryFactory implements Factory<Memory> {
  private final RxCacheModule module;

  public RxCacheModule_ProvideMemoryFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public Memory get() {
    return Preconditions.checkNotNull(
        module.provideMemory(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Memory> create(RxCacheModule module) {
    return new RxCacheModule_ProvideMemoryFactory(module);
  }

  /** Proxies {@link RxCacheModule#provideMemory()}. */
  public static Memory proxyProvideMemory(RxCacheModule instance) {
    return instance.provideMemory();
  }
}
