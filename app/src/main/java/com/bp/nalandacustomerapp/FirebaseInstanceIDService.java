package com.bp.nalandacustomerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.DatabaseHelper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by filipp on 5/23/2016.
 */
public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    private DatabaseHelper databaseHelper;

    @Override
    public void onTokenRefresh() {

        String token = FirebaseInstanceId.getInstance().getToken();
        databaseHelper = new DatabaseHelper(this);
        databaseHelper.clearUser();

        registerToken(token);
    }

    private void registerToken(String token) {
        databaseHelper = new DatabaseHelper(this);

        databaseHelper.saveUserData(1, token);


        /*OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Token",token).add("cid",id.trim())
                .build();

        Request request = new Request.Builder()
                .url("https://nalandamart.ml/android/user/reg_firebase.php")
                .post(body)
                .build();

        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}