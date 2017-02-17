package io.rx_cache.internal.migration;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GetClassesToEvictFromMigrations_Factory
    implements Factory<GetClassesToEvictFromMigrations> {
  private static final GetClassesToEvictFromMigrations_Factory INSTANCE =
      new GetClassesToEvictFromMigrations_Factory();

  @Override
  public GetClassesToEvictFromMigrations get() {
    return new GetClassesToEvictFromMigrations();
  }

  public static Factory<GetClassesToEvictFromMigrations> create() {
    return INSTANCE;
  }

  /** Proxies {@link GetClassesToEvictFromMigrations#GetClassesToEvictFromMigrations()}. */
  public static GetClassesToEvictFromMigrations newGetClassesToEvictFromMigrations() {
    return new GetClassesToEvictFromMigrations();
  }
}
