package com.captech.cosmonauts.cosmolte.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.captech.cosmonauts.cosmolte.ProfileInfoSingleton;
import com.captech.cosmonauts.cosmolte.R;
import com.captech.cosmonauts.cosmolte.fragments.CosmolteCalendarListFragment;
import com.captech.cosmonauts.cosmolte.fragments.CosmolteListFragment;
import com.captech.cosmonauts.cosmolte.network.CosmolteService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;

public class MainTabsActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainTabsActivity";
    private static final int NUMBER_OF_TABS = 3;
    private static final int ACTIVITY_RESULT_SIGNIN = 9001;

    private ViewPager pager;
    private CosmolteTotalReceiever cosmolteTotalReceiever;
    private TextView totalView;
    private GoogleApiClient googleApiClient;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        CosmoltePagerAdapter pagerAdapter = new CosmoltePagerAdapter(getSupportFragmentManager());

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setOffscreenPageLimit(3);
        pager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(pager);

        totalView = (TextView) findViewById(R.id.total);

        final View fabView = findViewById(R.id.fab);
        fabView.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(fabView.getContext(), CheckInActivity.class);
                startActivity(intent);
            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail().
                build();
        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).
                build();

        cosmolteTotalReceiever = new CosmolteTotalReceiever();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(CosmolteService.INTENT_TOTAL_ACTION);
        registerReceiver(cosmolteTotalReceiever, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(cosmolteTotalReceiever);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tabs_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_profile) {
            signIn();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, ACTIVITY_RESULT_SIGNIN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Play services connection failed -- " + connectionResult);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_RESULT_SIGNIN) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                Log.d(TAG, "Signing account " + googleSignInAccount.getGivenName());
                ProfileInfoSingleton profileInfoSingleton = ProfileInfoSingleton.getInstance();
                profileInfoSingleton.setEmail(googleSignInAccount.getEmail());
                profileInfoSingleton.setFirstName(googleSignInAccount.getGivenName());
                profileInfoSingleton.setLastName(googleSignInAccount.getFamilyName());
                toolbarTitle.setText(getResources().getString(R.string.cosmolte_welcome) + " - " + profileInfoSingleton.getFirstName());
                Toast.makeText(this, "Welcome " + profileInfoSingleton.getFullName(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public class CosmolteTotalReceiever extends BroadcastReceiver {

        private final static String TAG = "CosmolteTotalReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Recieved intent total with args --- " + intent.hasExtra(CosmolteService.EXTRA_TOTAL));

            totalView.setText(String.valueOf(intent.getIntExtra(CosmolteService.EXTRA_TOTAL, 0)));
        }
    }

    public static class CosmoltePagerAdapter extends FragmentPagerAdapter {

        private final static String TAG = "CosmoltePagerAdapter";

        public CosmoltePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "Getting item ---> " + position);
            switch (position) {
                case 0:
                    return CosmolteListFragment.newInstance(CosmolteService.EXPENSE_ITEM_TYPE);
                case 1:
                    return CosmolteListFragment.newInstance(CosmolteService.FREE_ITEM_TYPE);
                case 2:
                    return CosmolteCalendarListFragment.newInstance();
                default:
                    return null;
            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Expense";
                case 1:
                    return "Free";
                case 2:
                    return "Calendar";
                default:
                    return "none";
            }
        }

        @Override
        public int getCount() {
            return NUMBER_OF_TABS;
        }
    }
}
