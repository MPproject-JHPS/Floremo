package org.androidtown.Floremo.TodayFlower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import org.androidtown.Floremo.R;

import java.util.Random;

public class TodayFlowerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_flower);

        ImageButton bouquet = (ImageButton) findViewById(R.id.bouquet);

        bouquet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodayFlowerActivity.this, ShowPolorideActivity.class);
                startActivity(intent);
            }
        });

        int max_num_value = 5;
        int min_num_value = 1;

        Random random = new Random();

        int randomNum = random.nextInt(max_num_value - min_num_value + 1) + min_num_value;
        ImageView imageView = (ImageView)findViewById(R.id.bouquet);
        switch(randomNum) {

            case 1:
                imageView.setImageResource(R.drawable.bouquet_1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.bouquet_2);
                break;
            case 3:
                imageView.setImageResource(R.drawable.bouquet_3);
                break;
            case 4:
                imageView.setImageResource(R.drawable.bouquet_4);
                break;
            case 5:
                imageView.setImageResource(R.drawable.bouquet_5);
                break;
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김


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