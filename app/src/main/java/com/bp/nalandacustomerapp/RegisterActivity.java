package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedOutputStream;
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
        new UserRegtask().execute();
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
            OutputStream out = null;
            try {

                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                String line ="";
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                while ((line = br.readLine()) !=null){
                    res +=line;
                }


            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            //res  = strings[0];
            return res;
        }
    }
}
