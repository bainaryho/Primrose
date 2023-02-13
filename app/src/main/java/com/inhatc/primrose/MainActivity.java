package com.inhatc.primrose;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.Listener {
    TabHost myTabHost = null;
    TabHost.TabSpec myTabSpec;
    private ImageView image_info;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerName;
    private RecyclerView recyclerMean;
    private DatabaseReference mDatabase;
    private FirebaseDatabase Database;
    private FirebaseAuth mAuth;
    private ArrayList<Flower> ArrayList;
    private RecyclerAdapter Adapter;
    private RecyclerAdapter nameAdapter;
    private RecyclerAdapter meanAdapter;
    private RecyclerView.LayoutManager LayoutManager;
    private RecyclerView.LayoutManager NameLayoutManager;
    private RecyclerView.LayoutManager MeanLayoutManager;
    private Button search_name_btn;
    private ArrayList<Flower> snameList;
    private String sname;
    private Button search_mean_btn;
    private ArrayList<Flower> smeanList;
    private ArrayList<Flower> allList;
    private String smean;
    private EditText search_mean;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser(); // 현재 로그인 되어있는 사용자 가져오기
        if(currentUser == null){ // 로그인 되어있는 사용자가 없다면
            Intent intent1 = new Intent(this,LoginActivity.class); // 로그인 화면으로 이동
            startActivity(intent1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView1 = findViewById(R.id.Recycler_all);
        recyclerView1.setHasFixedSize(true); // 리사이클러뷰 활성화
        LayoutManager = new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(LayoutManager);
        Database = FirebaseDatabase.getInstance();
        mDatabase = Database.getReference("Flower"); // Flower 밑의 노드로 경로 지정
        ArrayList = new ArrayList<>();
        snameList = new ArrayList<>();
        smeanList = new ArrayList<>();
        allList = new ArrayList<>();

        // 파이어베이스에 저장 되어있는 꽃의 정보 받기
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){ //파이어베이스 실행이되는 순간 캡쳐
                    Flower flower = dataSnapshot.getValue(Flower.class); // Flower 객체로 값을 받아온다.
                    ArrayList.add(flower); // 리스트에 저장
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // 데이터 불러오기 실패 시
            }
        });

        Adapter = new RecyclerAdapter(ArrayList,this); // 받아온 리스트로 어뎁터에 연결
        Adapter.setListener(this); // 어뎁터의 리스너 생성
        recyclerView1.setAdapter(Adapter); // 리사이클러 어댑터 클래스랑 연결
        //리사이클러 어댑터 클래스는 item.xml에 연결해서 텍스트에 자료넣어주는것

        //Toolbar 생성
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 탭 생성
        myTabHost = (TabHost)findViewById(R.id.tabhost);
        myTabHost.setup();

        myTabSpec = myTabHost.newTabSpec("info").setIndicator("소개").setContent(R.id.tab1);
        myTabHost.addTab(myTabSpec);

        myTabSpec = myTabHost.newTabSpec("all").setIndicator("전체").setContent(R.id.tab2);
        myTabHost.addTab(myTabSpec);

        myTabSpec = myTabHost.newTabSpec("name_search").setIndicator("꽃 검색").setContent(R.id.tab3);
        myTabHost.addTab(myTabSpec);

        myTabSpec = myTabHost.newTabSpec("meaning_search").setIndicator("꽃말"+"\n"+"검색").setContent(R.id.tab4);
        myTabHost.addTab(myTabSpec);

        myTabHost.setCurrentTab(0);

        // 로그인 후 첫번째 탭의 이미지 넣어주기(프림로즈 사진)
        image_info = (ImageView) findViewById(R.id.imageView);
        image_info.setImageResource(R.drawable.info_primrose);

        search_name_btn = findViewById(R.id.searchNameBtn);

        recyclerName = findViewById(R.id.RecyclerNameSearch);
        recyclerName.setHasFixedSize(true);
        NameLayoutManager = new LinearLayoutManager(this);
        recyclerName.setLayoutManager(NameLayoutManager);

        search_name_btn.setOnClickListener(new View.OnClickListener() {
            // 입력받는 방법을 관리하는 Manager객체를 요청하여 InputMethodManager에 반환
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            @Override
            public void onClick(View view) {
                EditText search_name = findViewById(R.id.searchName);
                sname = search_name.getText().toString(); // 검색한 꽃 이름 받아오기

                // 파이어베이스에서 찾기
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){ //파이어베이스 실행이되는 순간 캡쳐
                            Flower flower = dataSnapshot.getValue(Flower.class); // Flower 객체로 정보 받아오기
                            allList.add(flower); // 리스트에 저장
                        }
                        for( Flower f : allList){
                            if(f.getFname().contains(sname)){ // 검색한 단어가 포함되는 이름의 꽃이 있다면
                                snameList.add(f); // 리스트에 저장
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                nameAdapter = new RecyclerAdapter(snameList,MainActivity.this); // snameList와 리사이클러 어뎁터 연결
                nameAdapter.setListener(MainActivity.this); // 리스너 생성
                recyclerName.setAdapter(nameAdapter); //리사이클러뷰의 어뎁터 지정
                nameAdapter.notifyDataSetChanged(); // 리사이클러뷰 내용 변경 알림

                search_name.setText(""); // 검색 단어 초기화
                imm.hideSoftInputFromWindow(search_name.getWindowToken(),0); // 키보드 자동 내림
                allList.clear(); // 리스트 초기화
                snameList.clear();
            }
        });

        search_mean_btn = findViewById(R.id.searchMeanBtn);
        recyclerMean = findViewById(R.id.RecyclerMeaningSearch);
        recyclerMean.setHasFixedSize(true);
        MeanLayoutManager = new LinearLayoutManager(this);
        recyclerMean.setLayoutManager(MeanLayoutManager);

        search_mean_btn.setOnClickListener(new View.OnClickListener() {
            // 입력받는 방법을 관리하는 Manager객체를 요청하여 InputMethodManager에 반환
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            @Override
            public void onClick(View view) {
                EditText search_mean = findViewById(R.id.searchMean);
                smean = search_mean.getText().toString(); // 입력한 단어 받아오기

                // 파이어베이스에서 검색
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){ //파이어베이스 실행이되는 순간 캡쳐
                            Flower flower = dataSnapshot.getValue(Flower.class); // Flower 객체로 정보 받아오기
                                allList.add(flower); // 리스트에 저장
                        }
                        for( Flower f : allList){
                            if(f.getFloriography().contains(smean)){ // 검색한 단어가 포함되는 꽃말이 있다면
                                smeanList.add(f); // 리스트에 저장
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                meanAdapter = new RecyclerAdapter(smeanList,MainActivity.this); // 리스트와 리사이클러 어뎁터 연결
                meanAdapter.setListener(MainActivity.this); // 리스너 생성
                recyclerMean.setAdapter(meanAdapter); //리사이클러 어댑터 클래스랑 연결
                meanAdapter.notifyDataSetChanged(); // 리사이클러뷰 내용 변경 알림

                search_mean.setText("");// 검색 단어 초기화
                imm.hideSoftInputFromWindow(search_mean.getWindowToken(),0); // 키보드 자동 내림
                allList.clear(); // 리스트 초기화
                smeanList.clear();
            }

        });
    }

    @Override
    public void onItemClickedAt(Integer position) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "로그아웃 되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(this,LoginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.map:
                Intent intent3 = new Intent(this,GoogleMapsActivity.class);
                startActivity(intent3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
