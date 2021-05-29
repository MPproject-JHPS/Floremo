package org.androidtown.Floremo.TodayFlower;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.androidtown.Floremo.Memo;
import org.androidtown.Floremo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

public class ShowPolorideActivity extends AppCompatActivity {
    long total;
    private FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference databaseReference;

    private EditText editText;
    private TextView date;
    private ImageView image;
    private ImageView flower;

    long[] p = {0,0,0,0,0}; //각각의 감정 개수를 받아오는 array 변수
    long happy_cnt, surprised_cnt, angry_cnt, sad_cnt, soso_cnt;
    int random_cnt;

    LinearLayout capture_target_Layout;
    String current_time;
    File file;
    String filename;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_poloride);
        checkSelfPermission();

        //database.getInstance();
        editText = findViewById(R.id.content);
        date = findViewById(R.id.textDate);
        image = findViewById(R.id.photo_image);
        flower = findViewById(R.id.tf_image);

        //툴바 기능
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        //폴로라이드 사진 캡쳐

        capture_target_Layout = (LinearLayout)findViewById(R.id.layout_poloride); //캡쳐할 영역의 레이아웃


        SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMddHHmmss"); //년,월,일,시간 포멧 설정
        Date time = new Date(); //파일명 중복 방지를 위해 사용될 현재시간
        current_time = sdf.format(time); //String형 변수에 저장

        //랜덤 기능
        countTotalFlower();
        Log.w("태그", String.valueOf(total));
        System.out.println(total);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_poloride, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
            case R.id.menu_captureNshare: {
                file = ScreenShotActivity(capture_target_Layout, current_time);

                if(file !=null){
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                }

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); //share 팝업나오게 하기
                sharingIntent.setType("image/*");
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/DCIM/Floremo/" + filename);
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, uri); //이미지를 uri로 전송
                startActivity(Intent.createChooser(sharingIntent, "Share 팝업"));
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public File ScreenShotActivity(View view, String date){
        view.setDrawingCacheEnabled(true);

        Bitmap screenBitmap = view.getDrawingCache();

        filename = "screenshot_"+date+".png";
        File file = new File(Environment.getExternalStorageDirectory()+"/DCIM/Floremo/", filename);
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 100, os); //PNG파일로 만들기
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }

    //권한에 대한 응답이 있을때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //권한을 허용 했을 경우
        if(requestCode == 1){
            int length = permissions.length;
            for (int i = 0; i < length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    // 동의
                    Log.d("showPolorideActivity","권한 허용 : " + permissions[i]);
                }
            }
        }

    }

    public void checkSelfPermission() {

        String temp = "";

        //파일 읽기 권한 확인
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        //파일 쓰기 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            // 권한 요청
            ActivityCompat.requestPermissions(this, temp.trim().split(" "),1);
        }
        else {
            // 모두 허용 상태
            Toast.makeText(this, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }
    }

    //기록한 꽃의 개수 세기
    public void countTotalFlower()
    {
        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/happy/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                happy_cnt = dataSnapshot.getChildrenCount();
                Log.w("Poloride", "happy count" + happy_cnt);
                p[0] = happy_cnt;
                //isInValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Poloride", String.valueOf(error.toException())); //에러문 출력
            }

        });

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/surprised/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                surprised_cnt = dataSnapshot.getChildrenCount();
                Log.w("Poloride", "surprised count" + surprised_cnt);
                p[1] = surprised_cnt;
                //isInValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Poloride", String.valueOf(error.toException())); //에러문 출력
            }
        });

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/angry/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                angry_cnt =dataSnapshot.getChildrenCount();
                Log.w("Poloride", "angry count" + angry_cnt);
                p[2] = angry_cnt;
                //isInValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Poloride", String.valueOf(error.toException())); //에러문 출력
            }
        });

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/sad/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
               sad_cnt = dataSnapshot.getChildrenCount();
                Log.w("Poloride", "sad count" + sad_cnt);
                p[3] = sad_cnt;
                //isInValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Poloride", String.valueOf(error.toException())); //에러문 출력
            }
        });

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/soso/");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                soso_cnt = dataSnapshot.getChildrenCount();
                Log.w("Poloride", "soso count" + soso_cnt);
                p[4] = soso_cnt;
                isInValue();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Poloride", String.valueOf(error.toException())); //에러문 출력
            }
        });

    }

    //0개 이상인 감정을 찾아낸다.
    public void isInValue(){
        for (int i = 0; i < p.length; i++) {
            if(p[i] > 0){
                random_cnt++;
                Log.w("Poloride", "random count" + random_cnt);
            }
        }
        Log.w("Poloride", "random count" + random_cnt);
        Random ran = new Random();
        int randomValue = ran.nextInt(random_cnt); //0부터 random_cnt까지의 숫자 랜덤으로 뽑기
        Log.w("Poloride", "random value" + randomValue);

        String emotion = null;
        if(randomValue == 0){ emotion ="happy"; }
        else if(randomValue == 1){ emotion = "surprised"; }
        else if(randomValue == 2){ emotion = "angry"; }
        else if(randomValue == 3){ emotion = "sad"; }
        else if(randomValue == 4){ emotion = "soso"; }

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/" + emotion);

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                // 선택된 감정 중에서 랜덤으로 인덱스 찾기
                int randomIndex = ran.nextInt((int) p[randomValue]);
                Log.w("Poloride", "random index" + randomIndex);

                Iterator itr = dataSnapshot.getChildren().iterator();
                for(int i =0; i < randomIndex; i++){
                    itr.next();
                }
                DataSnapshot childSnapshot = (DataSnapshot) itr.next();
                Memo memo = childSnapshot.getValue(Memo.class);

                date.setText(memo.getDate());
                editText.setText(memo.getTxt());
                Glide.with(ShowPolorideActivity.this)
                        .load(memo.getImageUrl())
                        .into(image);
                Glide.with(ShowPolorideActivity.this)
                        .load(memo.getFlowerImg())
                        .into(flower);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Poloride", String.valueOf(error.toException())); //에러문 출력
            }
        });
    }
}