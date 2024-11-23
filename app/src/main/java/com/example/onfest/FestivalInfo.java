package com.example.onfest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class FestivalInfo extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_festival_info);
        imageView = findViewById(R.id.festival_photo);
        // 시스템 바를 고려하여 뷰 패딩을 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrofit 객체 생성
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://3.35.29.16:8080/")  // 실제 API URL을 입력하세요
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Retrofit API 서비스 생성
        OneApiService service = retrofit.create(OneApiService.class);

        // ID 39로 요청
        Call<Festival> call = service.getFestivalById(39L);
        call.enqueue(new Callback<Festival>() {
            @Override
            public void onResponse(Call<Festival> call, Response<Festival> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Festival festival = response.body();
                    // 축제 이미지 URL을 가져옴
                    String imageUrl = festival.getFestivalImg();

                    // 이미지 뷰에 이미지 로딩 // 이미지뷰 ID에 맞게 수정
                    Glide.with(FestivalInfo.this)
                            .load(imageUrl)  // 서버에서 받은 이미지 URL
                            .into(imageView);
                    // 축제 정보 처리
                    Log.d("FestivalData", "ID: " + festival.getId());
                    Log.d("FestivalData", "Title: " + festival.getTitle());
                    Log.d("FestivalData", "Image URL: " + festival.getFestivalImg());
                    Log.d("FestivalData", "Start Date: " + festival.getStart());
                    Log.d("FestivalData", "End Date: " + festival.getEnd());
                    Log.d("FestivalData", "Body: " + festival.getBody());
                    Log.d("FestivalData", "Region: " + festival.getRegion());
                    Log.d("FestivalData", "Location: " + festival.getLocation());
                } else {
                    Log.e("FestivalData", "Failed to fetch data. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Festival> call, Throwable t) {
                Log.e("FestivalData", "Network Error: " + t.getMessage());
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_snippet);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    startActivity(new Intent(FestivalInfo.this, Home.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_explore) {
                    startActivity(new Intent(FestivalInfo.this, Home.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_snippet) {
                    startActivity(new Intent(FestivalInfo.this, FestivalInfo.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_note) {
                    startActivity(new Intent(FestivalInfo.this, Diary.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_account) {
                    startActivity(new Intent(FestivalInfo.this, Home.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    // Festival 응답을 받을 데이터 모델 클래스
    public class Festival {
        private long id;
        private String title;
        private String festivalImg;
        private String start;
        private String end;
        private String body;
        private String region;
        private String location;

        // Getter & Setter
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getFestivalImg() {
            return festivalImg;
        }

        public void setFestivalImg(String festivalImg) {
            this.festivalImg = festivalImg;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }
    }

    // Retrofit API 서비스 인터페이스
    interface OneApiService {
        @GET("/festival/{id}")
        Call<Festival> getFestivalById(@Path("id") long id);
    }
}