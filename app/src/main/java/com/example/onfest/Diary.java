package com.example.onfest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Date;

public class Diary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_diary);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_note);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.menu_home) {
                    startActivity(new Intent(Diary.this, Home.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_explore) {
                    startActivity(new Intent(Diary.this, Home.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_snippet) {
                    startActivity(new Intent(Diary.this, Home.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_note) {
                    startActivity(new Intent(Diary.this, Diary.class));
                    finish();
                    return true;
                } else if (id == R.id.menu_account) {
                    startActivity(new Intent(Diary.this, Home.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}