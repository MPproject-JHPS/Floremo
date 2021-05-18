package org.androidtown.Floremo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    int happy_cnt, surp_cnt, ang_cnt, sad_cnt, soso_cnt;
    int happy, surp, ang, sad, soso, sum;
    int[] p = {0,0,0,0,0}; //각각의 감정 개수를 받아오는 array 변수
    int max = 0;

    ImageView imgView[] = new ImageView[10];
    SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        mFirebaseDataBase = FirebaseDatabase.getInstance();


        Spinner y_spinner = (Spinner) findViewById(R.id.year); // 년도 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Year);
        y_spinner.setAdapter(adapter);

        y_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String y = y_spinner.getSelectedItem().toString();
                year = y;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Spinner m_spinner = (Spinner) findViewById(R.id.month); // 월 스피너
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

        //이미지뷰 배열 초기화
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


        Button btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                happy = 0;
                surp = 0;
                ang = 0;
                sad = 0;
                soso = 0;
                sum = happy + surp + ang + sad + soso;

                mFirebaseAuth = FirebaseAuth.getInstance(); //유저를 얻어온다
                mFirebaseUser = mFirebaseAuth.getCurrentUser();//혹시 인증 유지가 안될 수 있으니 유저 확인
                database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동

                databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/happy/");
                //databaseReference.orderByChild("date").startAt(year+"년 "+month+"월 1일").endAt(year+"년 "+(month+1)+"월 1일").addValueEventListener(new ValueEventListener() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //파이어베이스 데이터베이스의 데이터를 받아오는 곳

                        Object value = dataSnapshot.getValue(Object.class);
                        Log.w("FireBaseData", "getData" + value.toString());

                        String origin = value.toString();
                        String word1 = year + "년";
                        String word2 = month + "월";
                        happy_cnt = 0;

                        String[] sarr = origin.split(" ");
                        int len = sarr.length;
                        for (int i = 0; i < len; i++) {
                            if (word1.equals(sarr[i])) {
                                if (word2.equals(sarr[i + 1])) {
                                    happy_cnt++;
                                }
                            }
                        }
                        happy = happy_cnt;
                        Log.w("FireBaseData", "happy count" + happy_cnt);
                        p[0] = happy_cnt;
                        p[1] = surp_cnt;
                        p[2] = ang_cnt;
                        p[3] = sad_cnt;
                        p[4] = soso_cnt;
                        findMaxValue();

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //DB를 가져오던 중 에러 발생시
                        Log.w("Analysis", String.valueOf(error.toException())); //에러문 출력
                    }
                });

                databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/surprised/");
                //databaseReference.orderByChild("date").startAt(year+"년 "+month+"월 1일").endAt(year+"년 "+(month+1)+"월 1일").addValueEventListener(new ValueEventListener() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //파이어베이스 데이터베이스의 데이터를 받아오는 곳

                        Object value = dataSnapshot.getValue(Object.class);
                        Log.w("FireBaseData", "getData" + value.toString());

                        String origin = value.toString();
                        String word1 = year + "년";
                        String word2 = month + "월";
                        surp_cnt = 0;

                        String[] sarr = origin.split(" ");
                        int len = sarr.length;
                        for (int i = 0; i < len; i++) {
                            if (word1.equals(sarr[i])) {
                                if (word2.equals(sarr[i + 1])) {
                                    surp_cnt++;
                                }
                            }
                        }
                        surp = surp_cnt;
                        Log.w("FireBaseData", "surp count" + surp_cnt);
                        p[0] = happy_cnt;
                        p[1] = surp_cnt;
                        p[2] = ang_cnt;
                        p[3] = sad_cnt;
                        p[4] = soso_cnt;
                        findMaxValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //DB를 가져오던 중 에러 발생시
                        Log.w("Analysis", String.valueOf(error.toException())); //에러문 출력
                    }
                });

                databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/angry/");
                //databaseReference.orderByChild("date").startAt(year+"년 "+month+"월 1일").endAt(year+"년 "+(month+1)+"월 1일").addValueEventListener(new ValueEventListener() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //파이어베이스 데이터베이스의 데이터를 받아오는 곳

                        Object value = dataSnapshot.getValue(Object.class);
                        Log.w("FireBaseData", "getData" + value.toString());

                        String origin = value.toString();
                        String word1 = year + "년";
                        String word2 = month + "월";
                        ang_cnt = 0;

                        String[] sarr = origin.split(" ");
                        int len = sarr.length;
                        for (int i = 0; i < len; i++) {
                            if (word1.equals(sarr[i])) {
                                if (word2.equals(sarr[i + 1])) {
                                    ang_cnt++;
                                }
                            }
                        } ang = ang_cnt;
                        Log.w("FireBaseData", "ang count" + ang_cnt);
                        p[0] = happy_cnt;
                        p[1] = surp_cnt;
                        p[2] = ang_cnt;
                        p[3] = sad_cnt;
                        p[4] = soso_cnt;
                        findMaxValue();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //DB를 가져오던 중 에러 발생시
                        Log.w("Analysis", String.valueOf(error.toException())); //에러문 출력
                    }
                });

                databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/sad/");
                //databaseReference.orderByChild("date").startAt(year+"년 "+month+"월 1일").endAt(year+"년 "+(month+1)+"월 1일").addValueEventListener(new ValueEventListener() {
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //파이어베이스 데이터베이스의 데이터를 받아오는 곳

                        Object value = dataSnapshot.getValue(Object.class);
                        Log.w("FireBaseData", "getData" + value.toString());

                        String origin = value.toString();
                        String word1 = year + "년";
                        String word2 = month + "월";
                        sad_cnt = 0;

                        String[] sarr = origin.split(" ");
                        int len = sarr.length;
                        for (int i = 0; i < len; i++) {
                            if (word1.equals(sarr[i])) {
                                if (word2.equals(sarr[i + 1])) {
                                    sad_cnt++;
                                }
                            }
                        }sad = sad_cnt;
                        Log.w("FireBaseData", "sad count" + sad_cnt);
                        p[0] = happy_cnt;
                        p[1] = surp_cnt;
                        p[2] = ang_cnt;
                        p[3] = sad_cnt;
                        p[4] = soso_cnt;
                        findMaxValue();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //DB를 가져오던 중 에러 발생시
                        Log.w("Analysis", String.valueOf(error.toException())); //에러문 출력
                    }
                });

        databaseReference = database.getReference(mFirebaseUser.getUid() + "/memos/soso/");
        //databaseReference.orderByChild("date").startAt(year+"년 "+month+"월 1일").endAt(year+"년 "+(month+1)+"월 1일").addValueEventListener(new ValueEventListener() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳

                Object value = dataSnapshot.getValue(Object.class);
                Log.w("FireBaseData", "getData" + value.toString());

                String origin = value.toString();
                String word1 = year + "년";
                String word2 = month + "월";
                soso_cnt = 0;

                String[] sarr = origin.split(" ");
                int len = sarr.length;
                for (int i = 0; i < len; i++) {
                    if (word1.equals(sarr[i])) {
                        if (word2.equals(sarr[i + 1])) {
                            soso_cnt++;
                        }
                    }
                } soso = soso_cnt;
                Log.w("FireBaseData", "soso count" + soso_cnt);
                p[0] = happy_cnt;
                p[1] = surp_cnt;
                p[2] = ang_cnt;
                p[3] = sad_cnt;
                p[4] = soso_cnt;
                findMaxValue();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //DB를 가져오던 중 에러 발생시
                Log.w("Analysis", String.valueOf(error.toException())); //에러문 출력
            }
        });

    }
        });

    }

    private void findMaxValue() {
         max = p[0];

        for (int i = 0; i < p.length; i++) {

            if (max < p[i]) {
                max = p[i];
            }
        }
        happy = p[0];
        surp = p[1];
        ang = p[2];
        sad = p[3];
        soso = p[4];
        sum = p[0] + p[1] + p[2] + p[3] + p[4];
        Log.w("FireBaseData", "max " + max);

        TextView comment = findViewById(R.id.comment);
        if(p[0] == max )
        {
            comment.setText("이번 달은 기쁜 일이 많았네요");
        }
        if(p[1] == max )
        {
            comment.setText("이번 달은 놀라운 일이 많았네요");
        }
        if(p[2] == max )
        {
            comment.setText("이번 달은 화난 일이 많았네요");
        }
        if(p[3] == max )
        {
            comment.setText("이번 달은 슬픈 일이 많았네요");
        }
        if(p[4] == max )
        {
            comment.setText("이번 달은 그저그런 일이 많았네요");
        }
        if(max == 0 )
        {
            comment.setText("");
        }

        // 프로그래스 바 옆에 기록 개수
        TextView n1 = findViewById(R.id.num1);
        n1.setText(String.valueOf(p[0]));
        TextView n2 = findViewById(R.id.num2);
        n2.setText(String.valueOf(p[1]));
        TextView n3 = findViewById(R.id.num3);
        n3.setText(String.valueOf(p[2]));
        TextView n4 = findViewById(R.id.num4);
        n4.setText(String.valueOf(p[3]));
        TextView n5 = findViewById(R.id.num5);
        n5.setText(String.valueOf(p[4]));

        double happy1 =  ((double)p[0]/(double)sum)*100;
        double surp1 =  ((double)p[1]/(double)sum)*100;
        double ang1 =  ((double)p[2]/(double)sum)*100;
        double sad1 =  ((double)p[3]/(double)sum)*100;
        double soso1 =  ((double)p[4]/(double)sum)*100;

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

        int i = 0;
        for (i = 0; i < p[0]; i++) {
            imgView[i].setColorFilter(Color.argb(120, 240, 127, 184), PorterDuff.Mode.SRC_IN);
        }

        for (i = 0; i < p[1]; i++) {
            imgView[p[0] + i].setColorFilter(Color.argb(120, 235, 197, 129), PorterDuff.Mode.SRC_IN);
        }
        for (i = 0; i < p[2]; i++) {
            imgView[p[0] + p[1] + i].setColorFilter(Color.argb(120, 147, 224, 117), PorterDuff.Mode.SRC_IN);
        }
        for (i = 0; i < p[3]; i++) {
            imgView[p[0] + p[1] + p[2] + i].setColorFilter(Color.argb(120, 117, 206, 250), PorterDuff.Mode.SRC_IN);
        }
        for (i = 0; i < p[4]; i++) {
            imgView[p[0] + p[1] + p[2] + p[3] + i].setColorFilter(Color.argb(120, 226, 159, 240), PorterDuff.Mode.SRC_IN);
        }
        if(p[0] == 0 && p[1] == 0 && p[2] == 0 && p[3] == 0 && p[4] == 0)
        {
            for (i = 0; i < 10; i++) {
                imgView[i].setColorFilter(null);
            }
        }

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