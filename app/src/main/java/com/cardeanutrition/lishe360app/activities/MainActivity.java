package com.cardeanutrition.lishe360app.activities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.adapters.HomeViewPagerAdapter;
import com.cardeanutrition.lishe360app.fragments.HomeFragment;
import com.cardeanutrition.lishe360app.fragments.ScheduleFragment;
import com.cardeanutrition.lishe360app.fragments.UsersFragment;
import com.cardeanutrition.lishe360app.services.TypefaceUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

import java.util.Locale;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private ImageView homeDrawerBtn;
    private ViewPager homeViewPager;
    private HomeViewPagerAdapter homeViewPagerAdapter;
    private TabLayout tabLayout;
    private NavigationView navigationView;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private String currentUserId;
    private Locale locale;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Roboto-Regular.ttf");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (!"".equals(lang) && !config.locale.getLanguage().equals(lang)) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        }

        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        //setting tag for current user
        firebaseUser =auth.getCurrentUser();
        String login_user_email = null;
        if (firebaseUser != null) {
            login_user_email = firebaseUser.getEmail();
        }
        OneSignal.sendTag("User_id",login_user_email);



        homeDrawerBtn = findViewById(R.id.home_drawer_btn);
        homeDrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer((GravityCompat.START));
            }
        });

        tabLayout = findViewById(R.id.home_tab_layout);
        homeViewPager = findViewById(R.id.home_viewPager);


        homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragments(new HomeFragment(), "Home");
        homeViewPagerAdapter.addFragments(new UsersFragment(), "Midahalo");
        homeViewPagerAdapter.addFragments(new ScheduleFragment(), "Masomo");


        homeViewPager.setAdapter(homeViewPagerAdapter);
        tabLayout.setupWithViewPager(homeViewPager);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            currentUserId = firebaseUser.getUid();
        }

    }


    private void shareit() {

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "LISHE360");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Join us now so you can learn different technique about LISHEBORA ");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    private void sendmail() {
        Log.i("Send email", "");
        String[] TO = {"jualisheboratanzania@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }



    public void setLangRecreate(String langval) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        locale = new Locale(langval);
        Locale.setDefault(locale);
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.dodoso) {

            Intent dodoso;
            try {
                dodoso = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSeWj2ioPrPdXWrPgQhT4FALrZX4rDHwu0qd_PR1ZFNeBTJMRA/viewform"));
                startActivity(dodoso);
            } catch (ActivityNotFoundException e) {
                dodoso = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLSeWj2ioPrPdXWrPgQhT4FALrZX4rDHwu0qd_PR1ZFNeBTJMRA/viewform"));
                startActivity(dodoso);
            }
        } else if (id == R.id.facebook_follow) {

            Intent facebookIntent = openFacebook(MainActivity.this);
            startActivity(facebookIntent);

        } else if (id == R.id.lishe) {
            Intent lishe = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(lishe);

        } else if (id == R.id.nav_send) {
            Intent fed = new Intent(MainActivity.this, FeedBackActivity.class);
            startActivity(fed);
        } else if (id == R.id.nav_share) {
            shareit();


        } else if (id == R.id.btnLogout) {
            auth.signOut();
            Intent outng = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(outng);
            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }

    public void FacebookPage(View view)
    {

    }

    public static Intent openFacebook(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com//lishe360/"));
        } catch (Exception e) {

            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com//lishe360/"));
        }
    }


    //creating activity for back pressing from phone
    public void onBackPressed() {
        //creating a alert dialog(for exit)
        final AlertDialog.Builder exitbuilder = new AlertDialog.Builder(MainActivity.this);
        //setting the alertdialog title
        exitbuilder.setTitle("Attention");
        //setting the body message
        exitbuilder.setMessage("Do You Want To Exit?");
        //setting the icon
        exitbuilder.setIcon(R.drawable.exit);
        //set state for cancelling state
        exitbuilder.setCancelable(true);

        //setting activity for positive state button
        exitbuilder.setPositiveButton("YES, Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //setting activity for negative state button
        exitbuilder.setNegativeButton("NO, i don't", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        //alertdialog create
        AlertDialog mydialog=exitbuilder.create();
        //for working the alertdialog state
        mydialog.show();

        //for negative side button
        mydialog.getButton(mydialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.primary));
//for positive side button
        mydialog.getButton(mydialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.primary));
    }

}
