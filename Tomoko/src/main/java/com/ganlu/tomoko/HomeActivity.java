package com.ganlu.tomoko;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final Button btnHiragana = (Button) findViewById(R.id.button_hiragana);
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

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.home, menu);

        // Calling super after populating the menu is necessary here to ensure that the
        // action bar helpers have a chance to handle this event.
        return super.onCreateOptionsMenu(menu);
    }
}
