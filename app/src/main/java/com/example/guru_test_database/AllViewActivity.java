package com.example.guru_test_database;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
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
    RecyclerView recyclerViewTodo;
    Spinner category;
    int intCategory = 0;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private List<Memo> memos = new ArrayList<>();
    private List<Memo> todos = new ArrayList<>();
    private Member member;

//    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_view);

        Intent intent = getIntent();
        member = (Member)intent.getSerializableExtra("MEMBER");

        category = findViewById(R.id.category);
        //리사이클러뷰
        recyclerViewMemo = findViewById(R.id.recyclerviewMemos);
        recyclerViewMemo.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewTodo = findViewById(R.id.recyclerviewTodos);
        recyclerViewTodo.setLayoutManager(new LinearLayoutManager(this));

        //리스트뷰 관련 adapter 생성
        final BoardRecyclerViewAdapter adapter = new BoardRecyclerViewAdapter();
        recyclerViewMemo.setAdapter(adapter);
        recyclerViewTodo.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();



        //TODO: 스피너에 문자열 뜨게 하기
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.category, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                intCategory = position; // 0: 메모, 1: 할 일
                changeList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }); //스피너 리스너

        ////////////////////////////////////////////////////////////////////////////////////////////
        database.getReference().child("memos"+member.name).orderByChild("type").equalTo("memo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                memos.clear();

                //int index = 0; // for test

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Memo memo = snapshot.getValue(Memo.class);
                    memos.add(memo);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
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


    private void changeList() {
        if (intCategory == 0) {
            recyclerViewMemo.setVisibility(View.VISIBLE);
            recyclerViewTodo.setVisibility(View.INVISIBLE);
            //intCategory=1;



        } //메모 선택
        else {
            recyclerViewMemo.setVisibility(View.INVISIBLE);
            recyclerViewTodo.setVisibility(View.VISIBLE);
            //intCategory = 0;

        } //할 일 선택
    } //ChangeList()

    // TODO: 아답터
    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//        Context context;
//        LayoutInflater inflater;
//        private List<Memo> memoItems = null;
//        private ArrayList<Memo> arrayList;
//
//        public BoardRecyclerViewAdapter(Context context, List<Memo> memoItems) {
//            this.context = context;
//            this.memoItems = memoItems;
//            arrayList = new ArrayList<Memo>();
//            arrayList.addAll(memoItems);
//        } // BoardRecyclerViewAdapter()




//        //이부분 중요!! 검색 리스트를 나오게하기 위해 꼭 필요
//        public void setFilter(List<Memo> items) {
//            mValues.clear();
//            mValues.addAll(items);
//            notifyDataSetChanged();
//        } // setFilter()



        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_all, viewGroup, false);
            return new  CustomViewHolder(view, i);
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Memo memo = memos.get(i);
            //Memo todo = todos.get(i);

            switch (intCategory){
                case 0: //메모 객체 얻어오기
                    for(i=0;i<memos.size();i++) {
                        Log.d(TAG,"title==============="+memo.title+((CustomViewHolder) viewHolder).txtviewTitle.getText().toString());
                        //Log.d(TAG,"txtviewTitle=======>"+((CustomViewHolder) viewHolder).txtviewTitle.getText().toString());
                        ((CustomViewHolder) viewHolder).txtviewTitle.setText(memo.title);
                        // ((AllviewActivity.BoardRecyclerViewAdapter.CustomViewHolder) viewHolder).checkbox.setText(memo.content);
                        ((CustomViewHolder) viewHolder).txtviewContent.setText(memo.content);
                    }
                    break;
                /*case 1: //할 일 객체 얻어오기
                    for(i=0;i<todos.size();i++) {
                        ((CustomViewHolder) viewHolder).txtviewTitle.setText(todo.title);
                        // ((AllviewActivity.BoardRecyclerViewAdapter.CustomViewHolder) viewHolder).checkbox.setText(todo.content);
                        ((CustomViewHolder) viewHolder).txtviewContent.setText(todo.content);
                    }
                    break;*/

            }
        }

        @Override
        public int getItemCount() {
            if(intCategory ==0) {
                return memos.size();
            }
            else return todos.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            TextView txtviewTitle;
            TextView txtviewContent;
//            public final View mView;
//
//            public Memo memoItems;

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