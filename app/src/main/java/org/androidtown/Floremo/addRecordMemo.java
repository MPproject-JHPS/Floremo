package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.UploadTask;

public class addRecordMemo extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDataBase;
    ImageView image;
    private ImageButton location;
    private final int GET_GALLERY_IMAGE = 200;
    private EditText etContent;
    private DatabaseReference databaseReference;
    Uri selectedImageUri;
    private TextView textDate;
    private int emotion;
    private static final String TAG = "addRecordMemo";
    private String date;

    Uri uri_simage;
    ImageView image2;
    private ArrayList<Uri> ImageList = new ArrayList<Uri>();

    Memo memo = new Memo();
    String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_memo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDataBase.getReference();
        checkSelfPermission();
        textDate = findViewById(R.id.textDate);


        //Recording class에서 선택한 날짜 가져오기
        //intent로 감정을 넘겨받는다고 가정.
        Intent passedIntent = getIntent();
        if (passedIntent != null) {
            Bundle myBundle = passedIntent.getExtras();
            date = myBundle.getString("date");
            emotion = myBundle.getInt("emotion"); //happy, sad, ---
            uri_simage = passedIntent.getParcelableExtra("uri_simage"); //Recording 화면에서 전달한 꽃이미지에 대한 Uri 받음
            textDate.setText(date);
        }

        image2 = findViewById(R.id.image2); //꽃이미지 띄우는 ImageView --> width:0, height:0으로 설정하여 보이지 않게 처리하였음.

        image2.setImageURI(uri_simage);

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

        etContent = findViewById(R.id.content);
    }


    //툴바 선택 (DB 저장, 뒤로가기 버튼)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                clickUpload();
                saveMemo();
                return true;
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작. recording으로 돌아가기
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    //사진 선택하기
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            image.setImageURI(selectedImageUri);
        }
    }

    //툴바 사용
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //firebase storage에 업로드하기
    public void clickUpload() {
        //FirebaseStorage를 통해 관리하는 객체 얻어오기
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images");
        StorageReference userRef = imageRef.child(mFirebaseUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = mFirebaseUser.getUid() + "_" + timeStamp + "flower";
        StorageReference fileRef = userRef.child(filename);


        //참조객체를 통해 이미지 파일 업로드하기
        //업로드가 성공적으로 되면 images라는 폴더에 uid 폴더가 생성된다.
        //uid 폴더 안에 uid+날짜시간분초로 파일 이름이 생성된다.
        //UploadTask uploadTask = fileRef.putFile(selectedImageUri);
//        ImageList.add(uri_simage); //get(0)
//        ImageList.add(selectedImageUri); //get(1)
        //UploadTask uploadTask = fileRef.putFile(uri_simage);


        //꽃 이미지 저장
        UploadTask uploadTask = fileRef.putFile(uri_simage);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Uri downloadUrl = uri;
                        memo.setFlowerImg(uri_simage.toString());
                        //String key;
                        //받아온 감정에 따라서 분류해서 Realtime DB에 넣기
                        if (emotion == 0) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 1) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 2) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 3) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 4) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }


    private void saveMemo(){
        //FirebaseStorage를 통해 관리하는 객체 얻어오기
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images");
        StorageReference userRef = imageRef.child(mFirebaseUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = mFirebaseUser.getUid() + "_" + timeStamp;
        StorageReference fileRef = userRef.child(filename);

        //image2.setImageURI(uri_simage);

        //참조객체를 통해 이미지 파일 업로드하기
        //업로드가 성공적으로 되면 images라는 폴더에 uid 폴더가 생성된다.
        //uid 폴더 안에 uid+날짜시간분초로 파일 이름이 생성된다.
        //UploadTask uploadTask = fileRef.putFile(selectedImageUri);
        //ImageList.add(uri_simage); //get(0)
        //ImageList.add(selectedImageUri); //get(1)
        //UploadTask uploadTask = fileRef.putFile(uri_simage);


        //text+이미지 업로드
        UploadTask uploadTask = fileRef.putFile(selectedImageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Memo memo = new Memo();
                        Uri downloadUrl = uri;
                        //memo.setFlowerImg(downloadUrl.toString());
                        memo.setImageUrl(downloadUrl.toString());
                        String text = etContent.getText().toString();
                        memo.setTxt(etContent.getText().toString());
                        memo.setDate(date);
                        //String key;
                        //받아온 감정에 따라서 분류해서 Realtime DB에 넣기
                        if (emotion == 0) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 1) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 2) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 3) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 4) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "메모가 저장되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                });
            }
        });

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}