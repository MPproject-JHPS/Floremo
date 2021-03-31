package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class JoinActivity extends AppCompatActivity {
    private static final String TAG = "JoinActivity";
    private FirebaseAuth mAuth; //파이어베이스 인스턴스 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_member);

        // Initialize Firebase Auth
        //onCreate() 메서드에서 FirebaseAuth 인스턴스를 초기화합니다.
        mAuth = FirebaseAuth.getInstance();

        Button button = findViewById(R.id.joinButton); //회원가입 버튼 객체 생성 및 클릭 리스너
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("클릭", "클릭");
                joinMember();
            }
        });

        Button button2 = findViewById(R.id.gotoLoginButton); //로그인으로 이동 버튼 객체 생성 및 클릭 리스너
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity(); //로그인 페이지로 이동
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
    private void joinMember()
    {
        String email = ((EditText)findViewById(R.id.emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.passwordEditText)).getText().toString();
        String passwordCheck = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) { //editText에 입력되면
            if (password.equals(passwordCheck)) //비밀번호와 비밀번호 확인 모두 일치하면 회원가입 완료
            {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    startToast("회원가입 성공");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //성공 시 UI 로직
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
            } else {
                //비밀번호와 비밀번호 확인이 불일치 하다면 토스트 메세지 출력
                startToast("비밀번호가 일치하지 않습니다!");
            }
        } else {//editText에 입력 안됨
                startToast("이메일 또는 비밀번호를 입력해주세요.");
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1); //메인에서 로그아웃 하고 뒤로가기했는데 또 메인 뜨는 일 없게 강제종료
    }

    private void startToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void startLoginActivity()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}

