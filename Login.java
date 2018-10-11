package com.example.ashwin.crimesecurity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Login extends AppCompatActivity {
    EditText Password;
    EditText Username;
    public Activity activity = this;
    ProgressDialog prgDialog1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0174R.layout.activity_login);
        this.Username = (EditText) findViewById(C0174R.id.Username);
        this.Password = (EditText) findViewById(C0174R.id.Password);
        if (getSharedPreferences("My_Prefs", 0).getBoolean("value", false)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void Upload(final Context c, String password, String username) {
        new AsyncTask<String, Void, String>() {
            private String Phonenumber;
            private boolean issuccess = true;
            private String resp;

            protected void onPreExecute() {
                Login.this.prgDialog1 = new ProgressDialog(c);
                Login.this.prgDialog1.setCancelable(false);
                Login.this.prgDialog1.setProgressStyle(0);
                Login.this.prgDialog1.setIndeterminate(false);
                Login.this.prgDialog1.setMessage("Process...");
                Login.this.prgDialog1.show();
            }

            protected String doInBackground(String... params) {
                String username = params[0];
                String password = params[1];
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpUriRequest httpPost = new HttpPost("http://crsecurity.pe.hu/newlogin.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    this.resp = (String) httpClient.execute(httpPost, new BasicResponseHandler());
                } catch (IOException e) {
                    this.issuccess = false;
                } finally {
                    Login.this.prgDialog1.dismiss();
                }
                return "success";
            }

            protected void onPostExecute(String result) {
                if (this.issuccess) {
                    Toast.makeText(c, this.resp, 1).show();
                    if (this.resp.equals("yes")) {
                        Toast.makeText(c, "Login Successfully", 1).show();
                        Login.this.activity.finish();
                        Editor editor = Login.this.getSharedPreferences("My_Prefs", 0).edit();
                        Login.this.Username = (EditText) Login.this.findViewById(C0174R.id.Username);
                        editor.putString("username", Login.this.Username.getText().toString());
                        editor.putBoolean("value", true);
                        editor.commit();
                        Login.this.startactivity();
                        return;
                    }
                    Toast.makeText(c, "Not Registered", 1).show();
                    return;
                }
                Toast.makeText(c, "Error connecting server", 1).show();
            }
        }.execute(new String[]{username, password});
    }

    public void startactivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void login(View view) {
        String username = this.Username.getText().toString();
        String password = this.Password.getText().toString();
        if (username.equals("") && password.equals("")) {
            Toast.makeText(this, "Some Fields Are Empty", 1).show();
        } else {
            Upload(this, password, username);
        }
    }

    public void register(View view) {
        startActivity(new Intent(this, Register.class));
    }
}
