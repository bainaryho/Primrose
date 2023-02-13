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

public class FindEmailActivity extends AppCompatActivity {

    private EditText email_find_name;
    private EditText email_find_phone;
    private String FEname;
    private String FEphone;
    private String my_email = "";
    private Button find_email_btn;
    private TextView my_email_TV;
    private Button email_to_login_btn;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_find);

        email_find_name = findViewById(R.id.emailFindName);
        email_find_phone = findViewById(R.id.emailFindPhone);
        find_email_btn = findViewById(R.id.emailFindBtn);
        my_email_TV = findViewById(R.id.emailFindTV);
        email_to_login_btn = findViewById(R.id.emailFindAfterBtn);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //확인 버튼 클릭 시
        find_email_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 입력받은 값 가져오기
                FEname = email_find_name.getText().toString().trim();
                FEphone = email_find_phone.getText().toString().trim();

                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        my_email = "";
                        for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                            // 만약에 Info/name의 값이 입력받은 실명과 같고, Info/phone 의 값이 입력받은 전화번호와 같다면
                            if((fileSnapshot.child("Info/name").getValue(String.class).toString()).equals(FEname)
                                    && (fileSnapshot.child("Info/phone").getValue(String.class).toString()).equals(FEphone))
                            {
                                // my_email 에 저장
                                my_email = fileSnapshot.child("Info/email").getValue(String.class);
                                break;
                            }
                        }
                        // 만약 my_email 이 비어있다면 (회원 정보가 존재한다면)
                        if(my_email.equals("")){
                            my_email_TV.setText("회원 정보가 존재하지 않습니다.");
                        }else{ // my_email이 ""이 아니라면 (회원 정보가 존재한다면)
                            my_email_TV.setText("회원님의 이메일은  "+my_email+"  입니다.");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });
        // 로그인 화면으로 돌아가기 버튼 클릭 시
        email_to_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // 이메일 찾기 액티비티 종료(로그인 화면으로 돌아가기)
            }
        });
    }
}
