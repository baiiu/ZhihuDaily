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
public final class UpgradeCacheVersion_Factory implements Factory<UpgradeCacheVersion> {
  private final MembersInjector<UpgradeCacheVersion> upgradeCacheVersionMembersInjector;

  private final Provider<Persistence> persistenceProvider;

  public UpgradeCacheVersion_Factory(
      MembersInjector<UpgradeCacheVersion> upgradeCacheVersionMembersInjector,
      Provider<Persistence> persistenceProvider) {
    assert upgradeCacheVersionMembersInjector != null;
    this.upgradeCacheVersionMembersInjector = upgradeCacheVersionMembersInjector;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
  }

  @Override
  public UpgradeCacheVersion get() {
    return MembersInjectors.injectMembers(
        upgradeCacheVersionMembersInjector, new UpgradeCacheVersion(persistenceProvider.get()));
  }

  public static Factory<UpgradeCacheVersion> create(
      MembersInjector<UpgradeCacheVersion> upgradeCacheVersionMembersInjector,
      Provider<Persistence> persistenceProvider) {
    return new UpgradeCacheVersion_Factory(upgradeCacheVersionMembersInjector, persistenceProvider);
  }
}
