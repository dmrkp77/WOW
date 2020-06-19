package com.mobile.fm.category.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
import com.mobile.fm.category.adapter.CategoryRecommendationsAdapter;
import com.mobile.fm.category.data.RecommendationsListItem;
import com.mobile.fm.exerciseboard.activity.MainActivity;
import com.mobile.fm.music.adapter.CustomAdapterForSquareList;
import com.mobile.fm.music.adapter.CustomAdapterForSquareMusic;
import com.mobile.fm.music.data.Music;
import com.mobile.fm.music.data.MusicListSquare;
import com.mobile.fm.music.decoration.ListDecoration;

import java.util.ArrayList;

public class CategoryMainActivity  extends AppCompatActivity implements View.OnClickListener {

    private String category_name;
    private LinearLayout background;
    private RecyclerView recyclerView;
    private CategoryRecommendationsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Long> NumList;
    private ArrayList<RecommendationsListItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button recommendations_btn;
    private Button board_btn;
    private long[] NumArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_main);
        Intent intent = getIntent();
        //카테고리별로 테마 색상 바꿈, set_background_color에 추가가능.
        category_name =intent.getExtras().getString("category");
        set_background_color(category_name);

        //btn listener
        recommendations_btn=findViewById(R.id.recommendations_btn);
        board_btn=findViewById(R.id.board_btn);
        recommendations_btn.setOnClickListener(this);
        board_btn.setOnClickListener(this);

        database = FirebaseDatabase.getInstance();
        init_today_picks(category_name);
    }

    private void init_today_picks(final String category_name){
        recyclerView =findViewById(R.id.category_main_recyclerview_for_recommendations);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;

        //레이아웃 매니저가 수평으로 배치하게 만들어줌.
        //리니어 매니저는 수평,수직,or 일렬 cf)Gridlayout은 바둑판형으로 표시.
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        NumList =new ArrayList<>();
        arrayList=new ArrayList<>();
        databaseReference = database.getReference("Today"+category_name); //오늘의 노래들 번호를 받아오기위해 레퍼런스를 가져옴.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스  db를 받아오는 곳.
                NumList.clear();  //기존 배열 초기화, 방지차원.
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){//반복문으로 데이터 list를 추출해냄
                    Long num =snapshot.getValue(Long.TYPE); // 만들어 뒀떤 Music 객체에  데이터를 담음.
                    NumList.add(num);   //담은 데이터를 배열리스트ㅔ 넣고 리사이클 뷰로 보낼 준비
                }
//                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e(category_name+"Activity", String.valueOf(databaseError.toException()));
            }
        });
        databaseReference = database.getReference(category_name);

        System.out.println(category_name+"까지 db가져오기 성공");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                //dataSnapshot 여기에 Music에 대한 데이터들이 전부 들어 있다. 그 데이터들은 DataSnapshot 형식임.
                NumArray=new long[NumList.size()];
                int idx=0;
                for(Long test : NumList){
                    System.out.println("값"+test);
                    arrayList.add(dataSnapshot.child(Integer.toString(test.intValue())).getValue(RecommendationsListItem.class));
                    NumArray[idx++]=test;
                }
                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e(category_name+"Activity", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CategoryRecommendationsAdapter(arrayList, this);

        adapter.setOnItemClickListener(new CategoryRecommendationsAdapter.OnItemClickListener() {
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

    private void set_background_color(String category_name){
        background=findViewById(R.id.category_bg);
        System.out.println(category_name);
        if(category_name.equals("Music")){
            background.setBackground(getResources().getDrawable(R.drawable.bg_music));
        }
        else if(category_name.equals("Reading")){
            background.setBackground(getResources().getDrawable(R.drawable.bg_reading));
        }
        else if(category_name.equals("Travel")){
            background.setBackground(getResources().getDrawable(R.drawable.bg_travel));
        }
        else if(category_name.equals("Exercise")){
            background.setBackground(getResources().getDrawable(R.drawable.bg_exercise));
        }
        else if(category_name.equals("TV")){
            background.setBackground(getResources().getDrawable(R.drawable.bg_tv));
        }
        else if(category_name.equals("Movie")){
            background.setBackground(getResources().getDrawable(R.drawable.bg_movie));
        }
    }
    private void startYouTube(String link){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link));
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view == recommendations_btn) {
            Intent intent= new Intent(getApplicationContext(), RecommendationsActivity.class);
            intent.putExtra("category",category_name);
            intent.putExtra("num_list",NumArray);
            startActivity(intent);
        }
        else if (view == board_btn) {
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("category",category_name);
            intent.putExtra("num_list",NumArray);
            startActivity(intent);
        }

    }
}
