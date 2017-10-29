package com.example.ranzo1.student_app.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.ranzo1.student_app.R;
import com.example.ranzo1.student_app.mainActivities.MActivity;

import info.hoang8f.widget.FButton;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FButton btnGmail=(FButton)findViewById(R.id.gmailBtn);
        FButton btnSkip=(FButton)findViewById(R.id.skipBtn);


        LottieAnimationView lottieAnimationView = (LottieAnimationView)findViewById(R.id.animation_view1);

        lottieAnimationView.playAnimation();
        lottieAnimationView.loop(true);
        lottieAnimationView.isAnimating();



        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

    }
}
