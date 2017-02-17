package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.rx_cache.internal.encrypt.Encryptor;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideEncryptorFactory implements Factory<Encryptor> {
  private final RxCacheModule module;

  public RxCacheModule_ProvideEncryptorFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public Encryptor get() {
    return Preconditions.checkNotNull(
        module.provideEncryptor(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<Encryptor> create(RxCacheModule module) {
    return new RxCacheModule_ProvideEncryptorFactory(module);
  }

  /** Proxies {@link RxCacheModule#provideEncryptor()}. */
  public static Encryptor proxyProvideEncryptor(RxCacheModule instance) {
    return instance.provideEncryptor();
  }
}
