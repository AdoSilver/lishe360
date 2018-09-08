
package com.cardeanutrition.lishe360app.activities;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.managers.DatabaseHelper;
import com.cardeanutrition.lishe360app.managers.ProfileManager;
import com.cardeanutrition.lishe360app.managers.listeners.OnObjectExistListener;
import com.cardeanutrition.lishe360app.model.Profile;
import com.cardeanutrition.lishe360app.utils.LogUtil;
import com.cardeanutrition.lishe360app.utils.PreferencesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.truizlop.fabreveallayout.FABRevealLayout;
import com.truizlop.fabreveallayout.OnRevealChangeListener;

public class LoginActivity extends BaseActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();



    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;
    private Button btnLogin, player_video;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private TextView contact_us;
    private ImageView headerBackground;
    private TextView forget_password;
    private String profilePhotoUrlLarge;
    private FloatingActionButton registerBtn;
    private TextView loginHeader;
    private FABRevealLayout fabLoginLayout, fabRegisterLayout, fabForgotPassLayout;
    private EditText recoverPassinputEmail;
    private Button btnReset;
    private FirebaseAuth auth;
    private ProgressBar recoverPassprogressBar;
    private ImageView btnBack;
    private String initiator;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lishe_login);

        // Configure firebase auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        mAuth = FirebaseAuth.getInstance();

        try{
            initiator = getIntent().getExtras().getString("initiator");
        }catch (NullPointerException ie){
            ie.printStackTrace();
        }


        // Gathering views
        inputEmail = findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin);
        player_video = findViewById(R.id.reg_player_video);
        contact_us = findViewById(R.id.contact_us);
        forget_password = findViewById(R.id.forget_password);
        registerBtn = findViewById(R.id.register_btn);
        loginHeader = findViewById(R.id.login_header_text_view);
        fabLoginLayout = findViewById(R.id.fab_reveal_layout);
        fabRegisterLayout = findViewById(R.id.fab_register_layout);
        fabForgotPassLayout = findViewById(R.id.fab_recover_pass_layout);
        headerBackground = findViewById(R.id.header_background);
        //setting up header background
        try {
            headerBackground.setVisibility(View.VISIBLE);
            headerBackground.setImageDrawable(new BitmapDrawable(getResources(),decodeSampledBitmapFromResource(getResources(),R.drawable.splash_background,384,384)));
        }catch (OutOfMemoryError ie){
            ie.printStackTrace();
        }

        recoverPassinputEmail = findViewById(R.id.rpemail);
        btnReset =  findViewById(R.id.btn_reset_password);
        recoverPassprogressBar = findViewById(R.id.rpprogressBar);

        configureLoginFab(fabLoginLayout);
        configureRegisterFab(fabRegisterLayout);
        configureForgotPassFab(fabForgotPassLayout);


        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, AboutUsActivity.class);
                startActivity(i);
            }
        });


        player_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent watch = new Intent(LoginActivity.this, FullScreenVideoActivity.class);
                startActivity(watch);
            }
        });

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabLoginLayout.setVisibility(View.GONE);
                loginHeader.setText("Forgot Password?");
                fabForgotPassLayout.setVisibility(View.VISIBLE);
                fabForgotPassLayout.revealMainView();
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabLoginLayout.setVisibility(View.GONE);
                loginHeader.setText("Get Started");
                fabRegisterLayout.setVisibility(View.VISIBLE);
                fabRegisterLayout.revealMainView();
            }
        });


        //Switching according to initiator
        if(initiator.equals("get_started")){
            registerBtn.performClick();
        }




        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String email = recoverPassinputEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                return;
            }
            recoverPassprogressBar.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                //Reveal Login Portion
                                loginHeader.setText("Login");
                                fabForgotPassLayout.setVisibility(View.GONE);
                                fabLoginLayout.setVisibility(View.VISIBLE);
                                fabLoginLayout.revealMainView();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                            recoverPassprogressBar.setVisibility(View.GONE);
                        }
                    });
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProgress();
                String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    hideProgress();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    hideProgress();
                    return;
                }


            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                                hideProgress();
                            } else {
                                Toast.makeText(LoginActivity.this, "failed to login", Toast.LENGTH_LONG).show();
                                hideProgress();
                            }

                        } else {

                            startCreateProfileActivity();
                            hideProgress();
                        }
                    }
                });
            }
        });




    }


    private void checkIsProfileExist(final String userId) {
        ProfileManager.getInstance(this).isProfileExist(userId, new OnObjectExistListener<Profile>() {
            @Override
            public void onDataChanged(boolean exist) {
                if (!exist) {
                    startCreateProfileActivity();
                } else {
                    PreferencesUtil.setProfileCreated(LoginActivity.this, true);
                    DatabaseHelper.getInstance(LoginActivity.this.getApplicationContext())
                            .addRegistrationToken(FirebaseInstanceId.getInstance().getToken(), userId);
                }
                hideProgress();
                finish();
            }
        });
    }

    private void startCreateProfileActivity() {
        Intent intent = new Intent(LoginActivity.this, CreateProfileActivity.class);
        intent.putExtra(CreateProfileActivity.LARGE_IMAGE_URL_EXTRA_KEY, profilePhotoUrlLarge);
        startActivity(intent);
        finish();
    }

    private void handleAuthError(Task<AuthResult> task) {
        Exception exception = task.getException();
        LogUtil.logError(TAG, "signInWithCredential", exception);

        if (exception != null) {
            showWarningDialog(exception.getMessage());
        } else {
            showSnackBar(R.string.error_authentication);
        }

        hideProgress();
    }

    private void configureLoginFab(final FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabLayout, View mainView) {
                loginHeader.setText("Login");
                fabRegisterLayout.setVisibility(View.GONE);
                fabLoginLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabLayout, View secondaryView) {
                loginHeader.setText("Get Started");
                fabLoginLayout.setVisibility(View.GONE);
                fabRegisterLayout.setVisibility(View.VISIBLE);
                fabRegisterLayout.revealMainView();
            }
        });
    }


    private void configureRegisterFab(final FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabLayout, View mainView) {
                loginHeader.setText("Get Started");
                //loginRegisterBtn.performClick();
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabLayout, View secondaryView) {
                loginHeader.setText("Login");
                fabRegisterLayout.setVisibility(View.GONE);
                fabLoginLayout.setVisibility(View.VISIBLE);
                fabLoginLayout.revealMainView();
            }
        });
    }


    private void configureForgotPassFab(final FABRevealLayout fabRevealLayout) {
        fabRevealLayout.setOnRevealChangeListener(new OnRevealChangeListener() {
            @Override
            public void onMainViewAppeared(FABRevealLayout fabLayout, View mainView) {
                loginHeader.setText("Forgot Password?");
            }

            @Override
            public void onSecondaryViewAppeared(final FABRevealLayout fabLayout, View secondaryView) {
                loginHeader.setText("Login");
                fabForgotPassLayout.setVisibility(View.GONE);
                fabLoginLayout.setVisibility(View.VISIBLE);
                fabLoginLayout.revealMainView();
            }
        });
    }


    private void prepareBackTransition(final FABRevealLayout fabRevealLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fabRevealLayout.revealMainView();
            }
        }, 1000);
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



