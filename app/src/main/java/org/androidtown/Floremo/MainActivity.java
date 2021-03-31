package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.logoutButton); //로그아웃 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startJoinActivity(); //로그아웃되면 회원가입 화면으로 이동
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null) //현재 로그인된 유저가 있는지 확인
        {
            startJoinActivity(); //로그인이 안되어 있으면 회원가입 화면으로 이동
        }

    }

    private void startJoinActivity()
    {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }
}