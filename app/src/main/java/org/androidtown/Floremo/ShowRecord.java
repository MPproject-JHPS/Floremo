package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ShowRecord extends AppCompatActivity implements RecordAdapter.OnMemoListener {

    private ArrayList<Memo> arrayList;
    private RecordAdapter mRecordAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;
    private int flowerNumber;
    private String emotionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_record);

        recyclerView = findViewById(R.id.show_record); //아이디 연결
        recyclerView.setHasFixedSize(true); //리사이클러뷰 성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
        mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

        // mainActivity에서 선택된 꽃밭 번호을 넘겨 받는다.
        Intent passedIntent = getIntent();
        if (passedIntent != null) {
            flowerNumber = passedIntent.getIntExtra("selected_flower", 1);
        }

        if (flowerNumber == 1) {
            emotionName = "happy";
        } else if (flowerNumber == 2) {
            emotionName = "surprised";
        } else if (flowerNumber == 3) {
            emotionName = "angry";
        } else if (flowerNumber == 4) {
            emotionName = "sad";
        } else if (flowerNumber == 5) {
            emotionName = "soso";
        }

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/" + emotionName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); //기존 배열리스트가 존재하지 않게 초기화

                //반복문으로 데이터리스트 출력
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Memo memo = snapshot.getValue(Memo.class); //만들어뒀던 Item 객체에 데이터를 담는다
                    arrayList.add(memo); //담을 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비

                }
                mRecordAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.e("ShowRecord", String.valueOf(error.toException())); //에러문 출력
            }
        });

        // column이 4인 gridView로 만들기
        int numberOfColumns = 4;
        mRecordAdapter = new RecordAdapter(arrayList, this, this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setAdapter(mRecordAdapter); //리사이클러뷰에 adapter 연결

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbar.setNavigationIcon(R.drawable.back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
    }

    //툴바 뒤로가기
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

    @Override
    public void onFlowerClick(int position) {
        arrayList.get(position);
        Intent intent = new Intent(this, RecordedMemo.class);
        intent.putExtra("selected_memo", arrayList.get(position));
        intent.putExtra("emotion", emotionName);
        startActivity(intent);
    }
}