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
public final class SaveRecord_Factory implements Factory<SaveRecord> {
  private final MembersInjector<SaveRecord> saveRecordMembersInjector;

  private final Provider<Memory> memoryProvider;

  private final Provider<Persistence> persistenceProvider;

  private final Provider<Integer> maxMgPersistenceCacheProvider;

  private final Provider<EvictExpirableRecordsPersistence> evictExpirableRecordsPersistenceProvider;

  private final Provider<String> encryptKeyProvider;

  public SaveRecord_Factory(
      MembersInjector<SaveRecord> saveRecordMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<Integer> maxMgPersistenceCacheProvider,
      Provider<EvictExpirableRecordsPersistence> evictExpirableRecordsPersistenceProvider,
      Provider<String> encryptKeyProvider) {
    assert saveRecordMembersInjector != null;
    this.saveRecordMembersInjector = saveRecordMembersInjector;
    assert memoryProvider != null;
    this.memoryProvider = memoryProvider;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert maxMgPersistenceCacheProvider != null;
    this.maxMgPersistenceCacheProvider = maxMgPersistenceCacheProvider;
    assert evictExpirableRecordsPersistenceProvider != null;
    this.evictExpirableRecordsPersistenceProvider = evictExpirableRecordsPersistenceProvider;
    assert encryptKeyProvider != null;
    this.encryptKeyProvider = encryptKeyProvider;
  }

  @Override
  public SaveRecord get() {
    return MembersInjectors.injectMembers(
        saveRecordMembersInjector,
        new SaveRecord(
            memoryProvider.get(),
            persistenceProvider.get(),
            maxMgPersistenceCacheProvider.get(),
            evictExpirableRecordsPersistenceProvider.get(),
            encryptKeyProvider.get()));
  }

  public static Factory<SaveRecord> create(
      MembersInjector<SaveRecord> saveRecordMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<Integer> maxMgPersistenceCacheProvider,
      Provider<EvictExpirableRecordsPersistence> evictExpirableRecordsPersistenceProvider,
      Provider<String> encryptKeyProvider) {
    return new SaveRecord_Factory(
        saveRecordMembersInjector,
        memoryProvider,
        persistenceProvider,
        maxMgPersistenceCacheProvider,
        evictExpirableRecordsPersistenceProvider,
        encryptKeyProvider);
  }
}
