package com.captech.cosmonauts.cosmolte.adapter;

import android.content.Context;
import android.graphics.Color;
import android.provider.CalendarContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.captech.cosmonauts.cosmolte.R;
import com.captech.cosmonauts.cosmolte.network.CosmolteCalendarItem;
import com.captech.cosmonauts.cosmolte.network.CosmotleItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CosmolteCalendarItemRecyclerViewAdapter extends RecyclerView.Adapter<CosmolteCalendarItemRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CalItemRecycleAdapter";
    private static int nextEventPosition = -1;
    private static Context context;
    private Calendar todayCalendar = Calendar.getInstance();

    public List<CosmolteCalendarItem> cosmolteCalendarItems;

    public CosmolteCalendarItemRecyclerViewAdapter(Context context) {
        cosmolteCalendarItems = new ArrayList<>();
        this.context = context;
        nextEventPosition = -1;
    }

    public void setCosmolteCalendarItems(List<CosmolteCalendarItem> cosmolteCalendarItems) {
        this.cosmolteCalendarItems = cosmolteCalendarItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_calendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int month = cosmolteCalendarItems.get(position).getMonth();
        int day = cosmolteCalendarItems.get(position).getDay();
        if (isNextEvent(position)) {
            holder.view.setBackgroundColor(context.getResources().getColor(R.color.colorAccentLight));
            //holder.nameView.setTextColor(context.getResources().getColor(android.R.color.white));
            //holder.dateView.setTextColor(context.getResources().getColor(android.R.color.white));
        } else {
            holder.view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            //holder.nameView.setTextColor(context.getResources().getColor(android.R.color.black));
            //holder.dateView.setTextColor(context.getResources().getColor(android.R.color.black));
        }
        holder.nameView.setText(cosmolteCalendarItems.get(position).getName());
        holder.dateView.setText(cosmolteCalendarItems.get(position).getMonth() + "/" + cosmolteCalendarItems.get(position).getDay());
    }

    @Override
    public int getItemCount() {
        return cosmolteCalendarItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView nameView;
        public final TextView dateView;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            nameView = (TextView) view.findViewById(R.id.name);
            dateView = (TextView) view.findViewById(R.id.eventDate);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + nameView.getText() + "'";
        }
    }

    private boolean isNextEvent(int position) {
        if (todayCalendar.get(Calendar.DAY_OF_MONTH) == cosmolteCalendarItems.get(position).getDay() &&
                todayCalendar.get(Calendar.MONTH) + 1 == cosmolteCalendarItems.get(position).getMonth()
                && nextEventPosition < 0) {
            nextEventPosition = position;
            return true;
        } else if (todayCalendar.get(Calendar.MONTH) + 1 == cosmolteCalendarItems.get(position).getMonth() &&
                (position < (cosmolteCalendarItems.size() - 1)) &&
                todayCalendar.get(Calendar.DAY_OF_MONTH) > cosmolteCalendarItems.get(position + 1).getDay() &&
                nextEventPosition < 0) {
            nextEventPosition = position;
            return true;
        } else if (todayCalendar.get(Calendar.MONTH) > cosmolteCalendarItems.get(position).getMonth()) {
            nextEventPosition = position;
            return true;
        } else {
            return false;
        }

    }
}
