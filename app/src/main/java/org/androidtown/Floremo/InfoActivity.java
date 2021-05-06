package org.androidtown.Floremo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class InfoActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인

        TextView userName2 = findViewById(R.id.name2);
        TextView userEmail = findViewById(R.id.mail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                for (UserInfo profile : user.getProviderData()) {
                    // 사용자 이름 가져오기
                    String name = profile.getDisplayName();
                    String email = profile.getEmail();
                    userName2.setText(name);
                    userEmail.setText(email);

                }
            }
        Button change_pw = findViewById(R.id.change_pw);
            change_pw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCheckActivity(); // 계정 관리 버튼 누르면 상세 페이지로 이동
                }
            });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                break;
        }
        return true;
    }

    private void startCheckActivity()
    {
        Intent intent = new Intent(this, CheckActivity.class);
        startActivity(intent);
    }

    }
