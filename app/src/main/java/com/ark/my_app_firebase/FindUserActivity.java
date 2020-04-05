package com.ark.my_app_firebase;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private  RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;

   //contact list has the listof all your contact numbers on your phone
    //userlist will have the users of the app, those who are in the db
    ArrayList<UserObject> userList,contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contactList =  new ArrayList<>();
        userList =  new ArrayList<>();

        initializeRecyclerView();
        getContactList();
    }

    //GETTING CONTACT LIST ->firstly goes through all the contacts in my phone, then finds which users of those are app users
    private  void getContactList(){

        String isoPrefix = getCountryISO();

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phone = phone.replace(" ","");
            phone = phone.replace("-","");
            phone = phone.replace("(","");
            phone = phone.replace(")","");

            if(!String.valueOf(phone.charAt(0)).equals("+")){
                phone = isoPrefix + phone;
            }

             UserObject mContact = new UserObject("",name,phone);
             contactList.add(mContact);
             //calling to find the users on ur phone that are app users to chat further
             getUserDetails(mContact);

        }
    }
//displays those users who are app users from your contact list
    private void getUserDetails(UserObject mContact) {

        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("chatuser");
        //checking which user is on our db i.e app user
        Query query = mUserDB.orderByChild("phone").equalTo(mContact.getPhone());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    //user exist on app
                    String phone= ""
                            ,name ="";
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        if (childSnapshot.child("phone").getValue() != null)
                            phone = childSnapshot.child("phone").getValue().toString();
                        if (childSnapshot.child("name").getValue() != null)
                            name = childSnapshot.child("name").getValue().toString();


                        UserObject mUser = new UserObject(childSnapshot.getKey(),name,phone);
                        if (name.equals(phone)) {
                            for (UserObject mcontactIterator : contactList) {
                                if (mcontactIterator.getPhone().equals(mUser.getPhone())) {
                                    mUser.setName(mcontactIterator.getName());
                                }
                            }
                        }
                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();
                        return;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }


    //checking /working with the iso code for the contact in our phone as firebase always stores with a prefix like +353
    private String getCountryISO(){
        String iso = null;
        TelephonyManager telephonyManager =  (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if (telephonyManager.getNetworkCountryIso()!=null  ){
            if (!telephonyManager.getNetworkCountryIso().toString().equals("")){
                iso= telephonyManager.getNetworkCountryIso().toString();
            }
        }


        return CountryToPhonePrefix.getPhone(iso);
    }
//basically front-end recycler view = type of infinite scroll
    private void initializeRecyclerView() {
        mUserList= findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
}

}
