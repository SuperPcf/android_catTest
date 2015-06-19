package com.school.pcfcar.cartest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.os.*;
import android.widget.Toast;

public class MainActivity extends Activity implements Runnable {


    int testNum=1;//当前题目所在题号
    List<String> list=new ArrayList<String>();
    int bgNum=1;//背景号


    //测试图片缩放功能
    Bitmap bp=null;
    ImageView imageview;
    float scaleWidth;
    float scaleHeight;

    int h;
    boolean num=false;
    private boolean isInterrupted=true;
    private  Handler handler;
    private int t=0;

    Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //题目文本框

        final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        final TextView questionTxt = (TextView) findViewById(R.id.question);
        final TextView nowPageTxt = (TextView) findViewById(R.id.NowPage);
        final ImageView image = (ImageView) findViewById(R.id.image);
        final RadioButton rBa = (RadioButton) findViewById(R.id.radioButtonA);
        final RadioButton rBb = (RadioButton) findViewById(R.id.radioButtonB);
        final RadioButton rBc = (RadioButton) findViewById(R.id.radioButtonC);
        final RadioButton rBd = (RadioButton) findViewById(R.id.radioButtonD);
        final ImageView answerImg = (ImageView) findViewById(R.id.answerImg);
        final TextView answeTxt = (TextView) findViewById(R.id.answeTxt);
        Button btnSure = (Button) findViewById(R.id.btnSure);
        final Button btnPaUp = (Button) findViewById(R.id.btnPgUp);
        final Button btnPaDn = (Button) findViewById(R.id.btnPgDn);
        final TextView bgTxt = (TextView) findViewById(R.id.bgTxt);


        BufferedReader reader = null;
        InputStream is = getResources().openRawResource(R.raw.test);

        //以行为单位读取文件内容，一次读一整行
        reader = new BufferedReader(new InputStreamReader(is));

