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
import com.example.wmeaapp.R;

import java.util.List;

import Other.Notifications;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationSetGet> items;
    private OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener;
    private boolean clickable = true;

    public NotificationAdapter(List<NotificationSetGet> items) {
        this.items = items;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener ll) {
        mLongListener=ll;
    }
    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }


    public void updateData(List<NotificationSetGet> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_structure, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationSetGet item = items.get(position);
        holder.bind(item);

        // Set the background drawable for receiptStatus TextView based on the status
//        if (item.getStatus() != null && item.getStatus().equals("Debt")) {
//            holder.receiptStatus.setBackgroundResource(R.drawable.roundedred);
//        } else {
//            holder.receiptStatus.setBackgroundResource(R.drawable.roundedgreen);
//        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mLongListener = null){
                    mLongListener.onItemLongClick(position,item);
                }
                return false;
            }
        });
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
        private TextView notifiation_title;
        private TextView notification_message;
        private TextView notification_time;
        private TextView notification_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notifiation_title=itemView.findViewById(R.id.notificationTitle);
            notification_message = itemView.findViewById(R.id.notificationMessage);
            notification_time = itemView.findViewById(R.id.notificationTime);
            notification_status = itemView.findViewById(R.id.notificationStatus);
        }

        public void bind(NotificationSetGet itemSetGet) {
            notifiation_title.setText(itemSetGet.getTitle());
            notification_message.setText(itemSetGet.getMessage());
            notification_time.setText("Sent in: "+ itemSetGet.getSentTime());
            notification_status.setText("Status: "+ itemSetGet.getNotificationStatus());
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, NotificationSetGet itemSetGet);
    }
    public interface OnItemClickListener {
        void onItemClick(int position,NotificationSetGet itemSetGet);
    }
}

