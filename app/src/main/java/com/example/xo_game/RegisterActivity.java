package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,password,confirmPassword;
    Button register_btn;
    UserDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.Name_register);
        email=findViewById(R.id.email_register);
        password=findViewById(R.id.password_register);
        confirmPassword=findViewById(R.id.confirm_password_register);
        register_btn=findViewById(R.id.register_btn);
        db=new UserDatabase(this,UserDatabase.TABLE_USER,null,UserDatabase.DATABASE_VERSION);

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
        if(password.getText().toString().length()<8||(!isValidPassword(password.getText().toString()))){
            password.setError("please enter strong password not less than 6 numbers,one capital character and special character");
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
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}