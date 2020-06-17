package com.mobile.fm.exerciseboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.mobile.fm.R;
import com.mobile.fm.exerciseboard.WritePostActivity;

import java.util.ArrayList;
import java.util.Date;

public class ExerciseActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList;
    private ArrayList<String> arrayList1;
    private CollectionReference postsRef;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board);

        findViewById(R.id.addTextBtn2).setOnClickListener(onClickListener);
        postsRef=db.collection("posts");
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 성능강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<Post>();// 유저를 담을 어레이리스트(어댑터쪽으로 날림)
        Query a = postsRef.orderBy("createdAt", Query.Direction.DESCENDING);
        a.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("check", document.getId() + " => " + document.getData());
                                String str = document.getData().get("createdAt").toString();
                                String str1="";
                                for(int i =0;i<str.length();i++){
                                    if(str.charAt(i)==' ')break;
                                    str1+=str.charAt(i);
                                }
                                ArrayList<String> arrayList1 = (ArrayList<String>) document.getData().get("body");
                                arrayList.add(new Post(document.getData().get("uid").toString(),document.getData().get("author").toString(),document.getData().get("title").toString(), arrayList1,str1));
                            }

                        } else {
                            Log.w("check", "Error getting documents.", task.getException());
                        }

                        adapter = new CustomAdapter(arrayList, getApplicationContext());
                        recyclerView.setAdapter(adapter); //리사이클러뷰에 어댑터 연결
                    }
                });

    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.addTextBtn2:
                    myStartActivity(WritePostActivity.class);

                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent=new Intent(this,c);
        startActivity(intent);
        finish();
    }
}
