package de.pme.eventhunt.view.ui.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.pme.eventhunt.R;
import de.pme.eventhunt.model.documents.Event;
import de.pme.eventhunt.model.utilities.EventLocation;
import de.pme.eventhunt.model.utilities.LocalDateConverter;
import de.pme.eventhunt.view.ui.utilities.DateAndTime;

public class EventListAdapter extends  RecyclerView.Adapter<EventListAdapter.EventListViewHolder>{
    // Counter for the number of child items
    static int counter = 0;

    private final LayoutInflater inflater;
    private List<Event> eventList;
    private EventListViewHolder.OnNoteListener mOnNoteListener;

    public EventListAdapter(Context context, EventListViewHolder.OnNoteListener onNoteListener) {
        this.inflater = LayoutInflater.from(context);
        this.mOnNoteListener = onNoteListener;
    }


    @NonNull
    @Override
    public EventListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.inflater.inflate(R.layout.list_item_event, parent, false);

        Log.i("OnCreateViewHolder", "Count " + ++EventListAdapter.counter);

        return new EventListViewHolder(itemView, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListViewHolder holder, int position) {
        if(this.eventList != null && !this.eventList.isEmpty())
        {
            Event current = this.eventList.get(position);

            // Set id
            holder.eventId = current.getEventId();

            // Set title
            holder.eventTitle.setText(current.getTitle());

            // Set category
            String category = current.getCategory();

            if(category.equals("Socializing")) {
                holder.eventCategory.setBackgroundColor(Color.parseColor("#D63230"));
            }
            else if(category.equals("Party")) {
                holder.eventCategory.setBackgroundColor(Color.parseColor("#772D8B"));
            }
            else if(category.equals("Trip")) {
                holder.eventCategory.setBackgroundColor(Color.parseColor("#F39237"));
            }

            holder.eventCategory.setText(category);

            // Set date
            DateAndTime eventDate = null;
            try {
                eventDate = new DateAndTime(current.getStartTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
            assert eventDate != null;
            holder.eventDate.setText(eventDate.formatString());

            // Set location
            EventLocation eventLocation = current.getLocation();
            String locationString = "";
            try {
                locationString = eventLocation.getLocationString(inflater.getContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.eventLocation.setText(locationString);

            // Set image
            String eventImageUrl = current.getImageSmallRef();
            Picasso p = Picasso.get();
            p.setIndicatorsEnabled(true);
            p.load(eventImageUrl)
                .into(holder.eventImage);
        }
    }

    @Override
    public int getItemCount() {
        if (this.eventList != null && !this.eventList.isEmpty() )
        {
            return this.eventList.size();
        }
        else return 0;
    }

    public void setEventList(List<Event> eventList)
    {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    // View Holder definition
    static class EventListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String eventId = "";
        private final TextView eventTitle;
        private final TextView eventLocation;
        private final TextView eventCategory;
        private final TextView eventDate;
        private final ImageView eventImage;

        OnNoteListener onNoteListener;


        public EventListViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.eventTitle = itemView.findViewById(R.id.listTextView_title);
            this.eventLocation = itemView.findViewById(R.id.listTextView_location);;
            this.eventCategory = itemView.findViewById(R.id.listTextView_category);
            this.eventDate = itemView.findViewById(R.id.listTextView_date);

            this.eventImage = itemView.findViewById(R.id.listImageView_image);

            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.onNoteListener.onNoteClick(getAdapterPosition());
        }

        public interface OnNoteListener{
            void onNoteClick(int position);
        }
    }
}
