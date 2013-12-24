package com.ganlu.tomoko;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ResultActivity extends Activity {

    private StatusDataSource dtStatus;
    private ArrayList<Status> statusList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle b = getIntent().getExtras();
        int wrongCount = b.getInt("wrongCount");
        int rightCount = b.getInt("rightCount");
        ArrayList < Integer > test = b.getIntegerArrayList("test");
        TextView txtWrongCount = (TextView)(findViewById(R.id.txtWrong));
        TextView txtRightCount = (TextView)(findViewById(R.id.txtRight));
        txtWrongCount.setText(String.valueOf(wrongCount));
        txtRightCount.setText(String.valueOf(rightCount));

        Animation scale = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_PARENT, 1.0f);
        Animation alpha = new AlphaAnimation(0 , 1);
        scale.setDuration(1000);
        alpha.setDuration(2000);
        AnimationSet animSet = new AnimationSet(true);
        animSet.setFillEnabled(true);
        animSet.addAnimation(scale);
        ImageView rightCol = (ImageView)findViewById(R.id.rightCol);

        ImageView wrongCol = (ImageView)findViewById(R.id.wrongCol);
        int tot = wrongCount + rightCount;
        rightCol.getLayoutParams().height = rightCount * 250 / tot;
        wrongCol.getLayoutParams().height = wrongCount * 250 / tot;
        wrongCol.startAnimation(animSet);
        rightCol.startAnimation(animSet);
        txtWrongCount.startAnimation(alpha);
        txtRightCount.startAnimation(alpha);
        dtStatus = new StatusDataSource(this);
        dtStatus.open();


        statusList = dtStatus.getAllStatus();

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.statisticContainer);

        for (Integer idx : test){
            Status s = dtStatus.getStatusById(idx);
            View temp = getLayoutInflater().inflate(R.layout.statistic_row , null);
            ((TextView)temp.findViewById(R.id.txtCharacter)).setText(s.getCharacter() + " (" + s.getRome() + ")");
            ((TextView)temp.findViewById(R.id.txtRatio)).setText(String.valueOf((int)(s.getRatio() * 100)) + "%" );

            int w1 = (int)(150 * s.getRatio());
            int w2 = 150 - w1;
            temp.findViewById(R.id.percentageRight).getLayoutParams().width = w1;
            temp.findViewById(R.id.percentageWrong).getLayoutParams().width = w2;
            linearLayout.addView(temp);
        }

    }

    @Override
    public void onBackPressed() {
        finish();//go back to the previous Activity
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
