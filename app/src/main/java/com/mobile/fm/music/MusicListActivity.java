package com.mobile.fm.music;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.fm.R;

import java.util.ArrayList;

public class MusicListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomAdapterForBarMusic adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Music> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ImageView list_image;
    private TextView list_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        list_image = (ImageView)findViewById(R.id.list_image);
        list_text = (TextView)findViewById(R.id.list_text);

        Intent intent = getIntent();
        String name =intent.getExtras().getString("name");
        list_text.setText(name);
        Glide.with(this).load(intent.getExtras().getString("image")).into(list_image);
        init_recyclerView(name);
    }


    private void init_recyclerView(String name){
        recyclerView =findViewById(R.id.list_recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;

        //레이아웃 매니저가 수평으로 배치하게 만들어줌.
        //리니어 매니저는 수평,수직,or 일렬 cf)Gridlayout은 바둑판형으로 표시.
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList =new ArrayList<>();


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Today"); //데이터 베이스 생성 필요
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스  db를 받아오는 곳.
                arrayList.clear();  //기존 배열 초기화, 방지차원.
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){//반복문으로 데이터 list를 추출해냄
                    Music music =snapshot.getValue(Music.class); // 만들어 뒀떤 Music 객체에  데이터를 담음.
                    arrayList.add(music);   //담은 데이터를 배열리스트ㅔ 넣고 리사이클 뷰로 보낼 준비
                }

                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e("MainActivity", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CustomAdapterForBarMusic(arrayList, this);

        adapter.setOnItemClickListener(new CustomAdapterForBarMusic.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startYouTube(arrayList.get(position).getUrl());
            }
        }) ;
        recyclerView.setAdapter(adapter);//리사이클러 뷰의 어댑터 연결
    }
    private void startYouTube(String link){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }
}
