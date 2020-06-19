package com.mobile.fm.category.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mobile.fm.category.adapter.RecommendationsBarAdapter;
import com.mobile.fm.category.data.RecommendationsListItem;

import java.util.ArrayList;
public class RecommendationsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecommendationsBarAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private long[] NumList;
    private ArrayList<RecommendationsListItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ImageView recommendations_cover;
    private TextView recommendations_text;
    private String category_name;

    private LinearLayout recommendations_bg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        recommendations_cover = (ImageView)findViewById(R.id.recommendations_img);
        recommendations_text= (TextView)findViewById(R.id.recommendations_text);
        recommendations_bg=findViewById(R.id.recommendations_bg);

        Intent intent = getIntent();
        category_name =intent.getExtras().getString("category");
        NumList=intent.getExtras().getLongArray("num_list");

        recommendations_text.setText(category_name + "Recommendations");

        set_background_color(category_name);

        //Glide.with(this).load(intent.getExtras().getString("TODO")).into(recommendations_cover);

        init_recyclerView(category_name);
    }


    private void init_recyclerView(String name){
        recyclerView =findViewById(R.id.recommendations_recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;

        //레이아웃 매니저가 수평으로 배치하게 만들어줌.
        //리니어 매니저는 수평,수직,or 일렬 cf)Gridlayout은 바둑판형으로 표시.
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        arrayList =new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference(name);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                //dataSnapshot 여기에 Music에 대한 데이터들이 전부 들어 있다. 그 데이터들은 DataSnapshot 형식임.
                for(Long test : NumList){
                    arrayList.add(dataSnapshot.child(Integer.toString(test.intValue())).getValue(RecommendationsListItem.class));
                }
                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e("Activity", String.valueOf(databaseError.toException()));
            }
        });
        recyclerView.setAdapter(adapter);//리사이클러 뷰의 어댑터 연결

        adapter = new RecommendationsBarAdapter(arrayList, this);

        adapter.setOnItemClickListener(new RecommendationsBarAdapter.OnItemClickListener() {
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
    private void set_background_color(String category_name){
        System.out.println(category_name);
        if(category_name.equals("Music")){
            recommendations_bg.setBackground(getResources().getDrawable(R.drawable.bg_music));
        }
        else if(category_name.equals("Reading")){
            recommendations_bg.setBackground(getResources().getDrawable(R.drawable.bg_reading));
        }
        else if(category_name.equals("Travel")){
            recommendations_bg.setBackground(getResources().getDrawable(R.drawable.bg_travel));
        }
        else if(category_name.equals("Exercise")){
            recommendations_bg.setBackground(getResources().getDrawable(R.drawable.bg_exercise));
        }
        else if(category_name.equals("TV")){
            recommendations_bg.setBackground(getResources().getDrawable(R.drawable.bg_tv));
        }
        else if(category_name.equals("Movie")){
            recommendations_bg.setBackground(getResources().getDrawable(R.drawable.bg_movie));
        }
    }
}
