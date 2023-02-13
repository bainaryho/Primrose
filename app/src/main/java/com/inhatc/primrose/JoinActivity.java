package com.inhatc.primrose;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {

    private EditText joinEmail;
    private EditText joinPasswd1;
    private EditText joinPasswd2;
    private EditText joinName;
    private EditText joinPhone;
    private Button join_confirm_btn;
    private String jemail;
    private String jpasswd;
    private String conPasswd;
    private String jname;
    private String jphone;
    private DatabaseReference mDatabase;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join);

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        joinEmail = findViewById(R.id.joinEmail);
        joinPasswd1 = findViewById(R.id.joinpassword);
        joinPasswd2 = findViewById(R.id.confirmPassword);
        joinName = findViewById(R.id.joinName);
        joinPhone = findViewById(R.id.joinPhone);
        join_confirm_btn = findViewById(R.id.joinConfirmBtn);

        // 회원 가입 버튼 눌렀을 때
        join_confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 입력한 값 받아오기
                jemail = joinEmail.getText().toString();
                jpasswd = joinPasswd1.getText().toString();
                conPasswd = joinPasswd2.getText().toString();
                jname = joinName.getText().toString();
                jphone = joinPhone.getText().toString();

                //1차 비밀번호와 2차 비밀번호가 다르면
                if(!jpasswd.equals(conPasswd)){
                    Toast.makeText(JoinActivity.this, "비밀번호를 확인해주세요!", Toast.LENGTH_LONG ).show();
                    return;
                } else { // 1차 비밀번호와 2차 비밀번호가 같다면
                    firebaseAuth = FirebaseAuth.getInstance();

                    // 파이어베이스에서 제공하는 회원 가입 메소드
                    firebaseAuth.createUserWithEmailAndPassword(jemail, conPasswd)
                            .addOnCompleteListener(JoinActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // 회원 가입 성공 시
                                    if (task.isSuccessful()) {
                                        String cu = firebaseAuth.getUid(); // 가입자의 고유 uid 받기
                                        User userdata = new User(jemail, conPasswd, jname, jphone); // User 타입으로 객체 생성
                                        mDatabase.child(cu).child("Info").setValue(userdata); // 받은 uid 값/Info 아래에 생성한 객체 값 널기
                                        Toast.makeText(JoinActivity.this, "회원가입 성공! 로그인 해주세요.", Toast.LENGTH_LONG).show();
                                        // 로그인 화면으로 이동
                                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else { // 회원 가입 실패 시
                                        Toast.makeText(JoinActivity.this, "입력 사항을 확인해주세요!", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            });
                }
            }
        });
    }
}
