package dog.snow.androidrecruittest.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.activities.MainActivity;
import dog.snow.androidrecruittest.models.Item;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class ItemAdapter extends RealmRecyclerViewAdapter<Item,ItemAdapter.ViewHolder> {
    private Activity activity;
    private Realm realm;
    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.name_tv)
        TextView mName;
        @BindView(R.id.description_tv)
        TextView mDescription;
        @BindView(R.id.icon_ic)
        ImageView mIcon;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    public ItemAdapter(Activity activity, RealmResults<Item> items, Realm realm) {
        super(items,true);
        this.activity = activity;
        this.realm = realm;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = getData().get(position);
        holder.mName.setText(item.getName());
        holder.mDescription.setText(item.getDescription());
        Glide.with(activity)
                .load(item.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIcon);

    }

    public void filter(String query) {
        RealmResults<Item> results = realm.where(Item.class)
                .beginGroup()
                .contains("description", query)
                .or()
                .contains("name", query)
                .endGroup()
                .findAllAsync();
        results.addChangeListener((collection, changeSet) ->{
            ((MainActivity)activity).showEmptyList(results.isEmpty());
            if(!getData().equals(collection))
                updateData(collection);
        });
    }

}
