package com.example.guru_test_database;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class AllViewActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerViewMemo;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private List<Memo> memos = new ArrayList<>();
    private Member member;

//    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_view);

        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra("MEMBER");


        //리사이클러뷰
        recyclerViewMemo = findViewById(R.id.recyclerviewMemos);
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(this));


        //리스트뷰 관련 adapter 생성
        final BoardRecyclerViewAdapter adapter = new BoardRecyclerViewAdapter();
        recyclerViewMemo.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        ////////////////////////////////////////////////////////////////////////////////////////////
        database.getReference().child("memos" + member.name).orderByChild("type").equalTo("memo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memos.clear();

                //int index = 0; // for test

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Memo memo = snapshot.getValue(Memo.class);
                    memos.add(memo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        /*database.getReference().child("memos"+member.name).orderByChild("type").equalTo("todo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todos.clear();

                int index = 0; // for test

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Memo todo = snapshot.getValue(Memo.class);
                    todos.add(todo);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });*/

    } // OnCreate()

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }

    ////////////////////////////////////////////////////////////////////////////////////

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        SearchManager searchManager = (SearchManager) this.getSystemService(Context.SEARCH_SERVICE);
//        searchView = (SearchView) findViewById(R.id.searchView);
//        searchView.onActionViewExpanded(); //바로 검색 할 수 있도록
//
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
////            searchView.setQueryHint(getString(R.string.notice_search_hint));
//            queryTextListener = new SearchView.OnQueryTextListener() {
//                @Override
//                public boolean onQueryTextChange(String newText) {
//                    Log.i("onQueryTextChange", newText);
//
//                    adapter.setFilter(filter(mNoticeContent.getNoticeList(), newText));
//                    return true;
//                }
//
//                @Override
//                public boolean onQueryTextSubmit(String query) {
//                    Log.i("onQueryTextSubmit", query);
//
//                    return true;
//                }
//            };
//            searchView.setOnQueryTextListener(queryTextListener);
//        }
//
//        return true;
//    } // OnCreateOptionsMenu()


    // TODO: 아답터
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
                Log.d(TAG, "title===============" + memo.title + ((CustomViewHolder) viewHolder).txtviewTitle.getText().toString());
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
                Log.d(TAG,"여기서="+txtviewTitle.getText().toString());

                //mView = view;
            }


        } // CustomViewHolder
    } // BoardRecyclerViewAdapter

} // AllviewActivity