package com.example.ashwin.crimesecurity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class Complaint extends AppCompatActivity {
    private static final int TAKE_PHOTO_CODE = 1;
    EditText Age;
    EditText Contact;
    String Convetedimg;
    EditText Description;
    EditText Email;
    EditText Name;
    private final String TAG = getClass().getName();
    public Activity activity = this;
    int count = 0;
    public int count2;
    String dir;
    ProgressDialog prgDialog1;
    ProgressDialog prgDialog2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0174R.layout.activity_complaint);
        this.Name = (EditText) findViewById(C0174R.id.etName);
        this.Age = (EditText) findViewById(C0174R.id.etAge);
        this.Contact = (EditText) findViewById(C0174R.id.etContact);
        this.Email = (EditText) findViewById(C0174R.id.etEmail);
        this.Description = (EditText) findViewById(C0174R.id.etDescription);
    }

    public void Upload(final Context c, String name, String age, String contact, String email, String description) {
        new AsyncTask<String, Void, String>() {
            private boolean issuccess = true;
            private String resp;

            protected void onPreExecute() {
                Complaint.this.prgDialog1 = new ProgressDialog(c);
                Complaint.this.prgDialog1.setCancelable(false);
                Complaint.this.prgDialog1.setProgressStyle(0);
                Complaint.this.prgDialog1.setIndeterminate(false);
                Complaint.this.prgDialog1.setMessage("Registering...");
                Complaint.this.prgDialog1.show();
            }

            protected String doInBackground(String... params) {
                String name = params[0];
                String age = params[1];
                String contact = params[2];
                String email = params[3];
                String description = params[4];
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("age", age));
                nameValuePairs.add(new BasicNameValuePair("contact", contact));
                nameValuePairs.add(new BasicNameValuePair("email", email));
                nameValuePairs.add(new BasicNameValuePair("description", description));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpUriRequest httpPost = new HttpPost("http://crsecurity.pe.hu/complaint.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    this.resp = (String) httpClient.execute(httpPost, new BasicResponseHandler());
                } catch (IOException e) {
                    this.issuccess = false;
                } finally {
                    Complaint.this.prgDialog1.dismiss();
                }
                return "success";
            }

            protected void onPostExecute(String result) {
                if (this.issuccess) {
                    Toast.makeText(c, this.resp, 1).show();
                    if (this.resp.equals("yes")) {
                        Toast.makeText(c, "Registered", 1).show();
                        Complaint.this.activity.finish();
                        Complaint.this.startactivity();
                        return;
                    }
                    Toast.makeText(c, "Not Registered", 1).show();
                    return;
                }
                Toast.makeText(c, "Error connecting server", 1).show();
            }
        }.execute(new String[]{name, age, contact, email, description});
    }

    public void startactivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void register(View view) {
        String name = this.Name.getText().toString();
        String age = this.Age.getText().toString();
        String contact = this.Contact.getText().toString();
        String email = this.Email.getText().toString();
        String description = this.Description.getText().toString();
        if (name.equals("") || age.equals("") || contact.equals("") || email.equals("") || description.equals("")) {
            Toast.makeText(this, "Some Fields Are Empty", 1).show();
            return;
        }
        this.count2++;
        uploadimg(this, this.Convetedimg, Integer.toString(this.count2));
        Upload(this, name, age, contact, email, description);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            this.Convetedimg = encodeTobase64((Bitmap) data.getExtras().get("data"));
        }
    }

    public void capture(View view) {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 1);
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immage = Bitmap.createScaledBitmap(image, 600, HttpStatus.SC_BAD_REQUEST, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(CompressFormat.JPEG, 80, baos);
        return Base64.encodeToString(baos.toByteArray(), 0);
    }

    public void uploadimg(final Context c, String photo, String count) {
        new AsyncTask<String, Void, String>() {
            private boolean issuccess = true;
            private String resp;

            protected void onPreExecute() {
                Complaint.this.prgDialog2 = new ProgressDialog(c);
                Complaint.this.prgDialog2.setCancelable(false);
                Complaint.this.prgDialog2.setProgressStyle(0);
                Complaint.this.prgDialog2.setIndeterminate(false);
                Complaint.this.prgDialog2.setMessage("uploading image...");
                Complaint.this.prgDialog2.show();
            }

            protected String doInBackground(String... params) {
                String p = params[0];
                String k = params[1];
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("photo", p));
                nameValuePairs.add(new BasicNameValuePair("name", k));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpUriRequest httpPost = new HttpPost("http://crsecurity.pe.hu/upload.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    this.resp = (String) httpClient.execute(httpPost, new BasicResponseHandler());
                } catch (IOException e) {
                    this.issuccess = false;
                } finally {
                    Complaint.this.prgDialog2.dismiss();
                }
                return "success";
            }

            protected void onPostExecute(String result) {
                if (this.issuccess) {
                    Toast.makeText(c, this.resp, 1).show();
                } else {
                    Toast.makeText(c, "Error connecting server", 1).show();
                }
            }
        }.execute(new String[]{photo, count});
    }
}
