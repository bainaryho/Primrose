package com.inhatc.primrose;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

public class RecaptchaActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    CheckBox checkBox;
    GoogleApiClient googleApiClient;

    //사이트키
    String SiteKey = "6Le1oWcgAAAAAJ5EOVZKLtgSt4pCmfuOowsjaBui";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recaptcha);

        checkBox = findViewById(R.id.check_box);
        googleApiClient = new GoogleApiClient.Builder(this).addApi(SafetyNet.API)
                .addConnectionCallbacks(RecaptchaActivity.this)
                .build();
        googleApiClient.connect();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //체크박스 눌림여부 판단
                if(checkBox.isChecked()){
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if((status != null) && status.isSuccess()){
                                        //텍스트박스 컬러 체인지
                                        checkBox.setTextColor(Color.GREEN);
                                        //성공메세지 출력
                                        Toast.makeText(RecaptchaActivity.this, "로그인 성공!", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(RecaptchaActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }else{
                    //디폴트 체크박스 텍스트 컬러
                    checkBox.setTextColor(Color.BLACK);
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
