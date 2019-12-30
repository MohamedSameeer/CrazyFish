package com.example.crazyfish;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

public class CrazyFish extends View {

    private Bitmap rightArrow;
    private int rightArrowX,rightArrowY;

    private Bitmap leftArrow;
    private int leftArrowX,leftArrowY;

    private Bitmap topArrow;
    private int topArrowX,topArrowY;

    private Bitmap bottomArrow;
    private int bottomArrowX,bottomArrowY;

    private Bitmap[] fish=new Bitmap[2];

    private Bitmap background;

    private Bitmap life[]=new Bitmap[2];

    private Paint scorePaint=new Paint();

    private int fishX=10;
    private int fishY;
    private int fishSpeed;
    private int fishSpeedX;
    private boolean touch=false;
    private int canvasWidth,canvasHeight;

    private Runnable runnable;
    private Handler handler=new Handler();

    private int score;

    private int lifeCounter;

    private int yellowX,yellowY,yellowSpeed=16;
    private Paint yellowPaint=new Paint();

    private int redX,redY,redSpeed=18;
    private Paint redPaint=new Paint();

    private int greenX,greenY,greenSpeed=20;
    private Paint greenPaint=new Paint();
    private Context myContext;
    public CrazyFish(Context context) {
        super(context);
        myContext=context;

        leftArrow=getBitmapFromVectorDrawable(context,R.drawable.ic_arrow_back_black_24dp);
        rightArrow= getBitmapFromVectorDrawable(context,R.drawable.ic_arrow_forward_black_24dp);
        topArrow=getBitmapFromVectorDrawable(context,R.drawable.ic_arrow_upward_black_24dp);
        bottomArrow=getBitmapFromVectorDrawable(context,R.drawable.ic_arrow_downward_black_24dp);

        fish[0]= BitmapFactory.decodeResource(getResources(),R.drawable.fish1);
        fish[1]=BitmapFactory.decodeResource(getResources(),R.drawable.fish2);

        background=BitmapFactory.decodeResource(getResources(),R.drawable.background);

        scorePaint.setColor(Color.WHITE);
        scorePaint.setTextSize(40);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);
        scorePaint.setAntiAlias(true);

        yellowPaint.setColor(Color.YELLOW);
        yellowPaint.setAntiAlias(false);

        greenPaint.setColor(Color.GREEN);
        greenPaint.setAntiAlias(false);

        redPaint.setColor(Color.RED);
        redPaint.setAntiAlias(false);

        life[0]=BitmapFactory.decodeResource(getResources(),R.drawable.hearts);
        life[1]=BitmapFactory.decodeResource(getResources(),R.drawable.heart_grey);
        score=0;
        fishY=550;
        fishX=0;
        //fishSpeedX=0;
        lifeCounter=3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvasHeight=canvas.getHeight();
        canvasWidth=canvas.getWidth();
        canvas.drawBitmap(background,0,0,null);

        bottomArrowX=rightArrowX-leftArrow.getWidth();
        bottomArrowY=canvasHeight-bottomArrow.getHeight();

        rightArrowX=canvasWidth-rightArrow.getWidth();
        rightArrowY=canvasHeight-bottomArrow.getHeight()-rightArrow.getHeight();

        leftArrowX=rightArrowX-leftArrow.getWidth()-leftArrow.getWidth();
        leftArrowY=canvasHeight-bottomArrow.getHeight()-leftArrow.getHeight();

        topArrowX=rightArrowX-leftArrow.getWidth();
        topArrowY=rightArrowY-topArrow.getHeight();

        canvas.drawBitmap(rightArrow,rightArrowX,rightArrowY,null);
        canvas.drawBitmap(leftArrow,leftArrowX,leftArrowY,null);
        canvas.drawBitmap(topArrow,topArrowX,topArrowY,null);
        canvas.drawBitmap(bottomArrow,bottomArrowX,bottomArrowY,null);

        int minFishY=fish[0].getHeight();
        int maxFishY=canvasHeight-fish[0].getHeight();
       /* int maxFishX=rightArrowX;
        int minFishX=rightArrow.getWidth();*/
       // fishY=fishY+fishSpeed;

       /* Log.e("canvasHeight",canvasHeight+"");
        Log.e("minFishY",minFishY+"");
        Log.e("maxFishY",maxFishY+"");
        Log.e("fishY",fishY+"");
*/
        for(int i=1;i<=3;i++){
            int x=canvasWidth-(life[0].getWidth()*i)-24;
            int y=30;
            if(i<=lifeCounter){
                canvas.drawBitmap(life[0],x,y,null);
            }else{
                canvas.drawBitmap(life[1],x,y,null);
            }
        }
        /*if(fishY<minFishY){
            fishY=minFishY;
        }
        if(fishY>maxFishY){
            fishY=maxFishY;
        }

        fishSpeed=fishSpeed+2;*/
        /*fishX=fishX+fishSpeedX;*/
        if(touch){
            canvas.drawBitmap(fish[1],fishX,fishY,null);
            touch=false;
        }else{
            canvas.drawBitmap(fish[0],fishX,fishY,null);
        }

