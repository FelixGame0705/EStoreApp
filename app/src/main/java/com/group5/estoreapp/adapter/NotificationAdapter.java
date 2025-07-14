// NotificationAdapter.java
package com.group5.estoreapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.group5.estoreapp.R;
import com.group5.estoreapp.model.Notification;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notifications;
    private OnNotificationClickListener listener;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public void setNotifications(List<Notification> newNotifications) {
        notifications.clear();
        notifications.addAll(newNotifications);
        notifyDataSetChanged();
    }

    public void addNotification(Notification notification) {
        notifications.add(0, notification); // Thêm vào đầu danh sách
        notifyItemInserted(0);
    }

    public void updateNotification(Notification updatedNotification) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getNotificationID() == updatedNotification.getNotificationID()) {
                notifications.set(i, updatedNotification);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void removeNotification(int notificationId) {
        for (int i = 0; i < notifications.size(); i++) {
            if (notifications.get(i).getNotificationID() == notificationId) {
                notifications.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setOnNotificationClickListener(OnNotificationClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView textMessage;
        private final TextView textTime;
        private final View readIndicator;
        private final View itemContainer;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
            textTime = itemView.findViewById(R.id.textTime);
            readIndicator = itemView.findViewById(R.id.readIndicator);
            itemContainer = itemView.findViewById(R.id.itemContainer);
        }

        public void bind(Notification notification) {
            textMessage.setText(notification.getMessage());

            // Format time
            String timeText = formatTime(notification.getCreatedTime());
            textTime.setText(timeText);

            // Set read status
            if (notification.isRead()) {
                readIndicator.setVisibility(View.GONE);
                itemContainer.setBackgroundColor(itemView.getContext().getColor(R.color.gray_light));
                textMessage.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            } else {
                readIndicator.setVisibility(View.VISIBLE);
                itemContainer.setBackgroundColor(itemView.getContext().getColor(R.color.gray_light));
                textMessage.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            // Set click listeners
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onNotificationClick(notification, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) {
                    listener.onNotificationLongClick(notification, getAdapterPosition());
                }
                return true;
            });
        }

        private String formatTime(String timeString) {
            try {
                if (timeString == null || timeString.isEmpty()) {
                    return "Vừa xong";
                }

                // Parse time từ server (có thể cần điều chỉnh format)
                SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

                Date date = serverFormat.parse(timeString);
                return displayFormat.format(date);
            } catch (Exception e) {
                return "Vừa xong";
            }
        }
    }

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification, int position);
        void onNotificationLongClick(Notification notification, int position);
    }
}