package dog.snow.androidrecruittest.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dog.snow.androidrecruittest.R;
import dog.snow.androidrecruittest.models.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private List<Item> items;
    private Context context;

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

    public ItemAdapter(Context context, List<Item> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.mName.setText(item.getName());
        holder.mDescription.setText(item.getDescription());
        Glide.with(context)
                .load(item.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(holder.mIcon);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(Item item){
        items.add(item);
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }
}
