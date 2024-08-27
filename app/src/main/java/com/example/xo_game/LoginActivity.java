package com.example.xo_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    Button Login,SignUp;
    UserDatabase db;
    TextView set_Error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email =findViewById(R.id.email_login);
        password=findViewById(R.id.password_login);
        Login=findViewById(R.id.login_btn);
        SignUp=findViewById(R.id.register_btn);
        set_Error =findViewById(R.id.Error);
        db=new UserDatabase(this,UserDatabase.TABLE_USER,null,UserDatabase.DATABASE_VERSION);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

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
}