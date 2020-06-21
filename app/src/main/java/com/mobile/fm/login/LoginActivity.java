package com.mobile.fm.login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.fm.PreferenceManager;
import com.mobile.fm.main.ContentActivity;
import com.mobile.fm.R;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    //define view objects

    //    String id, pw;
    private CheckBox saveIdPassword;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;
    private TextView textviewSignup;
    private TextView textviewMessage;
    private TextView textviewFindPassword;
    private ProgressDialog progressDialog;
    //define firebase object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            //이미 로그인 되었다면 이 액티비티를 종료함
            finish();
            //그리고 원하는 액티비티를 연다.
            startActivity(new Intent(getApplicationContext(), ContentActivity.class)); //추가해 줄 Activity
        }

        //initializing checkbox
        saveIdPassword = (CheckBox) findViewById(R.id.saveIdPassword);
        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textviewMessage = (TextView) findViewById(R.id.textviewMessage);
        textviewFindPassword = (TextView) findViewById(R.id.textViewFindpassword);
        textviewSignup = (TextView) findViewById(R.id.textviewSignup);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        progressDialog = new ProgressDialog(this);


        //checkbox상태에 따른 수행내용
        boolean boo = PreferenceManager.getBoolean(this, "check");
        if (boo) {
            // 체크가 되어있다면 아래 코드를 수행
            // 저장된 아이디와 암호를 가져와 셋팅한다.
            editTextEmail.setText(PreferenceManager.getString(this, "id"));
            editTextPassword.setText(PreferenceManager.getString(this, "pw"));
            saveIdPassword.setChecked(true); //체크박스는 여전히 체크 표시 하도록 셋팅
        } else {
            editTextEmail.setText(PreferenceManager.getString(this, "id"));
        }

        //button click event
        buttonLogin.setOnClickListener(listener);
        textviewSignup.setOnClickListener(listener);
        textviewFindPassword.setOnClickListener(listener);
        saveIdPassword.setOnClickListener(checklistener);
    }

    //firebase userLogin method
    private void userLogin() {
        PreferenceManager.setString(this, "id", editTextEmail.getText().toString().trim()); //id라는 키값으로 저장
        PreferenceManager.setString(this, "pw", editTextPassword.getText().toString().trim()); //pw라는 키값으로 저장

//        String email = editTextEmail.getText().toString().trim();
//        String password = editTextPassword.getText().toString().trim();

        // 저장한 키 값으로 저장된 아이디와 암호를 불러와 String 값에 저장
        String checkId = PreferenceManager.getString(this, "id");
        String checkPw = PreferenceManager.getString(this, "pw");


        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            }
        }

        if (TextUtils.isEmpty(checkId)) {
            Toast.makeText(this, "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(checkPw)) {
            Toast.makeText(this, "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("로그인중입니다. 잠시 기다려 주세요...");
        progressDialog.show();

        //logging in the user
        firebaseAuth.signInWithEmailAndPassword(checkId, checkPw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), ContentActivity.class));
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 실패!", Toast.LENGTH_LONG).show();
                            textviewMessage.setText("로그인 실패 유형\n - password가 맞지 않습니다.\n -서버에러");
                        }
                    }
                });
    }


    //사용자 권한 허용 알림창
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

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

    //클릭이벤트 구현(CheckBox차원)
    CheckBox.OnClickListener checklistener = new CheckBox.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (((CheckBox) view).isChecked()) { // 체크박스 체크 되어 있으면
                // editText에서 아이디와 암호 가져와 PreferenceManager에 저장한다.
                PreferenceManager.setString(getApplicationContext(), "id", editTextEmail.getText().toString()); //id 키값으로 저장
                PreferenceManager.setString(getApplicationContext(), "pw", editTextPassword.getText().toString()); //pw 키값으로 저장
                PreferenceManager.setBoolean(getApplicationContext(), "check", saveIdPassword.isChecked()); //현재 체크박스 상태 값 저장
            } else { //체크박스가 해제되어있으면
                PreferenceManager.setBoolean(getApplicationContext(), "check", saveIdPassword.isChecked()); //현재 체크박스 상태 값 저장
                PreferenceManager.setString(getApplicationContext(), "id", editTextEmail.getText().toString()); //id 키값으로 저장
            }
        }
    };

    //클릭이벤트 구현(View차원)
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == buttonLogin) {
                userLogin();
            }
            if (view == textviewSignup) {
                finish();
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
            if (view == textviewFindPassword) {
                startActivity(new Intent(getApplicationContext(), FindActivity.class));
            }
        }
    };

    private long backKeyPressedTime = 0;
    private Toast toast;

    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            toast.makeText(this, "\\'뒤로\\' 버튼을 한번 더 누르시면 종료됩니다.",Toast.LENGTH_SHORT).show();
            return;
        } else {
            super.onBackPressed();
        }
    }
}
