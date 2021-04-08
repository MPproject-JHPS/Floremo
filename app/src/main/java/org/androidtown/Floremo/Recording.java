package org.androidtown.Floremo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class Recording extends AppCompatActivity implements View.OnClickListener {

    private TextView textView_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    Button prev;
    Button next;
    ViewFlipper flipper;

    ImageView img1;
    ImageView img2;
    ImageView img3;
    ImageView img4;
    ImageView img5;

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