package com.softup.utkarsh.feedhigh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Feedback extends AppCompatActivity {
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    TextView t5;
    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;
    EditText e5;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        t1=(TextView)findViewById(R.id.textView1);
        t2=(TextView)findViewById(R.id.textView2);
        t3=(TextView)findViewById(R.id.textView3);
        t4=(TextView)findViewById(R.id.textView4);
        t5=(TextView)findViewById(R.id.textView5);
        e1=(EditText) findViewById(R.id.editText1);
        e2=(EditText) findViewById(R.id.editText2);
        e3=(EditText) findViewById(R.id.editText3);
        e4=(EditText) findViewById(R.id.editText4);
        e5=(EditText) findViewById(R.id.editText5);

        b1=(Button)findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Feedback.this,OthersForm.class);
                startActivity(intent);
                finish();
            }
        });


        String rev1=e1.getText().toString();
        String rev2=e1.getText().toString();
        String rev3=e1.getText().toString();
        String rev4=e1.getText().toString();
        String rev5=e1.getText().toString();

    }
}
