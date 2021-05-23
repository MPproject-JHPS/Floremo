package org.androidtown.Floremo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//database에 넣을 메모 class
public class Memo implements Parcelable {
    private String txt;
    private String date;
    private String flowerImg;
    private String imageUrl;
    private String key;

    public Memo(){}

    protected Memo(Parcel in) {
        txt = in.readString();
        date = in.readString();
        flowerImg = in.readString();
        imageUrl = in.readString();
        key = in.readString();
    }

    public static final Creator<Memo> CREATOR = new Creator<Memo>() {
        @Override
        public Memo createFromParcel(Parcel in) {
            return new Memo(in);
        }

        @Override
        public Memo[] newArray(int size) {
            return new Memo[size];
        }
    };

    public String getFlowerImg(){
        return flowerImg;
    }
    public void setFlowerImg(String flowerImg){ this.flowerImg = flowerImg; }
    public String getTxt(){
        return txt;
    }
    public void setTxt(String txt){
        this.txt=txt;
    }
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getImageUrl(){ return imageUrl; }
    public void setImageUrl(String imageUrl){ this.imageUrl = imageUrl; }
    public String getKey(){ return key; }
    public void setKey(String key){ this.key = key; }

    @Override
    public int describeContents() {
        return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(txt);
        dest.writeString(date);
        dest.writeString(flowerImg);
        dest.writeString(imageUrl);
        dest.writeString(key);
    }


}