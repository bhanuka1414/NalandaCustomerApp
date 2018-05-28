package com.bp.nalandacustomerapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.DatabaseHelper;

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

public class WelcomeActivity extends AppCompatActivity {
    private TextView title;
    private TextView sub;
    private static int welcomeTimeOut = 2000;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        title=(TextView)findViewById(R.id.tit);
        sub = (TextView)findViewById(R.id.subtopic);
        databaseHelper = new DatabaseHelper(WelcomeActivity.this);

        Animation welcomeanim = AnimationUtils.loadAnimation(this,R.anim.mytra);
        title.startAnimation(welcomeanim);
        sub.startAnimation(welcomeanim);

        SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        String id = prefs.getString("id", null);
        if (id != null) {
            String token = "";

            Cursor res = databaseHelper.getUser();
            if (res.getCount() > 0) {

                while (res.moveToNext()) {
                    token = res.getString(1);

                }

            }

            new BackgroundRegToken().execute(id, token);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent homeavt = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(homeavt);
                    finish();
                }
            }, welcomeTimeOut);
        }
    }

    class BackgroundRegToken extends AsyncTask<String, Void, String> {
        String myUrl = CommonConstants.SITE_URL + "reg_firebase.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(WelcomeActivity.this,s,Toast.LENGTH_LONG).show();
            Intent homeavt = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(homeavt);
            finish();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cid", strings[0]));
                params.add(new BasicNameValuePair("Token", strings[1]));
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



/*

        //Toast.makeText(HomeActivity.this,testv,Toast.LENGTH_LONG).show();
        */
