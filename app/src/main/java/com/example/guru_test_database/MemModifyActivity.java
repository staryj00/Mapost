package com.example.guru_test_database;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guru_test_database.member.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;

public class MemModifyActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final int GALLERY_CODE = 1001;

    private Member member;
    private String identifier;
    private TextView txtvId;
    private TextView txtvName;
    private EditText etxtOld;
    private EditText etxtNew;
    private ImageView imgv;
    private FirebaseDatabase database;

    private String imgString;
    private String imgPath;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private String mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mem_modify);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        mail = user.getEmail();

        Intent intent = getIntent();
        member = (Member)intent.getSerializableExtra("MEMBER");
        identifier = intent.getStringExtra("IDENTIFIER");

        txtvId = findViewById(R.id.txtvId);
        txtvName = findViewById(R.id.txtvName);
        imgv = findViewById(R.id.imgv);
        Button btnmodify = findViewById(R.id.btnModify);

        txtvId.setText(member.id);
        txtvName.setText(member.name);
        if(member.imageUri!=null){
            imgv.setImageURI(Uri.parse(member.imageUri));
        }

        /*etxtOld = findViewById(R.id.etxtOld);
        etxtNew = findViewById(R.id.etxtNew);*/

        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });


        btnmodify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference();
                if(imgString!=null){
                    member.imageUri = imgString;
                }
                /*if(etxtNew.getText().toString()!=null){
                    user.updatePassword(etxtNew.getText().toString());
                }*/
                databaseref.child("members").child(member.name).setValue(member);
                Toast.makeText(getApplicationContext(), "회원정보 수정 완료", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_CODE && data != null) {
            imgPath = getPath(data.getData()); //갤러리에서 선택한 이미지의 경로가 넘어옴
            if (imgPath != null) {
                Log.d(TAG,"imgPath-------------------"+imgPath);
                File file = new File(imgPath);
                imgv.setImageURI(Uri.fromFile(file));
                imgString = Uri.fromFile(file).toString();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }
}