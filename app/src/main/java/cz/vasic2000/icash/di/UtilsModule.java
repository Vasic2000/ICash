package cz.vasic2000.icash.di;

import cz.vasic2000.icash.mvp.model.api.INetworkStatus;
import cz.vasic2000.icash.ui.NetworkStatus;
import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {
    @Provides
    public INetworkStatus networkStatus() {
        return new NetworkStatus();
    }
}
