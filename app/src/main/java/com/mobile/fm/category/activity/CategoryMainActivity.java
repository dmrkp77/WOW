package com.mobile.fm.category.activity;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobile.fm.R;
import com.mobile.fm.category.adapter.CategoryBoardAdapter;
import com.mobile.fm.category.adapter.CategoryRecommendationsAdapter;
import com.mobile.fm.category.data.RecommendationsListItem;
import com.mobile.fm.exerciseboard.PostInfo;
import com.mobile.fm.exerciseboard.activity.MainActivity;
import com.mobile.fm.exerciseboard.activity.PostActivity;
import com.mobile.fm.exerciseboard.adapter.HomeAdapter;
import com.mobile.fm.music.decoration.ListDecoration;

import java.util.ArrayList;
import java.util.Date;

public class CategoryMainActivity  extends AppCompatActivity implements View.OnClickListener {

    private String category_name;
    private LinearLayout background;
    private RecyclerView recyclerView;
    private RecyclerView boardRecyclerView;
    private CategoryRecommendationsAdapter adapter;
    private CategoryBoardAdapter badapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager BlayoutManager;
    private ArrayList<Long> NumList;
    private ArrayList<RecommendationsListItem> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button recommendations_btn;
    private Button board_btn;
    private long[] NumArray;
    private boolean updating;
    private FirebaseFirestore firebaseFirestore;
    private CategoryBoardAdapter categoryBoardAdapter;
    private ArrayList<PostInfo> postList;
    private Activity activity;
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

    private void init_today_picks(final String category_name) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        postList = new ArrayList<>();


        recyclerView = findViewById(R.id.category_main_recyclerview_for_recommendations);
        boardRecyclerView = findViewById(R.id.category_main_recyclerview_for_board);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;
        boardRecyclerView.setHasFixedSize(true);
        //레이아웃 매니저가 수평으로 배치하게 만들어줌.
        //리니어 매니저는 수평,수직,or 일렬 cf)Gridlayout은 바둑판형으로 표시.
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        BlayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        boardRecyclerView.setLayoutManager(BlayoutManager);
        NumList = new ArrayList<>();
        arrayList = new ArrayList<>();
        databaseReference = database.getReference("Today" + category_name); //오늘의 노래들 번호를 받아오기위해 레퍼런스를 가져옴.
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //파이어 베이스  db를 받아오는 곳.
                NumList.clear();  //기존 배열 초기화, 방지차원.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//반복문으로 데이터 list를 추출해냄
                    Long num = snapshot.getValue(Long.TYPE); // 만들어 뒀떤 Music 객체에  데이터를 담음.
                    NumList.add(num);   //담은 데이터를 배열리스트ㅔ 넣고 리사이클 뷰로 보낼 준비
                }
//                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e(category_name + "Activity", String.valueOf(databaseError.toException()));
            }
        });
        databaseReference = database.getReference(category_name);

        System.out.println(category_name + "까지 db가져오기 성공");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                //dataSnapshot 여기에 Music에 대한 데이터들이 전부 들어 있다. 그 데이터들은 DataSnapshot 형식임.
                NumArray = new long[NumList.size()];
                int idx = 0;
                for (Long test : NumList) {
                    System.out.println("값" + test);
                    arrayList.add(dataSnapshot.child(Integer.toString(test.intValue())).getValue(RecommendationsListItem.class));
                    NumArray[idx++] = test;
                }
                adapter.notifyDataSetChanged();//   리스트 저장 및  새롴고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생시.
                Log.e(category_name + "Activity", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new CategoryRecommendationsAdapter(arrayList, this);

        adapter.setOnItemClickListener(new CategoryRecommendationsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                startYouTube(arrayList.get(position).getUrl());
            }
        });
        recyclerView.setAdapter(adapter);//리사이클러 뷰의 어댑터 연결

        //각 ITEM 마다 갭 설정.
        ListDecoration decoration = new ListDecoration();
        recyclerView.addItemDecoration(decoration);


        //게시판 글 정보 받아오기
        final Boolean clear=true;
        updating = true;
        Date date = postList.size() == 0 || clear ? new Date() : postList.get(postList.size() - 1).getCreatedAt();
        CollectionReference collectionReference = firebaseFirestore.collection("posts");
        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(99).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (clear) {
                                postList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if (category_name.equals(document.getData().get("boardSelect").toString())) {
                                    postList.add(new PostInfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            (ArrayList<String>) document.getData().get("formats"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime()),
                                            document.getId(),
                                            document.getData().get("nid").toString(),
                                            category_name)
                                    );
                                }
                            }
                            categoryBoardAdapter.notifyDataSetChanged();
                        } else {

                        }
                        updating = false;
                    }
                });
        categoryBoardAdapter = new CategoryBoardAdapter(this,postList);

//        categoryBoardAdapter.setOnItemClickListener(new CategoryBoardAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position) {
//                Intent intent = new Intent(activity, PostActivity.class);
//                intent.putExtra("postInfo", postList.get(position));
//                activity.startActivity(intent);
//            }
//        });
        boardRecyclerView.setAdapter(categoryBoardAdapter);//게시판 리사이클러 뷰의 어댑터 연결

        ListDecoration Bdecoration = new ListDecoration();
        boardRecyclerView.addItemDecoration(Bdecoration);

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
