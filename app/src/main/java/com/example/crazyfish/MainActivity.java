package com.example.crazyfish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private CrazyFish gameView;
    private Handler handler=new Handler();
    private final static int interval=60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView=new CrazyFish(this);
        setContentView(gameView);

      /*  Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        gameView.invalidate();
                    }
                });
            }
        },0 ,interval);*/

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                gameView.invalidate();
                handler.postDelayed(this,60);
            }
        };
        runnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
