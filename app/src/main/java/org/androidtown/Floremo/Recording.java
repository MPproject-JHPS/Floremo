package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class Recording extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    ImageButton prev;
    ImageButton next;
    ViewFlipper flipper;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;
    int check_sum = 0, c1=0, c2=0, c3=0, c4=0, c5=0; // 감정 개수 관리하는 변수
    SeekBar sb1;
    SeekBar sb2;
    SeekBar sb3;
    SeekBar sb4;
    SeekBar sb5;
    OutputStream outputStream = null;

    int[] p = {0,0,0,0,0}; //각각의 시크바 진행도를 받아오는 array 변수
    int max1 = 0; // 제일 큰 값
    int max1_idx = 0; // 어떤 시크바가 제일 큰 값을 갖는지
    int max2 = 0; // 두번째로 큰 값
    int max2_idx = 0; // 어떤 시크바가 두번째로 큰 값을 갖는지

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        flipper = (ViewFlipper) findViewById(R.id.flipper);
        prev = (ImageButton) findViewById(R.id.prev);
        next = (ImageButton) findViewById(R.id.next);

        prev.setOnClickListener(this);
        next.setOnClickListener(this);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        this.InitializeView();
        this.InitializeListener();

        final TextView tv1 = (TextView)findViewById(R.id.textView1);
        sb1  = (SeekBar) findViewById(R.id.seekBar1);
        final TextView tv2 = (TextView)findViewById(R.id.textView2);
        sb2  = (SeekBar) findViewById(R.id.seekBar2);
        final TextView tv3 = (TextView)findViewById(R.id.textView3);
        sb3  = (SeekBar) findViewById(R.id.seekBar3);
        final TextView tv4 = (TextView)findViewById(R.id.textView4);
        sb4  = (SeekBar) findViewById(R.id.seekBar4);
        final TextView tv5 = (TextView)findViewById(R.id.textView5);
        sb5  = (SeekBar) findViewById(R.id.seekBar5);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb1.getProgress();
                if(a > 0)
                {
                    c1 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c1 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb1.setProgress(0);
                    c1=0;

                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                img1.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
//                img2.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
//                img3.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
//                img4.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
//                img5.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);

            }
        });


        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb2.getProgress();
                if(a > 0)
                {
                    c2 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c2 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb2.setProgress(0);
                    c2=0;

                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                img1.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
//                img2.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
//                img3.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
//                img4.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
//                img5.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);

            }
        });

        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb3.getProgress();
                if(a > 0)
                {
                    c3 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c3 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb3.setProgress(0);
                    c3=0;
                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                img1.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
//                img2.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
//                img3.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
//                img4.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
//                img5.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);

            }
        });

        sb4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb4.getProgress();
                if(a > 0)
                {
                    c4 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c4 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb4.setProgress(0);
                    c4=0;
                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                img1.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
//                img2.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
//                img3.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
//                img4.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
//                img5.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);

            }
        });

        sb5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb5.getProgress();
                if(a > 0)
                {
                    c5 = 1;
                    if(a == 0)
                    {
                        check_sum=0;
                    }
                }else{
                    c5 = 0;
                }

                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb5.setProgress(0);
                    c5=0;
                }else if(check_sum == 0 )
                {
                    check_sum = 0;
                }
                p[0] = sb1.getProgress();
                p[1] = sb2.getProgress();
                p[2] = sb3.getProgress();
                p[3] = sb4.getProgress();
                p[4] = sb5.getProgress();
                findTwoMaxValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                img1.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
//                img2.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
//                img3.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
//                img4.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
//                img5.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.recording_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                if(max1 == 0 && max2 == 0) // 바로 기록을 누르면
                {
                    startToast("입력이 안되었습니다.");
                    break;
                }
                Intent NewActivity = new Intent(getApplicationContext(), addRecordMemo.class);
                NewActivity.putExtra("date", textView_Date.getText().toString());
                setResult(RESULT_OK, NewActivity);
                startActivityForResult(NewActivity,1);
                break;
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                break;
        }


        return true;
    }

    private void startToast(String msg)
    {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void findTwoMaxValue() { // 제일 큰 값과 두번째로 큰 값을 찾는다.
        int i;
        max1 = -1;
        max1_idx = 0;
        max2 = -1;
        max2_idx = 0;

        for (i = 0; i < 5; i++) {
            if (i == 0) {
                max1 = p[i];
                max1_idx = i;
            } else {
                if (p[i] > max1) {
                    max2 = max1;
                    max2_idx = max1_idx;
                    max1 = p[i];
                    max1_idx = i;
                } else {
                    if (p[i] > max2) {
                        max2 = p[i];
                        max2_idx = i;
                    }
                }
            }
        }

//        String txt = "Max 1 : Emotion" + (max1_idx + 1) + "번째" + max1 + "값" + "    " + "Max 2 : Emotion" + (max2_idx + 1) + "번째" + max2 + "값";
//        aa.setText(txt);
        drawMaxColor();
    }
    private void drawMaxColor() { // 제일 큰 값의 색상만 입힌다.

        if(max1_idx == 0)
        {
            img1.setColorFilter(Color.argb(max1+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
            img2.setColorFilter(Color.argb(max1+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
            img3.setColorFilter(Color.argb(max1+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
            img4.setColorFilter(Color.argb(max1+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
            img5.setColorFilter(Color.argb(max1+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
        }
        else if(max1_idx == 1)
        {
            img1.setColorFilter(Color.argb(max1+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
            img2.setColorFilter(Color.argb(max1+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
            img3.setColorFilter(Color.argb(max1+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
            img4.setColorFilter(Color.argb(max1+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
            img5.setColorFilter(Color.argb(max1+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
        }
        else if(max1_idx == 2)
        {
            img1.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
            img2.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
            img3.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
            img4.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
            img5.setColorFilter(Color.argb(max1+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
        }else if(max1_idx == 3)
        {
            img1.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
            img2.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
            img3.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
            img4.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
            img5.setColorFilter(Color.argb(max1+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
        }else if(max1_idx == 4)
        {
            img1.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
            img2.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
            img3.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
            img4.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
            img5.setColorFilter(Color.argb(max1+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
        }
        if(max1 == 0 && max2 == 0) // 모두 0이면 검정색으로
        {
            img1.setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_IN);
            img2.setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_IN);
            img3.setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_IN);
            img4.setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_IN);
            img5.setColorFilter(Color.BLACK , PorterDuff.Mode.SRC_IN);
        }
    }

    public void onClick(View v) {
        if(v == prev) {
            flipper.showPrevious();
        }
        else if(v == next) {
            flipper.showNext();
        }
    }

    public void InitializeView()
    {
        textView_Date = (TextView)findViewById(R.id.textView_date);
    }

    public void InitializeListener()
    {
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                textView_Date.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 4, 1);
        dialog.show();
    }
}
