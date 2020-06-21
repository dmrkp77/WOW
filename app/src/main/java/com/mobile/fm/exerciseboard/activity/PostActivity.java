package com.mobile.fm.exerciseboard.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.mobile.fm.R;
import com.mobile.fm.category.adapter.CategoryBoardAdapter;
import com.mobile.fm.exerciseboard.CommentListItem;
import com.mobile.fm.exerciseboard.FirebaseHelper;
import com.mobile.fm.exerciseboard.PostInfo;
import com.mobile.fm.exerciseboard.adapter.CommentAdapter;
import com.mobile.fm.exerciseboard.listener.OnPostListener;
import com.mobile.fm.exerciseboard.view.ReadContentsVIew;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PostActivity extends BasicActivity {
    private static final String TAG ="" ;
    private PostInfo postInfo;
    private String postId;
    private FirebaseHelper firebaseHelper;
    private ReadContentsVIew readContentsVIew;
    private LinearLayout contentsLayout;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Button addCommentText;
    private EditText enterCommentText;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase database;
    private DocumentReference postRef;
    private DocumentReference commentRef;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private CommentAdapter adapter;
    private ArrayList<CommentListItem> commentList;
    private FirebaseUser user;
    private String uId;
    private boolean updating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        postId=getIntent().getStringExtra("postId");
        postInfo = (PostInfo) getIntent().getSerializableExtra("postInfo");
        contentsLayout = findViewById(R.id.contentsLayout);
        readContentsVIew = findViewById(R.id.readContentsView);
        addCommentText = findViewById(R.id.addCommentText);
        enterCommentText = findViewById(R.id.enterCommentText);

        firebaseFirestore = FirebaseFirestore.getInstance();
        commentList = new ArrayList<>();
        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        user = FirebaseAuth.getInstance().getCurrentUser();
        CollectionReference collectionReference = firebaseFirestore.collection("User");
        collectionReference.document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Map<String,Object> snap = task.getResult().getData();
                uId = snap.get("username").toString();


            }
        });
        uiUpdate();

        init_comment();


    }
    //댓글 가져오기
    private void init_comment(){
        recyclerView =findViewById(R.id.recyclerPostComments);
        recyclerView.setHasFixedSize(true);//리사이클러뷰 기존 성능 강화;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final Boolean clear=true;
        updating = true;
        String date;
        if(commentList.size()==0)date =" ";
        else date = commentList.get(commentList.size() - 1).getCreatedAt();
      CollectionReference collectionReference = firebaseFirestore.collection("posts").document(postId).collection("comments");
//        collectionReference.orderBy("createdAt", Query.Direction.DESCENDING).whereLessThan("createdAt", date).limit(10).get()
                collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (clear) {
                               commentList.clear();
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //시간 얻기
                                Date ndate= new Date();
                                Date ydate= document.getDate("createdAt");
                                long diff=ndate.getTime()-ydate.getTime();
                                diff=diff/60000;
                                String time;
                                if(ndate.getDay()!=ydate.getDay()) {
                                    time= new SimpleDateFormat("MM.dd", Locale.getDefault()).format(document.getDate("createdAt")).toString();
                                }
                                else if(diff<61){
                                    time= diff+"";
                                    time+="분 전";

                                }else if(diff<3601) {
                                    time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(document.getDate("createdAt")).toString();
                                }

                                else{
                                    time= new SimpleDateFormat("MM.dd", Locale.getDefault()).format(document.getDate("createdAt")).toString();
                                }
//                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    commentList.add(new CommentListItem(
                                            document.getData().get("username").toString(),
                                            time,
                                            document.getData().get("comments").toString()));
                            }
                           adapter.notifyDataSetChanged();
                        } else {

                        }
                        updating = false;
                    }
                });
        adapter = new CommentAdapter(commentList ,this);
        recyclerView.setAdapter(adapter);//게시판 리사이클러 뷰의 어댑터 연결
    }

    //댓글 추가 (데베 올리기)
    public void addCommentTextClicked(View view) {
        final String commentTxt = enterCommentText.getText().toString();
        postRef = FirebaseFirestore.getInstance().collection("posts").document(postId);

        if (view == addCommentText) {
            FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentSnapshot snapshot = transaction.get(postRef);
                    int numComments = postInfo.getNumComments()+1;
                    transaction.update(postRef,"numComments",numComments);
                    commentRef=FirebaseFirestore.getInstance().collection("posts").document(postId).collection("comments").document();
                    final HashMap<String, Object> data =  new HashMap<>();

                    data.put("comments",commentTxt);
                    data.put("username",uId);
                    data.put("createdAt",new Date());
                    transaction.set(commentRef,data);

                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(getApplicationContext(), "댓글이 게시되었습니다.", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Transaction success!");
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "댓글 오류.", Toast.LENGTH_SHORT).show();
                            Log.w(TAG, "Transaction failure.", e);
                        }
                    });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    postInfo = (PostInfo)data.getSerializableExtra("postinfo");
                    contentsLayout.removeAllViews();
                    uiUpdate();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        switch (item.getItemId()) {
            case R.id.delete:
                if(postInfo.getPublisher().toString().equals(firebaseUser.getUid().toString())) {
                    firebaseHelper.storageDelete(postInfo);
                }else{
                    Toast.makeText(this, "삭제 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.modify:
                if(postInfo.getPublisher().toString().equals(firebaseUser.getUid().toString())) {
                    myStartActivity(WritePostActivity.class, postInfo);
                }else{
                    Toast.makeText(this, "수정 권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    OnPostListener onPostListener = new OnPostListener() {
        @Override
        public void onDelete(PostInfo postInfo) {
            Log.e("로그 ","삭제 성공");
        }

        @Override
        public void onModify() {
            Log.e("로그 ","수정 성공");
        }
    };

    private void uiUpdate(){
        setToolbarTitle(postInfo.getTitle());
        readContentsVIew.setPostInfo(postInfo);
    }

    private void myStartActivity(Class c, PostInfo postInfo) {
        Intent intent = new Intent(this, c);
        intent.putExtra("category",postInfo.getBoardSelect());
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }
}
