package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button withFriend_btn,withAi_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        withFriend_btn=findViewById(R.id.withFriend_btn);
        withAi_btn=findViewById(R.id.withAi_btn);

        withFriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,NamesActivity.class);
                startActivity(i);
            }
        });

        withAi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this,GameActivity.class);
                startActivity(i);
            }
        });



    }
}