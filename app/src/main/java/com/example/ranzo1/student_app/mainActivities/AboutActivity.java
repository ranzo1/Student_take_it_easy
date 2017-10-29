package com.example.ranzo1.student_app.mainActivities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.example.ranzo1.student_app.R;
import com.google.android.gms.maps.model.Circle;
import com.mikhaellopez.circularimageview.CircularImageView;

public class AboutActivity extends AppCompatActivity {


    private MediaPlayer player;
    private Button btnInstagram,btnFacebook,btnEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_about);
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        CircularImageView circularImageView=(CircularImageView)findViewById(R.id.circularImageViewJa);

        btnInstagram=(Button)findViewById(R.id.instagBtnBtn);
        btnEmail=(Button)findViewById(R.id.gmailBtn);
        btnFacebook=(Button)findViewById(R.id.facebookBtn);


        btnInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.instagram.com/ranzo1/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ "protic80@gmail.com"});
                //email.putExtra(Intent.EXTRA_SUBJECT, editTextNaziv.getText().toString());//Naslov mail
                //email.putExtra(Intent.EXTRA_TEXT, editTextRecept.getText().toString());//Text mail

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("https://www.facebook.com/zoran.protic.73"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });



       /* player=MediaPlayer.create(AboutActivity.this,R.raw.mysound);
        player.start();*/







    }
    public void didTapButton(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_slow);
        //myAnim.setStartOffset(450);
        view.startAnimation(myAnim);
    }

    public void didTapButton2(View view) {
        //Button button = (Button)findViewById(R.id.button);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        myAnim.setStartOffset(450);
        view.startAnimation(myAnim);
    }

    @Override
    public void onBackPressed() {
        //player.stop();
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }


}
