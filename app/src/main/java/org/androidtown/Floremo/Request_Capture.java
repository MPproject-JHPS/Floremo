package org.androidtown.Floremo;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Request_Capture {


    public Request_Capture(LinearLayout view, String title){
        if(view==null){ //Null Point Exception ERROR 방지
            System.out.println("::::ERROR:::: view == NULL");
            return;
        }

        /* 캡쳐 파일 저장 */
        view.buildDrawingCache(); //캐시 비트 맵 만들기
        Bitmap bitmap = view.getDrawingCache();
        FileOutputStream fos;

        /* 저장할 폴더 Setting */
        File uploadFolder = Environment.getExternalStoragePublicDirectory("/DCIM/Floremo/"); //저장 경로 (File Type형 변수)


        if (!uploadFolder.exists()) { //만약 경로에 폴더가 없다면
            uploadFolder.mkdir(); //폴더 생성
        }

        /* 파일 저장 */
        String Str_Path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Floremo/"; //저장 경로 (String Type 변수)

        try{
            fos = new FileOutputStream(Str_Path+title+".jpg"); // 경로 + 제목 + .jpg로 FileOutputStream Setting
            bitmap.compress(Bitmap.CompressFormat.JPEG,80,fos);
        }catch (Exception e){
            e.printStackTrace();
        }

        MediaScanner ms = MediaScanner.newInstance(getApplicationContext());

        try { // TODO : 미디어 스캔
            ms.mediaScanning(Str_Path + title + ".jpg");
        }catch (Exception e) {
            e.printStackTrace();
            System.out.println("::::ERROR:::: "+e);
        }

    }
}
