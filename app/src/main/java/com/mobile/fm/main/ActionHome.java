package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.mobile.fm.R;
import com.mobile.fm.exerciseboard.ExerciseActivity;

import javax.annotation.Nullable;

public class ActionHome extends Fragment implements View.OnClickListener {
    ViewGroup viewGroup;

    private Button musicbtn;
    private Button readingbtn;
    private Button travelbtn;
    private Button exercisebtn;
    private Button tvbtn;
    private Button moviebtn;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container,false);
        musicbtn = (Button) viewGroup.findViewById(R.id.musicbtn);
        readingbtn = (Button) viewGroup.findViewById(R.id.readingbtn);
        travelbtn = (Button) viewGroup.findViewById(R.id.travelbtn);
        exercisebtn = (Button) viewGroup.findViewById(R.id.exercisebtn);
        tvbtn = (Button) viewGroup.findViewById(R.id.tvbtn);
        moviebtn = (Button) viewGroup.findViewById(R.id.moviebtn);


        //ClickListener 처리
        musicbtn.setOnClickListener(this);
        readingbtn.setOnClickListener(this);
        travelbtn.setOnClickListener(this);
        exercisebtn.setOnClickListener(this);
        tvbtn.setOnClickListener(this);
        moviebtn.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View view) {
        //버튼을 눌러 각 액티비티에 맞는 화면으로 이동한다.
        if (view == musicbtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }

        if (view == readingbtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }
        if (view == travelbtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }
        if (view == exercisebtn) {
            startActivity(new Intent(getContext(), ExerciseActivity.class));
        }
        if (view == tvbtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }
        if (view == moviebtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }
    }
}