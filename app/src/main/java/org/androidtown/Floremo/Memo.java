package org.androidtown.Floremo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

//database에 넣을 메모 class
public class Memo implements Parcelable {
    private String txt;
    private Date createDate, updateDate;
    private String flowerImg;

    public Memo(){}

    protected Memo(Parcel in) {
        txt = in.readString();
        flowerImg = in.readString();
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
    public void setFlowerImg(String flowerImg){
        this.flowerImg = flowerImg;
    }
    public String getTxt(){
        return txt;
    }
    public void setTxt(String txt){
        this.txt=txt;
    }
    public Date getCreateDate(){
        return createDate;
    }
    public void setCreateDate(Date createDate){
        this.createDate = createDate;
    }
    public Date getUpdateDate(){
        return updateDate;
    }
    public void setUpdateDate(Date updateDate){
        this.updateDate = updateDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(txt);
        dest.writeString(flowerImg);
    }
}
