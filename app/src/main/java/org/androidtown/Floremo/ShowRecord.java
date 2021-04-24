package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ShowRecord extends AppCompatActivity   {

    TextView txt_itemAdd;
    ArrayList<Item> item = new ArrayList<>();
    private RecordAdapter mRecordAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_record);

        // test 버전
        // 하단에 '+' 텍스트뷰를 누르면 아이템 추가.
        recyclerView = findViewById(R.id.show_record);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // column이 4인 gridView로 만들기
        int numberOfColumns = 4;
        mRecordAdapter = new RecordAdapter(item);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setAdapter(mRecordAdapter);

        //"+"텍스트뷰를 눌렀을 때 동작하게 하기
        txt_itemAdd = findViewById(R.id.txt_itemAdd);
        txt_itemAdd.setClickable(true);
        txt_itemAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecordAdapter.addItem(new Item(1));
                mRecordAdapter.notifyDataSetChanged();
            }
        });


    }


    
}