        String str = null;
        //添加题目
        try {

            while ((str = reader.readLine()) != null) {
                list.add(str);


            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        questionTxt.setText(list.get(0));

        image.setImageResource(getMyId(list.get(1)));

        rBa.setText(list.get(2));
        rBb.setText(list.get(3));
        rBc.setText(list.get(4));
        rBd.setText(list.get(5));


        // 1 上页按钮事件绑定
        btnPaDn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testNum >= (list.size() / 7)) {
                    testNum = list.size() / 7;

                } else {
                    testNum++;
                }
                int t = (testNum - 1) * 7;
                nowPageTxt.setText(testNum + "/" + (list.size() / 7));
                questionTxt.setText(list.get(0 + t));

                image.setImageResource(getMyId(list.get(1 + t)));

                rBa.setText(list.get(2 + t));
                rBb.setText(list.get(3 + t));
                rBc.setText(list.get(4 + t));
                rBd.setText(list.get(5 + t));

                //设置图片可见性为不可见
                answerImg.setVisibility(View.INVISIBLE);
                answeTxt.setText("");
            }
        });

        // 2 下翻按钮事件绑定
        btnPaUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (testNum <= 1) {
                    testNum = 1;

                } else {
                    testNum--;
                }
                t = (testNum - 1) * 7;
                nowPageTxt.setText(testNum + "/" + (list.size() / 7));
                questionTxt.setText(list.get(0 + t));

                image.setImageResource(getMyId(list.get(1 + t)));

                rBa.setText(list.get(2 + t));
                rBb.setText(list.get(3 + t));
                rBc.setText(list.get(4 + t));
                rBd.setText(list.get(5 + t));
                //设置图片可见性为不可见
                answerImg.setVisibility(View.INVISIBLE);
                answeTxt.setText("");
            }
        });

        // 3 确定按钮事件绑定
        btnSure.setOnClickListener(new View.OnClickListener() {
            String testAnwser = "";

            @Override
            public void onClick(View v) {
                if (rBa.isChecked()) {
                    testAnwser = "答案：A";
                } else if (rBb.isChecked()) {
                    testAnwser = "答案：B";
                } else if (rBc.isChecked()) {
                    testAnwser = "答案：C";
                } else if (rBd.isChecked()) {
                    testAnwser = "答案：D";
                }
                int t = (testNum - 1) * 7;
//                Log.e("---->>",testAnwser);
//                Log.e("---->>>",list.get(6));
//                boolean tm=list.get(t+6).equals(testAnwser);
//                Log.e("---->>>", String.valueOf(tm));
                //设置图片可见性为可见
                answerImg.setVisibility(View.VISIBLE);

                if (list.get(t + 6).equals(testAnwser)) {
                    answerImg.setImageResource(getMyId("tureimg.png"));
                    answeTxt.setText("");
                } else {
                    answerImg.setImageResource(getMyId("falseimg.png"));
                    answeTxt.setText(list.get(t + 6));
                }

                isInterrupted=false;
//                try {
//                    //确定按钮之后自动跳到下一页
//                    Thread.sleep(5000);
//                    if (testNum >= (list.size() / 7)) {
//                        testNum = list.size() / 7;
//
//                    } else {
//                        testNum++;
//                    }
//                    t = (testNum - 1) * 7;
//                    nowPageTxt.setText(testNum + "/" + (list.size() / 7));
//                    questionTxt.setText(list.get(0 + t));
//
//                    image.setImageResource(getMyId(list.get(1 + t)));
//
//                    rBa.setText(list.get(2 + t));
//                    rBb.setText(list.get(3 + t));
//                    rBc.setText(list.get(4 + t));
//                    rBd.setText(list.get(5 + t));
//
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        });


        // 4 切换背景事件绑定
        bgTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bgNum >= 9) {
                    bgNum = 1;
                } else {
                    bgNum++;
                }
                mainLayout.setBackgroundResource(getMyId("bg" + bgNum + ".png"));
            }
        });

        handler=new Handler(){
            @Override
            public void handleMessage(Message message){
                if(message.what==0x111){
                    nowPageTxt.setText(message.arg1 + "/" + (list.size() / 7));
                    questionTxt.setText(list.get(0 + t));

                    image.setImageResource(getMyId(list.get(1 + t)));

                    rBa.setText(list.get(2 + t));
                    rBb.setText(list.get(3 + t));
                    rBc.setText(list.get(4 + t));
                    rBd.setText(list.get(5 + t));

                }
            }
        };

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPath="android.resource://"+  getPackageName() + "/raw/"+list.get((testNum-1)*7+1);
                Intent intent = new Intent(MainActivity.this, SpaceImageDetailActivity.class);
                intent.putExtra("mPath",mPath);
                //intent.putExtra("position",position);
                int[] location = new int[2];
                image.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);//必须
                intent.putExtra("locationY", location[1]);//必须

                intent.putExtra("width", image.getWidth());//必须
                intent.putExtra("height", image.getHeight());//必须
                startActivity(intent);
                //overridePendingTransition(0, 0);
            }
        });

        }

        public void run(){

                try {
                    //确定按钮之后自动跳到下一页
                    Thread.sleep(5000);
                    if(testNum >= (list.size()/7)){
                        testNum = list.size()/7;

                    }else{
                        testNum++;
                    }
                    isInterrupted=true;
                }catch(InterruptedException e){
                        e.printStackTrace();
                    }

               Message message=new Message();
               message.what=0x111;
               message.arg1=(testNum-1)*7;

        }
        /**
         * 图片缩放
         */
//        Display display = getWindowManager().getDefaultDisplay();
//        imageview = (ImageView) findViewById(R.id.image);
//        bp = BitmapFactory.decodeResource(getResources(), getMyId(list.get((testNum - 1) * 7 + 1)));
//        int width = bp.getWidth();
//        int height = bp.getHeight();
//        int w = display.getWidth();
//        int h = display.getHeight();
//        scaleWidth = ((float) w) / width;
//        scaleHeight = ((float) h) / height;
//        imageview.setImageBitmap(bp);
//    }
//    public boolean onTouchEvent(MotionEvent event){
//
//            switch(event.getAction()){
//
//                case MotionEvent.ACTION_DOWN:
//                    if(num==true)        {
//                        Matrix matrix=new Matrix();
//                        matrix.postScale(scaleWidth,scaleHeight);
//
//                        Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
//                        imageview.setImageBitmap(newBitmap);
//                        num=false;
//                    }
//                    else{
//                        Matrix matrix=new Matrix();
//                        matrix.postScale(1.0f,1.0f);
//                        Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
//                        imageview.setImageBitmap(newBitmap);
//                        num=true;
//                    }
//                    break;
//            }
//
//
//            return super.onTouchEvent(event);
//        }
//

///
//





    /**
     * 从文件读取的图片名（包括后缀）获得资源ID
     * @param file
     * @return
     */
    private  int getMyId(String file){
        String[] name=file.split("\\.");

       if(name.length==2&&name[0]!=null&&name[0].trim()!="") {


           return this.getResources().getIdentifier(name[0], "raw", this.getPackageName());
       }
        throw new UnsupportedOperationException("图片加载错误，请 ");
    }




}
