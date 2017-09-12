package com.example.onthe.map;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onthe.map.data.PlaceContract;

/**
 * Created by phucle on 9/6/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceAdapterViewHolder> {

    // The context we use to inflate layouts and get UI resources.
    private final Context mContext;

    private final PlaceAdapterOnClickHandler mClickHandler;

    private Cursor mCursor;

    public interface PlaceAdapterOnClickHandler {
        void onClick(int placeId);
    }
    /**
     * Creates a PlaceAdapter
     *
     * @param context used to inflate layouts and get UI resources.
     */
    public PlaceAdapter(Context context, PlaceAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * Gets called when ViewHolder first created. Decide which layout to be inflated.
     *
     * @param parent   the parent ViewGroup that these ViewHolders are contained within.
     * @param viewType use if the RecyclerView has more than one type of item
     * @return a PlaceAdapterHolder that holds the View for each list item.
     */
    @Override
    public PlaceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.places_list_item;
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        view.setFocusable(true);
        return new PlaceAdapterViewHolder(view);
    }

    /**
     * Display the data at a specific position for the ViewHolders.
     *
     * @param holder   The ViewHolder should be updated to represent the content at
     *                 the given position.
     * @param position The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(PlaceAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String name = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_NAME));
        String address = mCursor.getString(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_ADDRESS));
        float rating = mCursor.getFloat(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_RATING));

        holder.nameTextView.setText(name);
        holder.addressTextView.setText(address);
        holder.ratingTextView.setText(String.valueOf(rating));
        holder.iconImageView.setImageResource(R.drawable.ic_food);
    }

    /**
     * Return the number of items to display.
     *
     * @return The number of items in the list item.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    /**
     * Cache of the child views for a list item.
     */
    class PlaceAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView iconImageView;

        final TextView nameTextView;
        final TextView addressTextView;
        final TextView ratingTextView;

        public PlaceAdapterViewHolder(View itemView) {
            super(itemView);

            iconImageView = (ImageView) itemView.findViewById(R.id.iv_place_icon);

            nameTextView = (TextView) itemView.findViewById(R.id.tv_place_name);
            addressTextView = (TextView) itemView.findViewById(R.id.tv_place_address);
            ratingTextView = (TextView) itemView.findViewById(R.id.tv_rating);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            int placeId = mCursor.getInt(mCursor.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID));
            mClickHandler.onClick(placeId);
        }
    }
}
