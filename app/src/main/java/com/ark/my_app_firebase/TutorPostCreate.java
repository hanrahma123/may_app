package com.ark.my_app_firebase;
//Written by Mark
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TutorPostCreate extends AppCompatActivity {

    private EditText subject, msg, Experience;
    private FirebaseDatabase database;
    private DatabaseReference myRef, Num;

    private Button btncreatePost;
    private FirebaseAuth mAuth;
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_post);

        number =1;
        subject = (EditText)findViewById(R.id.etSubjects);
        Experience = (EditText)findViewById(R.id.etExp);
        msg = (EditText)findViewById(R.id.etBio);

       database = FirebaseDatabase.getInstance();  //initialize db
        myRef = database.getReference();
        Num = myRef.child("Num");
        Num.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               try {
                   number = dataSnapshot.getValue(int.class);
                   Toast.makeText(getApplicationContext(), "Number of Posts on DB:" + number, Toast.LENGTH_LONG).show();
               }
               catch ( Exception e){
                   Toast.makeText(getApplicationContext(), "Err cant connect to DB,internet?" , Toast.LENGTH_LONG).show();
               }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btncreatePost = (Button)findViewById(R.id.btnAdd);
        btncreatePost.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createPost();
            }
        });

    }

    private void createPost(){
        mAuth = FirebaseAuth.getInstance();
        //String email = mAuth.getCurrentUser().getEmail().toString();
        Post newPost = new Post(subject.getText().toString(), /*email*/ "emailstring", msg.getText().toString(), Experience.getText().toString()); //mark change later

        //post new post
        myRef.child("Post").child(String.valueOf(number)).setValue(newPost);

        Num.setValue(number+1);

    }
}
