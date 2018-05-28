package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.CustomMyOrderAdapter_1;
import com.bp.nalandacustomerapp.services.models.OrderModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {

    private ListView myOrderList;
    TextView ttt;
    private ProgressDialog progressDialog;
    private String myUrl = CommonConstants.SITE_URL + "my_orders.php";
    private ArrayList<OrderModel> orderList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        myOrderList = (ListView) findViewById(R.id.listMyOrders);
        orderList = new ArrayList<>();
        ttt = (TextView) findViewById(R.id.restmp);
        SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("id", null);
        if (restoredText != null) {

            new BackgroundOrderList().execute(prefs.getString("id", "Not Loging"));

        } else {
            Toast.makeText(MyOrdersActivity.this, "not loging", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent = new Intent(MyOrdersActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }, 2000);
        }

        findViewById(R.id.back_button_myorders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    class BackgroundOrderList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MyOrdersActivity.this);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(MyOrdersActivity.this,s,Toast.LENGTH_SHORT).show();
            //ttt.setText(s);
            CustomMyOrderAdapter_1 adapter = new CustomMyOrderAdapter_1(MyOrdersActivity.this, orderList);
            myOrderList.setAdapter(adapter);
            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String status = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cid", strings[0]));
                //params.add(new BasicNameValuePair("sid", "YUYUTYU"));
                //params.add(new BasicNameValuePair("sort", "GHYGHJGHJG"));
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
            try {

                orderList.clear();
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("order_data").trim().equals("fail")) {
                    status = "error";

                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("order_data");
                    status = "suc";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        orderList.add(new OrderModel(c.getString("oid"), c.getString("date_time"), c.getString("confirm"), c.getString("c_remove"), c.getString("finish_deliver"), c.getString("amt")));
                        //JSONObject p = c.getJSONObject("phone");
                        //hashMap.put("mob",p.getString("mobile"));
                    }
                }


            } catch (Exception e) {

            }

            return status;
        }
    }

}
