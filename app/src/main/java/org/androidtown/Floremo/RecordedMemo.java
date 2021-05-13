package org.androidtown.Floremo;

import android.graphics.drawable.Drawable;
import android.net.Uri;
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
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recorded_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_delete:
                //
                return true;
            case R.id.menu_modify:
                updateMemo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDeleteContent(int position)
    {
//        mFirebaseDataBase.getReference().child("Content").child(uidList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(context, "삭제 성공", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("error: "+e.getMessage());
//                Toast.makeText(context, "삭제 실패", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //메모 업데이트하기
    public void updateMemo(){
        String text = editText.getText().toString();
        Log.d(TAG1,text);

        Memo updateMemo = new Memo();
        updateMemo.setTxt(text);
        DatabaseReference mReference = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/angry");

        //수정해야함!!
        mReference.child("-M_9Zjmfksth8pWNo2xh").child("txt").setValue(text);

    }
}