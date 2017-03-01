package dog.snow.androidrecruittest.injection.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private static final String SHARED_PREF_FILE = "private_prefs";
    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    ApiClientModule provideApiModule() {
        return new ApiClientModule();
    }

}