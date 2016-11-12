package com.captech.cosmonauts.cosmolte.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.captech.cosmonauts.cosmolte.R;
import com.captech.cosmonauts.cosmolte.adapter.CosmolteCalendarItemRecyclerViewAdapter;
import com.captech.cosmonauts.cosmolte.network.CosmolteCalendarItem;
import com.captech.cosmonauts.cosmolte.network.CosmolteService;
import com.captech.cosmonauts.cosmolte.network.CosmotleItem;

import java.util.List;

public class CosmolteCalendarListFragment extends Fragment {

    private static final String TAG = "CosmolteCalListFrag";

    private CosmolteCalendarListReceiver cosmolteCalendarListReceiver;
    private CosmolteCalendarItemRecyclerViewAdapter cosmolteCalendarItemRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private View progressBar;
    private LinearLayoutManager layoutManager;

    public CosmolteCalendarListFragment() {
    }


    public static CosmolteCalendarListFragment newInstance() {
        CosmolteCalendarListFragment cosmolteCalendarListFragment = new CosmolteCalendarListFragment();
        return cosmolteCalendarListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent cosmolteCalendarListIntent = new Intent(getActivity(), CosmolteService.class);
        cosmolteCalendarListIntent.putExtra(CosmolteService.EXTRA_OPERATION_TOCALL, CosmolteService.OPERATION_CALENDAR_ITEMS_TYPE);
        getActivity().startService(cosmolteCalendarListIntent);

        cosmolteCalendarListReceiver = new CosmolteCalendarListReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar_list, container, false);

        final View totalView = getActivity().findViewById(R.id.totalLayout);

        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.calendarlist);
        recyclerView.setLayoutManager(layoutManager);
        cosmolteCalendarItemRecyclerViewAdapter = new CosmolteCalendarItemRecyclerViewAdapter(getActivity());
        recyclerView.setAdapter(cosmolteCalendarItemRecyclerViewAdapter);

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisbleItems = layoutManager.findFirstVisibleItemPosition();
                if (dy > 0 && pastVisbleItems + visibleItemCount >= totalItemCount) {
                    totalView.setVisibility(View.GONE);
                } else if (dy < 0) {
                    totalView.setVisibility(View.VISIBLE);
                }
            }
        });*/

        progressBar = view.findViewById(R.id.calendarProgressBar);

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(cosmolteCalendarListReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().registerReceiver(cosmolteCalendarListReceiver, new IntentFilter(CosmolteService.INTENT_LISTCALENDAR_ACTION));
    }

    private class CosmolteCalendarListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received intent with args " + intent.hasExtra(CosmolteService.EXTRA_CALENDAR_LIST));

            progressBar.setVisibility(View.GONE);

            recyclerView.setVisibility(View.VISIBLE);

            List<CosmolteCalendarItem> cosmolteCalendarItemList = intent.getParcelableArrayListExtra(CosmolteService.EXTRA_CALENDAR_LIST);
            cosmolteCalendarItemRecyclerViewAdapter.setCosmolteCalendarItems(cosmolteCalendarItemList);
            recyclerView.removeAllViews();
            cosmolteCalendarItemRecyclerViewAdapter.notifyDataSetChanged();

        }
    }
}
