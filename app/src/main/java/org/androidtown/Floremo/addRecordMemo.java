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
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;

public class addRecordMemo extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDataBase;
    ImageView image;
    private ImageButton location;
    private final int GET_GALLERY_IMAGE = 200;
    private Button saveButton;
    private EditText etContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_memo);
        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("folder_name");
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
               // Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                //startActivity(intent);
            }
        });

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveMemo();

            }
        });
        etContent = findViewById(R.id.content);

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

    private void saveMemo(){
        String text = etContent.getText().toString();
        if(text.isEmpty()){ //텍스트를 입력하지 않으면 저장할 수 없다
            Snackbar.make(etContent, "메모를 입력하세요", Snackbar.LENGTH_LONG).show();
            return;
        }
        Memo memo = new Memo();
        memo.setTxt(etContent.getText().toString());
        memo.setCreateDate(new Date());
        mFirebaseDataBase.getReference("memos/" +mFirebaseUser.getUid()).push()
                .setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(etContent, "메모가 저장되었습니다.", Snackbar.LENGTH_LONG).show();
            }
        });
        /*Glide.with(getApplicationContext()).lode(storageReference).into(image);*/
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}