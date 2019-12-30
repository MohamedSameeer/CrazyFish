package com.example.crazyfish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread t=new Thread(){
            @Override
            public void run() {
               try {
                    sleep(5000);
               }catch (Exception e){
                    e.printStackTrace();
               }finally {
                   Intent i=new Intent(Splash.this,MainActivity.class);
                   startActivity(i);
               }
            }
        };
        t.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
