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

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.documents.EventUser;
import de.pme.eventhunt.model.documents.Notification;
import de.pme.eventhunt.R;
import de.pme.eventhunt.model.repositories.NotificationRepository;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;

public class notificationAdapter extends RecyclerView.Adapter<notificationAdapter.NotificationViewHolder> {

    // Counter for the number of child items - demo/debug purpose only
    static int counter = 0;

    private final LayoutInflater inflater;
    private List<Notification> notificationList; // Cached Copy of Notifications
    FirebaseFirestore db;
    FirebaseAuth auth;
    NotificationRepository notificationRepository;
    Notification mRecentlyDeletedItem;
    int mRecentlyDeletedItemPosition;


    public notificationAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.list_item_notification, parent, false);
        Log.i( "OnCreateViewHolder", "Count: " + ++notificationAdapter.counter);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        if (this.notificationList != null && !this.notificationList.isEmpty())
        {
            Notification current = this.notificationList.get(position);

            holder.notificationDescription.setText(String.format("%s", current.getNotificationDescription()));


            DateAndTime createdAt = null;
            try {
                createdAt = new DateAndTime(current.getCreatedAt());
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert createdAt != null;
            holder.notificationTime.setText(createdAt.formatString());

            // Set image
            String eventImageUrl = current.getEventImage();
            Picasso p = Picasso.get();
            p.setIndicatorsEnabled(true);
            p.load(eventImageUrl)
                    .into(holder.eventImage);
        }


        else {
            // Covers the case of data not being ready yet.
            holder.notificationDescription.setText(R.string.no_content);
        }
    }

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





    public void deleteItem(int position) {
        //mRecentlyDeletedItem = notificationList.get(position);
        //mRecentlyDeletedItemPosition = position;
        //notificationRepository.deleteNotification(mRecentlyDeletedItem.getNotificationId());
        notificationList.remove(position);
        notifyItemRemoved(position);

//        showUndoSnackbar();
    }

//    private void showUndoSnackbar() {
//        View view = mActivity.findViewById(R.id.coordinator_layout);
//        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
//                Snackbar.LENGTH_LONG);
//        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
//        snackbar.show();
//    }

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


