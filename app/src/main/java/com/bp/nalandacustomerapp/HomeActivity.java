package com.bp.nalandacustomerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.CustomListAdapter_1;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView userMailNav, logingLink;
    private String[] catNameList;
    private String[] catIdList;
    private String[] catImgList;
    private ListView cList;
    private ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Menu nav_Menu = navigationView.getMenu();


        //regLink = (TextView) header.findViewById(R.id.reg_link);
        SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
        String restoredText = prefs.getString("un", null);
        String name = "";
        if (restoredText != null) {
            name = prefs.getString("un", "Not Loging");
            nav_Menu.findItem(R.id.nav_signin).setVisible(false);

        }else{
            //Toast.makeText(HomeActivity.this,"not loging",Toast.LENGTH_SHORT).show();
        }
         userMailNav = (TextView) header.findViewById(R.id.userEmailNav);
         userMailNav.setText(name);


        cList = (ListView) findViewById(R.id.catList);
        bar = (ProgressBar) this.findViewById(R.id.progressBar);

        new BackgroundJson().execute();

        cList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String catId = ((TextView) view.findViewById(R.id.cat_id)).getText().toString();
                Intent intent = new Intent(HomeActivity.this, ProductListActivity.class);
                intent.putExtra("cat_id", catId);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_logout){
            SharedPreferences prefs = getSharedPreferences(CommonConstants.USER_PREFS_NAME, MODE_PRIVATE);
            prefs.edit().clear().commit();

            Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_signin) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_myaccount) {
            // Intent intent = new Intent(HomeActivity.this, ProductActivity.class);
            // startActivity(intent);
        } else if (id == R.id.nav_myorders) {
            Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_cart) {
            Intent intent = new Intent(HomeActivity.this , CartActivity.class);
            startActivity(intent);

        }else if (id == R.id.nav_message) {
            Intent intent = new Intent(HomeActivity.this, MyOrdersActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    class BackgroundJson extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            /*progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setIndeterminate(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();*/
            bar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void v) {
            bar.setVisibility(View.GONE);
            //ListAdapter adapter = new SimpleAdapter(HomeActivity.this, productList, R.layout.product_listview,
            //new String[]{"id", "name", "img"}, new int[]{R.id.nametxt, R.id.emtxt, R.id.pnotxt});
            //pList.setAdapter(adapter);
            CustomListAdapter_1 adapter = new CustomListAdapter_1(HomeActivity.this, catIdList, catImgList, catNameList);
            cList.setAdapter(adapter);
            super.onPostExecute(v);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String myUrl = CommonConstants.SITE_URL + "cat_load.php";
            String json = " ";

            try {
                URL url = new URL(myUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                String line = "";


                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                //line = br.readLine().toString();
                while ((line = br.readLine()) != null) {
                    json += line;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("cat_data");
                catImgList = new String[jsonArray.length()];
                catNameList = new String[jsonArray.length()];
                catIdList = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);
                    catNameList[i] = c.getString("name");
                    catImgList[i] = c.getString("img");
                    catIdList[i] = c.getString("id");
                    //JSONObject p = c.getJSONObject("phone");
                    //hashMap.put("mob",p.getString("mobile"));
                }

                //testing
                // Iterator<HashMap<String,String>> it = full_lList.iterator();
        /*    for (HashMap<String,String> h : full_lList){

                String name = h.get("name");
                String em = h.get("email");
                String p = h.get("mob");

                test += name+" "+em+" "+p+"\n";

            }*/

            } catch (Exception e) {

            }

            return null;
        }
    }

}
