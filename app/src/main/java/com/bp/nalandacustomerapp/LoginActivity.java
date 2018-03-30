package com.bp.nalandacustomerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bp.nalandacustomerapp.services.CommonConstants;
import com.bp.nalandacustomerapp.services.Validation;

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements Validation {
    private EditText txtUn, txtPw;
    private Button btnLoging;
    private final String URL_DB = CommonConstants.SITE_URL + "login_reg.php";
    ProgressDialog progressDialog;
    private TextView regLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUn = (EditText) findViewById(R.id.logUserName);
        txtPw = (EditText) findViewById(R.id.logPassword);
        btnLoging = (Button) findViewById(R.id.logBtn);
        regLink = (TextView) findViewById(R.id.reg_link);
        btnLoging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vlidateFields(txtUn, txtPw)) {
                    verifyUser(String.valueOf(txtUn.getText()), String.valueOf(txtPw.getText()));
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid inpus", Toast.LENGTH_LONG).show();
                }
            }
        });
        //backbtn
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        regLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean vlidateFields(EditText... editTexts) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (TextUtils.isEmpty(editTexts[0].getText())) {
            editTexts[0].setError("Username is required(username is your emil)!");
        } else if (!editTexts[0].getText().toString().trim().matches(emailPattern)) {
            editTexts[0].setError("Email is invalide!");
        } else if (TextUtils.isEmpty(editTexts[1].getText())) {
            editTexts[0].setError("Password is required");
        } else {
            return true;
        }
        return false;
    }

    private void verifyUser(String un, String pw) {
        class UserLoginTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("plz wait..");
                progressDialog.setCancelable(false);
                progressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                progressDialog.dismiss();

                if (s.toString().trim().equals("seccess")) {
                    Toast.makeText(LoginActivity.this, "ok", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
                super.onPostExecute(s);

            }

            @Override
            protected String doInBackground(String... strings) {
                String response = "";

                String data = null;
                try {
                    data = URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("login", "UTF-8") + "&" +
                            URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(strings[0], "UTF-8") + "&" +
                            URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(strings[1], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                try {
                    URL url = new URL(URL_DB);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));

                    bufferedWriter.write(data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        response += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return response;

                } catch (ClientProtocolException e) {
                    Toast.makeText(LoginActivity.this, "Protocol error", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Toast.makeText(LoginActivity.this, "IO error", Toast.LENGTH_LONG).show();
                }
                return response;
            }
        }
        new UserLoginTask().execute(un, pw);
    }

    //back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

