package io.rx_cache.internal;

import dagger.internal.Factory;
import io.rx_cache.internal.encrypt.FileEncryptor;
import io.victoralbertos.jolyglot.JolyglotGenerics;
import java.io.File;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class Disk_Factory implements Factory<Disk> {
  private final Provider<File> cacheDirectoryProvider;

  private final Provider<FileEncryptor> fileEncryptorProvider;

  private final Provider<JolyglotGenerics> jolyglotProvider;

  public Disk_Factory(
      Provider<File> cacheDirectoryProvider,
      Provider<FileEncryptor> fileEncryptorProvider,
      Provider<JolyglotGenerics> jolyglotProvider) {
    assert cacheDirectoryProvider != null;
    this.cacheDirectoryProvider = cacheDirectoryProvider;
    assert fileEncryptorProvider != null;
    this.fileEncryptorProvider = fileEncryptorProvider;
    assert jolyglotProvider != null;
    this.jolyglotProvider = jolyglotProvider;
  }

  @Override
  public Disk get() {
    return new Disk(
        cacheDirectoryProvider.get(), fileEncryptorProvider.get(), jolyglotProvider.get());
  }

  public static Factory<Disk> create(
      Provider<File> cacheDirectoryProvider,
      Provider<FileEncryptor> fileEncryptorProvider,
      Provider<JolyglotGenerics> jolyglotProvider) {
    return new Disk_Factory(cacheDirectoryProvider, fileEncryptorProvider, jolyglotProvider);
  }
}
