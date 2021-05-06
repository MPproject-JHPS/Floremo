package org.androidtown.Floremo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class CheckActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth; //파이어베이스 인스턴스 선언
    private static final String TAG = "CheckActivity";
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        mAuth = FirebaseAuth.getInstance();

        Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
    }



    private void send()
    {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();

        if(email.length()>0)
        {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                startToast("이메일을 보냈습니다.");
                                Log.d(TAG, "Email sent");
                                startInfoActivity(); //로그인 성공 시 메인 화면으로 이동
                            }
                        }
                    });

        }else {
            startToast("이메일을 입력해 주세요.");
        }

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

    private void startInfoActivity()
    {
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    private void startToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


}
