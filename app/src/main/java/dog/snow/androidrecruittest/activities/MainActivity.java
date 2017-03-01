package dog.snow.androidrecruittest.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import dog.snow.androidrecruittest.MyApp;
import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.models.Item;
import dog.snow.androidrecruittest.injection.modules.ApiClientModule;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    ApiClientModule.ApiClient apiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((MyApp)getApplication()).getNetComponent().inject(this);
        apiClient.getItems()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Item>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Api response",Log.getStackTraceString(e));
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        Log.d("Api response", items.toString() );

                    }
                });
    }
}
