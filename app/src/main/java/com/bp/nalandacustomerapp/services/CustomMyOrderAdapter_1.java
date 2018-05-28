package com.bp.nalandacustomerapp.services;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.YuvImage;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.HomeActivity;
import com.bp.nalandacustomerapp.MyOrdersActivity;
import com.bp.nalandacustomerapp.R;
import com.bp.nalandacustomerapp.SettingsActivity;
import com.bp.nalandacustomerapp.TrackOrderActivity;
import com.bp.nalandacustomerapp.services.models.OrderModel;
import com.squareup.picasso.Picasso;

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

public class CustomMyOrderAdapter_1 extends BaseAdapter {
    private Context context;
    private ArrayList<OrderModel> items;
    //private DatabaseHelper db;
    private ProgressDialog progressDialog;

    public CustomMyOrderAdapter_1() {
    }

    public CustomMyOrderAdapter_1(Context context, ArrayList<OrderModel> items) {
        this.context = context;
        this.items = items;
        //db = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate the layout for each list row
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.order_layout, parent, false);
        }
        final OrderModel orderItem = (OrderModel) getItem(position);

        TextView oidTxt = (TextView) convertView.findViewById(R.id.order_id);
        TextView dateTxt = (TextView) convertView.findViewById(R.id.order_date);
        TextView amrTxt = (TextView) convertView.findViewById(R.id.order_amount);
        Button trackBtn = (Button) convertView.findViewById(R.id.teackOrderBtn);
        Button feedbackBtn = (Button) convertView.findViewById(R.id.sendFeedbackBtn);
        final EditText feedbacktxt = (EditText) convertView.findViewById(R.id.feedbackTxt);
        //ImageButton btnRemove = (ImageButton) convertView.findViewById(R.id.cartItemRemoveBtn);
        if (orderItem.getConfirm().trim().equals("1") && orderItem.getFinish_driver().trim().equals("0")) {
            convertView.findViewById(R.id.teackOrderBtn).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.teackOrderBtn).setVisibility(View.GONE);
        }

        trackBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                //view.setBackgroundColor(R.color.colorPrimaryDark);
                Intent intent = new Intent(context, TrackOrderActivity.class);
                intent.putExtra("order_id", orderItem.getOno());
                context.startActivity(intent);
            }
        });

        feedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context,String.valueOf(orderItem.getOno()),Toast.LENGTH_LONG).show();
                new BackgroundFeedbacks().execute(String.valueOf(orderItem.getOno()), String.valueOf(feedbacktxt.getText()));
            }
        });

        oidTxt.setText(orderItem.getOno());
        dateTxt.setText(orderItem.getDate());
        amrTxt.setText(orderItem.getAmount());
        return convertView;
    }


    class BackgroundFeedbacks extends AsyncTask<String, Void, String> {
        String myUrl = CommonConstants.SITE_URL + "save_feedbacks.php";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //Toast.makeText(LocationPickerActivity.this,s,Toast.LENGTH_LONG).show();
            if (s.trim().equals("true")) {

                Toast.makeText(context, "Feedback sent successfully", Toast.LENGTH_LONG).show();


            } else {
                Toast.makeText(context, "error!", Toast.LENGTH_LONG).show();
            }


            progressDialog.dismiss();
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            try {

                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("oid", strings[0]));
                params.add(new BasicNameValuePair("msg", strings[1]));

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
