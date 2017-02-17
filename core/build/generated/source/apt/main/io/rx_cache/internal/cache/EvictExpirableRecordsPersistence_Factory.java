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
public final class EvictExpirableRecordsPersistence_Factory
    implements Factory<EvictExpirableRecordsPersistence> {
  private final MembersInjector<EvictExpirableRecordsPersistence>
      evictExpirableRecordsPersistenceMembersInjector;

  private final Provider<Memory> memoryProvider;

  private final Provider<Persistence> persistenceProvider;

  private final Provider<Integer> maxMgPersistenceCacheProvider;

  private final Provider<String> encryptKeyProvider;

  public EvictExpirableRecordsPersistence_Factory(
      MembersInjector<EvictExpirableRecordsPersistence>
          evictExpirableRecordsPersistenceMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<Integer> maxMgPersistenceCacheProvider,
      Provider<String> encryptKeyProvider) {
    assert evictExpirableRecordsPersistenceMembersInjector != null;
    this.evictExpirableRecordsPersistenceMembersInjector =
        evictExpirableRecordsPersistenceMembersInjector;
    assert memoryProvider != null;
    this.memoryProvider = memoryProvider;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert maxMgPersistenceCacheProvider != null;
    this.maxMgPersistenceCacheProvider = maxMgPersistenceCacheProvider;
    assert encryptKeyProvider != null;
    this.encryptKeyProvider = encryptKeyProvider;
  }

  @Override
  public EvictExpirableRecordsPersistence get() {
    return MembersInjectors.injectMembers(
        evictExpirableRecordsPersistenceMembersInjector,
        new EvictExpirableRecordsPersistence(
            memoryProvider.get(),
            persistenceProvider.get(),
            maxMgPersistenceCacheProvider.get(),
            encryptKeyProvider.get()));
  }

  public static Factory<EvictExpirableRecordsPersistence> create(
      MembersInjector<EvictExpirableRecordsPersistence>
          evictExpirableRecordsPersistenceMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<Integer> maxMgPersistenceCacheProvider,
      Provider<String> encryptKeyProvider) {
    return new EvictExpirableRecordsPersistence_Factory(
        evictExpirableRecordsPersistenceMembersInjector,
        memoryProvider,
        persistenceProvider,
        maxMgPersistenceCacheProvider,
        encryptKeyProvider);
  }
}
