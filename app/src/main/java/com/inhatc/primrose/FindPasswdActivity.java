package com.inhatc.primrose;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FindPasswdActivity extends AppCompatActivity {

    private EditText passwd_find_name;
    private EditText passwd_find_phone;
    private EditText passwd_find_email;
    private String FPname;
    private String FPphone;
    private String FPemail;
    private String my_passwd = "";
    private Button find_passwd_btn;
    private TextView my_passwd_TV;
    private Button passwd_to_login_btn;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.passwd_find);

        passwd_find_email = findViewById(R.id.passwdFindEmail);
        passwd_find_name = findViewById(R.id.passwdFindName);
        passwd_find_phone = findViewById(R.id.passwdFindPhone);

        find_passwd_btn = findViewById(R.id.passwdFindBtn);
        passwd_to_login_btn = findViewById(R.id.passwdFindAfterBtn);
        my_passwd_TV = findViewById(R.id.passwdFindTV);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //확인 버튼 클릭 시
        find_passwd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력받은 값 가져오기
                FPname = passwd_find_name.getText().toString().trim();
                FPphone = passwd_find_phone.getText().toString().trim();
                FPemail = passwd_find_email.getText().toString().trim();


                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        my_passwd = "";
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            // 만약에 Info/name의 값이 입력받은 실명과 같고, Info/phone 의 값이 입력받은 전화번호와 같다면
                            if((fileSnapshot.child("Info/name").getValue(String.class).toString()).equals(FPname)
                                    && (fileSnapshot.child("Info/phone").getValue(String.class)).equals(FPphone)
                                    && (fileSnapshot.child("Info/email").getValue(String.class)).equals(FPemail))
                            {
                                // my_passwd 에 저장
                                my_passwd = fileSnapshot.child("Info/passwd").getValue(String.class);
                                break;
                            }
                        }
                        // 만약 my_passwd 이 비어있다면 (회원 정보가 존재한다면)
                        if(my_passwd.equals("")){
                            my_passwd_TV.setText("회원 정보가 존재하지 않습니다.");
                        }else{ // my_passwd 이 ""이 아니라면 (회원 정보가 존재한다면)
                            my_passwd_TV.setText("회원님의 비밀번호는  "+my_passwd+"  입니다.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        // 로그인 화면으로 돌아가기 버튼 클릭 시
        passwd_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 비밀번호 찾기 액티비티 종료(로그인 화면으로 돌아가기)
            }
        });


    }
}
