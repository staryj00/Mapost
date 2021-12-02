package com.example.guru_test_database;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Locale;

public class TodoFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();

    RecyclerView recyclerView;

    private TextToSpeech tts;
    EditText etxtSearch;
    private List<Memo> todos = new ArrayList<>();
    ArrayList<Memo> arraylist;
    EditText etxtAdd;
    Button btnadd;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private String mail;
    private String title;
    private Member member;

    private double latitude;
    private double longitude;
    private boolean modify;

    private String text;
    private Memo todo2;

    BoardRecyclerViewAdapter adapter;


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        etxtAdd = view.findViewById(R.id.etxtAdd);
        etxtSearch = view.findViewById(R.id.etxtSearch);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        // addactivity에서 받아온 정보
        Intent intent = getActivity().getIntent();
        latitude = intent.getDoubleExtra("LATITUDE", 0);
        longitude = intent.getDoubleExtra("LONGITUDE", 0);
        member = (Member)intent.getSerializableExtra("MEMBER");
        modify = intent.getBooleanExtra("MODIFY",false);
        if(modify==true){
            latitude = intent.getDoubleExtra("LATITUDE2",0);
            longitude = intent.getDoubleExtra("LONGITUDE2",0);
        }

        Log.d(TAG,"member=========="+member.toString());
        title = intent.getStringExtra("TITLE");

        // 추가 버튼 리스너 - 리스트뷰에 뿌리기위함
        btnadd = view.findViewById(R.id.btnAdd);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseref = database.getReference();
                Memo todo = new Memo();
                todo.title = title;
                todo.content = etxtAdd.getText().toString();
                todo.isChecked = false;
                todo.share = false;
                todo.latitude = latitude;
                todo.longitude = longitude;
                todo.type = "todo";
                databaseref.child("memos" + member.name).push().setValue(todo);
                Toast.makeText(getContext(), "투두생성 완료", Toast.LENGTH_SHORT).show();
                etxtAdd.getText().clear();

                /*database.getReference("members"+member.name).orderByChild("type").equalTo("todo").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        memos.clear();

                        //int index = 0; // for test

                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
//                            Todo todo = snapshot.getValue(Todo.class);
//                            todos.add(todo);
//                    Log.d(TAG, "key=" + snapshot.getKey());
//                    Log.d(TAG, "onDataChange +" + index++ + "=>>" + imageDTO.toString());
//                    uidLists.add(0, snapshot.getKey());

                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });*/

            }
        });

        //리사이클러뷰
        recyclerView = view.findViewById(R.id.recyclerVItems);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));


        //리스트뷰 관련 adapter 생성
        adapter = new BoardRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////////////////////////
        //리스트뷰 관련 아이템 - DB에서 할일 불러오기
        database.getReference().child("memos"+member.name).orderByChild("type").equalTo("todo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                todos.clear();

                //int index = 0; // for test

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Memo todo = snapshot.getValue(Memo.class);
                    if(todo.latitude == latitude && todo.longitude == longitude) {
                        todos.add(todo);
                    }
//                    Log.d(TAG, "key=" + snapshot.getKey());
//                    Log.d(TAG, "onDataChange +" + index++ + "=>>" + imageDTO.toString());
//                    uidLists.add(0, snapshot.getKey());

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ////////////////////////////////////////////////////////////////////////////////////
//        //TODO: 검색 기능 구현
//        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
//        etxtSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                // input창에 문자를 입력할때마다 호출된다.
//                // search 메소드를 호출한다.
//                String text = etxtSearch.getText().toString();
//                search(text);
//            }
//        });

        //TTS
        tts = new TextToSpeech(getContext(),null);

        // mail
        /*mail = firebaseAuth.getCurrentUser().getEmail();
        database.getReference("members").orderByChild("id").equalTo(mail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        member = childSnapshot.getValue(Member.class);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        // 저장 버튼 동작
        Button btnSave = view.findViewById(R.id.btnSaveTodo);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    } //onCreateView()

//    //TODO: 검색 기능 구현 함수
//    // 검색을 수행하는 메소드
//    public void search(String charText) {
//
//        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
//        todos.clear();
//
//        // 문자 입력이 없을때는 모든 데이터를 보여준다.
//        if (charText.length() == 0) {
//            todos.addAll(todos);
//        }
//        // 문자 입력을 할때..
//        else {
//            // 리스트의 모든 데이터를 검색한다.
//            for (int i = 0; i < arraylist.size(); i++) {
//                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
//                if (arraylist.get(i).toLowerCase().contains(charText)) {
//                    // 검색된 데이터를 리스트에 추가한다.
//                    toods.add(items.get(i));
//                }
//            }
//        }
//        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
//        adapter.notifyDataSetChanged();
//    } //Search()


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

            for (i =0;i < todos.size(); i++) {
                ((CustomViewHolder) viewHolder).checkbox.setText(todo.content);
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
            CheckBox checkbox;

            public CustomViewHolder(View view, final int i) {
                super(view);
                checkbox = view.findViewById(R.id.checkBox);

                // 체크박스 클릭 리스너
                checkbox.setOnClickListener(new CheckBox.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO : 할 일 읽어주기
                        text = checkbox.getText().toString();
                        database.getReference("memos"+member.name).orderByChild("type").equalTo("todo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    todo2 = snapshot.getValue(Memo.class);
                                    String identifier = snapshot.getKey();
                                    if(todo2.latitude==latitude && todo2.longitude == longitude && todo2.content==text){
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
                        tts.setPitch((float)0.1);
                        tts.setSpeechRate((float)1.0);
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }); //체크박스 클릭 리스너

            }
        }


    } // BoardRecyclerViewAdapter

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int language = tts.setLanguage(Locale.KOREA);

            if (language == TextToSpeech.LANG_MISSING_DATA || language == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(getContext(), "지원하지않는 언어입니다.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "TTS를 실행 할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
        }


    }

}//TodoFragment