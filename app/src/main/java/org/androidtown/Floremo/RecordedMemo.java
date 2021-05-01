package org.androidtown.Floremo;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordedMemo extends AppCompatActivity {

    private static final String TAG = "RecordedMemo";
    EditText editText;
    ImageView imageView;
    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_record_memo);

        editText = findViewById(R.id.content);
        imageView = findViewById(R.id.image);
        if(getIntent().hasExtra("selected_memo")){
            Memo memo = getIntent().getParcelableExtra("selected_memo");
            editText.setText(memo.getTxt());
        }

    }
}
