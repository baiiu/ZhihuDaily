package io.rx_cache.internal.migration;

import dagger.internal.Factory;
import io.rx_cache.MigrationCache;
import io.rx_cache.internal.Persistence;
import java.util.List;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DoMigrations_Factory implements Factory<DoMigrations> {
  private final Provider<Persistence> persistenceProvider;

  private final Provider<List<MigrationCache>> migrationsProvider;

  private final Provider<String> encryptKeyProvider;

  public DoMigrations_Factory(
      Provider<Persistence> persistenceProvider,
      Provider<List<MigrationCache>> migrationsProvider,
      Provider<String> encryptKeyProvider) {
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert migrationsProvider != null;
    this.migrationsProvider = migrationsProvider;
    assert encryptKeyProvider != null;
    this.encryptKeyProvider = encryptKeyProvider;
  }

  @Override
  public DoMigrations get() {
    return new DoMigrations(
        persistenceProvider.get(), migrationsProvider.get(), encryptKeyProvider.get());
  }

  public static Factory<DoMigrations> create(
      Provider<Persistence> persistenceProvider,
      Provider<List<MigrationCache>> migrationsProvider,
      Provider<String> encryptKeyProvider) {
    return new DoMigrations_Factory(persistenceProvider, migrationsProvider, encryptKeyProvider);
  }
}
