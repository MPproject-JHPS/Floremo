package org.androidtown.Floremo;

//꽃밭에 들어가는 꽃들의 이미지 class

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;

public class Item {
    private static int image;
    private static Item instance;

    public Item(int img){
        this.image = image;
    }

    public static synchronized Item getInstance() {
        if (instance == null) {
            instance = new Item(image);
        }
        return instance;
    }

    public int getImage(){
        return image;
    }
    public void setImage(int image){
        this.image = image;
    }
}
