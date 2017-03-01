package dog.snow.androidrecruittest.injection.components;

import javax.inject.Singleton;

import dagger.Component;
import dog.snow.androidrecruittest.activities.MainActivity;
import dog.snow.androidrecruittest.injection.modules.ApiClientModule;
import dog.snow.androidrecruittest.injection.modules.AppModule;


@Singleton
@Component(modules = {AppModule.class, ApiClientModule.class})
public interface NetComponent {
    void inject(MainActivity activity);
}
