package com.reva.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Profile_Delete extends AppCompatActivity {
    EditText username, password;
    Button delete;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_delete);
        dbHelper=new DBHelper(this);
        username=findViewById(R.id.etDelAcc);
        password=findViewById(R.id.etDelAccPass);
        delete=findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username=username.getText().toString();
                String Password=password.getText().toString();
                if(dbHelper.checkUser(Username, Password)){
                    boolean isDeleted=dbHelper.deleteUserData(Username);
                    if(isDeleted){
                        Toast.makeText(Profile_Delete.this, "Account has been deleted!", Toast.LENGTH_LONG).show();
                        dbHelper.deleteUserData(Username);
                        Intent intent=new Intent(Profile_Delete.this, Login.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Profile_Delete.this, "Unable to delete!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}