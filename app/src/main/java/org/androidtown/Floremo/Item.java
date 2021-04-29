package org.androidtown.Floremo;

//꽃밭에 들어가는 꽃들의 이미지 class
public class Item {

    //String or Long: Firebase에서 이미지를 가져올 때 URL 주소로 가져오기 때문.
    //String: 코드로 이미지 삽입
    //Long: DB에서 직접 입력
    private String flowerImg;

    public Item(){}

    public String getFlowerImg(){
        return flowerImg;
    }

    public void setFlowerImg(String flowerImg){
        this.flowerImg = flowerImg;
    }
}
