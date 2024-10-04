package com.reva.recyclerview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Forgot_Password extends AppCompatActivity {
    EditText etUserName, etNewPassword, etNewRetypedPass;
    Button resetButton,mailButton;
    DBHelper dbHelper;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        etUserName=findViewById(R.id.resetEtUserName);
        dbHelper=new DBHelper(this);
        resetButton=findViewById(R.id.buttonReset);
        mailButton=findViewById(R.id.mail);
        mailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String stringSenderEmail = "sakethbhavanaka@gmail.com";
                    String stringReceiverEmail = "vishnusukumar2003@gmail.com";
                    String stringPasswordSenderEmail = "swycnvjcoqkugsuy";

                    String stringHost = "smtp.gmail.com";

                    Properties properties = System.getProperties();

                    properties.put("mail.smtp.host", stringHost);
                    properties.put("mail.smtp.port", "465");
                    properties.put("mail.smtp.ssl.enable", "true");
                    properties.put("mail.smtp.auth", "true");

                    javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                        }
                    });

                    MimeMessage mimeMessage = new MimeMessage(session);
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

                    mimeMessage.setSubject("Subject: Android App email");
                    mimeMessage.setText("Your Password is : "+dbHelper.fetchpass(stringReceiverEmail));

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Transport.send(mimeMessage);
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                } catch (AddressException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, newPassword, retypedPassword;
                username=etUserName.getText().toString();
                newPassword=etNewPassword.getText().toString();
                retypedPassword=etNewRetypedPass.getText().toString();
                if(username.isEmpty()){
                    Toast.makeText(Forgot_Password.this, "Please Fill All The Fields!", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if(dbHelper.checkUserName(username)){

                    }
                }

            }
        });

    }
}