package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Recording extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    ImageButton prev;
    ImageButton next;
    ViewFlipper flipper;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;

    Uri uri_simage;

    int check_sum = 0, c1=0, c2=0, c3=0, c4=0, c5=0; // 감정 개수 관리하는 변수
    SeekBar sb1;
    SeekBar sb2;
    SeekBar sb3;
    SeekBar sb4;
    SeekBar sb5;
    OutputStream outputStream = null; //다른 부분

    int[] p = {0,0,0,0,0}; //각각의 시크바 진행도를 받아오는 array 변수
    int max1 = 0; // 제일 큰 값
    int max1_idx = 0; // 어떤 시크바가 제일 큰 값을 갖는지
    int max2 = 0; // 두번째로 큰 값
    int max2_idx = 0; // 어떤 시크바가 두번째로 큰 값을 갖는지

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDataBase;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);


        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        databaseReference = mFirebaseDataBase.getReference();


        //색 변경된 꽃 이미지 잘 저장되었는지 불러와서 ImageView에 띄우는 테스트 버튼 핸들러
//        testBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
//                StorageReference imageRef = storageRef.child("images");
//                StorageReference userRef = imageRef.child(mFirebaseUser.getUid());
//
//                mFirebaseDataBase.getReference("flowerImages/").addValueEventListener(new ValueEventListener() {
//
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                            if (dataSnapshot.getKey().equals(filename)) {
//                                String image = dataSnapshot.getValue().toString();
//
//                                byte[] b = binaryStringToByteArray(image);
//                                ByteArrayInputStream is = new ByteArrayInputStream(b);
//                                Drawable reviewImage = Drawable.createFromStream(is, "testImageView");
//                                testImageView.setImageDrawable(reviewImage);
//                                startToast("테스트 버튼 작동");
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        this.InitializeView();
        this.InitializeListener();

        final TextView tv1 = (TextView)findViewById(R.id.textView1);
        sb1  = (SeekBar) findViewById(R.id.seekBar1);
        final TextView tv2 = (TextView)findViewById(R.id.textView2);
        sb2  = (SeekBar) findViewById(R.id.seekBar2);
        final TextView tv3 = (TextView)findViewById(R.id.textView3);
        sb3  = (SeekBar) findViewById(R.id.seekBar3);
        final TextView tv4 = (TextView)findViewById(R.id.textView4);
        sb4  = (SeekBar) findViewById(R.id.seekBar4);
        final TextView tv5 = (TextView)findViewById(R.id.textView5);
        sb5  = (SeekBar) findViewById(R.id.seekBar5);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb1.getProgress();
                if(a > 0)
                {
                    c1 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c1 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb1.setProgress(0);
                    c1=0;

                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });


        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb2.getProgress();
                if(a > 0)
                {
                    c2 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c2 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb2.setProgress(0);
                    c2=0;

                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb3.getProgress();
                if(a > 0)
                {
                    c3 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c3 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb3.setProgress(0);
                    c3=0;
                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

        sb4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb4.getProgress();
                if(a > 0)
                {
                    c4 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c4 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb4.setProgress(0);
                    c4=0;
                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

        sb5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb5.getProgress();
                if(a > 0)
                {
                    c5 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c5 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb5.setProgress(0);
                    c5=0;
                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recording_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                if (max1 == 0 && max2 == 0) // 바로 기록을 누르면
                {
                    startToast("입력이 안되었습니다.");
                    break;
                }
                if (flipper.getDisplayedChild() == 0) {
                    UploadFlower1();
                } else if (flipper.getDisplayedChild() == 1) {
                    UploadFlower2();
                } else if (flipper.getDisplayedChild() == 2) {
                    UploadFlower3();
                } else if (flipper.getDisplayedChild() == 3) {
                    UploadFlower4();
                } else if (flipper.getDisplayedChild() == 4) {
                    UploadFlower5();
                }

                Intent NewActivity = new Intent(getApplicationContext(), org.androidtown.Floremo.addRecordMemo.class);
                Bundle myBundle = new Bundle();
                NewActivity.putExtra("uri_simage", uri_simage); //addRecordMemo 화면으로 꽃이미지 Uri 넘겨줌
                myBundle.putString("date", textView_Date.getText().toString());
                myBundle.putInt("emotion", max1_idx); //0=happy, 1=sad, 2=angry, 3=surprised, 4=normal
                NewActivity.putExtras(myBundle);

                setResult(RESULT_OK, NewActivity);
                startActivityForResult(NewActivity, 1);

                break;
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                break;
        }
        return true;
    }

    private void startToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void findTwoMaxValue() { // 제일 큰 값과 두번째로 큰 값을 찾는다.
        int i;
        max1 = -1;
        max1_idx = 0;
        max2 = -1;
        max2_idx = 0;

        for (i = 0; i < 5; i++) {
            if (i == 0) {
                max1 = p[i];
                max1_idx = i;
            } else {
                if (p[i] > max1) {
                    max2 = max1;
                    max2_idx = max1_idx;
                    max1 = p[i];
                    max1_idx = i;
                } else {
                    if (p[i] > max2) {
                        max2 = p[i];
                        max2_idx = i;
                    }
                }
            }
        }

//        String txt = "Max 1 : Emotion" + (max1_idx + 1) + "번째" + max1 + "값" + "    " + "Max 2 : Emotion" + (max2_idx + 1) + "번째" + max2 + "값";
//        aa.setText(txt);
        drawMaxColor();
    }
    private void drawMaxColor() { // 제일 큰 값의 색상만 입힌다.

        if(max1_idx == 0) //가장 큰 감정이 Happy일 때
        {
            if(max2 > 0 && max2_idx == 1) { //두번째로 큰 감정이 Surprised이고 그 값이 0보다 클 때
                // max1_index: 핑크 & max2_index: 노랑
                img1.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 2) { //두번째로 큰 감정이 Angry이고 그 값이 0보다 클 때
                // max1_index: 핑크 & max2_index: 연두색
                img1.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 3){ //두번째로 큰 감정이 Sad이고 그 값이 0보다 클 때
                //max1_index: 핑크 & max2_index: 하늘색
                img1.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);

            }
            else if(max2 > 0 && max2_idx == 4){ //두번째로 큰 감정이 Soso이고 그 값이 0보다 클 때
                // max1_index: 핑크 & max2_index: 보라색
                img1.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);

            }
            else { //Happy 시크바만 선택되었을 때
                img1.setColorFilter(Color.argb(max1 + 120, 240, 127, 184), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max1 + 120, 240, 127, 184), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max1 + 120, 240, 127, 184), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max1 + 120, 240, 127, 184), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max1 + 120, 240, 127, 184), PorterDuff.Mode.SRC_IN);

            }
        }
        else if(max1_idx == 1) //가장 큰 감정이 Surprised일 때
        {
            if(max2 > 0 && max2_idx == 0) { //두번째로 큰 감정이 Happy이고 그 값이 0보다 클 때
                // max1_index: 노랑 & max2_index: 핑크
                img1.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 230, 140, 130), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 2) { //두번째로 큰 감정이 Angry이고 그 값이 0보다 클 때
                // max1_index: 노랑 & max2_index: 연두
                img1.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 3){ //두번째로 큰 감정이 Sad이고 그 값이 0보다 클 때
                //max1_index: 노랑 & max2_index: 하늘
                img1.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);

            }
            else if(max2 > 0 && max2_idx == 4){ //두번째로 큰 감정이 Soso이고 그 값이 0보다 클 때
                // max1_index: 노랑 & max2_index: 보라
                img1.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);

            }
            else { //Surprised 시크바만 선택되었을 때
                img1.setColorFilter(Color.argb(max1 + 120, 235, 197, 129), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max1 + 120, 235, 197, 129), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max1 + 120, 235, 197, 129), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max1 + 120, 235, 197, 129), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max1 + 120, 235, 197, 129), PorterDuff.Mode.SRC_IN);

            }

        }
        else if(max1_idx == 2)
        {
            if(max2 > 0 && max2_idx == 0) { //두번째로 큰 감정이 Happy이고 그 값이 0보다 클 때
                // max1_index: 연두 & max2_index: 핑크

                img1.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 200, 189, 146), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 1) { //두번째로 큰 감정이 Surprised이고 그 값이 0보다 클 때
                // max1_index: 연두 & max2_index: 노랑
                img1.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 200, 230, 100), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 3){ //두번째로 큰 감정이 Sad이고 그 값이 0보다 클 때
                // max1_index: 연두 & max2_index: 하늘
                img1.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);

            }
            else if(max2 > 0 && max2_idx == 4){ //두번째로 큰 감정이 Soso이고 그 값이 0보다 클 때
                // max1_index: 연두 & max2_index: 보라
                img1.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);

            }
            else { //Angry 시크바만 선택되었을 때
                img1.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);

            }

        }else if(max1_idx == 3)
        {
            if(max2 > 0 && max2_idx == 0) { //두번째로 큰 감정이 Happy이고 그 값이 0보다 클 때
                // max1_index: 하늘 & max2_index: 핑크
                img1.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 168, 147, 214), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 1) { //두번째로 큰 감정이 Surprised이고 그 값이 0보다 클 때
                // max1_index: 하늘 & max2_index: 노랑
                img1.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 155, 200, 168), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 2){ //두번째로 큰 감정이 Angry이고 그 값이 0보다 클 때
                // max1_index: 하늘 & max2_index: 연두
                img1.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 40, 200, 168), PorterDuff.Mode.SRC_IN);

            }
            else if(max2 > 0 && max2_idx == 4){ //두번째로 큰 감정이 Soso이고 그 값이 0보다 클 때
                // max1_index: 하늘 & max2_index: 보라
                img1.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
            }
            else { //Sad 시크바만 선택되었을 때
                img1.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);

            }

        }else if(max1_idx == 4)
        {
            if(max2 > 0 && max2_idx == 0) { //두번째로 큰 감정이 Happy이고 그 값이 0보다 클 때
                // max1_index: 보라 & max2_index: 핑크
                img1.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 232, 104, 214), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 1) { //두번째로 큰 감정이 Surprised이고 그 값이 0보다 클 때
                // max1_index: 보라 & max2_index: 노랑
                img1.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 227, 153, 177), PorterDuff.Mode.SRC_IN);
            }
            else if(max2 > 0 && max2_idx == 2){ //두번째로 큰 감정이 Angry이고 그 값이 0보다 클 때
                // max1_index: 보라 & max2_index: 연두
                img1.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 170, 187, 177), PorterDuff.Mode.SRC_IN);

            }
            else if(max2 > 0 && max2_idx == 3){ //두번째로 큰 감정이 Sad이고 그 값이 0보다 클 때
                // max1_index: 보라 & max2_index: 하늘
                img1.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max2 + 120, 90, 130, 250), PorterDuff.Mode.SRC_IN);
            }
            else { //Soso 시크바만 선택되었을 때
                img1.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
            }

        }
        if(max1 == 0 && max2 == 0) // 모두 0이면 초기화 (아무 시크바도 선택되지 않았을 때)
        {
            img1.setColorFilter(null);
            img2.setColorFilter(null);
            img3.setColorFilter(null);
            img4.setColorFilter(null);
            img5.setColorFilter(null);
        }
    }

    //ViewFlipper 클릭 이벤트 핸들러
    public void onClick(View v) {
        if(v == prev) {
            flipper.showPrevious();
        }
        else if(v == next) {
            flipper.showNext();
        }
    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.textView_date);
    }

    //Calendar
    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 4, 1);
        dialog.show();
    }

    //첫번째 디자인의 꽃 이미지에 대한 비트맵 생성 후 Uri 저장 (추후 addRecordMemo 화면으로 Uri 전달)
    public void UploadFlower1() {

        img1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img1.getDrawingCache());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "simage", null);
        uri_simage = Uri.parse(path);

        startToast("꽃 이미지 저장");
    }

    //두번째 디자인의 꽃 이미지에 대한 비트맵 생성 후 Uri 저장 (추후 addRecordMemo 화면으로 Uri 전달)
    public void UploadFlower2() {

        img2.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img2.getDrawingCache());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "simage", null);
        uri_simage = Uri.parse(path);

        startToast("꽃 이미지 저장");
    }

    //세번째 디자인의 꽃 이미지에 대한 비트맵 생성 후 Uri 저장 (추후 addRecordMemo 화면으로 Uri 전달)
    public void UploadFlower3() {

        img3.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img3.getDrawingCache());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "simage", null);
        uri_simage = Uri.parse(path);

        startToast("꽃 이미지 저장");
    }

    //네번째 디자인의 꽃 이미지에 대한 비트맵 생성 후 Uri 저장 (추후 addRecordMemo 화면으로 Uri 전달)
    public void UploadFlower4() {

        img4.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img4.getDrawingCache());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "simage", null);
        uri_simage = Uri.parse(path);

        startToast("꽃 이미지 저장");
    }

    //다섯번째 디자인의 꽃 이미지에 대한 비트맵 생성 후 Uri 저장 (추후 addRecordMemo 화면으로 Uri 전달)
    public void UploadFlower5() {

        img5.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(img5.getDrawingCache());

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "simage", null);
        uri_simage = Uri.parse(path);

        startToast("꽃 이미지 저장");
    }
}