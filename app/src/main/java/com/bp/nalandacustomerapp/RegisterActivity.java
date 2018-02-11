package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterActivity extends AppCompatActivity {
    private EditText name, address, phone, email, pw, rePw;
    private Button regBtn;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        pw = (EditText) findViewById(R.id.password);
        rePw = (EditText) findViewById(R.id.rePassword);
        regBtn = (Button) findViewById(R.id.regButton);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = String.valueOf(name.getText());
                String a = String.valueOf(address.getText());
                String pn = String.valueOf(phone.getText());
                String e = String.valueOf(email.getText());
                String pass = String.valueOf(pw.getText());
                String rePass = String.valueOf(rePw.getText());
                if (vlidateFields(name, address, phone, email, pw, rePw)) {
                    insertUser(n,a,pn,e,pass);
                }else {
                    //error
                }
            }
        });

    }

    private void insertUser(String n, String a, String pn, String e, String pw) {


        //Toast.makeText(RegisterActivity.this,data,Toast.LENGTH_LONG).show();
        new UserRegtask().execute(n,a,pn,e,pw);
    }

    private boolean vlidateFields(EditText ... editTexts) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (TextUtils.isEmpty(editTexts[0].getText())) {
            editTexts[0].setError( "Name is required!" );
        }else if (TextUtils.isEmpty(editTexts[1].getText())) {
            editTexts[1].setError( "Address is required!" );
        }else if (TextUtils.isEmpty(editTexts[2].getText())) {
            editTexts[2].setError( "Phone number is required!" );
        }else if (TextUtils.isEmpty(editTexts[3].getText())) {
            editTexts[3].setError( "Email is required!" );
        }else if (!editTexts[3].getText().toString().trim().matches(emailPattern)){
            editTexts[3].setError( "Email is invalide!" );
        }else if (TextUtils.isEmpty(editTexts[4].getText())) {
            editTexts[4].setError( "Password is required!" );
        }else if (TextUtils.isEmpty(editTexts[5].getText())) {
            editTexts[5].setError( "Re-password is required!" );
        }else if(!editTexts[4].getText().toString().trim().equals(editTexts[5].getText().toString().trim())){
            editTexts[5].setError( "Re-password is not match!" );
        }else{
            return true;
        }
        return false;
    }

    class UserRegtask extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("plz wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            address.setText(s);
            super.onPostExecute(s);

        }

        @Override
        protected String doInBackground(String... strings) {
            String res = "";
            String urlString = "https://nalanda-super.000webhostapp.com/android/user/login_reg.php";
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("name", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("address", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("phone", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("email", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("password", strings[4]));

            try {
                HttpClient httpClient = new DefaultHttpClient();

                HttpPost httpPost = new HttpPost(urlString);

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                res = String.valueOf(httpEntity.getContent());

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return res;
        }
    }
}
