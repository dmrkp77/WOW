package com.mobile.fm.exerciseboard;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.fm.R;
import com.mobile.fm.WriteInfo;
import com.mobile.fm.login.LoginActivity;
import com.mobile.fm.main.GalleryActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class WritePostActivity extends AppCompatActivity {
    private FirebaseUser user;
    private static final String TAG="WritePost Activity";
    private ArrayList<String> pathList= new ArrayList<>();
    private LinearLayout parent;


    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    int pathCount =0;
    int successCount=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);
        parent = findViewById(R.id.contentsLayout);
        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.imagebtn).setOnClickListener(onClickListener);
        findViewById(R.id.videobtn).setOnClickListener(onClickListener);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    String profilePath = data.getStringExtra("profilePath");
                    pathList.add(profilePath);

                    ViewGroup.LayoutParams layoutParams= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

                    ImageView imageView=new ImageView(WritePostActivity.this);
                    imageView.setLayoutParams(layoutParams);
                    Glide.with(this).load(profilePath).override(1000).into(imageView);
                    parent.addView(imageView);

                    EditText editText=new EditText(WritePostActivity.this);
                    editText.setLayoutParams(layoutParams);
                    editText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE| InputType.TYPE_CLASS_TEXT);
                    parent.addView(editText);


                }

                break;
            }

        }
    }


        View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.check:
                    postUpload();

                    break;
                case R.id.imagebtn:
                    if (ContextCompat.checkSelfPermission(WritePostActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(WritePostActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(WritePostActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    1);
                        } else {

                            startToast("권한을 허용해주세요");
                        }
                    } else {
                        myStartActivity(GalleryActivity.class,"image");
                    }

                    break;
                case R.id.videobtn:
                    if (ContextCompat.checkSelfPermission(WritePostActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(WritePostActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(WritePostActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                        } else {


                            startToast("권한을 허용해주세요");
                        }
                    } else {
                        myStartActivity(GalleryActivity.class,"video");
                    }
                    break;

            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    startToast("권한을 허용해주세요");
                }

            }
        }
    }
//    private void postUpload(){
//        final String title=((EditText) findViewById(R.id.titleEditText)).getText().toString();
//        final String content=((EditText) findViewById(R.id.contentEditText)).getText().toString();
//        if(title.length()>0&&content.length()>0){
//            fuser = FirebaseAuth.getInstance().getCurrentUser();
//            WriteInfo user = new WriteInfo(title,content,fuser.getEmail(),new Date());
//            uploader(user);
//        }
//    }
    private void postUpload(){
        final String title=((EditText) findViewById(R.id.titleEditText)).getText().toString();

        if(title.length()>0){
            final ArrayList<String> contentsList= new ArrayList<>();

            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference storageRef= storage.getReference();

            for(int i=0;i<parent.getChildCount();i++){
                View view= parent.getChildAt(i);

                if(view instanceof EditText){
                    String text= ((EditText)view).getText().toString();
                    if(text.length()>0){
                        contentsList.add(text);
                    }else{
                        contentsList.add(pathList.get(pathCount));

                        //user=FirebaseAuth.getInstance().getCurrentUser();
                        final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"/"+pathCount+".jpg");

                        try{
                            InputStream stream= new FileInputStream(new File(pathList.get(pathCount)));
                            StorageMetadata metadata= new StorageMetadata.Builder().setCustomMetadata("index",""+(contentsList.size()-1)).build();
                            UploadTask uploadTask=mountainImagesRef.putStream(stream, metadata);
                            //uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));

                                    mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Log.d("로그",""+uri);
                                            contentsList.set(index,uri.toString());
                                            successCount++;
                                            if(pathList.size()==successCount){
                                                //사진,동영상 업로드 완료
                                                WriteInfo writeInfo=new WriteInfo(title,contentsList, user.getUid(),new Date());
                                                uploader(writeInfo);

                                            }
                                        }
                                    });

                                }
                            });
                        }catch (FileNotFoundException e){
                            Log.e("로그","에러:"+e.toString());
                        }
                        pathCount++;
                    }
                }
            }

        }
    }
    private void uploader(WriteInfo writeInfo){
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        db.collection("posts").add(writeInfo)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        startToast("글이 게시되었습니다.");
                        Log.d(TAG,"글이 게시되었습니다.");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("글 업로드를 실패하였습니다.");
                        Log.w(TAG,"글 업로드를 실패하였습니다.");
                    }
                });
    }
    private void startToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }


    private void myStartActivity(Class c, String media){
        Intent intent=new Intent(this,c);
        intent.putExtra("media",media);
        startActivityForResult(intent,0);
    }
}




















