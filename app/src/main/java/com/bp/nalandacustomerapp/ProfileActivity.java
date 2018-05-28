package com.bp.nalandacustomerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bp.nalandacustomerapp.services.CommonConstants;

public class ProfileActivity extends AppCompatActivity {
    private TextView back_button_profile, settings_button_profile, name_profile_txt, email_profile_txt, phone_profile_txt, address_profile_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_profile_txt = findViewById(R.id.name_profile_txt);
        email_profile_txt = findViewById(R.id.email_profile_txt);
        phone_profile_txt = findViewById(R.id.phone_profile_txt);
        address_profile_txt = findViewById(R.id.address_profile_txt);


        back_button_profile = findViewById(R.id.back_button_profile);
        settings_button_profile = findViewById(R.id.settings_button_profile);

        back_button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        settings_button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("un", null);
        if (restoredText != null) {
            findViewById(R.id.signin_layout_profile).setVisibility(RelativeLayout.GONE);
            // nav_Menu.findItem(R.id.nav_signin).setVisible(false);
            name_profile_txt.setText(prefs.getString("name", "Not Loging"));
            email_profile_txt.setText(prefs.getString("un", "Not Loging"));
            phone_profile_txt.setText(prefs.getString("phone", "Not Loging"));
            address_profile_txt.setText(prefs.getString("address", "Not Loging"));

        } else {
            settings_button_profile.setVisibility(View.GONE);
            findViewById(R.id.loginBtnProfile).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }
}
