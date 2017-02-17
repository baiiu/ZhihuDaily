package io.rx_cache.internal;

import dagger.internal.Factory;
import io.rx_cache.internal.cache.EvictExpiredRecordsPersistence;
import io.rx_cache.internal.cache.GetDeepCopy;
import io.rx_cache.internal.cache.TwoLayersCache;
import io.rx_cache.internal.migration.DoMigrations;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class ProcessorProvidersBehaviour_Factory
    implements Factory<ProcessorProvidersBehaviour> {
  private final Provider<TwoLayersCache> twoLayersCacheProvider;

  private final Provider<Boolean> useExpiredDataIfLoaderNotAvailableProvider;

  private final Provider<EvictExpiredRecordsPersistence> evictExpiredRecordsPersistenceProvider;

  private final Provider<GetDeepCopy> getDeepCopyProvider;

  private final Provider<DoMigrations> doMigrationsProvider;

  public ProcessorProvidersBehaviour_Factory(
      Provider<TwoLayersCache> twoLayersCacheProvider,
      Provider<Boolean> useExpiredDataIfLoaderNotAvailableProvider,
      Provider<EvictExpiredRecordsPersistence> evictExpiredRecordsPersistenceProvider,
      Provider<GetDeepCopy> getDeepCopyProvider,
      Provider<DoMigrations> doMigrationsProvider) {
    assert twoLayersCacheProvider != null;
    this.twoLayersCacheProvider = twoLayersCacheProvider;
    assert useExpiredDataIfLoaderNotAvailableProvider != null;
    this.useExpiredDataIfLoaderNotAvailableProvider = useExpiredDataIfLoaderNotAvailableProvider;
    assert evictExpiredRecordsPersistenceProvider != null;
    this.evictExpiredRecordsPersistenceProvider = evictExpiredRecordsPersistenceProvider;
    assert getDeepCopyProvider != null;
    this.getDeepCopyProvider = getDeepCopyProvider;
    assert doMigrationsProvider != null;
    this.doMigrationsProvider = doMigrationsProvider;
  }

  @Override
  public ProcessorProvidersBehaviour get() {
    return new ProcessorProvidersBehaviour(
        twoLayersCacheProvider.get(),
        useExpiredDataIfLoaderNotAvailableProvider.get(),
        evictExpiredRecordsPersistenceProvider.get(),
        getDeepCopyProvider.get(),
        doMigrationsProvider.get());
  }

  public static Factory<ProcessorProvidersBehaviour> create(
      Provider<TwoLayersCache> twoLayersCacheProvider,
      Provider<Boolean> useExpiredDataIfLoaderNotAvailableProvider,
      Provider<EvictExpiredRecordsPersistence> evictExpiredRecordsPersistenceProvider,
      Provider<GetDeepCopy> getDeepCopyProvider,
      Provider<DoMigrations> doMigrationsProvider) {
    return new ProcessorProvidersBehaviour_Factory(
        twoLayersCacheProvider,
        useExpiredDataIfLoaderNotAvailableProvider,
        evictExpiredRecordsPersistenceProvider,
        getDeepCopyProvider,
        doMigrationsProvider);
  }
}
