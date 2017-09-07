package com.example.onthe.map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onthe.map.data.Place;

/**
 * Created by phucle on 9/6/17.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceAdapterViewHolder> {

    // The context we use to inflate layouts and get UI resources.
    private final Context mContext;
    // Fake place data
    private Place[] mPlaceData;

    private final PlaceAdapterOnClickHandler mClickHandler;

    public interface PlaceAdapterOnClickHandler {
        void onClick(Place aPlace);
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
        // TODO: set data
        Place aPlace = mPlaceData[position];
        String name = aPlace.getPlaceName();
        String address = aPlace.getPlaceAddress();
        double rating = aPlace.getPlaceRating();

        holder.nameTextView.setText(name);
        holder.addressTextView.setText(address);
        holder.ratingTextView.setText(String.valueOf(rating));
    }

    /**
     * Return the number of items to display.
     *
     * @return The number of items in the list item.
     */
    @Override
    public int getItemCount() {
        if (mPlaceData == null)
            return 0;
        return mPlaceData.length;
    }

    public void setPlaceData(Place[] foodPlace) {
        mPlaceData = foodPlace;
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
            Place aPlace = mPlaceData[adapterPosition];
            mClickHandler.onClick(aPlace);
        }
    }
}
