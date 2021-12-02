package com.example.guru_test_database;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.guru_test_database.member.Member;
import com.example.guru_test_database.member.Memo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MemoFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private String mail;
    private String title;
    private Member member;

    private Memo memo;
    private String identifier;
    private boolean modify;

    private double latitude;
    private double longitude;
    private double latitude2;
    private double longitude2;

    private final String TAG = this.getClass().getSimpleName();
    EditText etxtMemo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_memo, container, false);

        etxtMemo = view.findViewById(R.id.etxtMemo);
        Intent intent = getActivity().getIntent();
        latitude = intent.getDoubleExtra("LATITUDE",0);
        longitude = intent.getDoubleExtra("LONGITUDE",0);
        title = intent.getStringExtra("TITLE");

        //memo = (Memo) intent.getSerializableExtra("MEMO");
        //identifier = intent.getStringExtra("IDENTIFIER");
        latitude2 = intent.getDoubleExtra("LATITUDE2",0);
        longitude2 = intent.getDoubleExtra("LONGITUDE2",0);
        modify = intent.getBooleanExtra("MODIFY",false);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mail = firebaseAuth.getCurrentUser().getEmail();
        database.getReference("members").orderByChild("id").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        member = childSnapshot.getValue(Member.class);
                        if(modify==true) {
                            database.getReference("memos" + member.name).orderByChild("type").equalTo("memo")
                            //Query q2 = q1.orderByChild("latitude").equalTo(latitude);
                            //Query q3 = q2.orderByChild("longitude").equalTo(longitude);
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            memo = childSnapshot.getValue(Memo.class);
                                            Log.d(TAG,"memo---------------------------"+memo);
                                            Log.d(TAG,"latitude="+latitude2+"longitude"+longitude2);
                                            if(memo.latitude == latitude2 && memo.longitude ==longitude2){
                                                identifier = childSnapshot.getKey();
                                                etxtMemo.setText(memo.content);
                                                break;
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Button btnSaveMemo = view.findViewById(R.id.btnSaveMemo);
        if(modify==false) {
            //TODO: 저장 버튼 리스너
            btnSaveMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: 메모 내용을 DB에 저장
                    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference();
                    Memo memo = new Memo();
                    memo.title = title;
                    memo.content = etxtMemo.getText().toString();
                    memo.share = false;
                    memo.isChecked = false;
                    memo.latitude = latitude;
                    memo.longitude = longitude;
                    memo.type = "memo";
                    databaseref.child("memos" + member.name).push().setValue(memo);
                    Toast.makeText(getContext(), "메모생성 완료", Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(modify==true) {
            //TODO: 저장 버튼 리스너
            btnSaveMemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: 메모 내용을 DB에 저장
                    DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference();
                    memo.content = etxtMemo.getText().toString();
                    databaseref.child("memos" + member.name).child(identifier).setValue(memo);
                    Toast.makeText(getContext(), "메모수정 완료", Toast.LENGTH_SHORT).show();
                }
            });
        }


        return view;
    } //onCreateView()
} //MemoFragment
