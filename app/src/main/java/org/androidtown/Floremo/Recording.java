package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
    int check_sum = 0, c1=0, c2=0, c3=0, c4=0, c5=0;

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
        SeekBar sb1  = (SeekBar) findViewById(R.id.seekBar1);
        final TextView tv2 = (TextView)findViewById(R.id.textView2);
        SeekBar sb2  = (SeekBar) findViewById(R.id.seekBar2);
        final TextView tv3 = (TextView)findViewById(R.id.textView3);
        SeekBar sb3  = (SeekBar) findViewById(R.id.seekBar3);
        final TextView tv4 = (TextView)findViewById(R.id.textView4);
        SeekBar sb4  = (SeekBar) findViewById(R.id.seekBar4);
        final TextView tv5 = (TextView)findViewById(R.id.textView5);
        SeekBar sb5  = (SeekBar) findViewById(R.id.seekBar5);

        sb1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb1.getProgress();
                if(a > 0)
                {
                    c1 = 1;
                }else{
                    c1 = 0;
                }
                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb1.setProgress(0);
                    check_sum--;

                }
//                TextView aa = findViewById(R.id.textView6);
//                aa.setText(String.valueOf(c1));
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img1.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(progress+120, 240, 127, 184) , PorterDuff.Mode.SRC_IN);

            }
        });


        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb2.getProgress();
                if(a > 0)
                {
                    c2 = 1;
                }else{
                    c2 = 0;
                }
                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb2.setProgress(0);
                    check_sum--;

                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img1.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(progress+120, 235, 197, 129) , PorterDuff.Mode.SRC_IN);
            }
        });

        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb3.getProgress();
                if(a > 0)
                {
                    c3 = 1;
                }else{
                    c3 = 0;
                }
                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb3.setProgress(0);
                    check_sum--;

                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img1.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(progress+120, 147, 224, 117) , PorterDuff.Mode.SRC_IN);
            }
        });

        sb4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb4.getProgress();
                if(a > 0)
                {
                    c2 = 1;
                }else{
                    c2 = 0;
                }
                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb4.setProgress(0);
                    check_sum--;

                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img1.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(progress+120, 117, 206, 250) , PorterDuff.Mode.SRC_IN);
            }
        });

        sb5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                int a = sb5.getProgress();
                if(a > 0)
                {
                    c2 = 1;
                }else{
                    c2 = 0;
                }
                check_sum = c1+c2+c3+c4+c5;
                if(check_sum > 2)
                {
                    startToast("2가지 이상의 감정을 입력할 수 없습니다!") ;
                    sb5.setProgress(0);
                    check_sum--;

                }
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                img1.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img2.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img3.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img4.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
                img5.setColorFilter(Color.argb(progress+120, 226, 159, 240) , PorterDuff.Mode.SRC_IN);
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
