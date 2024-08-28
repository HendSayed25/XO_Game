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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //set tha language of app
        String lang=SharedPreferenceHelper.getLanguage(getApplicationContext());
        setLocale(lang);

        initValues();//set id for objects


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




    }

    @Override
    protected void onResume() {
        super.onResume();
            //play the sound
            media = MediaPlayer.create(RegisterActivity.this, R.raw.login_or_register);
            media.start();

    }

    @Override
    protected void onStop() { //how stop sound when click to moute it??????
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
        db=new UserDatabase(this,UserDatabase.TABLE_USER,null,UserDatabase.DATABASE_VERSION);
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
        getBaseContext().getResources().updateConfiguration(config,getApplicationContext().getResources().getDisplayMetrics());

        // Store the language preference
        SharedPreferenceHelper.saveLanguage(getApplicationContext(),languageCode);

        // Restart the activity to apply the language change
       /* Intent refresh = new Intent(this, RegisterActivity.class);
        startActivity(refresh);
        finish();*/
    }

}