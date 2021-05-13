package org.androidtown.Floremo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.androidtown.Floremo.RecordAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class RecordedMemo extends AppCompatActivity {

    private static final String TAG = "RecordedMemo";
    EditText editText;
    ImageView imageView;
    TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDataBase;
    String emotion;
    String key;
    private static final String TAG1 = "RecordedMemo";

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDataBase.getReference();

        setContentView(R.layout.add_record_memo);

        editText = findViewById(R.id.content);
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.textDate);
        String text = editText.getText().toString();
        Log.d(TAG1,text);
        if(getIntent().hasExtra("selected_memo")){
            Memo memo = getIntent().getParcelableExtra("selected_memo");
            editText.setText(memo.getTxt());
            Glide.with(this)
                    .load(memo.getImageUrl())
                    .into(imageView);
            textView.setText(memo.getDate());
            key = memo.getKey();
        }

        Intent passedIntent = getIntent();
        Bundle myBundle = passedIntent.getExtras();
        if(getIntent().hasExtra("emotion")){
            emotion = myBundle.getString("emotion");
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recorded_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: { //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
                case R.id.menu_delete: {
                    //여기에 alert를 넣으면 더 좋을듯
                    deleteMemo();
                    finish();
                    return true;
                }
                case R.id.menu_modify: {
                    updateMemo();
                    return true;
                }
            }
        return super.onOptionsItemSelected(item);
    }



//보류
    private void onDeleteContent(int position)
    {
//
//        mFirebaseDataBase.getReference().child("Content").child(uidList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "삭제 성공", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("error: "+e.getMessage());
//                Toast.makeText(getApplicationContext(), "삭제 실패", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void deleteMemo(){
        DatabaseReference mReference = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/"+emotion);
        mReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "메모가 삭제되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void updateMemo(){
        String text = editText.getText().toString();
        Log.d(TAG1,text);

        Memo updateMemo = new Memo();
        updateMemo.setTxt(text);
        DatabaseReference mReference = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/"+emotion);

        mReference.child(key).child("txt").setValue(text).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "메모가 수정되었습니다", Toast.LENGTH_SHORT).show();
            }
        });
    }

}