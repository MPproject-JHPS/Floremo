package org.androidtown.Floremo;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.icu.text.AlphabeticIndex;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.androidtown.Floremo.RecordAdapter;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordedMemo extends AppCompatActivity {
    EditText editText;
    ImageView imageView;
    TextView textView;
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mReference;
    String emotion;
    String key;
    private static final String TAG1 = "RecordedMemo";
    private final int GET_GALLERY_IMAGE = 200;
    Uri selectedImageUri;

    Memo updateMemo = new Memo();

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();
        mFirebaseDataBase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = mFirebaseDataBase.getReference();

        setContentView(R.layout.add_record_memo);

        editText = findViewById(R.id.content);
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.textDate);
        String text = editText.getText().toString();
        Log.d(TAG1,text);
        if(getIntent().hasExtra("selected_memo")){
            Memo memo = getIntent().getParcelableExtra("selected_memo");
            editText.setText(memo.getTxt());
            Glide.with(this)
                    .load(memo.getImageUrl())
                    .into(imageView);
            textView.setText(memo.getDate());
            key = memo.getKey();
        }

        Intent passedIntent = getIntent();
        Bundle myBundle = passedIntent.getExtras();
        if(getIntent().hasExtra("emotion")){
            emotion = myBundle.getString("emotion");
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // ???????????? ??????, ???????????? true??? ?????? ???????????? ??????

        //????????? ????????? ?????? ????????????
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent. setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });
    }

    //?????? ????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recorded_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: { //toolbar??? back??? ????????? ??? ??????
                finish();
                return true;
            }
                case R.id.menu_delete: {
                    //????????? alert??? ????????? ??? ?????????
                    deleteMemo();
                    finish();
                    return true;
                }
                case R.id.menu_modify: {
                    updateMemo();
                    return true;
                }
            }
        return super.onOptionsItemSelected(item);
    }



//??????
    private void onDeleteContent(int position)
    {
//
//        mFirebaseDataBase.getReference().child("Content").child(uidList.get(position)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                System.out.println("error: "+e.getMessage());
//                Toast.makeText(getApplicationContext(), "?????? ??????", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void deleteMemo(){
        DatabaseReference mReference = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/"+emotion);
        mReference.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "????????? ?????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void updateMemo(){
        String text = editText.getText().toString();
        Log.d(TAG1,text);

        updateMemo.setTxt(text);
        //FirebaseStorage??? ?????? ???????????? ?????? ????????????
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child("images");
        StorageReference userRef = imageRef.child(mFirebaseUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = mFirebaseUser.getUid() + "_" + timeStamp;
        StorageReference fileRef = userRef.child(filename);

        mReference = mFirebaseDataBase.getReference(mFirebaseUser.getUid() + "/memos/"+emotion);
        mReference.child(key).child("txt").setValue(text);

        //??????????????? ?????? ????????? ?????? ???????????????
        //???????????? ??????????????? ?????? images?????? ????????? uid ????????? ????????????.
        //uid ?????? ?????? uid+????????????????????? ?????? ????????? ????????????.
        //?????? ????????? storage??? ??????.
        UploadTask uploadTask = fileRef.putFile(selectedImageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        //updateMemo.setImageUrl(downloadUrl.toString());
                        //????????? ????????? ????????? ???????????? Realtime DB??? ??????
                        mReference.child(key).child("imageUrl").setValue(downloadUrl.toString()).addOnSuccessListener(RecordedMemo.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RecordedMemo.this, "????????? ?????????????????????..", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }
}