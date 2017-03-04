package dog.snow.androidrecruittest.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dog.snow.androidrecruittest.MyApp;
import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.adapters.ItemAdapter;
import dog.snow.androidrecruittest.injection.modules.ApiClientModule;
import dog.snow.androidrecruittest.models.Item;
import io.realm.OrderedRealmCollection;
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
        recyclerView.setHasFixedSize(true);
        Realm myRealm = Realm.getDefaultInstance();
        RealmResults<Item> items =  myRealm.where(Item.class).findAll();
        recyclerView.setAdapter(new ItemAdapter(this, items, myRealm));
        swipeRefreshLayout.setOnRefreshListener(this::getData);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((ItemAdapter)recyclerView.getAdapter()).filter(editable.toString());

            }
        });
        getData();

    }

    private void getData() {
        Realm myRealm = Realm.getDefaultInstance();
        apiClient.getItems()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .first(items -> {
                    myRealm.beginTransaction();
                    return true;
                })
                .subscribe(new Subscriber<List<Item>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("Realm", "complited transaction");
                        swipeRefreshLayout.setRefreshing(false);
                        myRealm.commitTransaction();
                        myRealm.close();
                    }

                    @Override
                    public void onError(Throwable e) {
                        myRealm.close();
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        myRealm.copyToRealmOrUpdate(items);
                        emptyList.setVisibility(items.isEmpty()?View.VISIBLE:View.GONE);
                    }
                });
    }
    public void showEmptyList(boolean isToShow){
        emptyList.setVisibility(isToShow?View.VISIBLE:View.GONE);
    }
}
