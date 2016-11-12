package com.captech.cosmonauts.cosmolte.network;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.captech.cosmonauts.cosmolte.ProfileInfoSingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jvazquez on 7/26/16.
 */
public class CosmolteService extends IntentService {

    private static final String COLLECTIONS_URL = "https://api.mongolab.com/api/1/databases/heroku_lphx2t8m/collections/";
    private static final String EVENTS_URL = "https://api.mongolab.com/api/1/databases/heroku_lphx2t8m/collections/";
    private static final String DEFAULT_API_KEY = "NgGXg3kUA9V4eh_fWe-ponEZCk7EINv2";
    private static final String TAG = "CosmolteService";
    private static final int MAX_NUM_CALENDAR_ITEMS = 6;

    public static final String EXPENSE_ITEM_TYPE = "expense";
    public static final String FREE_ITEM_TYPE = "free";
    public static final String OPERATION_ITEMS_TYPE = "items";
    public static final String OPERATION_CALENDAR_ITEMS_TYPE = "calendar-items";
    public static final String OPERATION_POST_CREDIT_TYPE = "post-credit";


    public static final String INTENT_LISTEXPENSE_ACTION = "com.captech.cosmonauts.cosmolte.LISTEXPENSE";
    public static final String INTENT_LISTFREE_ACTION = "com.captech.cosmonauts.cosmolte.LISTFREE";
    public static final String INTENT_TOTAL_ACTION = "com.captech.cosmonauts.cosmolte.TOTAL";
    public static final String INTENT_LISTCALENDAR_ACTION = "com.captech.cosmonauts.cosmolte.LISTCALENDAR";

    public static final String EXTRA_OPERATION_TOCALL = "operation-tocall";
    public static final String EXTRA_LIST = "cosmolte-list";
    public static final String EXTRA_TOTAL = "total";
    public static final String EXTRA_ITEM_TYPE = "item-type";
    public static final String EXTRA_CALENDAR_LIST = "cosmolte-calendar-list";

    private int totalExpense = 0;
    private int totalFree = 0;


    public CosmolteService() {
        super("CosmolteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        String operationToCall = intent.getStringExtra(EXTRA_OPERATION_TOCALL);
        if (operationToCall != null && operationToCall.equalsIgnoreCase(OPERATION_ITEMS_TYPE)) {
            String type = intent.getStringExtra(EXTRA_ITEM_TYPE);
            Log.d(TAG, "Calling with type --> " + type);
            if (TextUtils.isEmpty(type)) {
                type = EXPENSE_ITEM_TYPE;
            }

            getItems(type);
        } else if (operationToCall != null && operationToCall.equalsIgnoreCase(OPERATION_CALENDAR_ITEMS_TYPE)) {
            getCalendarItems();
        } else if (operationToCall != null && operationToCall.equalsIgnoreCase(OPERATION_POST_CREDIT_TYPE)) {
            String itemType = intent.getStringExtra(EXTRA_ITEM_TYPE);
            postCredit(itemType);
        }


    }

    private void getItems(String type) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl(COLLECTIONS_URL).
                addConverterFactory(GsonConverterFactory.create()).build();

        CosmotleItems cosmotleItems = retrofit.create(CosmotleItems.class);

        Call<List<CosmotleItem>> call = cosmotleItems.retrieveItems(type, DEFAULT_API_KEY);

        try {
            List<CosmotleItem> cosmotleItemList = call.execute().body();
            if (cosmotleItemList != null && !cosmotleItemList.isEmpty()) {


                Intent listUpdateIntent = new Intent();
                if (type.equalsIgnoreCase(EXPENSE_ITEM_TYPE)) {
                    listUpdateIntent.setAction(INTENT_LISTEXPENSE_ACTION);
                    for (CosmotleItem cosmotleItem : cosmotleItemList) {
                        totalExpense += cosmotleItem.getCount();
                    }
                } else {
                    listUpdateIntent.setAction(INTENT_LISTFREE_ACTION);
                    for (CosmotleItem cosmotleItem : cosmotleItemList) {
                        totalFree += cosmotleItem.getCount();
                    }
                }

                listUpdateIntent.putParcelableArrayListExtra(EXTRA_LIST, (ArrayList<CosmotleItem>) cosmotleItemList);
                //TODO: Should this be local or move to event bus impl?
                sendBroadcast(listUpdateIntent);

                if (totalExpense != 0 && totalFree != 0) {
                    Intent totalUpdateIntent = new Intent();
                    totalUpdateIntent.setAction(INTENT_TOTAL_ACTION);
                    totalUpdateIntent.putExtra(EXTRA_TOTAL, totalExpense + totalFree);
                    sendBroadcast(totalUpdateIntent);
                }

            }

        } catch (IOException exception) {

        }

    }

    private void postCredit(String itemType){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(COLLECTIONS_URL).
                addConverterFactory(GsonConverterFactory.create()).build();

        CosmotleItem item = new CosmotleItem();
        item.setName(ProfileInfoSingleton.getInstance().getFirstName());

        CosmotleItems cosmotleItems = retrofit.create(CosmotleItems.class);

        Call<CosmotleItem> call = cosmotleItems.postCredit(itemType, item);

        try {
            item = call.execute().body();

        } catch( IOException ioException){

        }

    }

    private void getCalendarItems() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(EVENTS_URL).
                addConverterFactory(GsonConverterFactory.create()).build();

        CosmolteCalendarItems cosmolteCalendarItems = retrofit.create(CosmolteCalendarItems.class);

        Call<List<CosmolteCalendarItem>> call = cosmolteCalendarItems.retrieveCalendarItems(DEFAULT_API_KEY);

        try {
            List<CosmolteCalendarItem> cosmolteCalendarItemList = call.execute().body();
            if (cosmolteCalendarItemList != null && !cosmolteCalendarItemList.isEmpty()) {
                Log.d(TAG, "Received elements for calendar" + cosmolteCalendarItemList.size());
                if (cosmolteCalendarItemList.size() > MAX_NUM_CALENDAR_ITEMS) {
                    cosmolteCalendarItemList.subList(0, cosmolteCalendarItemList.size() - MAX_NUM_CALENDAR_ITEMS).clear();
                }
                Collections.sort(cosmolteCalendarItemList);

                Intent listCalendarIntent = new Intent(INTENT_LISTCALENDAR_ACTION);
                listCalendarIntent.putParcelableArrayListExtra(EXTRA_CALENDAR_LIST, (ArrayList<CosmolteCalendarItem>) cosmolteCalendarItemList);
                sendBroadcast(listCalendarIntent);
            }

        } catch (IOException exception) {
            //TODO throw a snack?
        }
    }


}
