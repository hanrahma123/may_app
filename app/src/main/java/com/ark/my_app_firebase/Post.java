package com.ark.my_app_firebase;
//Written by Mark
public class Post {
    public String subject;
    public String email;
    public String msg;
    public String experience;

    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Post(String subject, String email , String msg, String Exp) {
        this.subject = subject;
        this.email = email;
        this.msg = msg;
        this.experience = Exp;

    }


}
