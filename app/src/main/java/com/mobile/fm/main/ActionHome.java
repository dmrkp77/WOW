package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.fm.R;
import com.mobile.fm.login.LoginActivity;
import com.mobile.fm.login.MainActivity;

import javax.annotation.Nullable;

public class ActionHome extends Fragment implements View.OnClickListener {
    ViewGroup viewGroup;
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private TextView textivewDelete;
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

        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_action_home, container,false);
        textViewUserEmail = (TextView) viewGroup.findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) viewGroup.findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) viewGroup.findViewById(R.id.textviewDelete);
        musicbtn = (Button) viewGroup.findViewById(R.id.musicbtn);
        readingbtn = (Button) viewGroup.findViewById(R.id.readingbtn);
        travelbtn = (Button) viewGroup.findViewById(R.id.travelbtn);
        exercisebtn = (Button) viewGroup.findViewById(R.id.exercisebtn);
        tvbtn = (Button) viewGroup.findViewById(R.id.tvbtn);
        moviebtn = (Button) viewGroup.findViewById(R.id.moviebtn);

        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
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
        if (view == buttonLogout) {
           // firebaseAuth.signOut();
            //finish();
            startActivity(new Intent(getContext(), LoginActivity.class));
        }
        //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야 겠다.
        if (view == textivewDelete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            //finish();
                                            startActivity(new Intent(getContext(), MainActivity.class));
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getContext(), "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }

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
            startActivity(new Intent(getContext(), SportsActivity.class));
        }
        if (view == tvbtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }
        if (view == moviebtn) {
            startActivity(new Intent(getContext(), MusicActivity.class));
        }
    }
}