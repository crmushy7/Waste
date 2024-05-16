package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.crmushi.wmeaapp.R;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    private List<ItemSetGet> items;
    private OnItemClickListener mListener;
    private boolean clickable = true;

    public ItemsAdapter(List<ItemSetGet> items) {
        this.items = items;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }


    public void updateData(List<ItemSetGet> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ItemSetGet item = items.get(position);
        holder.bind(item);

        // Set the background drawable for receiptStatus TextView based on the status
//        if (item.getStatus() != null && item.getStatus().equals("Debt")) {
//            holder.receiptStatus.setBackgroundResource(R.drawable.roundedred);
//        } else {
//            holder.receiptStatus.setBackgroundResource(R.drawable.roundedgreen);
//        }

        // Set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onItemClick(position, item);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView item_name;
        private TextView item_location;
        private TextView item_owner;
        private TextView dateTextView;
        private TextView item_weight;
        private ImageView itemPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name=itemView.findViewById(R.id.tv_itemName);
            item_location = itemView.findViewById(R.id.tv_itemLocation);
            item_owner = itemView.findViewById(R.id.tv_itemOwner);
            dateTextView = itemView.findViewById(R.id.tv_itemUploadDate);
            item_weight = itemView.findViewById(R.id.tv_itemWeight);
            itemPic=itemView.findViewById(R.id.item_image);
        }

        public void bind(ItemSetGet itemSetGet) {
            item_name.setText(itemSetGet.getItemName());
            item_location.setText("iyumbu,Dodoma");
            dateTextView.setText(itemSetGet.getItemUploadDate());
            item_owner.setText(itemSetGet.getItemOwner());
            item_weight.setText(itemSetGet.getItemWeight());

            Glide.with(itemView.getContext())
                    .load(itemSetGet.getItemImage())
                    .into(itemPic);


        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position,ItemSetGet itemSetGet);
    }
}

