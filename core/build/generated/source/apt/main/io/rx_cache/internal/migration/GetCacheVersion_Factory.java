package io.rx_cache.internal.migration;

import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import io.rx_cache.internal.Persistence;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GetCacheVersion_Factory implements Factory<GetCacheVersion> {
  private final MembersInjector<GetCacheVersion> getCacheVersionMembersInjector;

  private final Provider<Persistence> persistenceProvider;

  public GetCacheVersion_Factory(
      MembersInjector<GetCacheVersion> getCacheVersionMembersInjector,
      Provider<Persistence> persistenceProvider) {
    assert getCacheVersionMembersInjector != null;
    this.getCacheVersionMembersInjector = getCacheVersionMembersInjector;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
  }

  @Override
  public GetCacheVersion get() {
    return MembersInjectors.injectMembers(
        getCacheVersionMembersInjector, new GetCacheVersion(persistenceProvider.get()));
  }

  public static Factory<GetCacheVersion> create(
      MembersInjector<GetCacheVersion> getCacheVersionMembersInjector,
      Provider<Persistence> persistenceProvider) {
    return new GetCacheVersion_Factory(getCacheVersionMembersInjector, persistenceProvider);
  }
}
