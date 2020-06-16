package com.mobile.fm.main;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.fm.R;
import com.mobile.fm.WriteInfo;

public class WritePostActivity extends AppCompatActivity {
    private FirebaseUser user;
    private static final String TAG="WritePost Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        findViewById(R.id.check).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.check:
                    postUpload();

                    break;
            }
        }
    };

    private void postUpload(){
        final String title=((EditText) findViewById(R.id.titleEditText)).getText().toString();
        final String contents=((EditText) findViewById(R.id.contentEditText)).getText().toString();

        if(title.length()>0&&contents.length()>0){
            user = FirebaseAuth.getInstance().getCurrentUser();
            WriteInfo writeInfo=new WriteInfo(title,contents,user.getUid());
            uploader(writeInfo);
        }
    }
    private void uploader(WriteInfo writeInfo){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG,"글이 게시되었습니다.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG,"글 업로드를 실패하였습니다.");
                    }
                });
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}




















