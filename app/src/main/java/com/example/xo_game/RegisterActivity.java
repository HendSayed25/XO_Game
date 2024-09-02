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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,password,confirmPassword;
    Button register_btn;
    UserDatabase db;
    MediaPlayer media;
    Boolean sound;
    ImageView sound_icon,noSound_icon,language_icon,back_icon;
    String []languages={"English","Arabic","German","French","Italiano"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initValues();//set id for objects

        //set tha language of app
        String lang=SharedPreferenceHelper.getLanguage(getApplicationContext());
        sound=SharedPreferenceHelper.getSoundMode(getBaseContext());
        getIconAndLanguage(lang);


        //set the mode of sound
        sound=SharedPreferenceHelper.getSoundMode(getApplicationContext());
        if(sound){
            sound_icon.setVisibility(View.VISIBLE);
            noSound_icon.setVisibility(View.INVISIBLE);
        }else{
            sound_icon.setVisibility(View.INVISIBLE);
            noSound_icon.setVisibility(View.VISIBLE);
        }


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Valid()){
                    User user=new User(name.getText().toString(),email.getText().toString(),password.getText().toString());
                    db.insertUser(user);
                    Toast.makeText(RegisterActivity.this,"Register Successfully...",Toast.LENGTH_LONG).show();
                    //back to login activity
                    Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        sound_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //play the sound
                media=MediaPlayer.create(RegisterActivity.this,R.raw.click);
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
                    media = MediaPlayer.create(RegisterActivity.this, R.raw.click);
                    media.start();
                }

                showLanguagePopupMenu(v);
            }
        });
        back_icon.setOnClickListener(new View.OnClickListener() { //return to last activity
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(sound) {
            //play the sound
            media.start();
            media = MediaPlayer.create(RegisterActivity.this, R.raw.login_or_register);
            media.isLooping();
        }

    }

    @Override
    protected void onStop() { //how stop sound when click to mute it??????
        super.onStop();
            //play the sound
            media.stop();
    }

    public void initValues(){
        name=findViewById(R.id.Name_register);
        email=findViewById(R.id.email_register);
        password=findViewById(R.id.password_register);
        confirmPassword=findViewById(R.id.confirm_password_register);
        register_btn=findViewById(R.id.register_btn);
        sound_icon=findViewById(R.id.sound_btn);
        noSound_icon=findViewById(R.id.noSound_btn);
        language_icon=findViewById(R.id.language_icon);
        back_icon=findViewById(R.id.back_icon);
        db=new UserDatabase(this,UserDatabase.TABLE_USER,null,UserDatabase.DATABASE_VERSION);
    }
    public void showLanguagePopupMenu(View v){
        PopupMenu popupMenu = new PopupMenu(RegisterActivity.this, v);
        for (int i = 0; i < languages.length; i++) {
            popupMenu.getMenu().add(0, i, i, languages[i]);
        }

        popupMenu.setOnMenuItemClickListener(item -> {
            String selectedLanguage = languages[item.getItemId()];
            Toast.makeText(RegisterActivity.this, "Selected: " + selectedLanguage, Toast.LENGTH_SHORT).show();
            // Handle language selection here
            switch (selectedLanguage) {
                case "French":
                    setLocale("fr");
                    break;
                case "German":
                    setLocale("de");
                    break;
                case "Arabic":
                    setLocale("ar");
                    break;
                case "Italiano":
                    setLocale("it");
                    break;
                default:
                    setLocale("en"); // Default to English
                    break;
            }
            return true;
        });

        popupMenu.show();
    }
    public boolean Valid(){
        boolean valid=true;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(name.getText().toString().isEmpty()){
            name.setError("please Enter the Name");
            valid=false;
        }
        if(email.getText().toString().isEmpty()){
            email.setError("please Enter the Email");
            valid=false;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("please Enter password");
            valid=false;
        }
        if(confirmPassword.getText().toString().isEmpty()){
            confirmPassword.setError("please confirm the password");
            valid=false;
        }

        if(!password.getText().toString().equals(confirmPassword.getText().toString())){
            password.setError("Password not match the confirmed password");
            confirmPassword.setError("not match the Password");
            valid=false;
        }
        if(password.getText().toString().length()<5||(!isValidPassword(password.getText().toString()))){
            password.setError("please enter password not less than 5 character");
            valid=false;
        }
        if(!email.getText().toString().matches(emailPattern)){
            email.setError("Please Enter correct Email");
        }

        boolean existEmail=db.CheckEmail(email.getText().toString());
        if(existEmail){
            email.setError("This email used before");
            valid=false;
        }

        return valid;
    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());

        // Store the language preference
        SharedPreferenceHelper.saveLanguage(getBaseContext(),languageCode);

        //change the icon of language
        switch (languageCode) {
            case "fr":
                language_icon.setImageResource(R.drawable.french_icon);
                break;
            case "de":
                language_icon.setImageResource(R.drawable.german_icon);
                break;
            case "ar":
                language_icon.setImageResource(R.drawable.arabic_icon);
                break;
            case "it":
                language_icon.setImageResource(R.drawable.italy_icon);
                break;
            default:
                // Default to English
                language_icon.setImageResource(R.drawable.english_icon);
                break;
        }

        // Restart the activity to apply the language change
        Intent refresh =getIntent();
        finish();
        startActivity(refresh);


    }
    public void getIconAndLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getApplicationContext().getResources().updateConfiguration(config,getApplicationContext().getResources().getDisplayMetrics());

        //change the icon of language
        switch (languageCode) {
            case "fr":
                language_icon.setImageResource(R.drawable.french_icon);
                break;
            case "de":
                language_icon.setImageResource(R.drawable.german_icon);
                break;
            case "ar":
                language_icon.setImageResource(R.drawable.arabic_icon);
                break;
            case "it":
                language_icon.setImageResource(R.drawable.italy_icon);
                break;
            default:
                // Default to English
                language_icon.setImageResource(R.drawable.english_icon);
                break;
        }

    }


}