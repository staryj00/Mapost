package com.example.guru_test_database;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.guru_test_database.member.Member;
import com.example.guru_test_database.member.Memo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllTodoActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    RecyclerView recyclerViewTodo;


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private List<Memo> todos = new ArrayList<>();
    private Member member;

    private String text;
    private String [] ret = null;
    private Memo todo2;

//    SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_todo);

        Intent intent = getIntent();
        member = (Member) intent.getSerializableExtra("MEMBER");


        //리사이클러뷰

        recyclerViewTodo = findViewById(R.id.recyclerviewTodos);
        recyclerViewTodo.setLayoutManager(new LinearLayoutManager(this));

        //리스트뷰 관련 adapter 생성
        final BoardRecyclerViewAdapter adapter = new BoardRecyclerViewAdapter();
        recyclerViewTodo.setAdapter(adapter);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        ////////////////////////////////////////////////////////////////////////////////////////////
        database.getReference().child("memos" + member.name).orderByChild("type").equalTo("todo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todos.clear();

                //int index = 0; // for test

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Memo todo = snapshot.getValue(Memo.class);
                    todos.add(todo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    } // OnCreate()

    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(),RealMainActivity.class);
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }


    // TODO: 아답터
    class BoardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_todo, viewGroup, false);
            return new CustomViewHolder(view, i);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            Memo todo = todos.get(i);
            //Memo todo = todos.get(i);


            for (i =0;i < todos.size(); i++) {
                ((CustomViewHolder) viewHolder).checkbox.setText(todo.title+"에서 "+todo.content);
                if(todo.isChecked==true) {
                    ((CustomViewHolder) viewHolder).checkbox.setChecked(true);
                }
            }

        }

        @Override
        public int getItemCount() {
            return todos.size();
        }
        private class CustomViewHolder extends RecyclerView.ViewHolder {
            //TextView txtviewTitle;
            //TextView txtviewContent;
            CheckBox checkbox;
//            public final View mView;
//
//            public Memo memoItems;

            public CustomViewHolder(View view, final int i) {
                super(view);
                //txtviewTitle = view.findViewById(R.id.txtvT);
                //txtviewContent = view.findViewById(R.id.txtvC);
                //Log.d(TAG,"여기서="+txtviewTitle.getText().toString());
                checkbox = view.findViewById(R.id.checkBox);


                checkbox.setOnClickListener(new CheckBox.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO : 할 일 읽어주기
                        text = checkbox.getText().toString();
                        ret = text.split("에서 ");
                        Log.d(TAG,"ret------"+ret[0]+ret[1]);
                        database.getReference("memos"+member.name).orderByChild("type").equalTo("todo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    todo2 = snapshot.getValue(Memo.class);
                                    String identifier = snapshot.getKey();
                                    Log.d(TAG,"todo2앞"+todo2.toString());
                                    Log.d(TAG,"타이틀="+ret[0]+"내용"+ret[1]);
                                    if(todo2.title.equals(ret[0]) && todo2.content.equals(ret[1])){
                                        Log.d(TAG,"----------------todo"+todo2.toString());
                                        if(todo2.isChecked==false) {
                                            todo2.isChecked = true;
                                        }else{
                                            todo2.isChecked = false;
                                        }
                                        DatabaseReference databaseref = FirebaseDatabase.getInstance().getReference();
                                        databaseref.child("memos" + member.name).child(identifier).setValue(todo2);
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        /*
                        for(int j=0;j<todos.size();j++){
                            Memo todo = todos.get(j);
                            if(todo.content==text){
                                Log.d(TAG,"todo------------------------------------"+todo.toString());
                                todo.isChecked = true;

                                break;
                            }
                        }*/
                    }
                }); //체크박스 클릭 리스너

                //mView = view;
            }


        } // CustomViewHolder
    } // BoardRecyclerViewAdapter

} // AllviewActivity