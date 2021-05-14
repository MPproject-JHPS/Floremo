package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AnalysisActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference databaseReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mFirebaseAuth;

    public String[] Year = {"2021", "2017", "2018","2019",
            "2020","2022","2023","2024",
            "2025"};
    public String[] Month = {"1","2","3","4",
            "5","6","7","8",
            "9","10","11","12"};
    String year;
    String month;

    ImageView imgView[] = new ImageView[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        TextView a1 = findViewById(R.id.textView9);
        TextView a2 = findViewById(R.id.textView10);
        mFirebaseDataBase = FirebaseDatabase.getInstance();

        int happy, surp, ang, sad, soso, sum;
        happy = 0;
        surp = 0;
        ang = 0;
        sad = 0;
        soso = 0;
        sum = happy + surp + ang + sad + soso;

        Spinner y_spinner = (Spinner)findViewById(R.id.year); // 년도 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Year);
        y_spinner.setAdapter(adapter);

        y_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String y = y_spinner.getSelectedItem().toString();
                year = y;
                a1.setText(year);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner m_spinner = (Spinner)findViewById(R.id.month); // 월 스피너
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Month);
        m_spinner.setAdapter(adapter2);

        m_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String m = m_spinner.getSelectedItem().toString();
                month = m;
                //a2.setText(month);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ImageView i1 = findViewById(R.id.aimg1);
        ImageView i2 = findViewById(R.id.aimg2);
        ImageView i3 = findViewById(R.id.aimg3);
        ImageView i4 = findViewById(R.id.aimg4);
        ImageView i5 = findViewById(R.id.aimg5);
        ImageView i6 = findViewById(R.id.aimg6);
        ImageView i7 = findViewById(R.id.aimg7);
        ImageView i8 = findViewById(R.id.aimg8);
        ImageView i9 = findViewById(R.id.aimg9);
        ImageView i10 = findViewById(R.id.aimg10);

        imgView[0] = i1;
        imgView[1] = i2;
        imgView[2] = i3;
        imgView[3] = i4;
        imgView[4] = i5;
        imgView[5] = i6;
        imgView[6] = i7;
        imgView[7] = i8;
        imgView[8] = i9;
        imgView[9] = i10;

        Integer[] arr = {happy, surp, ang, sad, soso};
        int max = arr[0];

        for (int i=0; i<arr.length; i++){

            if(max < arr[i]) {
                max = arr[i];
            }
        }

        Button btn_search = findViewById(R.id.btn_search);
        double finalHappy = happy;
        int finalHappy1 = happy;
        int finalSurp = surp;
        int finalAng = ang;
        int finalSad = sad;
        int finalSoso = soso;
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
                mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
                database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

                //databaseReference = database.getReference("U007RmZPPcUgNzeAVp7m5spEq543/memos/angry/");
                databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/angry");
                //databaseReference.orderByChild("date").startAt(year+"년 "+month+"월 1일").endAt(year+"년 "+(month+1)+"월 1일").addValueEventListener(new ValueEventListener() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //파이어베이스 데이터베이스의 데이터를 받아오는 곳

                        Object value = dataSnapshot.getValue(Object.class);
                        Log.w("FireBaseData", "getData" + value.toString());

                        String origin = value.toString();
                        String word1 = year +"년";
                        String word2 = month + "월";
                        int angry_count = 0;

                        String[] sarr = origin.split(" ");
                        int len = sarr.length;
                        for(int i=0; i<len; i++) {
                            if(word1.equals(sarr[i])) {
                                if(word2.equals(sarr[i+1])) {
                                    angry_count++;
                                }
                            }
                        } Log.w("FireBaseData", "count" + angry_count);

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //DB를 가져오던 중 에러 발생시
                        Log.w("Analysis", String.valueOf(error.toException())); //에러문 출력
                    }
                });

                int i = 0;
                for(i = 0; i< finalHappy1; i++)
                {
                    imgView[i].setColorFilter(Color.argb(120, 240, 127, 184), PorterDuff.Mode.SRC_IN);
                }

                for(i = 0; i < finalSurp; i++)
                {
                    imgView[finalHappy1 + i].setColorFilter(Color.argb(120, 235, 197, 129), PorterDuff.Mode.SRC_IN);
                }
                for(i = 0; i < finalAng; i++)
                {
                    imgView[finalHappy1 + finalSurp + i].setColorFilter(Color.argb(120, 147, 224, 117), PorterDuff.Mode.SRC_IN);
                }
                for(i = 0; i < finalSad; i++)
                {
                    imgView[finalHappy1 + finalSurp + finalAng + i].setColorFilter(Color.argb(120, 117, 206, 250), PorterDuff.Mode.SRC_IN);
                }
                for(i = 0; i < finalSoso; i++)
                {
                    imgView[finalHappy1 + finalSurp + finalAng + finalSad + i].setColorFilter(Color.argb(120, 226, 159, 240), PorterDuff.Mode.SRC_IN);
                }
            }

        });
        TextView comment = findViewById(R.id.comment);
        if(happy == max )
        {
            comment.setText("이번 달은 기쁜 일이 많았네요");
        }
        if(surp == max )
        {
            comment.setText("이번 달은 놀라운 일이 많았네요");
        }
        if(ang == max )
        {
            comment.setText("이번 달은 화난 일이 많았네요");
        }
        if(sad == max )
        {
            comment.setText("이번 달은 슬픈 일이 많았네요");
        }
        if(soso == max )
        {
            comment.setText("이번 달은 그저그런 일이 많았네요");
        }


        TextView n1 = findViewById(R.id.num1);
        n1.setText(String.valueOf(happy));
        TextView n2 = findViewById(R.id.num2);
        n2.setText(String.valueOf(surp));
        TextView n3 = findViewById(R.id.num3);
        n3.setText(String.valueOf(ang));
        TextView n4 = findViewById(R.id.num4);
        n4.setText(String.valueOf(sad));
        TextView n5 = findViewById(R.id.num5);
        n5.setText(String.valueOf(soso));

        double happy1 =  ((double)happy/(double)sum)*100;
        double surp1 =  ((double)surp/(double)sum)*100;
        double ang1 =  ((double)ang/(double)sum)*100;
        double sad1 =  ((double)sad/(double)sum)*100;
        double soso1 =  ((double)soso/(double)sum)*100;

        happy = Integer.parseInt(String.valueOf(Math.round(happy1)));
        surp = Integer.parseInt(String.valueOf(Math.round(surp1)));
        ang = Integer.parseInt(String.valueOf(Math.round(ang1)));
        sad = Integer.parseInt(String.valueOf(Math.round(sad1)));
        soso = Integer.parseInt(String.valueOf(Math.round(soso1)));

        ProgressBar p1 = findViewById(R.id.progBar1);
        p1.setProgress(happy);
        //n1.setText(String.valueOf(happy));

        ProgressBar p2 = findViewById(R.id.progBar2);
        p2.setProgress(surp);

        ProgressBar p3 = findViewById(R.id.progBar3);
        p3.setProgress(ang);

        ProgressBar p4 = findViewById(R.id.progBar4);
        p4.setProgress(sad);

        ProgressBar p5 = findViewById(R.id.progBar5);
        p5.setProgress(soso);

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
}

