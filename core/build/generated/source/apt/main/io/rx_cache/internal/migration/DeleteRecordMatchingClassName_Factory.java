package io.rx_cache.internal.migration;

import dagger.internal.Factory;
import io.rx_cache.internal.Persistence;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class DeleteRecordMatchingClassName_Factory
    implements Factory<DeleteRecordMatchingClassName> {
  private final Provider<Persistence> persistenceProvider;

  private final Provider<String> encryptKeyProvider;

  public DeleteRecordMatchingClassName_Factory(
      Provider<Persistence> persistenceProvider, Provider<String> encryptKeyProvider) {
    assert persistenceProvider != null;
    this.persistenceProvider = persistenceProvider;
    assert encryptKeyProvider != null;
    this.encryptKeyProvider = encryptKeyProvider;
  }

  @Override
  public DeleteRecordMatchingClassName get() {
    return new DeleteRecordMatchingClassName(persistenceProvider.get(), encryptKeyProvider.get());
  }

  public static Factory<DeleteRecordMatchingClassName> create(
      Provider<Persistence> persistenceProvider, Provider<String> encryptKeyProvider) {
    return new DeleteRecordMatchingClassName_Factory(persistenceProvider, encryptKeyProvider);
  }
}
