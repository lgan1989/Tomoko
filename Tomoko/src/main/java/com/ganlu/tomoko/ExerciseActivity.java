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
    private ArrayList<Status> statusList;
    private ArrayList < Integer > test;
    private int idx;
    private int wrongCount;
    private int rightCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        Bundle extras = getIntent().getExtras();
        String msg = extras.getString("type");
        idx = 0;
        wrongCount = 0;
        rightCount = 0;

        dtStatus = new StatusDataSource(this);
        dtStatus.open();


        int type = -1;
        int testSize = 30;
        if (msg.equals(getResources().getString(R.string.hiragana))){
            testSize = 20;
            type = 0;
        }
        else if (msg.equals(getResources().getString(R.string.katakana))){
            testSize = 20;
            type = 1;
        }

        statusList = dtStatus.getAllStatusByType(type);
        test = generateTest(testSize);

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
        Status status = dtStatus.getStatusById(test.get(0));
        text.setText(status.getCharacter());

        final Button btnLeft = (Button) findViewById(R.id.btnLeft);
        final Button btnMiddle = (Button) findViewById(R.id.btnMiddle);
        final Button btnRight = (Button) findViewById(R.id.btnRight);
        final TextView textProgress = (TextView)findViewById(R.id.textProgress);
        textProgress.setText(String.valueOf(idx+1) + "/" + String.valueOf(test.size()));

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idx == test.size())
                    return;
                idx ++;
                rightCount ++;
                Status status =  dtStatus.getStatusById(test.get(idx - 1));
                String rome = status.getRome();
                dtStatus.update(status.getRightCount() + 1 , status.getWrongCount() , status.getId());
                btnMiddle.setText(rome);
                btnLeft.setEnabled(false);
                Handler mHandler = new Handler();
                Runnable r=new Runnable() {

                    @Override
                    public void run() {
                        if (idx < test.size()){

                            text.setText(dtStatus.getStatusById(test.get(idx)).getCharacter());

                            btnMiddle.setText("?");
                            textProgress.setText(String.valueOf(idx+1) + "/" + String.valueOf(test.size()));
                            btnLeft.setEnabled(true);
                        }
                        else{
                            postTest();
                        }
                    }
                };

                mHandler.postDelayed(r, 1000);

            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (idx == test.size())
                    return;
                idx ++;
                wrongCount ++;
                Status status =  dtStatus.getStatusById(test.get(idx - 1));
                String rome = status.getRome();
                dtStatus.update(status.getRightCount()  , status.getWrongCount() + 1, status.getId());
                btnMiddle.setText(rome);
                btnRight.setEnabled(false);
                Handler mHandler = new Handler();
                Runnable r=new Runnable() {

                    @Override
                    public void run() {
                        if (idx < test.size()){
                            text.setText(dtStatus.getStatusById(test.get(idx)).getCharacter());

                            btnMiddle.setText("?");
                            textProgress.setText(String.valueOf(idx+1) + "/" + String.valueOf(test.size()));
                            btnRight.setEnabled(true);
                        }
                        else{
                            postTest();
                        }
                    }
                };

                mHandler.postDelayed(r, 1000);

            }
        });

        return;

    }

    public ArrayList < Integer > generateTest(int size){

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
        Collections.shuffle(statusList);
        for (int i = 0 ; i < len && size > 0; i ++){
            float prop = 0.5f;

            float delta = maxRatio - statusList.get(i).getRatio();
            prop = delta > 0.5f ? 1.0f : delta + prop;
            float temp = (float)Math.random();
            if (temp <= prop || (len - i <= size)){
                result.add( statusList.get(i).getId() );
                size --;
            }
        }
        return result;
    }

    public void postTest(){
        Intent intent=new Intent(getApplicationContext(),ResultActivity.class);
        Bundle b = new Bundle();
        b.putInt("wrongCount", wrongCount);
        b.putInt("rightCount" , rightCount);
        b.putIntegerArrayList("test" , test);
        intent.putExtras(b);

        startActivity(intent);
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
