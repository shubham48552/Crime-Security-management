package com.example.ashwin.crimesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    class C01751 implements Runnable {
        C01751() {
        }

        public void run() {
            Splash.this.startActivity(new Intent(Splash.this, Login.class));
            Splash.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) C0174R.layout.activity_splash);
        new Handler().postDelayed(new C01751(), 3000);
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}
