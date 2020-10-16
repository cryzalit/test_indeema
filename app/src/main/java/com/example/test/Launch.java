package com.example.test;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class Launch extends AppCompatActivity {

    Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finishAffinity();
            System.exit(0);
            com.example.test.Launch.this.finish();
            if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
                com.example.test.Launch.this.finishAffinity();
            } else if(Build.VERSION.SDK_INT>=21){
                com.example.test.Launch.this.finishAndRemoveTask();
            }

        }


        setContentView(R.layout.activity_launch);


        thread = new Thread(){
            @Override
            public void run(){
                try{
                    sleep(4000);
                } catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(new Intent(com.example.test.Launch.this,MainActivity.class));
                    finish();
                }

            }
        };

        new Handler().postDelayed(new Runnable() {
            public void run() {
                thread.start();
            }
        }, 200);

    }





}
