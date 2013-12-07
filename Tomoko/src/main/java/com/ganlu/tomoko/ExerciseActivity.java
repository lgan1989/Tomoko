package com.ganlu.tomoko;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Activity;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.content.res.XmlResourceParser;
import android.widget.ViewSwitcher;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;

public class ExerciseActivity extends Activity {


    private StatusDataSource dtStatus;
    private ArrayList<String> hiraganaCharters;
    private ArrayList<Status> statusList;
    private ArrayList < Integer > test;
    private int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
    }

    private void firstTimeRun()
    {
        XmlResourceParser xrp;
        Resources r = getResources();
        xrp = r.getXml(R.xml.characters);
        String text = "";
        String rome = "";
        try {
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                    if (xrp.getName().equals("string")){
                        rome = xrp.getAttributeValue(0);
                        rome = rome.substring(rome.indexOf('_') + 1);
                    }
                } else if (xrp.getEventType() == XmlPullParser.END_TAG) {
                } else if (xrp.getEventType() == XmlPullParser.TEXT) {
                    text = xrp.getText();
                    dtStatus.newRecord(text , rome);
                }
                xrp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .edit()
                .putBoolean("firstrun", false)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Bundle extras = getIntent().getExtras();
        String msg = extras.getString("type");
        idx = 1;

        if (msg == getResources().getString(R.string.hiragana)){

            hiraganaCharters = new ArrayList<String>();

        }
        dtStatus = new StatusDataSource(this);
        dtStatus.open();
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
            firstTimeRun();
        }


        statusList = dtStatus.getAllStatus();
        test = generateTest();

    //    final TextView text = (TextView)findViewById(R.id.textDisplay);
        final TextSwitcher text =  (TextSwitcher)findViewById(R.id.textSwitcher);
        text.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(ExerciseActivity.this);
                myText.setTextSize(200.0f);
                myText.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);

                return myText;
            }
        });
        Status status = statusList.get(test.get(1));
        text.setText(status.getCharacter());

        getMenuInflater().inflate(R.menu.exercise, menu);

        final Button btnLeft = (Button) findViewById(R.id.btnLeft);
        final Button btnMiddle = (Button) findViewById(R.id.btnMiddle);
        final Button btnRight = (Button) findViewById(R.id.btnRight);
        final TextView textProgress = (TextView)findViewById(R.id.textProgress);
        textProgress.setText(String.valueOf(idx) + "/" + String.valueOf(test.size()));

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idx ++;

                if (idx == test.size()){
                    Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                else{
                    String rome = statusList.get(test.get(idx-1)).getRome();
                    btnMiddle.setText(rome);
                    Handler mHandler = new Handler();
                    Runnable r=new Runnable() {

                        @Override
                        public void run() {
                            Status status = statusList.get(test.get(idx));
                            text.setText(status.getCharacter());
                            statusList.get(test.get(idx)).addRight();
                            btnMiddle.setText("?");
                            textProgress.setText(String.valueOf(idx) + "/" + String.valueOf(test.size()));
                        }
                    };

                    mHandler.postDelayed(r, 1000);
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idx ++;

                if (idx == test.size()){
                    Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
                else{

                    String rome = statusList.get(test.get(idx-1)).getRome();
                    btnMiddle.setText(rome);
                    Handler mHandler = new Handler();
                    Runnable r=new Runnable() {

                        @Override
                        public void run() {
                            Status status = statusList.get(test.get(idx));
                            text.setText(status.getCharacter());
                            statusList.get(test.get(idx)).addWrong();
                            btnMiddle.setText("?");
                            textProgress.setText(String.valueOf(idx) + "/" + String.valueOf(test.size()));
                        }
                    };

                    mHandler.postDelayed(r, 1000);
                }
            }
        });

        return true;
    }

    public class StatusComparator implements Comparator<Status> {
        @Override
        public int compare(Status o1, Status o2) {
            return o1.getRatio() < o2.getRatio() ? 1 : 0;
        }
    }

    public ArrayList < Integer > generateTest(){

        int len = statusList.size();
        ArrayList < Integer > result = new ArrayList<Integer>();

        float maxRatio = -1;
        for (int i = 0 ; i < len ; i ++){
            Status temp = statusList.get(i);
            long r = temp.getRightCount();
            long w = temp.getWrongCount();
            float ra = 0;
            if (r + w > 0)ra = r * 1.0f/(r + w);

            statusList.get(i).setRatio( ra );
            if (ra > maxRatio)
                maxRatio = ra;
        }

        for (int i = 0 ; i < len ; i ++){
            float prop = 0.5f;
            float delta = maxRatio - statusList.get(i).getRatio();
            prop = delta > 0.5f ? 1.0f : delta + prop;
            float temp = (float)Math.random();
            if (temp <= prop){
                result.add( i );
            }
        }
        return result;
    }

    public void postTest(){

    }

    @Override
    public void onBackPressed() {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
