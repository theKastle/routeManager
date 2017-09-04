package com.example.onthe.map;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onthe.map.MapUtils.RouteReadModel;

import java.util.List;

/**
 * Created by onthe on 6/18/2017.
 */

public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.RouteViewHolder> {

    private List<RouteReadModel> routes;

    public RoutesAdapter(List<RouteReadModel> routes) {
        this.routes = routes;
        Log.i("Routes", this.routes.toString());
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RouteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final RouteViewHolder holder, int position) {
        RouteReadModel aRoute = routes.get(position);
        holder.description.setText(aRoute.description);
        holder.distance.setText(aRoute.distance);
        holder.duration.setText(aRoute.duration);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener(){
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0, 0, "Delete");
                menu.add(holder.getAdapterPosition(), 1, 0, "Go to map");
                menu.add(holder.getAdapterPosition(), 2, 0, "View Detail");
            }
        });
    }

    @Override
    public int getItemCount() {
        return routes.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView description, duration, distance;

        public RouteViewHolder(View itemView) {
            super(itemView);

            description = (TextView) itemView.findViewById(R.id.view_item_description);
            duration = (TextView) itemView.findViewById(R.id.view_item_duration);
            distance = (TextView) itemView.findViewById(R.id.view_item_distance);
        }
    }

}
