package com.captech.cosmonauts.cosmolte.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.captech.cosmonauts.cosmolte.ProfileInfoSingleton;
import com.captech.cosmonauts.cosmolte.R;
import com.captech.cosmonauts.cosmolte.network.CosmotleItem;

import java.util.ArrayList;
import java.util.List;


public class CosmolteItemRecyclerViewAdapter extends RecyclerView.Adapter<CosmolteItemRecyclerViewAdapter.ViewHolder> {

    private List<CosmotleItem> cosmotleItems;

    public CosmolteItemRecyclerViewAdapter() {
        cosmotleItems = new ArrayList<>();
    }

    public void setCosmolteItems(List<CosmotleItem> cosmotleItemsList) {
        cosmotleItems = cosmotleItemsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ProfileInfoSingleton.getInstance().addName(cosmotleItems.get(position).getName());
        holder.mIdView.setText(cosmotleItems.get(position).getName());
        holder.mContentView.setText(String.valueOf(cosmotleItems.get(position).getCount()));

    }

    @Override
    public int getItemCount() {
        return cosmotleItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
