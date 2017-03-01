package dog.snow.androidrecruittest.injection.components;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import dog.snow.androidrecruittest.injection.modules.AppModule;

@Singleton
@Component( modules = AppModule.class)
public interface AppComponent {
    Application application();
}

