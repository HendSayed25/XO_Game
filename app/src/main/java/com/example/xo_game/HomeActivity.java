package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    Button withFriend_btn,withAi_btn,play_btn;
    EditText firstName,secondName;
    MediaPlayer media;
    ImageView sound_icon,noSound_icon,language_icon;
    String []languages={"English","Arabic","German","French","Italiano"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initValues();//set the id for objects

        withFriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_icon.getVisibility()==View.VISIBLE) {
                    //play the sound
                    media = MediaPlayer.create(HomeActivity.this, R.raw.click);
                    media.start();
                }

                withAi_btn.setVisibility(View.INVISIBLE);
                withFriend_btn.setVisibility(View.INVISIBLE);
                firstName.setVisibility(View.VISIBLE);
                secondName.setVisibility(View.VISIBLE);
                play_btn.setVisibility(View.VISIBLE);
                play_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(sound_icon.getVisibility()==View.VISIBLE) {
                            //play the sound
                            media = MediaPlayer.create(HomeActivity.this, R.raw.click);
                            media.start();
                        }

                        if(valid()){
                            Intent i = new Intent(HomeActivity.this, GameActivity.class);
                            i.putExtra("firstName", firstName.getText().toString());
                            i.putExtra("secondName", secondName.getText().toString());
                            i.putExtra("Ai",false);
                            startActivity(i);
                        }
                    }
                });


            }
        });

        withAi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_icon.getVisibility()==View.VISIBLE) {
                    //play the sound
                    media = MediaPlayer.create(HomeActivity.this, R.raw.click);
                    media.start();
                }

                Intent i=new Intent(HomeActivity.this,GameActivity.class);
                i.putExtra("firstName", "You");
                i.putExtra("secondName", "Computer");
                i.putExtra("Ai",true);
                startActivity(i);
            }
        });

        sound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play the sound
                media=MediaPlayer.create(HomeActivity.this,R.raw.click);
                media.start();

                sound_icon.setVisibility(View.INVISIBLE);
                noSound_icon.setVisibility(View.VISIBLE);

            }
        });

        noSound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_icon.setVisibility(View.VISIBLE);
                noSound_icon.setVisibility(View.INVISIBLE);

            }
        });

        language_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound_icon.getVisibility()==View.VISIBLE) {
                    //play the sound
                    media = MediaPlayer.create(HomeActivity.this, R.raw.click);
                    media.start();
                }

                showLanguagePopupMenu(v);
            }
        });



    }
    public void initValues(){
        withFriend_btn=findViewById(R.id.withFriend_btn);
        withAi_btn=findViewById(R.id.withAi_btn);
        play_btn=findViewById(R.id.play_btn);
        firstName=findViewById(R.id.firstName);
        secondName = findViewById(R.id.secondName);
        sound_icon=findViewById(R.id.sound_btn);
        noSound_icon=findViewById(R.id.noSound_btn);
        language_icon=findViewById(R.id.language_icon);
    }
    public Boolean valid(){
        boolean ok=true;
        if (firstName.getText().toString().isEmpty()) {
            firstName.setError("Enter First Name");
            ok=false;
        }
        if (secondName.getText().toString().isEmpty()) {
            secondName.setError("Enter Second Name");
            ok=false;
        }
        return ok;
    }
    public void showLanguagePopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, v);
        for (int i = 0; i < languages.length; i++) {
            popupMenu.getMenu().add(0, i, i, languages[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedLanguage = languages[item.getItemId()];
            Toast.makeText(HomeActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
            // Handle language selection here
            return true;
        });

        popupMenu.show();
    }
}