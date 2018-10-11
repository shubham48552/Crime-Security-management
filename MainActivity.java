package com.example.ashwin.crimesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0174R.layout.activity_main);
    }

    public void complaint(View view) {
        startActivity(new Intent(this, Complaint.class));
    }

    public void missing(View view) {
        startActivity(new Intent(this, Missing.class));
    }

    public void logout(View view) {
        Toast.makeText(this, "Logged Out Successfully", 0).show();
        getSharedPreferences("My_Prefs", 0).edit().clear().commit();
        startActivity(new Intent(this, Login.class));
    }
}
