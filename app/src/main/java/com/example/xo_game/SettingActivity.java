package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    ImageView sound_icon,noSound_icon,language_icon;
    String []languages={"English","Arabic","German","French","Italiano"};
    MediaPlayer media;
    Boolean sound=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sound_icon=findViewById(R.id.sound_btn);
        noSound_icon=findViewById(R.id.noSound_btn);
        language_icon=findViewById(R.id.language_icon);


       // getTheIconAndLanguage();

        sound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play the sound
                media= MediaPlayer.create(SettingActivity.this,R.raw.click);
                media.start();

                sound_icon.setVisibility(View.INVISIBLE);
                noSound_icon.setVisibility(View.VISIBLE);

                //change the mood
                SharedPreferenceHelper.saveSoundMode(getApplicationContext(),false);
                sound=false;


            }
        });

        noSound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound_icon.setVisibility(View.VISIBLE);
                noSound_icon.setVisibility(View.INVISIBLE);
                sound_icon.setVisibility(View.INVISIBLE);
                noSound_icon.setVisibility(View.VISIBLE);

                //change the mood
                SharedPreferenceHelper.saveSoundMode(getApplicationContext(),true);
                sound=true;


            }
        });

        language_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if(sound) {
                    //play the sound
                    media = MediaPlayer.create(getApplicationContext(), R.raw.click);
                    media.start();
               // }

                showLanguagePopupMenu(v);
            }
        });
    }

    public void showLanguagePopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(SettingActivity.this, v);
        for (int i = 0; i < languages.length; i++) {
            popupMenu.getMenu().add(0, i, i, languages[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedLanguage = languages[item.getItemId()];
            Toast.makeText(SettingActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
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
        getApplicationContext().getResources().updateConfiguration(config,getApplicationContext().getResources().getDisplayMetrics());
        // Store the language preference

        SharedPreferenceHelper.saveLanguage(getApplicationContext(),languageCode);

        // Restart the activity to apply the language change
        Intent refresh =getIntent();
        startActivity(refresh);
       finish();

    }
    public void getTheIconAndLanguage() {
        String selectedLanguage = SharedPreferenceHelper.getLanguage(getApplicationContext()); // Default to English if no language is set
        Boolean sound=SharedPreferenceHelper.getSoundMode(getApplicationContext());
        //sound icon
        if(sound){
            sound_icon.setVisibility(View.VISIBLE);
            noSound_icon.setVisibility(View.INVISIBLE);
        }else{
            noSound_icon.setVisibility(View.VISIBLE);
            sound_icon.setVisibility(View.INVISIBLE);
        }
        //language
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
    }
}