package com.ark.my_app_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static android.icu.lang.UCharacter.JoiningGroup.PE;

public  class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    //declaring constant for inage chooser
    private static  final int PICK_IMAGE_REQUEST=1;
    //String download_url;




    int x;
    String user_type="";
    Uri l;

    //variable views for the signup activity
    private Button button_signup;
    private EditText email ;
    private EditText pass ;

    private EditText name ;
    private EditText phone ;
    private EditText type ;
    private ImageView  imageView;
    private Button button_profile;
    private Switch aSwitch;
    private Uri mImageUri;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    private ProgressBar progressBar2;
    //declaring instance of firebase auth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        //Initializing views for this activity
        email = (EditText)findViewById(R.id.etEmail);
        pass = (EditText)findViewById(R.id.etPassword);
        name = (EditText)findViewById(R.id.etName);
        phone = (EditText)findViewById(R.id.etPhone);
        //type = (EditText)findViewById(R.id.editText3_signup_op);


        button_signup=(Button)findViewById(R.id.btnRegister);
        button_profile=(Button)findViewById(R.id.button_profilepic);
        progressBar2 = (ProgressBar)findViewById(R.id.register_progress);

        imageView=(ImageView)findViewById(R.id.imageView);

        aSwitch=(Switch) findViewById(R.id.isTutor);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        button_signup.setOnClickListener(this);


        //upload and retrieve image from uploads table
        mStorageRef= FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("uploads");

        //listener for image view

       /* imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImagechoser();
            }
        });*/

        button_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cheack permission runtime
                    /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                            //permission not granted , request it
                            String[] permissions ={Manifest.permission.READ_EXTERNAL_STORAGE};
                            requestPermissions(permissions,PERM);
                        }else{
                            //permission already granted
                        }
                    }else{

                    }*/
                    openFileChooser();
            }
        });




        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    x=1;// user is tutor
                    user_type="Tutor";

                }else{
                    x=0;// user is student
                    user_type="Student";
                }
            }
        });


    }

    private void openFileChooser(){
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null ){
            mImageUri=data.getData();
            imageView.setImageURI(mImageUri);//loading image onto the image view
        }
    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CHOOSE_IMAGE  && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriprofile_image = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriprofile_image);
                imageView.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImageToFirebaseStorage() {
    StorageReference profileimageref= FirebaseStorage.getInstance().getReference("profilepics/"+  System.currentTimeMillis()+".jpg");
      if(uriprofile_image !=null){
          profileimageref.putFile(uriprofile_image)
                  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             download_url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                      }
                  })
                 .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                      }
          });
      }

    }

    private void showImagechoser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile picture!"),CHOOSE_IMAGE);
    }*/

    public void gotopage_func(String mail){

        uploadFile(mail);

        if(x==1){
            Intent intent = new Intent(SignupActivity.this,Tutor_View.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//CLEARS ALL ACTIVITIES ON TOP OF STACK
            intent.putExtra("email", mail);
           // intent.putExtra("url", l);
            startActivity(intent);


        }else{
            Intent intent = new Intent(SignupActivity.this,Student_View.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//CLEARS ALL ACTIVITIES ON TOP OF STACK
            intent.putExtra("email", mail);
           // intent.putExtra("url", l);
            startActivity(intent);


        }

    }


    private String getFileExtension(Uri uri){
        ContentResolver cr= getContentResolver();
        MimeTypeMap mime= MimeTypeMap.getSingleton();
        return  mime.getExtensionFromMimeType(cr.getType(uri));
    }


    private void uploadFile(final String email){


        if (mImageUri != null)
        {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+ "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
            {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                {
                    if (!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>()
            {
                @Override
                public void onComplete(@NonNull Task<Uri> task)
                {
                    if (task.isSuccessful())
                    {
                        Uri downloadUri = task.getResult();
                        //Log.e(TAG, "then: " + downloadUri.toString());


                        upload u = new upload(email,
                                downloadUri.toString());

                        mDatabaseRef.push().setValue(u);
                    } else
                    {
                        Toast.makeText(getApplicationContext(), "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }








        /*
        if(mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()+ "." + getFileExtension(mImageUri));

           mUploadTask =  fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // l=taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                            //Uri downloadUri = taskSnapshot.getResult();
                           // String downloadURL = downloadUri.toString();
                            upload u= new upload(email,taskSnapshot..toString());
                            String uploadId= mDatabaseRef.push().getKey();

                            mDatabaseRef.child(uploadId).setValue(u);
                            //Toast.makeText(getApplicationContext(), "Picture was uploaded!", Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

        }else{
            Toast.makeText(this, "NO file selected!", Toast.LENGTH_LONG).show();

        }*/
    }







    public void registerUser(){

        final String name1 =name.getText().toString().trim();
        final String phone1 = phone.getText().toString().trim();
        final String type1 = user_type;
        final String mail= email.getText().toString();
        String password = pass.getText().toString();

        //uploadFile(mail);

        if(mail.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
    /*
        if(Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Email is Invalid!");
            email.requestFocus();
            return;
        }*/

        if(password.length() < 6){
            pass.setError("Password should be atleast 6 characters long!");
            pass.requestFocus();
            return;

        }

        if(password.isEmpty()){
            pass.setError("Password is required!");
            pass.requestFocus();
            return;

        }

        //CHECKING TO PREVENT MULTIPLE IMAGES UPLOAD FOR INE USER AT A SINGLE TIME
        if(mUploadTask != null && mUploadTask.isInProgress()){
            Toast.makeText(getApplicationContext(), "Upload in progress!", Toast.LENGTH_LONG).show();
        }else{
            uploadFile(mail);
        }

        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                  @Override
                                  public void onComplete(@NonNull Task<AuthResult> task) {
                                      progressBar2.setVisibility(View.GONE);
                                        if(task.isSuccessful()) {

                                            //string additional fields like name, type ,phone number
                                            User user = new User(name1,mail,phone1,type1);

                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        //Toast.makeText(getApplicationContext(), "Successfully registered!", Toast.LENGTH_LONG).show();
                                                        finish();
                                                        //based on switch go to tutor/student page

                                                        gotopage_func(mail);


                                                    }else{
                                                        //display any other msg
                                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });


                                            //CODE FOR INTENT TO THE NEXT PAGE->DASH BOARD FOR STUDENT/TUTOR
                                        }else if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                            Toast.makeText(getApplicationContext(), "You are already registered!", Toast.LENGTH_LONG).show();

                                        }else{
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                  }
                              });


        //saving the profile pic in firebase storage
        /*FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null && download_url != null){
           UserProfileChangeRequest profile = new UserProfileChangeRequest().Builder().getDisplayName(name1).setPhotoUri(Uri.parse(download_url)).build();

        }*/



    }



   @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnRegister:
                //Toast.makeText(getApplicationContext(), "clicked register buton1", Toast.LENGTH_LONG).show();

                                        registerUser();
                                        break;
          /*  case  R.id.button_login:finish();
                                    startActivity(new Intent(this,MainActivity.class));
                                    break;*/
        }
    }
}
