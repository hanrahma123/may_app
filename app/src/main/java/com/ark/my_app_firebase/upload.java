package com.ark.my_app_firebase;

public class upload {
    private String name;
    private String imgurl;
    public upload(){

    }
    public upload(String name, String imageurl){
        if(name.trim().equals((""))){
            name="NO Name";
        }
        this.name=name;
        imgurl=imageurl;


    }
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getImageUrl(){
        return imgurl;
    }

    public void setImageUrl(String imageurl){
        imgurl=imageurl;
    }



}
