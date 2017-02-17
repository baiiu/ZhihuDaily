package io.rx_cache.internal.encrypt;

import dagger.internal.Factory;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class FileEncryptor_Factory implements Factory<FileEncryptor> {
  private final Provider<Encryptor> encryptorProvider;

  public FileEncryptor_Factory(Provider<Encryptor> encryptorProvider) {
    assert encryptorProvider != null;
    this.encryptorProvider = encryptorProvider;
  }

  @Override
  public FileEncryptor get() {
    return new FileEncryptor(encryptorProvider.get());
  }

  public static Factory<FileEncryptor> create(Provider<Encryptor> encryptorProvider) {
    return new FileEncryptor_Factory(encryptorProvider);
  }
}
