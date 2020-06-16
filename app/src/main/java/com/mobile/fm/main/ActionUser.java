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
import com.mobile.fm.login.SignUpActivity;

import javax.annotation.Nullable;

public class ActionUser extends Fragment {
    ViewGroup viewGroup;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    private Button buttonLogout;
    private TextView textViewUserEmail;
    private TextView textivewDelete;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                              @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_user, container,false);
        textViewUserEmail = (TextView) viewGroup.findViewById(R.id.textviewUserEmail);
        buttonLogout = (Button) viewGroup.findViewById(R.id.buttonLogout);
        textivewDelete = (TextView) viewGroup.findViewById(R.id.textviewDelete);

        buttonLogout.setOnClickListener(listener);
        textivewDelete.setOnClickListener(listener);

        return viewGroup;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //로그아웃 클릭 시 로그아웃
            if(view == buttonLogout){
                Intent intent = new Intent(getContext(),ContentActivity.class);
                intent.putExtra("로그아웃",true);
                startActivity(intent);
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
        }
    };
}