package com.reva.recyclerview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class WelcomePage extends AppCompatActivity {
    Button updateProfButton, signOutButton, deleteAccountButton, visitLibrary,imageUpdate;
    DBHelper dbHelper;
    ImageView image;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        updateProfButton = findViewById(R.id.btnUpdateProfile);
        signOutButton = findViewById(R.id.btnSignOut);
        deleteAccountButton = findViewById(R.id.btnDeleteProfile);
        visitLibrary = findViewById(R.id.btnLibrary);
        imageUpdate=findViewById(R.id.imageUpdate);
        image=findViewById(R.id.imageView);
        updateProfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, Update_Profile.class);
                startActivity(intent);
            }
        });
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, Profile_Delete.class);
                startActivity(intent);
            }
        });
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomePage.this, Login.class);
                startActivity(intent);
            }
        });
        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(WelcomePage.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        visitLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WelcomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        dbHelper=new DBHelper(WelcomePage.this);
        String username = getIntent().getStringExtra("username");
        Uri uri=data.getData();
        image.setImageURI(uri);
        boolean imageAdded=dbHelper.insertImageData(username,uri);
        if(imageAdded){
            Toast.makeText(WelcomePage.this,"Image Uploaded!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(WelcomePage.this, "Couldn't Upload Image!", Toast.LENGTH_LONG).show();
        }
    }
}