        yellowX=yellowX-yellowSpeed;
        if(eatBall(yellowX,yellowY)){
            score+=5;
            yellowX=-100;
        }
        if(yellowX<0){
            yellowX=canvasWidth+21;
            yellowY=(int) Math.floor(Math.random() *(maxFishY -minFishY))+minFishY;
        }

        canvas.drawCircle(yellowX,yellowY,20,yellowPaint);

        greenX=greenX-greenSpeed;
        if(eatBall(greenX,greenY)){
            score+=15;
            greenX=-100;
        }
        if(greenX<0){
            greenX=canvasWidth+21;
            greenY=(int)Math.floor(Math.random()*(maxFishY-minFishY))+minFishY;
        }
        canvas.drawCircle(greenX,greenY,15,greenPaint);

        redX=redX-redSpeed;
        if(eatBall(redX,redY)){
            redX=-100;
            lifeCounter--;
            if(lifeCounter<=0){
                Intent i =new Intent(myContext,GameOver.class);
                myContext.startActivity(i);
            }
        }
        if(redX<0){
            redX=canvasWidth+21;
            redY=(int)Math.floor(Math.random()*(maxFishY-minFishY))+minFishY;

        }
        canvas.drawCircle(redX,redY,25,redPaint);
        canvas.drawText("Score: "+score,20,60,scorePaint);


    }
    public boolean eatBall(int x, int y){
        if(fishX<x && x<(fishX+fish[0].getWidth())
                && fishY<y && y<(fishY+fish[0].getHeight())){
            return true;
        };
        return false;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=event.getX();
        float y=event.getY();
       /* switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(rightArrowX<x && x<rightArrow.getWidth()+rightArrowX
                        && rightArrowY<y&&y<rightArrow.getHeight()+rightArrowY){

                    fishX+=12;
                    if(fishX<0){
                        fishX=0;
                    }
                    if(fishX>rightArrowX){
                        fishX=rightArrowX;
                    }
                    touch=true;
                break;
        }*/
        if(event.getAction()==MotionEvent.ACTION_DOWN){
           /* Log.e("x=",x+"");Log.e("y=",y+"");
            Log.e("cx=",canvasWidth+rightArrowX+"");Log.e("cy=",canvasHeight+rightArrowY+"");*/
            if(rightArrowX<x && x<rightArrow.getWidth()+rightArrowX
                    && rightArrowY<y&&y<rightArrow.getHeight()+rightArrowY){

                fishX+=12;
                if(fishX<0){
                    fishX=0;
                }
                if(fishX>rightArrowX){
                    fishX=rightArrowX;
                }
                touch=true;
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        fishX+=12;
                        if(fishX<0){
                            fishX=0;
                        }
                        if(fishX>rightArrowX){
                            fishX=rightArrowX;
                        }
                        touch=true;
                        handler.postDelayed(runnable,100);
                    }
                };
                runnable.run();
                return true;

            }else if(leftArrowX<x && x<leftArrow.getWidth()+leftArrowX
                    && leftArrowY<y && y<leftArrow.getHeight()+leftArrowY ){
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        fishX-=12;
                        if(fishX<0){
                            fishX=0;
                        }
                        if(fishX>leftArrowX){
                            fishX=leftArrowX;
                        }
                        touch=true;
                        handler.postDelayed(runnable,100);
                    }
                };
                runnable.run();
                return true;

            }else if(topArrowX<x && x<topArrow.getWidth()+topArrowX
                    && topArrowY<y && y<topArrow.getHeight()+topArrowY){
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        fishY-=12;
                        if(fishY<fish[0].getHeight()){
                            fishY=fish[0].getHeight();
                        }
                        if(fishY>canvasHeight-fish[0].getHeight()){
                            fishY=canvasHeight-fish[0].getHeight();
                        }
                        touch=true;
                        handler.postDelayed(runnable,100);
                    }
                };
                runnable.run();
                return true;

            }else if(bottomArrowX<x && x<bottomArrow.getWidth()+bottomArrowX
                    && bottomArrowY<y && y<bottomArrow.getHeight()+bottomArrowY){
              //  Log.e("eeeeeee","eeeeeeeeeeeeeeeeeee");
                runnable=new Runnable() {
                    @Override
                    public void run() {
                        fishY+=12;
                        if(fishY<fish[0].getHeight()){
                            fishY=fish[0].getHeight();
                        }
                        if(fishY>canvasHeight-fish[0].getHeight()){
                            fishY=canvasHeight-fish[0].getHeight();
                        }
                        touch=true;
                        handler.postDelayed(runnable,100);
                    }
                };
                runnable.run();
                return true;
            }
        }
        else if(event.getAction()==MotionEvent.ACTION_UP){
           // Log.e("ssssssss","ssssssssssss");
                handler.removeCallbacks(runnable);
        }
        /* if(event.getAction()==MotionEvent.ACTION_DOWN){
            touch=true;
            fishSpeed=-22;
        }*/


        return false;
    }

    // Convert Vector Asset To Map
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }
}
