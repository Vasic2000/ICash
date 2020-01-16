package cz.vasic2000.icash.di;

import cz.vasic2000.icash.App;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private App app;

    public AppModule(App app){
        this.app = app;
    }

    @Provides
    public App app(){
        return app;
    }
}
