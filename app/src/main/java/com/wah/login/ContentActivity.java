package com.wah.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wah.login.R;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ContentActivity";

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;
    private TextView textivewDelete;
    private Button musicbtn;
    private Button readingbtn;
    private Button travelbtn;
    private Button exercisebtn;
    private Button tvbtn;
    private Button moviebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) findViewById(R.id.textviewDelete);
        musicbtn = (Button) findViewById(R.id.musicbtn);
        readingbtn = (Button) findViewById(R.id.readingbtn);
        travelbtn = (Button) findViewById(R.id.travelbtn);
        exercisebtn = (Button) findViewById(R.id.exercisebtn);
        tvbtn = (Button) findViewById(R.id.tvbtn);
        moviebtn = (Button) findViewById(R.id.moviebtn);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();
        //유저가 로그인 하지 않은 상태라면 null 상태이고 이 액티비티를 종료하고 로그인 액티비티를 연다.
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        //유저가 있다면, null이 아니면 계속 진행
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //textViewUserEmail의 내용을 변경해 준다.
        textViewUserEmail.setText("반갑습니다.\n" + user.getEmail() + "으로 로그인 하였습니다.");

        //logout button event
        buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        musicbtn.setOnClickListener(this);
        readingbtn.setOnClickListener(this);
        travelbtn.setOnClickListener(this);
        exercisebtn.setOnClickListener(this);
        tvbtn.setOnClickListener(this);
        moviebtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        //회원탈퇴를 클릭하면 회원정보를 삭제한다. 삭제전에 컨펌창을 하나 띄워야 겠다.
        if (view == textivewDelete) {
            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ContentActivity.this);
            alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ContentActivity.this, "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        }
                                    });
                        }
                    }
            );
            alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(ContentActivity.this, "취소", Toast.LENGTH_LONG).show();
                }
            });
            alert_confirm.show();
        }

        //버튼을 눌러 각 액티비티에 맞는 화면으로 이동한다.
        if (view == musicbtn) {
            startActivity(new Intent(this, MusicActivity.class));
        }

        if (view == readingbtn) {
            startActivity(new Intent(this, MusicActivity.class));
        }
        if (view == travelbtn) {
            startActivity(new Intent(this, MusicActivity.class));
        }
        if (view == exercisebtn) {
            startActivity(new Intent(this, MusicActivity.class));
        }
        if (view == tvbtn) {
            startActivity(new Intent(this, MusicActivity.class));
        }
        if (view == moviebtn) {
            startActivity(new Intent(this, MusicActivity.class));
        }
    }
}
