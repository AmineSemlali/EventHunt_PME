package de.pme.eventhunt.view.ui.notifications;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import com.squareup.picasso.Picasso;

import java.util.List;


import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.R;
import de.pme.eventhunt.model.repositories.NotificationRepository;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.NotificationViewHolder> {

    // Counter for the number of child items - demo/debug purpose only
    static int counter = 0;


    // View Holder definition
    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final TextView notificationDescription;
        private final ImageView eventImage;
        private final TextView notificationTime;

        private NotificationViewHolder(View itemView) {
            super(itemView);
            this.notificationDescription = itemView.findViewById(R.id.list_item_notification_content);
            this.eventImage = itemView.findViewById(R.id.list_item_notification_image);
            this.notificationTime = itemView.findViewById(R.id.list_item_notification_time);
        }
    }

    private final LayoutInflater inflater;
    private List<Notification> notificationList; // Cached Copy of Notifications


    public notificationAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.list_item_notification, parent, false);
        Log.i( "OnCreateViewHolder", "Count: " + ++notificationAdapter.counter);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        if (this.notificationList != null && !this.notificationList.isEmpty())
        {
            Notification current = this.notificationList.get(position);

            holder.notificationDescription.setText(String.format("%s", current.getNotificationDescription()));
            holder.notificationTime.setText(String.format("%s", current.getCreatedAt()));

            // We use Picasso to load, cache, transform and display profile images
            Picasso p = Picasso.get();
            p.setIndicatorsEnabled(true);                   // Show indicators for debugging

            p.load(current.getEventImage())            // Which image to load
                    .placeholder(R.drawable.image_placeholder_icon)   // Placeholder during loading
                    .error(R.drawable.icon_error)           // Image shown in case of an error
                    .resize(80, 80) // Resizing
                    .rotate(-15.0f)                         // Rotation (15° to the left)
                    .centerCrop()                           // Crop Image
                    .into( holder.eventImage );           // Target ImageView

        }
        else {
            // Covers the case of data not being ready yet.
            holder.notificationDescription.setText(R.string.no_content);
        }
    }

    @Override
    public int getItemCount() {
        if( this.notificationList != null && !this.notificationList.isEmpty() )
            return this.notificationList.size();
        else
            return 0;
    }

    public void setNotifications(List<Notification> notificationList){
        this.notificationList = notificationList;
        notifyDataSetChanged();
    }

}


