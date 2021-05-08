package org.androidtown.Floremo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RecordedMemo extends AppCompatActivity {

    private static final String TAG = "RecordedMemo";
    EditText editText;
    ImageView imageView;
    TextView textView;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_memo);

        editText = findViewById(R.id.content);
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.textDate);

        if(getIntent().hasExtra("selected_memo")){
            Memo memo = getIntent().getParcelableExtra("selected_memo");
            editText.setText(memo.getTxt());
            Glide.with(this)
                    .load(memo.getImageUrl())
                    .into(imageView);
        }
    }
}