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
import android.widget.ImageView;
import android.widget.TextView;
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

public class TodoFragment extends BaseFragment {
    private final String TAG = this.getClass().getSimpleName();

    RecyclerView recyclerView;

    private TextToSpeech tts;
    TextView txtvTitle;
    private List<Memo> todos = new ArrayList<>();
    ArrayList<Memo> arraylist;
    EditText etxtAdd;
    Button btnadd;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;

    private String mail;
    private String titleimsi;
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
        txtvTitle = view.findViewById(R.id.txtvTitle);

        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        // addactivity?????? ????????? ??????
        Intent intent = getActivity().getIntent();
        latitude = intent.getDoubleExtra("LATITUDE", 0);
        longitude = intent.getDoubleExtra("LONGITUDE", 0);
        member = (Member)intent.getSerializableExtra("MEMBER");
        title = intent.getStringExtra("TITLE");
        modify = intent.getBooleanExtra("MODIFY",false);
        if(modify==true){
            latitude = intent.getDoubleExtra("LATITUDE2",0);
            longitude = intent.getDoubleExtra("LONGITUDE2",0);
            title = intent.getStringExtra("TITLE2");
        }

        Log.d(TAG,"member=========="+member.toString());
        txtvTitle.setText(title);


        // ?????? ?????? ????????? - ??????????????? ???????????????i
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
                Toast.makeText(getContext(), "???????????? ??????", Toast.LENGTH_SHORT).show();
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
        Button btnSaveTodo = view.findViewById(R.id.btnSaveTodo);
        btnSaveTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),RealMainActivity.class);
                startActivity(intent);
            }
        });

        ImageView imgHome = view.findViewById(R.id.imgHome);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),RealMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        //??????????????????
        recyclerView = view.findViewById(R.id.recyclerVItems);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));


        //???????????? ?????? adapter ??????
        adapter = new BoardRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        ////////////////////////////////////////////////////////////////////////////////////
        //???????????? ?????? ????????? - DB?????? ?????? ????????????
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
//        //TODO: ?????? ?????? ??????
//        // input?????? ???????????? ????????? "addTextChangedListener" ????????? ???????????? ????????????.
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
//                // input?????? ????????? ?????????????????? ????????????.
//                // search ???????????? ????????????.
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

        // ?????? ?????? ??????
        Button btnSave = view.findViewById(R.id.btnSaveTodo);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;
    } //onCreateView()

//    //TODO: ?????? ?????? ?????? ??????
//    // ????????? ???????????? ?????????
//    public void search(String charText) {
//
//        // ?????? ??????????????? ???????????? ????????? ?????? ????????????.
//        todos.clear();
//
//        // ?????? ????????? ???????????? ?????? ???????????? ????????????.
//        if (charText.length() == 0) {
//            todos.addAll(todos);
//        }
//        // ?????? ????????? ??????..
//        else {
//            // ???????????? ?????? ???????????? ????????????.
//            for (int i = 0; i < arraylist.size(); i++) {
//                // arraylist??? ?????? ???????????? ???????????? ??????(charText)??? ???????????? ????????? true??? ????????????.
//                if (arraylist.get(i).toLowerCase().contains(charText)) {
//                    // ????????? ???????????? ???????????? ????????????.
//                    toods.add(items.get(i));
//                }
//            }
//        }
//        // ????????? ???????????? ????????????????????? ???????????? ???????????? ????????? ???????????? ????????? ????????????.
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

                // ???????????? ?????? ?????????
                checkbox.setOnClickListener(new CheckBox.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO : ??? ??? ????????????
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
                }); //???????????? ?????? ?????????

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
                Toast.makeText(getContext(), "?????????????????? ???????????????.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "TTS??? ?????? ??? ??? ????????????.", Toast.LENGTH_LONG).show();
            }
        }


    }

}//TodoFragment