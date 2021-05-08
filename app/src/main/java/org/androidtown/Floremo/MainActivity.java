package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DrawerLayout drawerLayout;
    private View drawerView;
    private long backBtnTime = 0; // 뒤로가기 버튼 누를 때 필요

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인

        //로딩화면 열기
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        TextView userName = findViewById(R.id.nickname);
        TextView userName2 = findViewById(R.id.name2);
        TextView userEmail = findViewById(R.id.mail);

        String nickName = intent.getStringExtra("name"); //구글 로그인으로부터 닉네임 전달받음
        userName.setText(nickName);
        //userName2.setText(nickName);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) //현재 로그인된 유저가 있는지 확인
        {
            startJoinActivity(); //로그인이 안되어 있으면 회원가입 화면으로 이동
        }else { //현재 로그인 되어 있다면
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // 사용자 이름 가져오기
                    String name = profile.getDisplayName();
                    String email = profile.getEmail();
                    userName.setText(name);
                    userName2.setText(name);
                    userEmail.setText(email);

                }
            }
        }


        if(mFirebaseUser == null){//null인 경우엔 보고있는 해당 창을 닫고 login activity를 열도록 한다.
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);
        ImageButton btn_open = (ImageButton) findViewById(R.id.navigation_menu);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
            }
        });

        //네비게이션 메뉴 닫기 버튼 삭제
//        Button btn_close = (Button) findViewById(R.id.btn_close);
//        btn_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                drawerLayout.closeDrawers();
//            }
//        });


        Button button = findViewById(R.id.btn_logout); //로그아웃 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startJoinActivity(); //로그아웃되면 회원가입 화면으로 이동
            }
        });

        drawerLayout.addDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        //감정꽃밭
        ImageButton img_btn1 = (ImageButton) findViewById(R.id.emotion_vase1);
        ImageButton img_btn2 = (ImageButton) findViewById(R.id.emotion_vase2);
        ImageButton img_btn3 = (ImageButton) findViewById(R.id.emotion_vase3);
        ImageButton img_btn4 = (ImageButton) findViewById(R.id.emotion_vase4);
        ImageButton img_btn5 = (ImageButton) findViewById(R.id.emotion_vase5);

        img_btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ShowRecord.class);
                intent.putExtra("selected_flower", 1);
                startActivity(intent);
            }
        });

        img_btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ShowRecord.class);
                intent.putExtra("selected_flower", 2);
                startActivity(intent);
            }
        });

        img_btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ShowRecord.class);
                intent.putExtra("selected_flower" ,3);
                startActivity(intent);
            }
        });

        img_btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ShowRecord.class);
                intent.putExtra("selected_flower", 4);
                startActivity(intent);
            }
        });

        img_btn5.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), ShowRecord.class);
                intent.putExtra("selected_flower", 5);
                startActivity(intent);
            }
        });


        //감정 추가하기
        ImageButton recordingButton = (ImageButton) findViewById(R.id.recordingButton);
        recordingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Recording.class);
                startActivity(intent);
            }
        });

        //환경설정
        ImageButton settingsButton = (ImageButton) findViewById(R.id.settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        //navigation drawer에 있는 버튼
        TextView btn_todayFlower = (TextView) findViewById(R.id.btn_todayFlower);
        btn_todayFlower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TodayFlowerActivity.class);
                startActivity(intent);
            }
        });

        TextView btn_analysis = (TextView) findViewById(R.id.btn_analysis);
        btn_analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AnalysisActivity.class);
                startActivity(intent);
            }
        });

    }



    //해당 동작을 할 때 상태값을 받아 커스터마이징 할 수 있음

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        long gapTime = curTime - backBtnTime;

        if(0 <= gapTime && 2000 >= gapTime) {
            super.onBackPressed();
        }
        else {
            backBtnTime = curTime;
            Toast.makeText(this, "한번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();
        }

    }

    private void startJoinActivity()
    {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

}
