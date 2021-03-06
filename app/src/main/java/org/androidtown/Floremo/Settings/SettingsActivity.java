package org.androidtown.Floremo.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.androidtown.Floremo.JoinActivity;
import org.androidtown.Floremo.R;


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
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.bg_d));
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    button.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.transperent));
                }
                return false;
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

        Button account_management = findViewById(R.id.account_management);
        account_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInfoActivity(); // 계정 관리 버튼 누르면 상세 페이지로 이동
            }
        });
        account_management.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    account_management.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.bg_d));
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    account_management.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.transperent));
                }
                return false;
            }
        });
        Button version_info = findViewById(R.id.version_info);
        version_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVInfoActivity(); // 버전 정보 버튼 누르면 상세 페이지로 이동
            }
        });
        version_info.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    version_info.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.bg_d));
                } else if(event.getAction() == MotionEvent.ACTION_UP) {
                    version_info.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.transperent));
                }
                return false;
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

    private void revokeAccess()
    {
        mFirebaseAuth.getCurrentUser().delete();
    }


    private void startJoinActivity() //회원가입 화면으로
    {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

    private void startInfoActivity()
    {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }
    private void startVInfoActivity()
    {
        Intent intent = new Intent(this, VInfoActivity.class);
        startActivity(intent);
    }

    //개인정보처리방침
    public void onClick(View view) {
    }
}