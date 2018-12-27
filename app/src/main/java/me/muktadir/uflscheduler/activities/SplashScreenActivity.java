package me.muktadir.uflscheduler.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.database.DataBaseOpenHelper;
import me.muktadir.uflscheduler.utilities.AllSharedPreferencesKeys;
import me.muktadir.uflscheduler.utilities.SharedPreferencesManager;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class SplashScreenActivity extends BaseActivity {

    private ImageView mAppLogoImageView;
    private TextView mAppNameTextView;
    private ProgressBar mDataLoadingProgressBar;
    private TextView mDataLoadingTextView;
    private int mProgressBarIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressBarIndex = 0;

        new CountDownTimer(5000, 100) {

            public void onTick(long millisUntilFinished) {
                onProgressDialog(getString(R.string.text_loading), mProgressBarIndex += 2);
            }

            public void onFinish() {
                onProgressDialog(getString(R.string.text_finished), 100);
                onTimeSpent();
            }
        }.start();
        if (SharedPreferencesManager.getInstance(getApplicationContext()).getBooleanValue(AllSharedPreferencesKeys.KEY_APP_START_FIRST_TIME, true)) {
            SharedPreferencesManager.getInstance(getApplicationContext()).setBooleanValue(AllSharedPreferencesKeys.KEY_APP_START_FIRST_TIME, false);
            DataBaseOpenHelper dataBaseOpenHelper = new DataBaseOpenHelper(this);
            SQLiteDatabase sqLiteDatabase = dataBaseOpenHelper.getWritableDatabase();
//            dataBaseOpenHelper.dropDatabase(sqLiteDatabase);
            dataBaseOpenHelper.createDatabase(sqLiteDatabase);
            sqLiteDatabase.close();
        }
    }

    @Override
    void setContentView() {
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    void setupActionBar() {

    }

    @Override
    void initializeEditTextComponents() {

    }

    @Override
    void initializeButtonComponents() {

    }

    @Override
    void initializeTextViewComponents() {
        mAppNameTextView = (TextView) findViewById(R.id.activity_splash_screen_app_name_tv);
        mDataLoadingTextView = (TextView) findViewById(R.id.activity_splash_screen_data_loading_tv);

    }

    @Override
    void initializeOtherViewComponents() {
        mAppLogoImageView = (ImageView) findViewById(R.id.activity_splash_screen_app_logo_iv);
        mDataLoadingProgressBar = (ProgressBar) findViewById(R.id.activity_splash_screen_data_loading_pb);
        Animation shake = AnimationUtils.loadAnimation(SplashScreenActivity.this, R.anim.shake_1);
        mAppLogoImageView.startAnimation(shake);
        mAppNameTextView.startAnimation(shake);

    }

    @Override
    void initializeViewComponentsEventListeners() {

    }

    @Override
    void removeViewComponentsEventListeners() {

    }

    @Override
    void exitThisWithAnimation() {

    }

    @Override
    void startNextWithAnimation(Intent intent, boolean isFinish) {
        if (isFinish) {
            finish();
        }
        startActivity(intent);
        overridePendingTransition(R.anim.trans_left_in,
                R.anim.trans_left_out);
    }

    private void onTimeSpent() {
        Intent intent = new Intent(SplashScreenActivity.this, ScheduleListActivity.class);
        startNextWithAnimation(intent, true);
    }

    private void onProgressDialog(String progressText, int totalProgress) {
        mDataLoadingTextView.setText(progressText);
        mDataLoadingProgressBar.setProgress(totalProgress);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onBackPressed() {
    }
}
