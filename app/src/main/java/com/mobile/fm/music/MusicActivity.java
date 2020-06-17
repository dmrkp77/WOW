package com.mobile.fm.music;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.fm.R;

import java.util.ArrayList;

public class MusicActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private CustomAdapterForSquareMusic adapter;
    private CustomAdapterForSquareList adapter2;  //따로
    private CustomAdapterForSquareList adapter3;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Long> musicNumList;
    private ArrayList<Music> arrayList;
    private ArrayList<MusicListSquare> arrayList2;    //따로
    private ArrayList<MusicListSquare> arrayList3;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        database = FirebaseDatabase.getInstance();

        init_today_picks();
        init_mood();
        init_place();
    }

    private void init_today_picks(){
        recyclerView =findViewById(R.id.recyclerView1);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;

        //레이아웃 매니저가 수평으로 배치하게 만들어줌.
        //리니어 매니저는 수평,수직,or 일렬 cf)Gridlayout은 바둑판형으로 표시.
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        musicNumList =new ArrayList<>();
        arrayList=new ArrayList<>();

        databaseReference = database.getReference("Today"); //오늘의 노래들 번호를 받아오기위해 레퍼런스를 가져옴.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스  db를 받아오는 곳.
                musicNumList.clear();  //기존 배열 초기화, 방지차원.
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){//반복문으로 데이터 list를 추출해냄
                    Long num =snapshot.getValue(Long.TYPE); // 만들어 뒀떤 Music 객체에  데이터를 담음.
                    musicNumList.add(num);   //담은 데이터를 배열리스트ㅔ 넣고 리사이클 뷰로 보낼 준비
                }
//                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e("MusicActivity", String.valueOf(databaseError.toException()));
            }
        });
        databaseReference = database.getReference("Music");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                //dataSnapshot 여기에 Music에 대한 데이터들이 전부 들어 있다. 그 데이터들은 DataSnapshot 형식임.
                for(Long test : musicNumList){
                    System.out.println("값"+test);
                    arrayList.add(dataSnapshot.child(Integer.toString(test.intValue())).getValue(Music.class));
                }
                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e("MusicActivity", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CustomAdapterForSquareMusic(arrayList, this);

        adapter.setOnItemClickListener(new CustomAdapterForSquareMusic.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startYouTube(arrayList.get(position).getUrl());
            }
        }) ;
        recyclerView.setAdapter(adapter);//리사이클러 뷰의 어댑터 연결

        //각 ITEM 마다 갭 설정.
        ListDecoration decoration = new ListDecoration();
        recyclerView.addItemDecoration(decoration);
    }

    private void init_mood(){
        recyclerView =findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList2 =new ArrayList<>();


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Genre");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스  db를 받아오는 곳.
                arrayList2.clear();  //기존 배열 초기화, 방지차원.
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){//반복문으로 데이터 list를 추출해냄
                    MusicListSquare music =snapshot.getValue(MusicListSquare.class); // 만들어 뒀떤 Music 객체에  데이터를 담음.
                    arrayList2.add(music);   //담은 데이터를 배열리스트ㅔ 넣고 리사이클 뷰로 보낼 준비
                }

                adapter2.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e("MusicActivity", String.valueOf(databaseError.toException()));
            }
        });
        adapter2 = new CustomAdapterForSquareList(arrayList2, this);
        adapter2.setOnItemClickListener(new CustomAdapterForSquareList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicListActivity.class);
                intent.putExtra("name",arrayList2.get(position).getName());
                intent.putExtra("image",arrayList2.get(position).getImage());
                startActivity(intent);
            }
        }) ;
        recyclerView.setAdapter(adapter2);//리사이클러 뷰의 어댑터 연결

        //각 ITEM 마다 갭 설정.
        ListDecoration decoration = new ListDecoration();
        recyclerView.addItemDecoration(decoration);
    }


    private void init_place(){
        recyclerView =findViewById(R.id.recyclerView3);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList3 =new ArrayList<>();


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Place");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스  db를 받아오는 곳.
                arrayList3.clear();  //기존 배열 초기화, 방지차원.
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){//반복문으로 데이터 list를 추출해냄
                    MusicListSquare music =snapshot.getValue(MusicListSquare.class); // 만들어 뒀떤 Music 객체에  데이터를 담음.
                    arrayList3.add(music);   //담은 데이터를 배열리스트ㅔ 넣고 리사이클 뷰로 보낼 준비
                }

                adapter3.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e("MusicActivity", String.valueOf(databaseError.toException()));
            }
        });
        adapter3 = new CustomAdapterForSquareList(arrayList3, this);
        adapter3.setOnItemClickListener(new CustomAdapterForSquareList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(), MusicListActivity.class);
                intent.putExtra("name",arrayList3.get(position).getName());
                intent.putExtra("image",arrayList3.get(position).getImage());
                startActivity(intent);
            }
        }) ;
        recyclerView.setAdapter(adapter3);//리사이클러 뷰의 어댑터 연결

        //각 ITEM 마다 갭 설정.
        ListDecoration decoration = new ListDecoration();
        recyclerView.addItemDecoration(decoration);
    }


    private void startYouTube(String link){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }
}