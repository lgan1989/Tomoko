package com.ganlu.tomoko;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends Activity {
    private StatusDataSource dtStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final Button btnHiragana = (Button) findViewById(R.id.button_hiragana);
        final Button btnKatakana = (Button) findViewById(R.id.button_katakana);
        final Button btnMixed = (Button) findViewById(R.id.button_mix);
        final Button btnStatus = (Button) findViewById(R.id.button_status);
        initDB();
        btnHiragana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String exerciseType = getResources().getString(R.string.hiragana);
                Intent intent=new Intent(getApplicationContext(),ExerciseActivity.class);

                intent.putExtra("type" , exerciseType);

                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        btnKatakana.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String exerciseType = getResources().getString(R.string.katakana);
                Intent intent=new Intent(getApplicationContext(),ExerciseActivity.class);

                intent.putExtra("type" , exerciseType);

                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        btnMixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String exerciseType = getResources().getString(R.string.mix);
                Intent intent=new Intent(getApplicationContext(),ExerciseActivity.class);

                intent.putExtra("type" , exerciseType);

                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(getApplicationContext(),StatusActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });

    }


    private void initDB()
    {
        dtStatus = new StatusDataSource(this);
        dtStatus.open();
        boolean firstrun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("firstrun", true);
        if (firstrun){
            firstTimeRun(R.xml.hiragana , 0);
            firstTimeRun(R.xml.katakana , 1);
        }
    }
    private void firstTimeRun(int resourceId , int type)
    {
        XmlResourceParser xrp;
        Resources r = getResources();
        xrp = r.getXml(resourceId);
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
                    dtStatus.newRecord(text , rome , type);
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


}
