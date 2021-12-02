package com.example.guru_test_database;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guru_test_database.member.Member;
import com.example.guru_test_database.member.Memo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindMemActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private EditText etxtMem;
    private Member friend;

    private TextView txtvFriend;
    private ImageView imgv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mem);

        database = FirebaseDatabase.getInstance();

        etxtMem = findViewById(R.id.etxtMem);
        imgv=findViewById(R.id.imgv);
        txtvFriend=findViewById(R.id.txtvFriend);

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.getReference("members").orderByChild("name").equalTo(etxtMem.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                friend = snapshot.getValue(Member.class);
                                break;
                            }
                        txtvFriend.setText(friend.name);
                            if(friend.imageUri!=null){
                                imgv.setImageURI(Uri.parse(friend.imageUri));
                            }
                            else{
                                imgv.setImageResource(R.mipmap.ic_launcher);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
