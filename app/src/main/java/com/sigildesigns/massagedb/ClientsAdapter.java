package com.sigildesigns.massagedb;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sigildesigns.massagedb.data.ClientContract;

/**
 * Adapter to inject the data into the RecyclerView
 */

// Create the basic adapter, extending from RecyclerView.Adapter

public class ClientsAdapter extends RecyclerView.Adapter<ClientsAdapter.ViewHolder> {
    // An onClick handler that we've defined to make it easy for an Activity to interface with
    // our RecyclerView.
    private final ClientsAdapterOnClickHandler mClickHandler;

    // The interface that receives onClick messages.
    public interface ClientsAdapterOnClickHandler {
        void onClick(String client);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView nameTextView;
        public TextView lastServiceTextView;
        public ImageView itWorksImageView;
        public ImageView importantImageView;
        private TextView idTextView;

        // We also create a constructor that accepts the entire item row and does the view
        // lookups to find each subview

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used to access
            // the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.tv_list_name);
            lastServiceTextView = (TextView) itemView.findViewById(R.id.tv_list_last_service_date);
            itWorksImageView = (ImageView) itemView.findViewById(R.id.iv_list_itworks);
            importantImageView = (ImageView) itemView.findViewById(R.id.iv_list_important);
            idTextView = (TextView) itemView.findViewById(R.id.tv_list_client_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClients.moveToPosition(adapterPosition);
            int clientID = mClients.getInt(mClients.getColumnIndex(ClientContract.ClientEntry._ID));
            mClickHandler.onClick(String.valueOf(clientID));
        }
    }

    // Store a member variable for the clients
    private Cursor mClients;
    // Store the context for easy access
    private Context mContext;

    // Pass in the client array into the constructor
    public ClientsAdapter(Context context, Cursor cursor, ClientsAdapterOnClickHandler clickHandler) {
        mClients = cursor;
        mContext = context;
        mClickHandler = clickHandler;
    }

    // Easy access to the context object in the recyclerview
    private Context getmContext() {
        return mContext;
    }

    @Override
    public ClientsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .client_list_item, parent, false);
        return new ViewHolder(cardview);
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mClients == cursor) {
            return null;
        }
        Cursor oldCursor = mClients;
        this.mClients = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    // Involves populating data into the item through holder

    @Override
    public void onBindViewHolder(ClientsAdapter.ViewHolder viewHolder, int position) {
        // Move to correct position in cursor
        mClients.moveToPosition(position);

        // Find indexes for the parts we care about.
        int firstNameIndex = mClients.getColumnIndex(ClientContract.ClientEntry.COLUMN_FIRST_NAME);
        int lastNameIndex = mClients.getColumnIndex(ClientContract.ClientEntry.COLUMN_LAST_NAME);
        int lastServiceIndex = mClients.getColumnIndex(ClientContract.ClientEntry
                .COLUMN_LAST_SERVICE);
        int itWorksIndex = mClients.getColumnIndex(ClientContract.ClientEntry.COLUMN_IT_WORKS);
        int importantIndex = mClients.getColumnIndex(ClientContract.ClientEntry.COLUMN_IMPORTANT);
        int clientIDIndex = mClients.getColumnIndex(ClientContract.ClientEntry._ID);

        // Set item views based on views and data model
        String firstName = mClients.getString(firstNameIndex);
        String lastName = mClients.getString(lastNameIndex);
        viewHolder.nameTextView.setText(firstName + " " + lastName);

        String lastService = mClients.getString(lastServiceIndex);
        viewHolder.lastServiceTextView.setText(lastService);

        int itWorksInt = mClients.getInt(itWorksIndex);
        boolean itWorks;
        if (itWorksInt == 1) {
            itWorks = true;
        } else {
            itWorks = false;
        }
        if (itWorks) {
            viewHolder.itWorksImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.itWorksImageView.setVisibility(View.INVISIBLE);
        }

        int importantInt = mClients.getInt(importantIndex);
        boolean important;
        if (importantInt == 1) {
            important = true;
        } else {
            important = false;
        }
        if (important) {
            viewHolder.importantImageView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.importantImageView.setVisibility(View.INVISIBLE);
        }
        int clientID = mClients.getInt(clientIDIndex);
        viewHolder.idTextView.setText(String.valueOf(clientID));

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return (mClients == null) ? 0 : mClients.getCount();
    }
}
