package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
    Uri selectedImageUri = null;
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
        mFirebaseAuth = FirebaseAuth.getInstance(); //????????? ????????????
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//?????? ?????? ????????? ?????? ??? ????????? ?????? ??????
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDataBase.getReference();

        textDate = findViewById(R.id.textDate);


        //Recording class?????? ????????? ?????? ????????????
        //intent??? ????????? ?????????????????? ??????.
        Intent passedIntent = getIntent();
        if (passedIntent != null) {
            Bundle myBundle = passedIntent.getExtras();
            date = myBundle.getString("date");
            emotion = myBundle.getInt("emotion"); //happy, sad, ---
            uri_simage = passedIntent.getParcelableExtra("uri_simage"); //Recording ???????????? ????????? ??????????????? ?????? Uri ??????
            textDate.setText(date);
        }

        image2 = findViewById(R.id.image2); //???????????? ????????? ImageView --> width:0, height:0?????? ???????????? ????????? ?????? ???????????????.

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



    //?????? ?????? (DB ??????, ???????????? ??????)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Recording recording = (Recording) Recording.Recording;
        switch (item.getItemId()) {
            case R.id.menu:
                if(selectedImageUri == null){
                    Toast.makeText(addRecordMemo.this, "???????????? ??????????????????", Toast.LENGTH_LONG).show();
                    break;
                }
                clickUpload();
                saveMemo();
                finish();
                recording.finish();
                return true;
            case android.R.id.home: //toolbar??? back??? ????????? ??? ??????. recording?????? ????????????
                onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //?????? ????????????
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

    //?????? ??????
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    //firebase storage??? ???????????????
    public void clickUpload() {
        //FirebaseStorage??? ?????? ???????????? ?????? ????????????
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images");
        StorageReference userRef = imageRef.child(mFirebaseUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = mFirebaseUser.getUid() + "_" + timeStamp + "flower";
        StorageReference fileRef = userRef.child(filename);


        //??????????????? ?????? ????????? ?????? ???????????????
        //???????????? ??????????????? ?????? images?????? ????????? uid ????????? ????????????.
        //uid ?????? ?????? uid+????????????????????? ?????? ????????? ????????????.
        //UploadTask uploadTask = fileRef.putFile(selectedImageUri);
//        ImageList.add(uri_simage); //get(0)
//        ImageList.add(selectedImageUri); //get(1)
        //UploadTask uploadTask = fileRef.putFile(uri_simage);


        //??? ????????? ??????
        UploadTask uploadTask = fileRef.putFile(uri_simage);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        memo.setFlowerImg(downloadUrl.toString());
                        //String key;
                        //????????? ????????? ????????? ???????????? Realtime DB??? ??????
                        if (emotion == 0) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 1) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 2) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 3) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 4) {
                            key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").push().getKey();
                            memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }


    private void saveMemo(){
        //FirebaseStorage??? ?????? ???????????? ?????? ????????????
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images");
        StorageReference userRef = imageRef.child(mFirebaseUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = mFirebaseUser.getUid() + "_" + timeStamp;
        StorageReference fileRef = userRef.child(filename);

        //??????????????? ?????? ????????? ?????? ???????????????
        //???????????? ??????????????? ?????? images?????? ????????? uid ????????? ????????????.
        //uid ?????? ?????? uid+????????????????????? ?????? ????????? ????????????.
        //UploadTask uploadTask = fileRef.putFile(selectedImageUri);
        //ImageList.add(uri_simage); //get(0)
        //ImageList.add(selectedImageUri); //get(1)
        //UploadTask uploadTask = fileRef.putFile(uri_simage);


        //text+????????? ?????????
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
                        //????????? ????????? ????????? ???????????? Realtime DB??? ??????
                        if (emotion == 0) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/happy").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 1) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/surprised").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 2) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 3) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/sad").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (emotion == 4) {
                            //key = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").push().getKey();
                            //memo.setKey(key);
                            mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/soso").child(key).setValue(memo).addOnSuccessListener(addRecordMemo.this, new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(addRecordMemo.this, "????????? ?????????????????????.", Toast.LENGTH_LONG).show();
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