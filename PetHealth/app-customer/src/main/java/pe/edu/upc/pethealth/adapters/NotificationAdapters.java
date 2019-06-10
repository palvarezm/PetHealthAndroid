package pe.edu.upc.pethealth.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.edu.upc.pethealth.R;
import pe.edu.upc.pethealth.models.Notification;

/**
 * Created by genob on 29/09/2017.
 */

public class NotificationAdapters extends RecyclerView.Adapter<NotificationAdapters.ViewHolder> {

    private List<Notification> notifications;

    public NotificationAdapters() {
    }

    public NotificationAdapters(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public NotificationAdapters setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        return this;
    }

    @Override
    public NotificationAdapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notifications,parent,false));
    }

    @Override
    public void onBindViewHolder(NotificationAdapters.ViewHolder holder, int position) {

        final Notification notification = notifications.get(position);
        holder.tittleTextView.setText(notification.getNotificationtittle());
        holder.descriptionTextView.setText(notification.getNotificationcontent());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tittleTextView;
        TextView descriptionTextView;
        public ViewHolder(View itemView) {
            super(itemView);
            tittleTextView = (TextView) itemView.findViewById(R.id.notificationTittleTextView);
            descriptionTextView = (TextView)itemView.findViewById(R.id.contentTextView);
        }
    }
}
