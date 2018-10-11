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

public class Missing extends AppCompatActivity {
    private static final int TAKE_PHOTO_CODE = 1;
    EditText Address;
    EditText Age;
    EditText Contact;
    String Convetedimg;
    EditText Description;
    EditText District;
    EditText Mname;
    EditText Name;
    EditText Pincode;
    EditText State;
    private final String TAG = getClass().getName();
    public Activity activity = this;
    int count = 0;
    int count2;
    String dir;
    ProgressDialog prgDialog1;
    ProgressDialog prgDialog2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0174R.layout.activity_missing);
        this.Name = (EditText) findViewById(C0174R.id.etName);
        this.Mname = (EditText) findViewById(C0174R.id.etMname);
        this.Age = (EditText) findViewById(C0174R.id.etAge);
        this.Contact = (EditText) findViewById(C0174R.id.etContact);
        this.Address = (EditText) findViewById(C0174R.id.etAddress);
        this.State = (EditText) findViewById(C0174R.id.etState);
        this.District = (EditText) findViewById(C0174R.id.etDistrict);
        this.Pincode = (EditText) findViewById(C0174R.id.etPincode);
        this.Description = (EditText) findViewById(C0174R.id.etDescription);
    }

    public void Upload(final Context c, String name, String mname, String age, String contact, String address, String state, String district, String pincode, String description) {
        new AsyncTask<String, Void, String>() {
            private boolean issuccess = true;
            private String resp;

            protected void onPreExecute() {
                Missing.this.prgDialog1 = new ProgressDialog(c);
                Missing.this.prgDialog1.setCancelable(false);
                Missing.this.prgDialog1.setProgressStyle(0);
                Missing.this.prgDialog1.setIndeterminate(false);
                Missing.this.prgDialog1.setMessage("Registering...");
                Missing.this.prgDialog1.show();
            }

            protected String doInBackground(String... params) {
                String name = params[0];
                String mname = params[1];
                String age = params[2];
                String contact = params[3];
                String address = params[4];
                String state = params[5];
                String district = params[6];
                String pincode = params[7];
                String description = params[8];
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("name", name));
                nameValuePairs.add(new BasicNameValuePair("mname", mname));
                nameValuePairs.add(new BasicNameValuePair("age", age));
                nameValuePairs.add(new BasicNameValuePair("contact", contact));
                nameValuePairs.add(new BasicNameValuePair("address", address));
                nameValuePairs.add(new BasicNameValuePair("state", state));
                nameValuePairs.add(new BasicNameValuePair("district", district));
                nameValuePairs.add(new BasicNameValuePair("pincode", pincode));
                nameValuePairs.add(new BasicNameValuePair("description", description));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpUriRequest httpPost = new HttpPost("http://crsecurity.pe.hu/missingDetail.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    this.resp = (String) httpClient.execute(httpPost, new BasicResponseHandler());
                    Missing.this.prgDialog1.dismiss();
                } catch (IOException e) {
                    this.issuccess = false;
                    Missing.this.prgDialog1.dismiss();
                } catch (Throwable th) {
                    Missing.this.prgDialog1.dismiss();
                }
                return "success";
            }

            protected void onPostExecute(String result) {
                if (this.issuccess) {
                    Toast.makeText(c, this.resp, 1).show();
                    if (this.resp.equals("yes")) {
                        Toast.makeText(c, "Registered", 1).show();
                        Missing.this.activity.finish();
                        Missing.this.startactivity();
                        return;
                    }
                    Toast.makeText(c, "Not Registered", 1).show();
                    return;
                }
                Toast.makeText(c, "Error connecting server", 1).show();
            }
        }.execute(new String[]{name, mname, age, contact, address, state, district, pincode, description});
    }

    public void startactivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void register(View view) {
        String name = this.Name.getText().toString();
        String mname = this.Mname.getText().toString();
        String age = this.Age.getText().toString();
        String contact = this.Contact.getText().toString();
        String address = this.Address.getText().toString();
        String state = this.State.getText().toString();
        String district = this.District.getText().toString();
        String pincode = this.Pincode.getText().toString();
        String description = this.Description.getText().toString();
        if (name.equals("") || mname.equals("") || age.equals("") || contact.equals("") || address.equals("") || state.equals("") || district.equals("") || pincode.equals("") || description.equals("")) {
            Toast.makeText(this, "Some Fields Are Empty", 1).show();
            return;
        }
        uploadimg(this, this.Convetedimg, Integer.toString(this.count2));
        Upload(this, name, mname, age, contact, address, state, district, pincode, description);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            this.Convetedimg = encodeTobase64((Bitmap) data.getExtras().get("data"));
            this.count2++;
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
                Missing.this.prgDialog2 = new ProgressDialog(c);
                Missing.this.prgDialog2.setCancelable(false);
                Missing.this.prgDialog2.setProgressStyle(0);
                Missing.this.prgDialog2.setIndeterminate(false);
                Missing.this.prgDialog2.setMessage("uploading image...");
                Missing.this.prgDialog2.show();
            }

            protected String doInBackground(String... params) {
                String p = params[0];
                String k = params[1];
                List<NameValuePair> nameValuePairs = new ArrayList();
                nameValuePairs.add(new BasicNameValuePair("photo", p));
                nameValuePairs.add(new BasicNameValuePair("name", k));
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpUriRequest httpPost = new HttpPost("http://crsecurity.pe.hu/missingupload.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    this.resp = (String) httpClient.execute(httpPost, new BasicResponseHandler());
                } catch (IOException e) {
                    this.issuccess = false;
                } finally {
                    Missing.this.prgDialog2.dismiss();
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
