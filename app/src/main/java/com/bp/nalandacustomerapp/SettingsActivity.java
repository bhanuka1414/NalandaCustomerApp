package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CommonConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    private TextView back_button_profile_settings;
    private EditText name_profile_txt, email_profile_txt, phone_profile_txt, address_profile_txt;
    private Button update_user_setings_btn;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        name_profile_txt = (EditText) findViewById(R.id.name_profile_etxt);
        email_profile_txt = (EditText) findViewById(R.id.email_profile_etxt);
        phone_profile_txt = (EditText) findViewById(R.id.phone_profile_etxt);
        address_profile_txt = (EditText) findViewById(R.id.address_profile_etxt);

        update_user_setings_btn = findViewById(R.id.update_user_setings_btn);


        back_button_profile_settings = findViewById(R.id.back_button_profile_settings);

        back_button_profile_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("un", null);
        if (restoredText != null) {
            name_profile_txt.setText(prefs.getString("name", "Not Loging"), TextView.BufferType.EDITABLE);
            email_profile_txt.setText(prefs.getString("un", "Not Loging"), TextView.BufferType.EDITABLE);
            phone_profile_txt.setText(prefs.getString("phone", "Not Loging"), TextView.BufferType.EDITABLE);
            address_profile_txt.setText(prefs.getString("address", "Not Loging"), TextView.BufferType.EDITABLE);

        } else {
            Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(intent);

        }

        update_user_setings_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundUpdateSettings().execute();
            }
        });

    }


    class BackgroundUpdateSettings extends AsyncTask<String, Void, String> {
        String myUrl = CommonConstants.SITE_URL + "user_profile.php";
        //SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        //String restoredText = prefs.getString("un", null);
        //String cid = prefs.getString("id", "Not Loging");

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SettingsActivity.this);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(LocationPickerActivity.this,s,Toast.LENGTH_LONG).show();
            if (s.trim().equals("true")) {

                Toast.makeText(SettingsActivity.this, "ok", Toast.LENGTH_LONG).show();

                SharedPreferences.Editor editor = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("name", String.valueOf(name_profile_txt.getText()));
                editor.putString("address", String.valueOf(address_profile_txt.getText()));
                editor.putString("phone", String.valueOf(phone_profile_txt.getText()));
                editor.putString("un", String.valueOf(email_profile_txt.getText()));//email = un
                editor.apply();

                Toast.makeText(SettingsActivity.this, "Settings Updated!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(SettingsActivity.this, "error!", Toast.LENGTH_LONG).show();
            }


            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {

                SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cid", prefs.getString("id", null)));
                params.add(new BasicNameValuePair("name", String.valueOf(name_profile_txt.getText())));
                params.add(new BasicNameValuePair("address", String.valueOf(address_profile_txt.getText())));
                params.add(new BasicNameValuePair("phone", String.valueOf(phone_profile_txt.getText())));
                params.add(new BasicNameValuePair("email", String.valueOf(email_profile_txt.getText())));
                /*System.out.println("GO TO?????" + query_string);
                System.out.println("GO TO PARAM???" + params);*/
                httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                //view_account.setText(httpResponse.getStatusLine().toString());
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();

                BufferedReader bufReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder builder = new StringBuilder();
                String line = null;
                while ((line = bufReader.readLine()) != null) {
                    builder.append(line + "\n");
                }
                inputStream.close();
                result = builder.toString();
            } catch (Exception e) {
                Log.e("log_tag", e.toString());
            }


            return result;
        }
    }
}
