package dog.snow.androidrecruittest.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
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
    @BindView(R.id.search_et)
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((MyApp) getApplication()).getNetComponent().inject(this);
        search.clearFocus();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ItemAdapter(this, new ArrayList<>()));
        swipeRefreshLayout.setOnRefreshListener(this::getData);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Realm myRealm = Realm.getDefaultInstance();
                ((ItemAdapter) recyclerView.getAdapter()).clear();
                String query = "*" + charSequence.toString() + "*";
                RealmResults<Item> realmResults = myRealm.where(Item.class)
                        .beginGroup()
                        .like("description", query)
                        .or()
                        .like("name", query)
                        .endGroup()
                        .findAllAsync();
                realmResults.addChangeListener((collection, changeSet) -> {
                    ItemAdapter itemAdapter =((ItemAdapter) recyclerView.getAdapter());
                    for (Item item1 : realmResults) {
                        itemAdapter.addItem(item1);
                    }
                    itemAdapter.notifyDataSetChanged();
                    myRealm.close();

                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        Realm.init(this);
        getData();

    }

    private void getData() {
        Realm myRealm = Realm.getDefaultInstance();
        apiClient.getItems()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(items -> {
                    myRealm.beginTransaction();
                    return Observable.from(items);
                })
                .take(10)
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {
                        emptyList.setVisibility(View.GONE);
                        Log.d("Realm", "complited transaction");
                        myRealm.commitTransaction();
                        readData();
                        myRealm.close();
                    }

                    @Override
                    public void onError(Throwable e) {
                        myRealm.close();
                        readData();
                    }

                    @Override
                    public void onNext(Item item) {
                        Log.d("Api response", item.getName());
                        myRealm.copyToRealmOrUpdate(item);


                    }
                });
    }

    private void readData() {
        ((ItemAdapter) recyclerView.getAdapter()).clear();

        ItemAdapter itemAdapter = ((ItemAdapter) recyclerView.getAdapter());
        Realm myRealm = Realm.getDefaultInstance();
        RealmResults<Item> results = myRealm.where(Item.class).findAllAsync();
        results.addChangeListener((items, changeSet) -> {
            for (Item item : items) {
                Log.d("Item", item.getId() + " " + item.getName());
                itemAdapter.addItem(item);
            }
            itemAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
            emptyList.setVisibility(View.GONE);
        });

        if (results.isEmpty())
            emptyList.setVisibility(View.VISIBLE);
    }
}
