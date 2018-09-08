package com.cardeanutrition.lishe360app.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MotionEvent;
import android.widget.TextView;

import com.cardeanutrition.lishe360app.Feedlist.model.Topics;
import com.cardeanutrition.lishe360app.R;
import com.cardeanutrition.lishe360app.services.SquareImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


public class DetailsActivity extends BaseActivity {

    private static final String EXTRA_IMAGE = "extraImage";
    private static final String EXTRA_TITLE = "extraTitle";
    private static final String EXTRA_DISC = "extradisc";
    private static final String EXTRA_TIME = "extraTime";
    private static final String EXTRA_SUB = "extraSub";
    private static final String EXTRA_ONE = "extraOne";
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public static void navigate(FragmentActivity activity, Topics viewModel) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra(EXTRA_IMAGE, viewModel.getPhoto());
        intent.putExtra(EXTRA_TITLE, viewModel.getMaintitle());
        intent.putExtra(EXTRA_DISC,viewModel.getDescption());
        intent.putExtra(EXTRA_TIME,viewModel.getTime());
        intent.putExtra(EXTRA_SUB,viewModel.getSubtitle());
        intent.putExtra(EXTRA_ONE,viewModel.getDescption_one());
        activity.startActivity(intent);

    }

    @SuppressWarnings("ConstantConditions")
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_details);

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String itemTitle = getIntent().getStringExtra(EXTRA_TITLE);
        String itemDesc = getIntent().getStringExtra(EXTRA_DISC);
        String itemTime = getIntent().getStringExtra(EXTRA_TIME);
        String itemOne = getIntent().getStringExtra(EXTRA_ONE);
        String itemSub = getIntent().getStringExtra(EXTRA_SUB);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        final SquareImageView images = (SquareImageView) findViewById(R.id.image);
        Picasso.with(this).load(getIntent().getStringExtra(EXTRA_IMAGE)).into(images, new Callback() {
            @Override public void onSuccess() {
                Bitmap bitmap = ((BitmapDrawable) images.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        applyPalette(palette);
                    }
                });
            }

            @Override public void onError() {

            }
        });

        TextView title = (TextView) findViewById(R.id.title);
        TextView desc = findViewById(R.id.description);
        TextView time_details = findViewById(R.id.time_details);
        TextView heading_sub = findViewById(R.id.heading_sub);
        TextView descption_2 = findViewById(R.id.description_2);
        title.setText(itemTitle);
        desc.setText(itemDesc);
        time_details.setText(itemTime);
        heading_sub.setText(itemSub);
        descption_2.setText(itemOne);
    }

    @Override public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    private void applyPalette(Palette palette) {
        int primaryDark = getResources().getColor(R.color.primary_dark);
        int primary = getResources().getColor(R.color.primary);
        collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(primary));
        collapsingToolbarLayout.setStatusBarScrimColor(palette.getDarkMutedColor(primaryDark));
        supportStartPostponedEnterTransition();
    }

}

