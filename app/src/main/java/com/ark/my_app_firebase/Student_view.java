package com.ark.my_app_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ToggleButton button1;
    private ToggleButton button2;
    private ToggleButton button3;

    int counter1 = 0;
    int flag1 = 0;
    TextView score1;

    int counter2 = 0;
    int flag2 = 0;
    TextView score2;

    int counter3 = 0;
    int flag3 = 0;
    TextView score3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score1 = (TextView) findViewById(R.id.scoreView1);
        score2 = (TextView) findViewById(R.id.scoreView2);
        score3 = (TextView) findViewById(R.id.scoreView3);

        button1 = (ToggleButton) findViewById(R.id.button_1);
        button2 = (ToggleButton) findViewById(R.id.button_2);
        button3 = (ToggleButton) findViewById(R.id.button_3);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Subjects));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        Spinner upvotesSpinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> upvoteAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.UpVotes));
        upvoteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        upvotesSpinner.setAdapter(upvoteAdapter);

        Spinner courseLevelSpinner = (Spinner) findViewById(R.id.spinner3);

        ArrayAdapter<String> courseLevelAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.CourseLevel));
        courseLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseLevelSpinner.setAdapter(courseLevelAdapter);
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.button_1:
                if (button1.isChecked()) {
                    button1.setChecked(true);
                    Toast.makeText(MainActivity.this, "Liked", Toast.LENGTH_LONG).show();
                    counter1++;

                } else {
                    button1.setChecked(false);
                    Toast.makeText(MainActivity.this, "Unliked", Toast.LENGTH_LONG).show();
                    counter1--;
                }
                score1.setText(Integer.toString(counter1));


                break;
            case R.id.button_2:
                if (button2.isChecked()) {
                    button2.setChecked(true);
                    Toast.makeText(MainActivity.this, "Liked", Toast.LENGTH_LONG).show();
                    counter2++;

                } else {
                    button2.setChecked(false);
                    Toast.makeText(MainActivity.this, "Unliked", Toast.LENGTH_LONG).show();
                    counter2--;
                }
                score2.setText(Integer.toString(counter2));

                break;
            case R.id.button_3:
                if (button3.isChecked()) {
                    button3.setChecked(true);
                    Toast.makeText(MainActivity.this, "Liked", Toast.LENGTH_LONG).show();
                    counter3++;

                } else {
                    button3.setChecked(false);
                    Toast.makeText(MainActivity.this, "Unliked", Toast.LENGTH_LONG).show();
                    counter3--;
                }
                score3.setText(Integer.toString(counter3));

                break;

            default:
                break;
        }
    }
}
