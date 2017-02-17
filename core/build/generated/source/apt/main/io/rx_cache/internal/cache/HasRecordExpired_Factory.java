package io.rx_cache.internal.cache;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class HasRecordExpired_Factory implements Factory<HasRecordExpired> {
  private static final HasRecordExpired_Factory INSTANCE = new HasRecordExpired_Factory();

  @Override
  public HasRecordExpired get() {
    return new HasRecordExpired();
  }

  public static Factory<HasRecordExpired> create() {
    return INSTANCE;
  }
}
