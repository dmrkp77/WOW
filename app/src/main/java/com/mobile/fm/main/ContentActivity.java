package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.fm.R;
import com.mobile.fm.login.LoginActivity;

public class ContentActivity extends AppCompatActivity {
    private static final String TAG = "ContentActivity";

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //menu objects
    BottomNavigationView bottomNavigationView;
    ActionBookmark actionBookmark;
    ActionHome actionHome;
    ActionSearch actionSearch;
    ActionUser actionUser;

    //view objects
   /* private TextView textViewUserEmail;
    private Button buttonLogout;
    private TextView textivewDelete;
    private Button musicbtn;
    private Button readingbtn;
    private Button travelbtn;
    private Button exercisebtn;
    private Button tvbtn;
    private Button moviebtn;*/

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Intent intent = getIntent();
//        Boolean logout = intent.getBooleanExtra("로그아웃", false);
//
//        if (logout.equals(true)) {
//            firebaseAuth.signOut();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//
//    }

    public void Logout() {
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //making fragment
        actionBookmark = new ActionBookmark();
        actionHome = new ActionHome();
        actionSearch = new ActionSearch();
        actionUser = new ActionUser(this);

        //initializing views
       /* textViewUserEmail = (TextView) findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) findViewById(R.id.textviewDelete);
        musicbtn = (Button) findViewById(R.id.musicbtn);
        readingbtn = (Button) findViewById(R.id.readingbtn);
        travelbtn = (Button) findViewById(R.id.travelbtn);
        exercisebtn = (Button) findViewById(R.id.exercisebtn);
        tvbtn = (Button) findViewById(R.id.tvbtn);
        moviebtn = (Button) findViewById(R.id.moviebtn);*/

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
        //textViewUserEmail.setText("반갑습니다.\n" + user.getEmail() + "으로 로그인 하였습니다.");

        //logout button event
        /*buttonLogout.setOnClickListener(this);
        textivewDelete.setOnClickListener(this);
        musicbtn.setOnClickListener(this);
        readingbtn.setOnClickListener(this);
        travelbtn.setOnClickListener(this);
        exercisebtn.setOnClickListener(this);
        tvbtn.setOnClickListener(this);
        moviebtn.setOnClickListener(this);*/


        //제일 처음 띄워줄 뷰를 세팅해줍니다. commit();까지 해줘야 합니다.
        getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, actionHome).commitAllowingStateLoss();

        //bottomnavigationview의 아이콘을 선택 했을때 원하는 프래그먼트가 띄워질 수 있도록 리스너를 추가합니다.
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            //menu.xml에서 지정해줬던 아이디 값을 받아와서 각 아이디값마다 다른 이벤트를 발생시킵니다.
                            case R.id.action_home: {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, actionHome).commitAllowingStateLoss();
                                return true;
                            }

                            case R.id.action_search: {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, actionSearch).commitAllowingStateLoss();
                                return true;
                            }

                            case R.id.action_bookmark: {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, actionBookmark).commitAllowingStateLoss();
                                return true;
                            }

                            case R.id.action_user: {
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.content_layout, actionUser).commitAllowingStateLoss();
                                return true;
                            }

                            default:
                                return false;

                        }
                    }
                });
    }

    /*@Override
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
    }*/
}
