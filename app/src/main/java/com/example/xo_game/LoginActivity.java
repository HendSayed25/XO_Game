package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button Login,SignUp;
    UserDatabase db;
    TextView set_Error;
    SharedPreferences sp;
    Boolean check;
    ImageView sound_icon,noSound_icon,language_icon,setting_icon;
    String []languages={"English","Arabic","German","French","Italiano"};
    MediaPlayer media;
    Boolean sound=true;
    String selectedLanguage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initValues();
        //getTheIconAndLanguage();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sound) {
                    //play the sound
                    media = MediaPlayer.create(LoginActivity.this, R.raw.click);
                    media.start();
                }
                //check if valid or not from database
                if(validData()){
                    boolean found=db.CheckEmailAndPassword(email.getText().toString(),password.getText().toString());
                    if(found){
                        Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                        startActivity(i);
                    }else{
                        set_Error.setText("Wrong Email or password ...");
                    }
                }
            }
        });

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(sound) {
                    //play the sound
                    media = MediaPlayer.create(LoginActivity.this, R.raw.click);
                    media.start();
                //}

                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

       /* setting_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,SettingActivity.class);
                startActivity(i);
            }
        });*/

        sound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play the sound
                media=MediaPlayer.create(LoginActivity.this,R.raw.click);
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
                if(sound_icon.getVisibility()==View.VISIBLE) {
                    //play the sound
                    media = MediaPlayer.create(LoginActivity.this, R.raw.click);
                    media.start();
                }

                showLanguagePopupMenu(v);
            }
        });

    }
    public void initValues(){
        email =findViewById(R.id.email_login);
        password=findViewById(R.id.password_login);
        Login=findViewById(R.id.login_btn);
        SignUp=findViewById(R.id.register_btn);
        set_Error =findViewById(R.id.Error);
        sound_icon=findViewById(R.id.sound_btn);
        noSound_icon=findViewById(R.id.noSound_btn);
        language_icon=findViewById(R.id.language_icon);
      //  setting_icon=findViewById(R.id.setting_icon);
        db=new UserDatabase(this,UserDatabase.TABLE_USER,null,UserDatabase.DATABASE_VERSION);
    }
    public boolean validData(){
        boolean ok=true;
        if(email.getText().toString().isEmpty()){
            email.setError("Please Enter Your Email");
            ok=false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Please Enter Your Password");
            ok=false;
        }

        return ok;
    }

    public void showLanguagePopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(LoginActivity.this, v);
        for (int i = 0; i < languages.length; i++) {
            popupMenu.getMenu().add(0, i, i, languages[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedLanguage = languages[item.getItemId()];
            Toast.makeText(LoginActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
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

    public void getTheIconAndLanguage() {
        selectedLanguage = SharedPreferenceHelper.getLanguage(getBaseContext()); // Default to English if no language is set
        sound=SharedPreferenceHelper.getSoundMode(getBaseContext());
        //sound icon
        if(sound){
            sound_icon.setVisibility(View.INVISIBLE);
        }else{
            noSound_icon.setVisibility(View.VISIBLE);
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
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
        // Store the language preference
        SharedPreferenceHelper.saveLanguage(getApplicationContext(),languageCode);

        // Restart the activity to apply the language change
       Intent refresh =getIntent();
       startActivity(refresh);
        finish();

    }
}