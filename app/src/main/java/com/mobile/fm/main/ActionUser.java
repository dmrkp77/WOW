package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobile.fm.R;
import com.mobile.fm.login.LoginActivity;
import com.mobile.fm.login.SignUpActivity;

import javax.annotation.Nullable;

public class ActionUser extends Fragment {
    ViewGroup viewGroup;

    ContentActivity activity;

    public ActionUser(Context context) {
        this.activity = (ContentActivity) context;
    }

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private ImageView userPicture;
    private Button buttonUserInfo;
    private Button buttonLogout;
    private TextView textViewUserEmail;
    private TextView textivewDelete;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_user, container,false);
        textViewUserEmail = (TextView) viewGroup.findViewById(R.id.textviewUserEmail);
        buttonUserInfo = (Button) viewGroup.findViewById(R.id.buttonUserInfo);
        buttonLogout = (Button) viewGroup.findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) viewGroup.findViewById(R.id.textviewDelete);

        buttonUserInfo.setOnClickListener(listener);
        buttonLogout.setOnClickListener(listener);
        textivewDelete.setOnClickListener(listener);


        //textViewUserEmail의 내용을 변경해 준다.
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        textViewUserEmail.setText(user.getEmail());

        return viewGroup;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if(view == buttonUserInfo){
                activity.startActivity(new Intent(getContext(), UserInfoActivity.class));
            }

            //로그아웃 클릭 시 로그아웃
            if(view == buttonLogout){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("로그아웃 하시겠습니까?").setCancelable(false).
                        setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                activity.Logout();
//                                Intent intent = new Intent(getContext(),ContentActivity.class);
//                                intent.putExtra("로그아웃",true);
//                                Toast.makeText(getContext(),"로그아웃 하였습니다.", Toast.LENGTH_LONG).show();
//                                startActivity(intent);
                            }
                        });

                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) { }});
                alert_confirm.show();
            }

            //계정삭제 버튼 추가
            if (view == textivewDelete) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getContext());
                alert_confirm.setMessage("정말 계정을 삭제 할까요?").setCancelable(false).

                        setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getContext(), "계정이 삭제 되었습니다.", Toast.LENGTH_LONG).show();
                                                //finish();
                                                startActivity(new Intent(getContext(), LoginActivity.class));
                                            }
                                        });
                            }
                        });

                alert_confirm.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }});
                alert_confirm.show();
            }
        }
    };
}