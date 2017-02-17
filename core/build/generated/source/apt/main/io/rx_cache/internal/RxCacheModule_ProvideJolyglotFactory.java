package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import io.victoralbertos.jolyglot.JolyglotGenerics;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideJolyglotFactory implements Factory<JolyglotGenerics> {
  private final RxCacheModule module;

  public RxCacheModule_ProvideJolyglotFactory(RxCacheModule module) {
    assert module != null;
    this.module = module;
  }

  @Override
  public JolyglotGenerics get() {
    return Preconditions.checkNotNull(
        module.provideJolyglot(), "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<JolyglotGenerics> create(RxCacheModule module) {
    return new RxCacheModule_ProvideJolyglotFactory(module);
  }

  /** Proxies {@link RxCacheModule#provideJolyglot()}. */
  public static JolyglotGenerics proxyProvideJolyglot(RxCacheModule instance) {
    return instance.provideJolyglot();
  }
}
