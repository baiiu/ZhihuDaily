package io.rx_cache.internal;

import dagger.internal.DoubleCheck;
import dagger.internal.MembersInjectors;
import dagger.internal.Preconditions;
import io.rx_cache.MigrationCache;
import io.rx_cache.internal.cache.EvictExpirableRecordsPersistence;
import io.rx_cache.internal.cache.EvictExpirableRecordsPersistence_Factory;
import io.rx_cache.internal.cache.EvictExpiredRecordsPersistence;
import io.rx_cache.internal.cache.EvictExpiredRecordsPersistence_Factory;
import io.rx_cache.internal.cache.EvictRecord;
import io.rx_cache.internal.cache.EvictRecord_Factory;
import io.rx_cache.internal.cache.GetDeepCopy;
import io.rx_cache.internal.cache.GetDeepCopy_Factory;
import io.rx_cache.internal.cache.HasRecordExpired_Factory;
import io.rx_cache.internal.cache.RetrieveRecord;
import io.rx_cache.internal.cache.RetrieveRecord_Factory;
import io.rx_cache.internal.cache.SaveRecord;
import io.rx_cache.internal.cache.SaveRecord_Factory;
import io.rx_cache.internal.cache.TwoLayersCache;
import io.rx_cache.internal.cache.TwoLayersCache_Factory;
import io.rx_cache.internal.encrypt.Encryptor;
import io.rx_cache.internal.encrypt.FileEncryptor;
import io.rx_cache.internal.encrypt.FileEncryptor_Factory;
import io.rx_cache.internal.migration.DoMigrations;
import io.rx_cache.internal.migration.DoMigrations_Factory;
import io.victoralbertos.jolyglot.JolyglotGenerics;
import java.io.File;
import java.util.List;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DaggerRxCacheComponent implements RxCacheComponent {
  private Provider<Memory> provideMemoryProvider;

  private Provider<File> provideCacheDirectoryProvider;

  private Provider<Encryptor> provideEncryptorProvider;

  private Provider<FileEncryptor> fileEncryptorProvider;

  private Provider<JolyglotGenerics> provideJolyglotProvider;

  private Provider<Disk> diskProvider;

  private Provider<Persistence> providePersistenceProvider;

  private Provider<EvictRecord> evictRecordProvider;

  private Provider<String> provideEncryptKeyProvider;

  private Provider<RetrieveRecord> retrieveRecordProvider;

  private Provider<Integer> maxMbPersistenceCacheProvider;

  private Provider<EvictExpirableRecordsPersistence> evictExpirableRecordsPersistenceProvider;

  private Provider<SaveRecord> saveRecordProvider;

  private Provider<TwoLayersCache> twoLayersCacheProvider;

  private Provider<Boolean> useExpiredDataIfLoaderNotAvailableProvider;

  private Provider<EvictExpiredRecordsPersistence> evictExpiredRecordsPersistenceProvider;

  private Provider<GetDeepCopy> getDeepCopyProvider;

  private Provider<List<MigrationCache>> provideMigrationsProvider;

  private Provider<DoMigrations> doMigrationsProvider;

  private Provider<ProcessorProvidersBehaviour> processorProvidersBehaviourProvider;

  private Provider<ProcessorProviders> provideProcessorProvidersProvider;

  private DaggerRxCacheComponent(Builder builder) {
    assert builder != null;
    initialize(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  @SuppressWarnings("unchecked")
  private void initialize(final Builder builder) {

    this.provideMemoryProvider =
        DoubleCheck.provider(RxCacheModule_ProvideMemoryFactory.create(builder.rxCacheModule));

    this.provideCacheDirectoryProvider =
        DoubleCheck.provider(
            RxCacheModule_ProvideCacheDirectoryFactory.create(builder.rxCacheModule));

    this.provideEncryptorProvider =
        DoubleCheck.provider(RxCacheModule_ProvideEncryptorFactory.create(builder.rxCacheModule));

    this.fileEncryptorProvider = FileEncryptor_Factory.create(provideEncryptorProvider);

    this.provideJolyglotProvider =
        DoubleCheck.provider(RxCacheModule_ProvideJolyglotFactory.create(builder.rxCacheModule));

    this.diskProvider =
        Disk_Factory.create(
            provideCacheDirectoryProvider, fileEncryptorProvider, provideJolyglotProvider);

    this.providePersistenceProvider =
        DoubleCheck.provider(
            RxCacheModule_ProvidePersistenceFactory.create(builder.rxCacheModule, diskProvider));

    this.evictRecordProvider =
        EvictRecord_Factory.create(
            MembersInjectors.<EvictRecord>noOp(),
            provideMemoryProvider,
            providePersistenceProvider);

    this.provideEncryptKeyProvider =
        DoubleCheck.provider(RxCacheModule_ProvideEncryptKeyFactory.create(builder.rxCacheModule));

    this.retrieveRecordProvider =
        RetrieveRecord_Factory.create(
            MembersInjectors.<RetrieveRecord>noOp(),
            provideMemoryProvider,
            providePersistenceProvider,
            evictRecordProvider,
            HasRecordExpired_Factory.create(),
            provideEncryptKeyProvider);

    this.maxMbPersistenceCacheProvider =
        DoubleCheck.provider(
            RxCacheModule_MaxMbPersistenceCacheFactory.create(builder.rxCacheModule));

    this.evictExpirableRecordsPersistenceProvider =
        DoubleCheck.provider(
            EvictExpirableRecordsPersistence_Factory.create(
                MembersInjectors.<EvictExpirableRecordsPersistence>noOp(),
                provideMemoryProvider,
                providePersistenceProvider,
                maxMbPersistenceCacheProvider,
                provideEncryptKeyProvider));

    this.saveRecordProvider =
        SaveRecord_Factory.create(
            MembersInjectors.<SaveRecord>noOp(),
            provideMemoryProvider,
            providePersistenceProvider,
            maxMbPersistenceCacheProvider,
            evictExpirableRecordsPersistenceProvider,
            provideEncryptKeyProvider);

    this.twoLayersCacheProvider =
        DoubleCheck.provider(
            TwoLayersCache_Factory.create(
                evictRecordProvider, retrieveRecordProvider, saveRecordProvider));

    this.useExpiredDataIfLoaderNotAvailableProvider =
        DoubleCheck.provider(
            RxCacheModule_UseExpiredDataIfLoaderNotAvailableFactory.create(builder.rxCacheModule));

    this.evictExpiredRecordsPersistenceProvider =
        DoubleCheck.provider(
            EvictExpiredRecordsPersistence_Factory.create(
                MembersInjectors.<EvictExpiredRecordsPersistence>noOp(),
                provideMemoryProvider,
                providePersistenceProvider,
                HasRecordExpired_Factory.create(),
                provideEncryptKeyProvider));

    this.getDeepCopyProvider =
        GetDeepCopy_Factory.create(
            MembersInjectors.<GetDeepCopy>noOp(),
            provideMemoryProvider,
            providePersistenceProvider,
            provideJolyglotProvider);

    this.provideMigrationsProvider =
        DoubleCheck.provider(RxCacheModule_ProvideMigrationsFactory.create(builder.rxCacheModule));

    this.doMigrationsProvider =
        DoMigrations_Factory.create(
            providePersistenceProvider, provideMigrationsProvider, provideEncryptKeyProvider);

    this.processorProvidersBehaviourProvider =
        ProcessorProvidersBehaviour_Factory.create(
            twoLayersCacheProvider,
            useExpiredDataIfLoaderNotAvailableProvider,
            evictExpiredRecordsPersistenceProvider,
            getDeepCopyProvider,
            doMigrationsProvider);

    this.provideProcessorProvidersProvider =
        RxCacheModule_ProvideProcessorProvidersFactory.create(
            builder.rxCacheModule, processorProvidersBehaviourProvider);
  }

  @Override
  public ProcessorProviders providers() {
    return provideProcessorProvidersProvider.get();
  }

  public static final class Builder {
    private RxCacheModule rxCacheModule;

    private Builder() {}

    public RxCacheComponent build() {
      if (rxCacheModule == null) {
        throw new IllegalStateException(RxCacheModule.class.getCanonicalName() + " must be set");
      }
      return new DaggerRxCacheComponent(this);
    }

    public Builder rxCacheModule(RxCacheModule rxCacheModule) {
      this.rxCacheModule = Preconditions.checkNotNull(rxCacheModule);
      return this;
    }
  }
}
