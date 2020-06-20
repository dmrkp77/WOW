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
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.protobuf.Any;
import com.mobile.fm.R;
import com.mobile.fm.category.activity.CategoryMainActivity;
import com.mobile.fm.category.activity.RecommendationsActivity;
import com.mobile.fm.exerciseboard.FirebaseHelper;
import com.mobile.fm.exerciseboard.PostInfo;
import com.mobile.fm.exerciseboard.listener.OnPostListener;
import com.mobile.fm.exerciseboard.view.ReadContentsVIew;

import java.util.Date;
import java.util.HashMap;

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

        firebaseHelper = new FirebaseHelper(this);
        firebaseHelper.setOnPostListener(onPostListener);
        uiUpdate();
    }

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

                    HashMap<String, Object> data =  new HashMap<>();
                    data.put("comments",commentTxt);
                    data.put("username",FirebaseAuth.getInstance().getCurrentUser().getUid());
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
        intent.putExtra("postInfo", postInfo);
        startActivityForResult(intent, 0);
    }
}
