package com.reva.recyclerview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Button addBtn;
    DBHelper dbHelper;
    ArrayList<String> book_id, book_title, book_author, book_pages;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        addBtn=findViewById(R.id.addButton);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, AddNew.class);
                startActivity(intent);
            }
        });
        dbHelper=new DBHelper(MainActivity.this);
        book_id=new ArrayList<>();
        book_title=new ArrayList<>();
        book_author=new ArrayList<>();
        book_pages=new ArrayList<>();
        storeDataInArrays();
        customAdapter=new CustomAdapter(MainActivity.this, book_id, book_title, book_author, book_pages);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }
    void storeDataInArrays(){
        Cursor cursor=dbHelper.readAll();
        if(cursor.getCount()==0){
            Toast.makeText(MainActivity.this, "No Data!", Toast.LENGTH_LONG).show();
        }
        else{
            while(cursor.moveToNext()){
                book_id.add(cursor.getString(0));
                book_title.add(cursor.getString(1));
                book_author.add(cursor.getString(2));
                book_pages.add(cursor.getString(3));
            }
        }
    }
}