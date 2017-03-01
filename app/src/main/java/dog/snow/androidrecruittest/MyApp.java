package dog.snow.androidrecruittest;

import android.app.Application;

import dog.snow.androidrecruittest.injection.components.AppComponent;
import dog.snow.androidrecruittest.injection.components.DaggerAppComponent;
import dog.snow.androidrecruittest.injection.components.DaggerNetComponent;
import dog.snow.androidrecruittest.injection.components.NetComponent;
import dog.snow.androidrecruittest.injection.modules.ApiClientModule;
import dog.snow.androidrecruittest.injection.modules.AppModule;

/**
 * Created by mruga on 01.03.2017.
 */

public class MyApp extends Application{

    private NetComponent mNetComponent;
    private AppComponent mApplicationComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        initNetComponent();
        initAppComponent();
    }
    private void initNetComponent(){
        mNetComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .apiClientModule(new ApiClientModule())
                .build();
    }
    private void initAppComponent(){
        mApplicationComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
    public NetComponent getNetComponent() {
        return mNetComponent;
    }
}
