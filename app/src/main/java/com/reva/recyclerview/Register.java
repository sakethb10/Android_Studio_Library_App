package com.reva.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    EditText etName, etDob, etUserName, etPass, etRepPass;
    Button btnRegister;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper=new DBHelper(this);
        etName=findViewById(R.id.editTextName);
        etDob=findViewById(R.id.editTextDOB);
        etUserName=findViewById(R.id.editTextUsername);
        etPass=findViewById(R.id.editTextPassword);
        etRepPass=findViewById(R.id.editTextRetypePassword);
        btnRegister=findViewById(R.id.buttonRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,dob,username,password,repeatPassword;
                name=etName.getText().toString();
                dob=etDob.getText().toString();
                username=etUserName.getText().toString();
                password=etPass.getText().toString();
                repeatPassword=etRepPass.getText().toString();
                if(name.isEmpty()||dob.isEmpty()||username.isEmpty()||password.isEmpty()||repeatPassword.isEmpty()){
                    Toast.makeText(Register.this, "Please fill all the fields!", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if(password.equals(repeatPassword)){
                        if(dbHelper.checkUserName(username)){
                            Toast.makeText(Register.this, "Username Taken!", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Proceed
                        boolean registerUser=dbHelper.insertUserData(username,name,dob,password);
                        if(registerUser){
                            Toast.makeText(Register.this, "Registration Successful!", Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(Register.this, Login.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Register.this, "Registration Failed!", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    else{
                        Toast.makeText(Register.this, "Passwords don't match!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        });
    }
    Pattern email = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    private boolean isValid(String userid){
        if(!email.matcher(userid).matches()){
            return false;
        }
        return true;
    }

}