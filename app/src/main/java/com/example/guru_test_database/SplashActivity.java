package com.example.guru_test_database;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends SplashBaseActivity {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);


            String str = "MaPost.";
            TextView tv = (TextView)findViewById(R.id.animation_text);
            SpannableStringBuilder ssb = new SpannableStringBuilder(str);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFDA1414")),0,1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF1621F")),1,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#FFF2DB2F")),2,3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tv.setText(ssb);

            LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
            animationView.setAnimation("planet_rotating.json");
            animationView.loop(true);
            animationView.playAnimation();


            // 추가된 애니메이션 코드
            TextView text = findViewById(R.id.animation_text);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            });




//            Thread backgroud = new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
//                }
//            };
//
//            backgroud.start();
        }

        //   @Override
//    protected void onDestroy() {
//        super.onDestroy();
//    }
}