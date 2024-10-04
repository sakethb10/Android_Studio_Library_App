package com.reva.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Update_Profile extends AppCompatActivity {
    EditText etExistingUsername, etUpdateName, etUpdateDob, etUpdatePassword;
    Button updatesButton;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        dbHelper=new DBHelper(this);
        etExistingUsername=findViewById(R.id.existingUserName);
        etUpdateName=findViewById(R.id.changeName);
        etUpdateDob=findViewById(R.id.changeDob);
        etUpdatePassword=findViewById(R.id.changePassword);
        updatesButton=findViewById(R.id.changesButton);
        updatesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etExistingUsername.getText().toString();
                String newName = etUpdateName.getText().toString();
                String newDob = etUpdateDob.getText().toString();
                String newPassword = etUpdatePassword.getText().toString();
                boolean flag = true;
                if (!newName.isEmpty()) {
                    flag = dbHelper.updateName(username, newName);
                    if (!flag) {
                        Toast.makeText(Update_Profile.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!newDob.isEmpty()) {
                    flag = dbHelper.updateDob(username, newDob);
                    if (!flag) {
                        Toast.makeText(Update_Profile.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!newPassword.isEmpty()) {
                    flag = dbHelper.updatePassword(username, newPassword);
                    if (!flag) {
                        Toast.makeText(Update_Profile.this, "Update Failed!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(Update_Profile.this, "Updates Made!", Toast.LENGTH_SHORT).show();
                Intent i=new Intent(Update_Profile.this,WelcomePage.class);
                startActivity(i);
            }
        });
    }
}