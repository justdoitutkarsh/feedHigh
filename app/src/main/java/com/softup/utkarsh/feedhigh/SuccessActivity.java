package com.softup.utkarsh.feedhigh;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by AnupTechTips on 13/7/18.
 */

public class SuccessActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        tv=(TextView)findViewById(R.id.tv);
        // Its used to retrieve data
       // SharedPreferences sharedPref = getSharedPreferences("mypref", 0);
       // String name = sharedPref.getString("name", "");
        //tv.setText(name);

    }
}
