package com.example.guru_test_database;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guru_test_database.member.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity {

    private final String TAG = this.getClass().getSimpleName();

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText etxtid;
    private EditText etxtpwd;
    private Member member = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etxtid = findViewById(R.id.etxtid);
        etxtpwd = findViewById(R.id.etxtpwd);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();



        String str = "MaPost.";
        TextView tv = (TextView)findViewById(R.id.tv);
        SpannableStringBuilder ssb = new SpannableStringBuilder(str);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFDA1414")),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF1621F")),1,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF2DB2F")),2,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(ssb);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "---------------------------------" + etxtid.getText().toString());
                login(etxtid.getText().toString(),etxtpwd.getText().toString());
            }
        });
        Button btnReg = findViewById(R.id.btnReg);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegActivity.class);
                startActivity(intent);
            }
        });

        if(firebaseAuth.getCurrentUser()!=null){
            Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
            startActivity(intent);
        }
    }

    private void login(final String email, final String pwd) {
        Log.d(TAG, "---------------------------------" + email);
        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                            /*database.getReference("members").orderByChild("id").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                                            Log.d(TAG, "있는데?");
                                            member = childSnapshot.getValue(Member.class);
                                            Log.d(TAG,"---------------member"+member.toString());
                                        }
                                    }
                                    Log.d(TAG, "없는데?");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });*/
                            Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
