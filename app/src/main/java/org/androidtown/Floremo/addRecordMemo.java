package org.androidtown.Floremo;



import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;

public class addRecordMemo extends AppCompatActivity {
    ImageView image;
    private ImageButton location;
    private final int GET_GALLERY_IMAGE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_memo);
        checkSelfPermission();

        image = findViewById(R.id.image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });


        location = findViewById(R.id.locationButton);
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });


    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions,
                                          @NonNull int[] grantResults) {
        if(requestCode==1){
            int length = permissions.length;
            for(int i =0; i<length; i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    //동의
                    Log.d("addRecordMemo","권한 허용 : " +permissions[i]);
                }
            }
        }
    }
    public void checkSelfPermission() {
        String temp = "";
        //파일 읽기 권한 확인
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (TextUtils.isEmpty(temp) == false) {
            //권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else {
            //모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            image.setImageURI(selectedImageUri);
        }
    }

}