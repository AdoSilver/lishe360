package com.cardeanutrition.lishe360app.services;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.activities.LoginActivity;

public class SplashScreen extends AppCompatActivity {


    public Button getStartedBtn, loginBtn;
    public ImageView backgroundImage;

    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
            setTheme(R.style.AppTheme);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splashscreen);
            backgroundImage = findViewById(R.id.splash_background);

            try {
                backgroundImage.setVisibility(View.VISIBLE);
                backgroundImage.setImageDrawable(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(),R.drawable.splash_background,384,384)));
            }catch (OutOfMemoryError ie){
                ie.printStackTrace();
            }

            getStartedBtn = findViewById(R.id.get_started_btn);
            loginBtn = findViewById(R.id.login_btn);


            getStartedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    i.putExtra("initiator","get_started");
                    startActivity(i);
                }
            });


            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    i.putExtra("initiator","login");
                    startActivity(i);
                }
            });

        }


        public static int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // INITIALING THE DIMENSIONS OF THE IMAGE IN PROCESS
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1 ;
            if (height > reqHeight || width > reqWidth) {
                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                // CALCULATING LARGEST inSampleSize VALUE THAT IS OF POWER OF 2 AND KEEPING
                // HEIGHT AND WIDTH LARGER THAN THE REQUESTED_HEIGHT AND REQUESTED_WIDTH
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }


        public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                             int reqWidth, int reqHeight) {
            // DECODING IMAGE WITH inJustDecodeBounds=true TO CHECK THE DIMENSIONS OF THE IMAGE
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);
            // CALCULATING inSampleSize, FOR IMAGE RESIZING
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            // DECODING BITMAP WITH CALCULATED inSampleSize SET
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }

}
