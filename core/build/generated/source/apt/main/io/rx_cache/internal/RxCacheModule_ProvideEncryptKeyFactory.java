package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideEncryptKeyFactory implements Factory<String> {
  private final RxCacheModule module;

  public RxCacheModule_ProvideEncryptKeyFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public String get() {
    return Preconditions.checkNotNull(
        module.provideEncryptKey(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<String> create(RxCacheModule module) {
    return new RxCacheModule_ProvideEncryptKeyFactory(module);
  }

  /** Proxies {@link RxCacheModule#provideEncryptKey()}. */
  public static String proxyProvideEncryptKey(RxCacheModule instance) {
    return instance.provideEncryptKey();
  }
}
