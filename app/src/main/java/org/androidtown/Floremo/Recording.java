package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Recording extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    Button prev;
    Button next;
    ViewFlipper flipper;


    TextView testText;
    private int rgb_r;
    private int rgb_g;
    private int rgb_b;
    private int sum = 155;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recording);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        flipper = (ViewFlipper) findViewById(R.id.flipper);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);


        this.InitializeView();
        this.InitializeListener();



        testText = findViewById(R.id.testText);


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
            }
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgb_r = progress + sum;
                rgb_g = sb2.getProgress();
                if(rgb_g > 0) {
                    rgb_g += sum;
                }

                rgb_b = sb3.getProgress();
                if(rgb_b > 0) {
                    rgb_b += sum;
                }

                testText.setTextColor(Color.rgb(rgb_r, rgb_g, rgb_b));
            }
        });



        sb2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                rgb_r = sb1.getProgress();
                if(rgb_r > 0) {
                    rgb_r += sum;
                }

                rgb_g = progress + sum;

                rgb_b = sb3.getProgress();
                if(rgb_b > 0) {
                    rgb_b += sum;
                }
                testText.setTextColor(Color.rgb(rgb_r, rgb_g, rgb_b));
            }
        });


        sb3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rgb_r = sb1.getProgress();
                if(rgb_r > 0) {
                    rgb_r += sum;
                }
                rgb_g = sb2.getProgress();
                if(rgb_g > 0) {
                    rgb_g += sum;
                }
                rgb_b = progress + sum;

                testText.setTextColor(Color.rgb(rgb_r, rgb_g, rgb_b));
            }
        });



        sb4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv4.setText("onStop TrackingTouch");
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv4.setText("onStart TrackingTouch");
            }
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                tv4.setText("onProgressChanged : " + progress);
            }
        });



        sb5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                tv5.setText("onStop TrackingTouch");
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                tv5.setText("onStart TrackingTouch");
            }
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                tv5.setText("onProgressChanged : " + progress);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu:
                Intent NewActivity = new Intent(getApplicationContext(), addRecordMemo.class);
                startActivity(NewActivity);
                break;
        }
        return true;
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
                textView_Date.setText(year + "년" + (monthOfYear+1) + "월" + dayOfMonth + "일");
            }
        };
    }

    public void OnClickHandler(View view)
    {
        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, 2021, 4, 1);
        dialog.show();
    }
}