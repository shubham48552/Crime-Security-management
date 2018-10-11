package com.example.ashwin.crimesecurity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class Register extends AppCompatActivity {
    EditText Age;
    EditText City;
    EditText Confirm;
    EditText Contact;
    EditText Email;
    EditText Name;
    EditText Password;
    EditText Username;
    public Activity activity = this;
    ProgressDialog prgDialog1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0174R.layout.activity_register);
        this.Name = (EditText) findViewById(C0174R.id.etName);
        this.Email = (EditText) findViewById(C0174R.id.etEmail);
        this.Age = (EditText) findViewById(C0174R.id.etAge);
        this.Contact = (EditText) findViewById(C0174R.id.etContact);
        this.Username = (EditText) findViewById(C0174R.id.etUsername);
        this.Password = (EditText) findViewById(C0174R.id.etPassword);
        this.City = (EditText) findViewById(C0174R.id.etCity);
        this.Confirm = (EditText) findViewById(C0174R.id.etConfirmPassword);
    }

    public void Upload(final Context c, String name, String age, String contact, String email, String password, String username, String city) {
        new AsyncTask<String, Void, String>() {
            private boolean issuccess = true;
            private String resp;

            protected void onPreExecute() {
                Register.this.prgDialog1 = new ProgressDialog(c);
                Register.this.prgDialog1.setCancelable(false);
                Register.this.prgDialog1.setProgressStyle(0);
                Register.this.prgDialog1.setIndeterminate(false);
                Register.this.prgDialog1.setMessage("Registering...");
                Register.this.prgDialog1.show();
            }

            protected String doInBackground(String... params) {
                String Firstname = params[0];
                String age = params[1];
                String contact = params[2];
                String email = params[3];
                String username = params[4];
                String password = params[5];
                String city = params[6];
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("firstname", Firstname));
                nameValuePairs.add(new BasicNameValuePair("age", age));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("password", password));
                nameValuePairs.add(new BasicNameValuePair("username", username));
                nameValuePairs.add(new BasicNameValuePair("city", city));
                nameValuePairs.add(new BasicNameValuePair("contact", contact));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpUriRequest httpPost = new HttpPost("http://crsecurity.pe.hu/test.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    this.resp = (String) httpClient.execute(httpPost, new BasicResponseHandler());
                } catch (IOException e) {
                    this.issuccess = false;
                } finally {
                    Register.this.prgDialog1.dismiss();
                }
                return "success";
            }

            protected void onPostExecute(String result) {
                if (this.issuccess) {
                    Toast.makeText(c, this.resp, 1).show();
                    if (this.resp.equals("yes")) {
                        Toast.makeText(c, "Registered", 1).show();
                        Register.this.activity.finish();
                        Register.this.startactivity();
                        return;
                    }
                    Toast.makeText(c, "Not Registered", 1).show();
                    return;
                }
                Toast.makeText(c, "Error connecting server", 1).show();
            }
        }.execute(new String[]{name, age, contact, email, username, password, city});
    }

    public void startactivity() {
        startActivity(new Intent(this, Login.class));
    }

    public void register(View view) {
        String name = this.Name.getText().toString();
        String age = this.Age.getText().toString();
        String email = this.Email.getText().toString();
        String contact = this.Contact.getText().toString();
        String username = this.Username.getText().toString();
        String password = this.Password.getText().toString();
        String city = this.City.getText().toString();
        String confirmpass = this.Confirm.getText().toString();
        if (name.equals("") || age.equals("") || email.equals("") || contact.equals("") || username.equals("") || password.equals("") || confirmpass.equals("") || city.equals("")) {
            Toast.makeText(this, "Some Fields Are Empty", 1).show();
        } else if (password.equals(confirmpass)) {
            Upload(this, name, age, contact, email, password, username, city);
        } else {
            Toast.makeText(this, "Password doesn't match", 1).show();
        }
    }
}
