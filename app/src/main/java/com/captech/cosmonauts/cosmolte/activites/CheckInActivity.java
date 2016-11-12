package com.captech.cosmonauts.cosmolte.activites;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.captech.cosmonauts.cosmolte.ProfileInfoSingleton;
import com.captech.cosmonauts.cosmolte.R;

import java.util.ArrayList;

public class CheckInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        TextView nameTextView = (TextView) findViewById(R.id.nameTextView);

        ProfileInfoSingleton profileInfoSingleton = ProfileInfoSingleton.getInstance();
        if (profileInfoSingleton.getFirstName() == null || profileInfoSingleton.getFirstName().equals("")) {
            Spinner spinner = (Spinner) findViewById(R.id.nameSpinner);
            spinner.setVisibility(View.VISIBLE);
            nameTextView.setVisibility(View.GONE);
            String[] nameArray = new String[profileInfoSingleton.getNameList().size()];
            nameArray = (String[]) profileInfoSingleton.getNameList().toArray(nameArray);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_dropdown_item,
                    nameArray);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);
        } else {
            /*View questionLabel = findViewById(R.id.questionLabel);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) questionLabel.getLayoutParams();
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.nav_header_height), 0, 0);*/
            nameTextView.setText(ProfileInfoSingleton.getInstance().getFirstName());
        }

        View expenseButton = findViewById(R.id.expenseButton);
        expenseButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {


            }
        });

    }
}
