package com.captech.cosmonauts.cosmolte.fragments;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.captech.cosmonauts.cosmolte.adapter.CosmolteItemRecyclerViewAdapter;
import com.captech.cosmonauts.cosmolte.R;
import com.captech.cosmonauts.cosmolte.network.CosmolteService;
import com.captech.cosmonauts.cosmolte.network.CosmotleItem;

import java.util.ArrayList;
import java.util.List;

public class CosmolteListFragment extends Fragment {

    private static final String TAG = "CosmolteListFragment";

    private CosmolteItemRecyclerViewAdapter cosmolteItemRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private View progressBar;
    private View totalView;
    private String listType = CosmolteService.EXPENSE_ITEM_TYPE;
    private LinearLayoutManager layoutManager;

    private CosmolteListReceiver cosmolteListReceiver;

    public CosmolteListFragment() {
    }

    public static CosmolteListFragment newInstance(String listType) {
        CosmolteListFragment cosmolteListFragment = new CosmolteListFragment();

        Bundle args = new Bundle();
        args.putString(CosmolteService.EXTRA_ITEM_TYPE, listType);
        cosmolteListFragment.setArguments(args);
        return cosmolteListFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            listType = args.getString(CosmolteService.EXTRA_ITEM_TYPE);
            Log.d(TAG, "List type received ----> " + listType);
        }

        //Make call to populate list
        Intent cosmolteListIntent = new Intent(getActivity(), CosmolteService.class);
        cosmolteListIntent.putExtra(CosmolteService.EXTRA_OPERATION_TOCALL, CosmolteService.OPERATION_ITEMS_TYPE);
        cosmolteListIntent.putExtra(CosmolteService.EXTRA_ITEM_TYPE, listType);
        getActivity().startService(cosmolteListIntent);

        cosmolteListReceiver = new CosmolteListReceiver();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        totalView = getActivity().findViewById(R.id.totalLayout);

        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        cosmolteItemRecyclerViewAdapter = new CosmolteItemRecyclerViewAdapter();
        recyclerView.setAdapter(cosmolteItemRecyclerViewAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));

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

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter intentFilter;
        if (listType.equalsIgnoreCase(CosmolteService.EXPENSE_ITEM_TYPE)) {
            intentFilter = new IntentFilter(CosmolteService.INTENT_LISTEXPENSE_ACTION);
        } else {
            intentFilter = new IntentFilter(CosmolteService.INTENT_LISTFREE_ACTION);
        }

        getActivity().registerReceiver(cosmolteListReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(cosmolteListReceiver);
    }

    public class CosmolteListReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Received intent with args" + intent.hasExtra(CosmolteService.EXTRA_LIST));

            progressBar.setVisibility(View.GONE);

            recyclerView.setVisibility(View.VISIBLE);

            List<CosmotleItem> cosmotleItemList = intent.getParcelableArrayListExtra(CosmolteService.EXTRA_LIST);
            cosmolteItemRecyclerViewAdapter.setCosmolteItems(cosmotleItemList);
            recyclerView.removeAllViews();
            cosmolteItemRecyclerViewAdapter.notifyDataSetChanged();

        }
    }

    public class DividerItemDecoration extends RecyclerView.ItemDecoration{

        private final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable divider;

        public DividerItemDecoration(Context context){
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            divider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + divider.getIntrinsicHeight();

                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
        }
    }

}
