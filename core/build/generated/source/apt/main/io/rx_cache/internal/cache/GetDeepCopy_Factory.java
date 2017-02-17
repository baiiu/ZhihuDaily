package io.rx_cache.internal.cache;

import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import io.rx_cache.internal.Memory;
import io.rx_cache.internal.Persistence;
import io.victoralbertos.jolyglot.JolyglotGenerics;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GetDeepCopy_Factory implements Factory<GetDeepCopy> {
  private final MembersInjector<GetDeepCopy> getDeepCopyMembersInjector;

  private final Provider<Memory> memoryProvider;

  private final Provider<Persistence> persistenceProvider;

  private final Provider<JolyglotGenerics> jolyglotProvider;

  public GetDeepCopy_Factory(
      MembersInjector<GetDeepCopy> getDeepCopyMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<JolyglotGenerics> jolyglotProvider) {
    assert getDeepCopyMembersInjector != null;
    this.getDeepCopyMembersInjector = getDeepCopyMembersInjector;
    assert memoryProvider != null;
    this.memoryProvider = memoryProvider;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert jolyglotProvider != null;
    this.jolyglotProvider = jolyglotProvider;
  }

  @Override
  public GetDeepCopy get() {
    return MembersInjectors.injectMembers(
        getDeepCopyMembersInjector,
        new GetDeepCopy(memoryProvider.get(), persistenceProvider.get(), jolyglotProvider.get()));
  }

  public static Factory<GetDeepCopy> create(
      MembersInjector<GetDeepCopy> getDeepCopyMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<JolyglotGenerics> jolyglotProvider) {
    return new GetDeepCopy_Factory(
        getDeepCopyMembersInjector, memoryProvider, persistenceProvider, jolyglotProvider);
  }
}
