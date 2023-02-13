package com.inhatc.primrose;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private Button btn_findEmail;
    private Button btn_findPasswd;
    private EditText login_email;
    private EditText login_passwd;
    private String lemail;
    private String lpasswd;
    private Button btn_join;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        firebaseAuth = firebaseAuth.getInstance(); // firebaseAuth의 인스턴스를 가져옴

        btn_login = findViewById(R.id.loginBtn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_email = findViewById(R.id.editTextEmail);
                login_passwd = findViewById(R.id.editTextPassword);

                //String형 변수로 입력 값 받기
                lemail = login_email.getText().toString().trim();
                lpasswd = login_passwd.getText().toString().trim();

                // 파이어베이스에서 제공하는 로그인 메소드
                firebaseAuth.signInWithEmailAndPassword(lemail, lpasswd)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) { // 로그인 성공 시
                                    Intent intent = new Intent(LoginActivity.this, RecaptchaActivity.class);
                                    startActivity(intent);
                                } else { // 로그인 실패 시
                                    Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요!", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        btn_join = findViewById(R.id.joinBtn);
        btn_findEmail = findViewById(R.id.findMyEmailBtn);
        btn_findPasswd = findViewById(R.id.findMyPasswdBtn);

        // 회원가입 버튼 클릭 시
        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent joinIntent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(joinIntent);
            }
        });

        //이메일 찾기 버튼 클릭 시
        btn_findEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FindEmailActivity 로 이동
                Intent findEmailIntent = new Intent(LoginActivity.this, FindEmailActivity.class);
                startActivity(findEmailIntent);
            }
        });

        //비밀번호 찾기 버튼 클릭 시
        btn_findPasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FindPasswdActivity 로 이동
                Intent findPasswdIntent = new Intent(LoginActivity.this, FindPasswdActivity.class);
                startActivity(findPasswdIntent);
            }
        });
    }
}
