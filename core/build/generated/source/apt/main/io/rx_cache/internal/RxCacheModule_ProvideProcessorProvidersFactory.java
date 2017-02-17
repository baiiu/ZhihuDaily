package io.rx_cache.internal;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
  value = "dagger.internal.codegen.ComponentProcessor",
  comments = "https://google.github.io/dagger"
)
public final class RxCacheModule_ProvideProcessorProvidersFactory
    implements Factory<ProcessorProviders> {
  private final RxCacheModule module;

  private final Provider<ProcessorProvidersBehaviour> processorProvidersBehaviourProvider;

  public RxCacheModule_ProvideProcessorProvidersFactory(
      RxCacheModule module,
      Provider<ProcessorProvidersBehaviour> processorProvidersBehaviourProvider) {
    assert module != null;
    this.module = module;
    assert processorProvidersBehaviourProvider != null;
    this.processorProvidersBehaviourProvider = processorProvidersBehaviourProvider;
  }

  @Override
  public ProcessorProviders get() {
    return Preconditions.checkNotNull(
        module.provideProcessorProviders(processorProvidersBehaviourProvider.get()),
        "Cannot return null from a non-@Nullable @Provides method");
  }

  public static Factory<ProcessorProviders> create(
      RxCacheModule module,
      Provider<ProcessorProvidersBehaviour> processorProvidersBehaviourProvider) {
    return new RxCacheModule_ProvideProcessorProvidersFactory(
        module, processorProvidersBehaviourProvider);
  }

  /** Proxies {@link RxCacheModule#provideProcessorProviders(ProcessorProvidersBehaviour)}. */
  public static ProcessorProviders proxyProvideProcessorProviders(
      RxCacheModule instance, ProcessorProvidersBehaviour processorProvidersBehaviour) {
    return instance.provideProcessorProviders(processorProvidersBehaviour);
  }
}
