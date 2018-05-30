package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private ListView pList;
    private ProgressDialog progressDialog;
    private String[] productNameList;
    private String[] productIdList;
    private String[] productImgList;
    private String[] productPriceList;
    private String[] productStockList;
    private String[] productDisList;

    private List<String> subCatsList;
    private HashMap<Integer,String> subCatsMap;

    private Toolbar tb;
    private Spinner subCatSpinner;
    private Spinner sortSpinner;


    String id = "";
    String sid = "all";
    String sort = "";
    int sid_position = 0;
    String myUrl = CommonConstants.SITE_URL + "product_load.php";

    boolean error = false;
    boolean alreadyExecuted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Intent intent = getIntent();
        id = intent.getStringExtra("cat_id");

        tb =(Toolbar) findViewById(R.id.toolPList);
        subCatSpinner = (Spinner)findViewById(R.id.subCatSpinner);
        sortSpinner = (Spinner)findViewById(R.id.sortSpinner);
        pList = (ListView) findViewById(R.id.productList);

        subCatsList = new ArrayList<>();
        subCatsMap = new HashMap<>();


        subCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(ProductListActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                sid = subCatsMap.get(i);
                sid_position = i;

                if (!alreadyExecuted){
                    new BackgroundProductList().execute();
                }
                alreadyExecuted = false;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ProductListActivity.this, String.valueOf(i), Toast.LENGTH_LONG).show();
                sort = String.valueOf(i);
                if (!alreadyExecuted){
                    new BackgroundProductList().execute();
                }
                alreadyExecuted = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        pList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object product_id = (Object) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(ProductListActivity.this, ItemDetailActivity.class);
                intent.putExtra("product_id", String.valueOf(product_id));
                startActivity(intent);
            }
        });

        findViewById(R.id.back_button_plist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Toast.makeText(ProductListActivity.this,id,Toast.LENGTH_LONG).show();


            //new BackgroundProductList().execute();



    }

    class BackgroundProductList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ProductListActivity.this);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //subCatsList.add("test1");
            //subCatsList.add("test2");
            if (!error){
                ArrayAdapter<String> subCatAdp = new ArrayAdapter<String>(
                        ProductListActivity.this,
                        android.R.layout.simple_spinner_item,
                        subCatsList);
                subCatAdp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                subCatSpinner.setAdapter(subCatAdp);
                subCatSpinner.setSelection(sid_position);

                CustomProductListAdapter_1 adapter = new CustomProductListAdapter_1(ProductListActivity.this, productIdList, productImgList, productNameList, productPriceList, productStockList,productDisList);
                pList.setAdapter(adapter);
            }else{
                Toast.makeText(ProductListActivity.this, "No Products in this category", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(ProductListActivity.this , HomeActivity.class);
                //startActivity(intent);
            }

           // Toast.makeText(ProductListActivity.this, String.valueOf(sid), Toast.LENGTH_LONG).show();
            error = false;
            alreadyExecuted = true;
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String result = "";

            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(myUrl);
                ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cid", id));
                params.add(new BasicNameValuePair("sid", sid));
                params.add(new BasicNameValuePair("sort", sort));
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
                subCatsList.clear();
                subCatsMap.clear();
                subCatsList.add("ALL");
                subCatsMap.put(0, "all");

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("product_data").trim().equals("fail")){
                    error = true;
                }else{
                    error = false;
                    JSONArray jsonArray = jsonObject.getJSONArray("product_data");


                    if (!jsonObject.getString("subcat_data").equals("fail")){
                        JSONArray subcatArray = jsonObject.getJSONArray("subcat_data");
                        for (int i = 0; i < subcatArray.length(); i++) {
                            JSONObject s = subcatArray.getJSONObject(i);
                            subCatsList.add(s.getString("name"));
                            subCatsMap.put(i+1,s.getString("id"));
                        }
                    }
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


            }catch (Exception e){

            }

            return null;
        }
    }
}
