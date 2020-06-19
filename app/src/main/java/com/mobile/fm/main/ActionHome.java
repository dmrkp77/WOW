package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.fm.R;
import com.mobile.fm.category.activity.CategoryMainActivity;
import com.mobile.fm.exerciseboard.activity.MainActivity;
import com.mobile.fm.music.activity.MusicActivity;
import com.mobile.fm.music.activity.MusicListActivity;

import javax.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class ActionHome extends Fragment implements View.OnClickListener {
    ViewGroup viewGroup;

    ContentActivity activity;

    private TextView fragmentUsername;

    private Button musicbtn;
    private Button readingbtn;
    private Button travelbtn;
    private Button exercisebtn;
    private Button tvbtn;
    private Button moviebtn;

    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container,false);

        //initializing username from firebase
        fragmentUsername = (TextView) viewGroup.findViewById(R.id.fragmentHome_UserName);

        musicbtn = (Button) viewGroup.findViewById(R.id.musicbtn);
        readingbtn = (Button) viewGroup.findViewById(R.id.readingbtn);
        travelbtn = (Button) viewGroup.findViewById(R.id.travelbtn);
        exercisebtn = (Button) viewGroup.findViewById(R.id.exercisebtn);
        tvbtn = (Button) viewGroup.findViewById(R.id.tvbtn);
        moviebtn = (Button) viewGroup.findViewById(R.id.moviebtn);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User").document(firebaseUser.getEmail());


        //ClickListener 처리
        musicbtn.setOnClickListener(this);
        readingbtn.setOnClickListener(this);
        travelbtn.setOnClickListener(this);
        exercisebtn.setOnClickListener(this);
        tvbtn.setOnClickListener(this);
        moviebtn.setOnClickListener(this);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Log.d("mingyu",documentSnapshot.getString("username"));
                            fragmentUsername.setText(documentSnapshot.getString("username"));

                        } else {
                            Toast.makeText(activity.getApplicationContext(), "No Nickname", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity.getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });

        return viewGroup;
    }

    @Override
    public void onClick(View view) {
        //버튼을 눌러 각 액티비티에 맞는 화면으로 이동한다.
        Intent intent = new Intent(getContext(), CategoryMainActivity.class);
        if (view == musicbtn) {
            intent.putExtra("category","Music");
        }
        else if (view == readingbtn) {
            intent.putExtra("category","Reading");
        }
        else if (view == travelbtn) {
            intent.putExtra("category","Travel");
        }
        else if (view == exercisebtn) {
            intent.putExtra("category","Exercise");
        }
        else if (view == tvbtn) {
            intent.putExtra("category","TV");
        }
        else if (view==moviebtn){
            intent.putExtra("category","Movie");
        }
        System.out.println("여기까지완료");
        startActivity(intent);
    }
}