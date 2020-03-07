package com.ark.my_app_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.net.URI;


//THIS IS THE TUTOR'S PORTAL PAGE SO WHEN HE LOGS IN OR SIGNS IN WE NEED TO SEND DATA WITH INTENT LIKE EMAIL->TO MAKE SURE WHICH USER IS THE CURRENT
//USER ...IN ORDER TO GET HIS DETAILS ON THE PORTAL PAGE


public class profileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference dbref;
    private DatabaseReference dbref2;



    private static final String USERS ="Users";
    String email,k;

    private TextView textView6_showname;
    private TextView textView9_email;
    private TextView textView11_phone;
    private TextView textView3;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toast.makeText(getApplicationContext(), "Welcome to your tutor profile!", Toast.LENGTH_LONG).show();
        //Toolbar t = findViewById(R.id.toolbar);
        //loadUserInformation();

        textView6_showname = (TextView)findViewById(R.id.textView6_showname);
        textView9_email = (TextView)findViewById(R.id.textView9);
        textView11_phone = (TextView)findViewById(R.id.textView11);
        textView3= (TextView)findViewById(R.id.textView3);
        imageView=(ImageView)findViewById(R.id.imageView2);

        //creating firebase instances for realtime db
        db = FirebaseDatabase.getInstance();
        dbref = db.getReference(USERS);
        dbref2 = db.getReference("uploads");



        //intent get from the login page
        Intent intent =getIntent();
        email = intent.getStringExtra("email");
       // k = intent.getStringExtra("url");
       // final Uri myUri = Uri.parse(k);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds :dataSnapshot.getChildren()){
                    if(ds.child("email").getValue().equals(email)){
                        textView6_showname.setText(ds.child("name").getValue(String.class));
                        textView9_email.setText(ds.child("email").getValue(String.class));
                        textView11_phone.setText(ds.child("phone").getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        dbref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // Picasso.get().load(myUri).into(imageView);
                for(DataSnapshot pds : dataSnapshot.getChildren()){
                    if(pds.child("name").getValue().equals(email)){
                     String url=pds.child("imageUrl").getValue(String.class);
                    // Uri z = Uri.parse(url);
                        //textView3.setText(url);
                       //String link=url.getName();

                        Picasso.get().load(url).rotate(90).fit().centerInside().transform(new RoundedTransformation(100, 0)).into(imageView);
                       // Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/fir-f5631.appspot.com/o/uploads%2F1582886990379.jpg?alt=media&token=7cdbb31f-ead2-4477-ac5b-e43b5b7d1bcd").into(imageView);
                       // Toast.makeText(getApplicationContext(), (CharSequence) z, Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public class RoundedTransformation implements com.squareup.picasso.Transformation {
        private final int radius;
        private final int margin;

        public RoundedTransformation(final int radius, final int margin) {
            this.radius = radius;
            this.margin = margin;
        }

        @Override
        public Bitmap transform(final Bitmap source) {
            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP,
                    Shader.TileMode.CLAMP));

            Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            canvas.drawRoundRect(new RectF(margin, margin, source.getWidth() - margin,
                    source.getHeight() - margin), radius, radius, paint);

            if (source != output) {
                source.recycle();
            }
            return output;
        }

        @Override
        public String key() {
            return "rounded(r=" + radius + ", m=" + margin + ")";
        }
    }





    //code for logout menu
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
