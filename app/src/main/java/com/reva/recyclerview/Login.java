package com.reva.recyclerview;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Login extends AppCompatActivity {
    EditText etUserName, etPassword;
    Button loginButton, registerButton, forgotPassButton,report;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper=new DBHelper(this);
        etUserName=findViewById(R.id.loginUsername);
        etPassword=findViewById(R.id.loginPassword);
        loginButton=findViewById(R.id.buttonLogin);
        registerButton=findViewById(R.id.btnReg);
        forgotPassButton=findViewById(R.id.btnForgotPass);
        report=findViewById(R.id.report);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username,password;
                username=etUserName.getText().toString();
                password=etPassword.getText().toString();
                boolean isLoggedIn=dbHelper.checkUser(username, password);
                if(dbHelper.checkUserName(username)){
                    if(!isLoggedIn){
                        Toast.makeText(Login.this, "Incorrect Password!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(isLoggedIn){
                    Toast.makeText(Login.this, "Successfully Logged In!", Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(Login.this, WelcomePage.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Login.this, "User Doesn't Exist!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        forgotPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this, Forgot_Password.class);
                startActivity(intent);
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfDocument document = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
                PdfDocument.Page page = document.startPage(pageInfo);

                // Retrieve data from the SQLite database
                StringBuilder reportData = new StringBuilder();
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM users", null);
                if (cursor.moveToFirst()) {
                    do {
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            reportData.append("username=");
                            // Append the value of each column to reportData
                            reportData.append(cursor.getString(i)).append("\n");
                            i++;
                            reportData.append("name");
                            // Append the value of each column to reportData
                            reportData.append(cursor.getString(i)).append("\n");
                            i++;
                            reportData.append("dob=");
                            // Append the value of each column to reportData
                            reportData.append(cursor.getString(i)).append("\n");
                            i++;
                            reportData.append("password=");
                            // Append the value of each column to reportData
                            reportData.append(cursor.getString(i)).append("\n");
                            i++;
                            reportData.append("----------------------------\n");
                        }
                        // Append a new line after each row
                        reportData.append("\n");
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

                // Write data to PDF
                Paint paint = new Paint();
                paint.setColor(Color.BLACK);
                paint.setTextSize(12);
                float x = 10, y = 25; // Starting position
                String[] lines = reportData.toString().split("\n");
                for (String line : lines) {
                    page.getCanvas().drawText(line, x, y, paint);
                    y += paint.descent() - paint.ascent();
                }

                document.finishPage(page);

                // Save PDF file to the "Downloads" directory
                try {
                    File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File pdfFile = new File(directory, "report.pdf");
                    document.writeTo(new FileOutputStream(pdfFile));
                    document.close();

                    Toast.makeText(Login.this, "PDF report generated", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Error generating PDF report", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}