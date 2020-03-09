package com.ark.my_app_firebase;
//Written by Mark
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riyagayasen.easyaccordion.AccordionView;

public class Student_View extends AppCompatActivity {

    private DatabaseReference myRef, posts;
    private FirebaseDatabase database;
    private TextView tv1, tv2, tv3;
    private String msg1, msg2, msg3;
    private AccordionView h1,h2,h3;
   // private Button addpost; //added a button to create posts remove if un-needed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_view);

      // addpost =findViewById(R.id.add_post);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        h1 = findViewById(R.id.sv_subject1);
        h2 = findViewById(R.id.sv_subject2);
        h3 = findViewById(R.id.sv_subject3);


        database = FirebaseDatabase.getInstance();  //initialize db
        myRef = database.getReference();
        posts = myRef.child("Post");

//        addpost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(getBaseContext(), TutorPostCreate.class);
//                startActivity(i);
//            }
//        });

        posts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    msg1 = (dataSnapshot.child("0").child("msg").getValue()).toString() ;
                    h1.setHeadingString("Subjects:" + (dataSnapshot.child("0").child("subject").getValue()).toString() +
                            "\n Email:" + (dataSnapshot.child("0").child("email").getValue()).toString()
                    );
                    tv1.setText(msg1);

                    try{    msg2 = (dataSnapshot.child("1").child("msg").getValue()).toString() ;
                        h2.setHeadingString("Subjects:" + (dataSnapshot.child("1").child("subject").getValue()).toString() +
                                "\n Email:" + (dataSnapshot.child("1").child("email").getValue()).toString()
                        );
                        tv2.setText(msg2);
                    }catch (Exception e){
                        h2.setVisibility(View.INVISIBLE);
                    }


                    try{    msg3 = (dataSnapshot.child("2").child("msg").getValue()).toString();
                        h3.setHeadingString("Subjects:" + (dataSnapshot.child("1").child("subject").getValue()).toString() +
                                "\n Email:" + (dataSnapshot.child("2").child("email").getValue()).toString()
                        );
                        tv3.setText(msg3);
                    }catch (Exception e){
                        h3.setVisibility(View.INVISIBLE);
                    }

                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Err cant connect to DB,internet?", Toast.LENGTH_LONG).show();
                    h1.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return  super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return true;
    }
}