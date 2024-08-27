package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NamesActivity extends AppCompatActivity {
    EditText firstName,secondName;
    Button play_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names);

        firstName=findViewById(R.id.firstName);
        secondName = findViewById(R.id.secondName);
        play_btn=findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(NamesActivity.this,GameActivity.class);
                i.putExtra("firstName",firstName.getText().toString());
                i.putExtra("secondName",secondName.getText().toString());
                startActivity(i);
            }
        });
    }
}