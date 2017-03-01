package dog.snow.androidrecruittest.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dog.snow.androidrecruittest.MyApp;
import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.adapters.ItemAdapter;
import dog.snow.androidrecruittest.injection.modules.ApiClientModule;
import dog.snow.androidrecruittest.models.Item;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @Inject
    ApiClientModule.ApiClient apiClient;
    @BindView(R.id.items_rv)
    RecyclerView recyclerView;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_list_tv)
    TextView emptyList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((MyApp)getApplication()).getNetComponent().inject(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ItemAdapter(this, new ArrayList<>()));
        swipeRefreshLayout.setOnRefreshListener(this::getData);

    }
    private void getData(){
        ((ItemAdapter)recyclerView.getAdapter()).clear();
        apiClient.getItems()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(rx.Observable::from)
                .take(10)
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        emptyList.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("Api response",Log.getStackTraceString(e));
                    }

                    @Override
                    public void onNext(Item item) {
                        Log.d("Api response", item.toString() );
                        ((ItemAdapter)recyclerView.getAdapter()).addItem(item);

                    }
                });
    }
}
