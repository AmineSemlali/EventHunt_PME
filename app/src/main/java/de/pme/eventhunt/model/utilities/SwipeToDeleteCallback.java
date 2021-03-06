package de.pme.eventhunt.model.utilities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import de.pme.eventhunt.view.ui.notifications.notificationAdapter;

// class used for deleting notifications

public class SwipeToDeleteCallback  extends ItemTouchHelper.SimpleCallback {

    private notificationAdapter adapter;

    public SwipeToDeleteCallback(notificationAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);

    }


}