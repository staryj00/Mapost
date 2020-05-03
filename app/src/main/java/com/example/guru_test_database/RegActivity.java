package com.example.guru_test_database;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.guru_test_database.member.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;

public class RegActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private static final int GALLERY_CODE = 1001;

    private EditText etxtID;
    private EditText etxtPwd;
    private EditText etxtName;
    private ImageView imgv;
    private String imgString;

    private String imgPath;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage storage;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        etxtID = findViewById(R.id.etxtID);
        etxtPwd = findViewById(R.id.etxtPwd);
        etxtName = findViewById(R.id.etxtName);
        imgv = findViewById(R.id.imgv);
        imgv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        Button btnJoin = findViewById(R.id.btnJoin);
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "--------------------------------------------------여기까지1" + etxtName);
                database.getReference("members").orderByChild("name").equalTo(etxtName.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            Toast.makeText(getApplicationContext(), "닉네임 중복", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            createUser(etxtID.getText().toString(), etxtPwd.getText().toString(), etxtName.getText().toString());
                            //finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == GALLERY_CODE && data != null) {
            imgPath = getPath(data.getData()); //갤러리에서 선택한 이미지의 경로가 넘어옴

            if (imgPath != null) {
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

    private void createUser(final String email,final String pwd,final String name){ //인터페이스에서 쓰려면 final을 붙여줘야 함

        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //dialog.dismiss(); //다이얼로그 제거

                        if(task.isSuccessful()){
                            //firebaseAuth.getCurrentUser().getEmail();
                            Member member = new Member();
                            member.id = email;
                            member.name = name;
                            member.imageUri = imgString;
                            DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference();
                            databaseref.child("members").child(member.name).setValue(member);
                            Toast.makeText(getApplicationContext(),"회원가입 완료",Toast.LENGTH_SHORT).show();
                            finish();
                        }else{
                            //loginEmail(email, pwd);
                            Toast.makeText(getApplicationContext(),"아이디가 중복됐거나, " +
                                    "입력이 잘못됐습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
