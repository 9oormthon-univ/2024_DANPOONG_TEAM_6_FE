package com.example.onfest;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MakeProfile extends AppCompatActivity {

    Button submitButton;
    ImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        submitButton = findViewById(R.id.BtnSm);
        profile_image = findViewById(R.id.profile_image);
        EditText nicknameInput = findViewById(R.id.nickname_input);

        //상태바 툴바과 색상 맞추기
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.Primary, getTheme()));
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profile_image.getVisibility() == View.VISIBLE||nicknameInput.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(v, "프로필사진과 닉네임을 입력하세요", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(android.graphics.Color.parseColor("#E32D2D")); // 배경 색상 설정
                    snackbar.setTextColor(android.graphics.Color.parseColor("#FFFFFF"));
                    snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
            }
        });

    }
}