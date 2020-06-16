package com.mobile.fm.exerciseboard;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.fm.R;

import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<User> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();// 유저를 담을 어레이리스트(어댑터쪽으로 날림)

        database = FirebaseDatabase.getInstance();//파이어베이스 연동
        databaseReference = database.getReference("User");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear();//기존 배열리스트 초기화
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){//반복문으로 데이터 list추출
                    User user = snapshot.getValue(User.class); // 만들어뒀던 User객체에 데이터를 담는다.
                    arrayList.add(user); // 배열리스트에 추가 리사이클러뷰에 보낼 준비
                }
                adapter.notifyDataSetChanged();//리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스를 가지고 오던중 오류발생
                Log.e("MainActivity", String.valueOf(databaseError.toException()));//에러문 출력
            }
        });
        adapter = new CustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결


    }
}
