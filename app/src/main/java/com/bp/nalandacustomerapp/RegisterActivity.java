package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import org.apache.http.client.ClientProtocolException;
import java.io.IOException;
import java.io.InputStream;



import java.io.BufferedReader;
import java.io.BufferedWriter;
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
            String response = "";
            String urlString = "https://nalanda-super.000webhostapp.com/android/user/login_reg.php";
            /*
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            nameValuePairs.add(new BasicNameValuePair("name", strings[0]));
            nameValuePairs.add(new BasicNameValuePair("address", strings[1]));
            nameValuePairs.add(new BasicNameValuePair("phone", strings[2]));
            nameValuePairs.add(new BasicNameValuePair("email", strings[3]));
            nameValuePairs.add(new BasicNameValuePair("password", strings[4]));
            */
            String data = null;
            try {
                data =  URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(strings[0],"UTF-8")+"&"+
                        URLEncoder.encode("address","UTF-8")+"="+URLEncoder.encode(strings[1],"UTF-8")+"&"+
                        URLEncoder.encode("phone","UTF-8")+"="+URLEncoder.encode(strings[2],"UTF-8")+"&"+
                        URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(strings[3],"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(strings[4],"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            try {
                URL url = new URL(urlString);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String line = "";
                while ((line = bufferedReader.readLine())!=null)
                {
                    response+= line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }
            return response;
        }
    }
}
