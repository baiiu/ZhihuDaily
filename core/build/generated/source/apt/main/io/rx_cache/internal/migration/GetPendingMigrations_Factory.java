package io.rx_cache.internal.migration;

import dagger.internal.Factory;
import javax.annotation.Generated;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class GetPendingMigrations_Factory implements Factory<GetPendingMigrations> {
  private static final GetPendingMigrations_Factory INSTANCE = new GetPendingMigrations_Factory();

  @Override
  public GetPendingMigrations get() {
    return new GetPendingMigrations();
  }

  public static Factory<GetPendingMigrations> create() {
    return INSTANCE;
  }

  /** Proxies {@link GetPendingMigrations#GetPendingMigrations()}. */
  public static GetPendingMigrations newGetPendingMigrations() {
    return new GetPendingMigrations();
  }
}
