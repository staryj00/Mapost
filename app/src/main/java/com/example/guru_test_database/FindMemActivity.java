package com.example.guru_test_database;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.guru_test_database.member.Member;
import com.example.guru_test_database.member.Memo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindMemActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private EditText etxtMem;
    private Member friend;

    private TextView txtvFriend;
    private ImageView imgv;

    RecyclerView recyclerViewMemo;
    private FirebaseAuth firebaseAuth;
    private List<Memo> memos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_mem);

        database = FirebaseDatabase.getInstance();

        etxtMem = findViewById(R.id.etxtMem);
        imgv=findViewById(R.id.imgv);
        txtvFriend=findViewById(R.id.txtvFriend);

        recyclerViewMemo = findViewById(R.id.recyclerviewMemos);
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(this));

        final BoardRecyclerViewAdapter adapter = new BoardRecyclerViewAdapter();
        recyclerViewMemo.setAdapter(adapter);

        Button btnSearch = findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgv.setVisibility(View.VISIBLE);
                database.getReference("members").orderByChild("name").equalTo(etxtMem.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        memos.clear();
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                friend = snapshot.getValue(Member.class);
                                break;
                            }
                            txtvFriend.setText(friend.name);

                            /*if(friend.imageUri!=null){
                                imgv.setImageURI(Uri.parse(friend.imageUri));
                            }
                            else{
                                imgv.setImageResource(R.mipmap.ic_launcher);
                            }*/
                            database.getReference("memos"+friend.name).orderByChild("type").equalTo("memo").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Memo memo = snapshot.getValue(Memo.class);
                                            if(memo.share==true){
                                                memos.add(memo);
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
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_all, viewGroup, false);
            return new CustomViewHolder(view, i);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Memo memo = memos.get(i);
            //Memo todo = todos.get(i);


            for (i = 0; i < memos.size(); i++) {
                //Log.d(TAG, "title===============" + memo.title + ((AllViewActivity.BoardRecyclerViewAdapter.CustomViewHolder) viewHolder).txtviewTitle.getText().toString());
                //Log.d(TAG,"txtviewTitle=======>"+((CustomViewHolder) viewHolder).txtviewTitle.getText().toString());
                ((CustomViewHolder) viewHolder).txtviewTitle.setText(memo.title);
                // ((AllviewActivity.BoardRecyclerViewAdapter.CustomViewHolder) viewHolder).checkbox.setText(memo.content);
                ((CustomViewHolder) viewHolder).txtviewContent.setText(memo.content);
            }
        }

        @Override
        public int getItemCount() {
            return memos.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView txtviewTitle;
            TextView txtviewContent;


            public CustomViewHolder(View view, final int i) {
                super(view);
                txtviewTitle = view.findViewById(R.id.txtvT);
                txtviewContent = view.findViewById(R.id.txtvC);
                //Log.d(TAG,"여기서="+txtviewTitle.getText().toString());

                //mView = view;
            }


        } // CustomViewHolder
    } // BoardRecyclerViewAdapter

}
