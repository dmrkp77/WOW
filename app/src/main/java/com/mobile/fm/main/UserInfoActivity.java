package com.mobile.fm.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mobile.fm.R;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

public class UserInfoActivity extends AppCompatActivity {
    private final String TAG = "UserInfoActivity";

    private final String KEY_USERNAME = "username";
    private final String KEY_PASSWORD = "password";
    private final String KEY_REGISTERDATE = "registerDate";

    private ImageButton backbtn;
    private EditText username;
    private EditText password;
    private TextView registerDate;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private DocumentReference docRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //initializig firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        docRef = db.collection("User").document(firebaseUser.getEmail());

        backbtn = (ImageButton) findViewById(R.id.userInfo_backBtn);
        username = (EditText) findViewById(R.id.userInfo_userEditText);
        password = (EditText) findViewById(R.id.userInfo_passwordEditText);
        registerDate = (TextView) findViewById(R.id.userInfo_registerDate);

        backbtn.setOnClickListener(listener);

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            username.setText(documentSnapshot.getString(KEY_USERNAME));
                            password.setText(documentSnapshot.getString(KEY_PASSWORD));
                            registerDate.setText(documentSnapshot.getString(KEY_REGISTERDATE));

//                            Map<String, Object> note = documentSnapshot.getData();
                        } else {
                            Toast.makeText(getApplicationContext(), "존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == backbtn) {
                finish();
            }
        }
    };
}