package io.rx_cache.internal.cache;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class TwoLayersCache_Factory implements Factory<TwoLayersCache> {
  private final Provider<EvictRecord> evictRecordProvider;

  private final Provider<RetrieveRecord> retrieveRecordProvider;

  private final Provider<SaveRecord> saveRecordProvider;

  public TwoLayersCache_Factory(
      Provider<EvictRecord> evictRecordProvider,
      Provider<RetrieveRecord> retrieveRecordProvider,
      Provider<SaveRecord> saveRecordProvider) {
    assert evictRecordProvider != null;
    this.evictRecordProvider = evictRecordProvider;
    assert retrieveRecordProvider != null;
    this.retrieveRecordProvider = retrieveRecordProvider;
    assert saveRecordProvider != null;
    this.saveRecordProvider = saveRecordProvider;
  }

  @Override
  public TwoLayersCache get() {
    return new TwoLayersCache(
        evictRecordProvider.get(), retrieveRecordProvider.get(), saveRecordProvider.get());
  }

  public static Factory<TwoLayersCache> create(
      Provider<EvictRecord> evictRecordProvider,
      Provider<RetrieveRecord> retrieveRecordProvider,
      Provider<SaveRecord> saveRecordProvider) {
    return new TwoLayersCache_Factory(
        evictRecordProvider, retrieveRecordProvider, saveRecordProvider);
  }
}
