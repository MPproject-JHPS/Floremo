package org.androidtown.Floremo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class DrawerActivity extends AppCompatActivity {


    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        TextView userName = findViewById(R.id.name);
        Button button = findViewById(R.id.btn_logout); //로그아웃 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startJoinActivity(); //로그아웃되면 회원가입 화면으로 이동
            }
        });

        Intent intent = getIntent();
        String nickName = intent.getStringExtra("name"); //구글 로그인으로부터 닉네임 전달받음
        userName.setText(nickName);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) //현재 로그인된 유저가 있는지 확인
        {
            startJoinActivity(); //로그인이 안되어 있으면 회원가입 화면으로 이동
        } else { //현재 로그인 되어 있다면
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // 사용자 이름 가져오기
                    String name = profile.getDisplayName();
                    userName.setText(name);
                }
            }

        }
    }

    private void startJoinActivity()
    {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }
}