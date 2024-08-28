package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {
    Button withFriend_btn,withAi_btn,play_btn;
    EditText firstName,secondName;
    MediaPlayer media;
    ImageView sound_icon,noSound_icon,language_icon;
    String []languages={"English","Arabic","German","French","Italiano"};
    Boolean sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //set tha language of app
        String lang=SharedPreferenceHelper.getLanguage(getApplicationContext());
        sound=SharedPreferenceHelper.getSoundMode(getApplicationContext());
        setLocale(lang);

        initValues();//set the id for objects

        withFriend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound) {
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
                        if(sound) {
                            //play the sound
                            media = MediaPlayer.create(HomeActivity.this, R.raw.click);
                            media.start();
                        }

                        if(valid()){
                            Intent i = new Intent(HomeActivity.this, GameActivity_pVSP.class);
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
                if(sound) {
                    //play the sound
                    media = MediaPlayer.create(HomeActivity.this, R.raw.click);
                    media.start();
                }

                Intent i=new Intent(HomeActivity.this, GameActivity_pVSP.class);
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

                SharedPreferenceHelper.saveSoundMode(getApplicationContext(),false);
                sound=false;

            }
        });

        noSound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_icon.setVisibility(View.VISIBLE);
                noSound_icon.setVisibility(View.INVISIBLE);


                SharedPreferenceHelper.saveSoundMode(getApplicationContext(),true);
                sound=true;

            }
        });

        language_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound) {
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
            switch (selectedLanguage) {
                case "French":
                    setLocale("fr");
                    language_icon.setImageResource(R.drawable.french_icon);
                    break;
                case "German":
                    setLocale("de");
                    language_icon.setImageResource(R.drawable.german_icon);
                    break;
                case "Arabic":
                    setLocale("ar");
                    language_icon.setImageResource(R.drawable.arabic_icon);

                    break;
                case "Italiano":
                    setLocale("it");
                    language_icon.setImageResource(R.drawable.italy_icon);
                    break;
                default:
                    setLocale("en"); // Default to English
                    language_icon.setImageResource(R.drawable.english_icon);

                    break;
            }
            return true;
        });

        popupMenu.show();
    }
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        // Store the language preference
        SharedPreferenceHelper.saveLanguage(getBaseContext(),languageCode);

        // Restart the activity to apply the language change
       // Intent refresh =getIntent();
        //startActivity(refresh);
        //finish();
    }
}