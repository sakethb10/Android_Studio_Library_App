package com.reva.recyclerview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddNew extends AppCompatActivity {
    DBHelper dbHelper;
    EditText title, author, pages;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        title=findViewById(R.id.textTitle);
        author=findViewById(R.id.textAuthor);
        pages=findViewById(R.id.textPages);
        add=findViewById(R.id.buttonAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper=new DBHelper(AddNew.this);
                String Title=title.getText().toString().trim();
                String Author=author.getText().toString().trim();
                int Pages=Integer.valueOf(pages.getText().toString().trim());
                dbHelper.addBook(Title, Author, Pages);
                Intent intent=new Intent(AddNew.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}