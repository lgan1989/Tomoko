package com.ganlu.tomoko;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends Activity {


    private StatusDataSource dtStatus;
    private ArrayList<String> hiraganaCharters;
    private ArrayList<Status> statusList;
    private int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
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
      //  dtStatus.newRecord( getResources().getString(R.string.hiragana_a) , (float) 0.0);
        XmlResourceParser xrp;
        Resources r = getResources();
        xrp = r.getXml(R.xml.characters);
        try {
            //如果没有到文件尾继续执行
            while (xrp.getEventType() != XmlResourceParser.END_DOCUMENT) {
                //如果是开始标签
                if (xrp.getEventType() == XmlResourceParser.START_TAG) {
                } else if (xrp.getEventType() == XmlPullParser.END_TAG) {
                } else if (xrp.getEventType() == XmlPullParser.TEXT) {
                    String temp = xrp.getText();
                    dtStatus.newRecord(xrp.getText(), (float) 0.0);
                }
                //下一个标签
                xrp.next();
            }
           // myTextView.setText(sb.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){

            dtStatus.newRecord( getResources().getString(R.string.hiragana) , (float) 0.0);
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("firstrun", false)
                    .commit();
        }


        statusList = dtStatus.getAllStatus();
        final TextView text = (TextView)findViewById(R.id.textDisplay);


        final Button btnLeft = (Button) findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                idx ++;
                Status status = statusList.get(idx);
                text.setText(status.getCharacter());
            }
        });



        Status status = statusList.get(1);
        text.setText(status.getCharacter());



        getMenuInflater().inflate(R.menu.exercise, menu);



        return true;
    }

    @Override
    public void onBackPressed() {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
