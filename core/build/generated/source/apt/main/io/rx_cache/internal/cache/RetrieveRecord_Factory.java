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
public final class RetrieveRecord_Factory implements Factory<RetrieveRecord> {
  private final MembersInjector<RetrieveRecord> retrieveRecordMembersInjector;

  private final Provider<Memory> memoryProvider;

  private final Provider<Persistence> persistenceProvider;

  private final Provider<EvictRecord> evictRecordProvider;

  private final Provider<HasRecordExpired> hasRecordExpiredProvider;

  private final Provider<String> encryptKeyProvider;

  public RetrieveRecord_Factory(
      MembersInjector<RetrieveRecord> retrieveRecordMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<EvictRecord> evictRecordProvider,
      Provider<HasRecordExpired> hasRecordExpiredProvider,
      Provider<String> encryptKeyProvider) {
    assert retrieveRecordMembersInjector != null;
    this.retrieveRecordMembersInjector = retrieveRecordMembersInjector;
    assert memoryProvider != null;
    this.memoryProvider = memoryProvider;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert evictRecordProvider != null;
    this.evictRecordProvider = evictRecordProvider;
    assert hasRecordExpiredProvider != null;
    this.hasRecordExpiredProvider = hasRecordExpiredProvider;
    assert encryptKeyProvider != null;
    this.encryptKeyProvider = encryptKeyProvider;
  }

  @Override
  public RetrieveRecord get() {
    return MembersInjectors.injectMembers(
        retrieveRecordMembersInjector,
        new RetrieveRecord(
            memoryProvider.get(),
            persistenceProvider.get(),
            evictRecordProvider.get(),
            hasRecordExpiredProvider.get(),
            encryptKeyProvider.get()));
  }

  public static Factory<RetrieveRecord> create(
      MembersInjector<RetrieveRecord> retrieveRecordMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider,
      Provider<EvictRecord> evictRecordProvider,
      Provider<HasRecordExpired> hasRecordExpiredProvider,
      Provider<String> encryptKeyProvider) {
    return new RetrieveRecord_Factory(
        retrieveRecordMembersInjector,
        memoryProvider,
        persistenceProvider,
        evictRecordProvider,
        hasRecordExpiredProvider,
        encryptKeyProvider);
  }
}
