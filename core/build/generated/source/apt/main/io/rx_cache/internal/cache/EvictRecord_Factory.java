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
public final class EvictRecord_Factory implements Factory<EvictRecord> {
  private final MembersInjector<EvictRecord> evictRecordMembersInjector;

  private final Provider<Memory> memoryProvider;

  private final Provider<Persistence> persistenceProvider;

  public EvictRecord_Factory(
      MembersInjector<EvictRecord> evictRecordMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider) {
    assert evictRecordMembersInjector != null;
    this.evictRecordMembersInjector = evictRecordMembersInjector;
    assert memoryProvider != null;
    this.memoryProvider = memoryProvider;
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
  }

  @Override
  public EvictRecord get() {
    return MembersInjectors.injectMembers(
        evictRecordMembersInjector,
        new EvictRecord(memoryProvider.get(), persistenceProvider.get()));
  }

  public static Factory<EvictRecord> create(
      MembersInjector<EvictRecord> evictRecordMembersInjector,
      Provider<Memory> memoryProvider,
      Provider<Persistence> persistenceProvider) {
    return new EvictRecord_Factory(evictRecordMembersInjector, memoryProvider, persistenceProvider);
  }
}
