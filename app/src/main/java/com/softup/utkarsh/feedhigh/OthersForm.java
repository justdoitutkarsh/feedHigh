package com.softup.utkarsh.feedhigh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hsalf.smilerating.SmileRating;

public class OthersForm extends AppCompatActivity {

    SmileRating smileRating;
    int score=0;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_form);
        btn=(Button)findViewById(R.id.btnSave);
        smileRating=(SmileRating)findViewById(R.id.smiley);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(int smiley, boolean reselected) {

                switch (smiley)
                {
                    case SmileRating.BAD:
                        Toast.makeText(OthersForm.this,"BAD", Toast.LENGTH_SHORT).show();
                        score=2;
                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(OthersForm.this,"GOOD", Toast.LENGTH_SHORT).show();
                        score=4;
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(OthersForm.this,"GREAT", Toast.LENGTH_SHORT).show();
                        score=5;
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(OthersForm.this,"OKAY", Toast.LENGTH_SHORT).show();
                        score=3;
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(OthersForm.this,"TERRIBLE", Toast.LENGTH_SHORT).show();
                        score=1;
                        break;


                }
            }
        });

      /*  smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {

               // Toast.makeText(MainActivity.this,"Selected rating"+level,Toast.LENGTH_LONG).show();
                Toast.makeText(MainActivity.this,"Selected rating"+score,Toast.LENGTH_LONG).show();
            }
        });


*/
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OthersForm.this,Home.class);
                startActivity(intent);
            }
        });

    }
}
