package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth; //파이어베이스 인스턴스 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        //onCreate() 메서드에서 FirebaseAuth 인스턴스를 초기화합니다.
        mAuth = FirebaseAuth.getInstance();

        Button button2 = findViewById(R.id.loginButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("클릭", "클릭");
                login();
            }
        });
    }

    @Override
    //활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인합니다.
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    //createUserWithEmailAndPassword 메서드를 사용하여 이메일 주소와 비밀번호를 가져와 유효성을 검사한 후 신규 사용자를 만드는 새 createAccount 메서드를 만듭니다.
    private void login()
    {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 ) { //editText에 입력되면
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인 성공");
                                startMainActivity(); //로그인 성공 시 메인 화면으로 이동
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                if (task.getException() != null) //로그인 중 에러 발생 시 메세지 출력
                                {
                                    startToast(task.getException().toString());
                                }

                                //실패 시 UI 로직
                            }
                        }
                    });

        } else {//editText에 입력 안됨
            startToast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startMainActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 로그인 후 메인으로 가면 뒤로가기 시 다시 로그인정보가 들어 있는 화면이 아닌 앱꺼지기
        startActivity(intent);
    }
}

