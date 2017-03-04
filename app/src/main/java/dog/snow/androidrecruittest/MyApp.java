package dog.snow.androidrecruittest;

import android.app.Application;

import java.io.File;

import dog.snow.androidrecruittest.injection.components.AppComponent;
import dog.snow.androidrecruittest.injection.components.DaggerAppComponent;
import dog.snow.androidrecruittest.injection.components.DaggerNetComponent;
import dog.snow.androidrecruittest.injection.components.NetComponent;
import dog.snow.androidrecruittest.injection.modules.ApiClientModule;
import dog.snow.androidrecruittest.injection.modules.AppModule;
import dog.snow.androidrecruittest.models.Item;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application{

    private NetComponent mNetComponent;
    private AppComponent mApplicationComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        initNetComponent();
        initAppComponent();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
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
