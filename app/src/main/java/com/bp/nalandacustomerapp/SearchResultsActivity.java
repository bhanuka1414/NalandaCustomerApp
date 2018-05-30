package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.CustomProductListAdapter_1;

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

public class SearchResultsActivity extends AppCompatActivity {

    private ListView pList;
    private ProgressDialog progressDialog;
    private String[] productNameList;
    private String[] productIdList;
    private String[] productImgList;
    private String[] productPriceList;
    private String[] productStockList;
    private String[] productDisList;
    boolean error = false;
    private SearchView searchView;

    String val = "";
    TextView title_text_search_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Intent intent = getIntent();
        val = intent.getStringExtra("searchVal");

        pList = (ListView) findViewById(R.id.searchList);
        title_text_search_result = (TextView) findViewById(R.id.title_text_search_result);
        searchView = (SearchView) findViewById(R.id.productSearchView1);


        findViewById(R.id.back_button_profile_search_result).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object product_id = (Object) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(SearchResultsActivity.this, ItemDetailActivity.class);
                intent.putExtra("product_id", String.valueOf(product_id));
                startActivity(intent);
            }
        });


        //Toast.makeText(SearchResultsActivity.this,"searchVal",Toast.LENGTH_SHORT).show();
        new BackgroundSearch().execute();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                val = s;
                new BackgroundSearch().execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }


    class BackgroundSearch extends AsyncTask<Void, Void, Void> {
        String myUrl = CommonConstants.SITE_URL + "search.php";

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SearchResultsActivity.this);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //subCatsList.add("test1");
            //subCatsList.add("test2");
            //Toast.makeText(SearchResultsActivity.this, test, Toast.LENGTH_LONG).show();
            if (!error) {
                CustomProductListAdapter_1 adapter = new CustomProductListAdapter_1(SearchResultsActivity.this, productIdList, productImgList, productNameList, productPriceList, productStockList, productDisList);
                pList.setAdapter(adapter);
            } else {
                Toast.makeText(SearchResultsActivity.this, "No Products", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ProductListActivity.this , HomeActivity.class);
                //startActivity(intent);
            }

            // Toast.makeText(ProductListActivity.this, String.valueOf(sid), Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
            title_text_search_result.setText(val);
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String result = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("search", val));

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

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("product_data").trim().equals("fail")) {
                    error = true;
                } else {
                    error = false;
                    JSONArray jsonArray = jsonObject.getJSONArray("product_data");

                    productImgList = new String[jsonArray.length()];
                    productNameList = new String[jsonArray.length()];
                    productIdList = new String[jsonArray.length()];
                    productPriceList = new String[jsonArray.length()];
                    productStockList = new String[jsonArray.length()];
                    productDisList = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        productNameList[i] = c.getString("name");
                        productImgList[i] = c.getString("img");
                        productIdList[i] = c.getString("id");
                        productPriceList[i] = c.getString("unit_price");
                        productStockList[i] = c.getString("qty");
                        productDisList[i] = c.getString("discription_long");
                        //JSONObject p = c.getJSONObject("phone");
                        //hashMap.put("mob",p.getString("mobile"));
                    }
                }


            } catch (Exception e) {

            }

            return null;
        }
    }
}
