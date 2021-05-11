package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class AnalysisActivity extends AppCompatActivity {
    public String[] Year = {"2017","2018","2019","2020",
            "2021","2022","2023","2024",
            "2025"};
    public String[] Month = {"1","2","3","4",
                            "5","6","7","8",
                            "9","10","11","12"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        Spinner y_spinner = (Spinner)findViewById(R.id.year); // 년도 스피너
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Year);
        y_spinner.setAdapter(adapter);

        y_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner m_spinner = (Spinner)findViewById(R.id.month); // 년도 스피너
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Month);
        m_spinner.setAdapter(adapter2);

        m_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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