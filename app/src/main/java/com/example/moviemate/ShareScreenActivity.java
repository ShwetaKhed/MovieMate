package com.example.moviemate;


import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.moviemate.databinding.ShareScreenBinding;
import com.example.moviemate.model.Movie;
import com.example.moviemate.service.FacebookMessengerService;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class ShareScreenActivity extends AppCompatActivity {
    private ShareScreenBinding binding;

    Button sendButton;

    Movie movie;

    FacebookMessengerService facebookMessengerService = new FacebookMessengerService();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ShareScreenBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.dark_purple)));
        movie = (Movie) getIntent().getSerializableExtra("movie");
        sendButton = binding.emailButton;

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText rEmail = findViewById(R.id.r_email);
                EditText sendMessage = findViewById(R.id.message);

                String rEmailVal = rEmail.getText().toString().trim();
                String sendMessageVal = sendMessage.getText().toString().trim();
                if(TextUtils.isEmpty(rEmailVal)) {
                    rEmail.setError( "Email is required!" );
                    toastMsg("Please enter Email");
                    return;
                }
                final String username = "student.monash6@gmail.com";
                final String password = "znclnpumxvixlerk";
                Properties properties = new Properties();
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                Session session = Session.getInstance(properties,
                        new javax.mail.Authenticator(){
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });
                try{
                    String email = "student.monash6@gmail.com";
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(rEmailVal));
                    message.setSubject("Want to watch the movie:" + movie.getOriginalTitle());

                    MimeMultipart multipart = new MimeMultipart();
                    MimeBodyPart textPart = new MimeBodyPart();
                    textPart.setText(sendMessageVal);

                    MimeBodyPart imagePart = new MimeBodyPart();
                    imagePart.setContent("<br><img src=\"" + movie.getPosterPath() + "\">", "text/html");

                    multipart.addBodyPart(textPart);
                    multipart.addBodyPart(imagePart);

                    message.setContent(multipart);
                    Transport.send(message);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShareScreenActivity.this);
                    builder.setTitle("Email Sent Successfully.");
                    builder.setPositiveButton("OK", (DialogInterface.OnClickListener) (dialog, which) -> {
                        finish();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();

                } catch (AddressException e) {
                    throw new RuntimeException(e);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }

            }
        });
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding.messengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textMessage = binding.message.getText().toString() + " " + movie.getOriginalTitle() + ", coming out on " + movie.getReleaseDate();
                String recipientId = "6611845722169325";
                facebookMessengerService.sendMessage(recipientId, textMessage);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void toastMsg(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
