package io.rx_cache.internal.cache;

import dagger.MembersInjector;
import dagger.internal.Factory;
import dagger.internal.MembersInjectors;
import io.rx_cache.internal.Memory;
import io.rx_cache.internal.Persistence;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class EvictExpiredRecordsPersistence_Factory
    implements Factory<EvictExpiredRecordsPersistence> {
  private final MembersInjector<EvictExpiredRecordsPersistence>
      evictExpiredRecordsPersistenceMembersInjector;

  private final Provider<Memory> memoryProvider;

  private final Provider<Persistence> persistenceProvider;

  private final Provider<HasRecordExpired> hasRecordExpiredProvider;

  private final Provider<String> encryptKeyProvider;

  public EvictExpiredRecordsPersistence_Factory(
      MembersInjector<EvictExpiredRecordsPersistence> evictExpiredRecordsPersistenceMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<HasRecordExpired> hasRecordExpiredProvider,
      Provider<String> encryptKeyProvider) {
    assert evictExpiredRecordsPersistenceMembersInjector != null;
    this.evictExpiredRecordsPersistenceMembersInjector =
        evictExpiredRecordsPersistenceMembersInjector;
    assert memoryProvider != null;
    this.memoryProvider = memoryProvider;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert hasRecordExpiredProvider != null;
    this.hasRecordExpiredProvider = hasRecordExpiredProvider;
    assert encryptKeyProvider != null;
    this.encryptKeyProvider = encryptKeyProvider;
  }

  @Override
  public EvictExpiredRecordsPersistence get() {
    return MembersInjectors.injectMembers(
        evictExpiredRecordsPersistenceMembersInjector,
        new EvictExpiredRecordsPersistence(
            memoryProvider.get(),
            persistenceProvider.get(),
            hasRecordExpiredProvider.get(),
            encryptKeyProvider.get()));
  }

  public static Factory<EvictExpiredRecordsPersistence> create(
      MembersInjector<EvictExpiredRecordsPersistence> evictExpiredRecordsPersistenceMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<HasRecordExpired> hasRecordExpiredProvider,
      Provider<String> encryptKeyProvider) {
    return new EvictExpiredRecordsPersistence_Factory(
        evictExpiredRecordsPersistenceMembersInjector,
        memoryProvider,
        persistenceProvider,
        hasRecordExpiredProvider,
        encryptKeyProvider);
  }
}
