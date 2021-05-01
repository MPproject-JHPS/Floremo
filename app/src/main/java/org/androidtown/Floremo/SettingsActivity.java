package org.androidtown.Floremo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SettingsActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다

        Button button = findViewById(R.id.withdraw); //회원탈퇴 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
                dialog  .setTitle("회원 탈퇴")
                        .setMessage("탈퇴 하시겠습니까?")
                        .setPositiveButton("탈퇴", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                revokeAccess();
                                startJoinActivity(); //회원탈퇴 되면 회원가입 화면으로 이동
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(SettingsActivity.this, "취소했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();


            }
        });
        Switch alarm = findViewById(R.id.alarm);
        SharedPreferences sharedPreferences = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        alarm.setChecked(sharedPreferences.getBoolean("Key", true)); // 처음 실행 시 true

        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true)
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // 알림 허용 상태
                    editor.putBoolean("Key", true);
                    editor.apply();
                    alarm.setChecked(true);

                } else{
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // 알림 해제 상태
                    editor.putBoolean("Key", false);
                    editor.apply();
                    alarm.setChecked(false);

                }
            }
        });

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void revokeAccess()
    {
        mFirebaseAuth.getCurrentUser().delete();
    }


    private void startJoinActivity() //회원가입 화면으로
    {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    //개인정보처리방침
    public void onClick(View view) {
    }
}