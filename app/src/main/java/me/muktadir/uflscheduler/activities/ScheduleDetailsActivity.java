package me.muktadir.uflscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.models.ScheduleModelItem;
import me.muktadir.uflscheduler.utilities.AllSharedPreferencesKeys;
import me.muktadir.uflscheduler.utilities.DateTimeUtils;
import me.muktadir.uflscheduler.utilities.MapUtils;
import me.muktadir.uflscheduler.utilities.SharedPreferencesManager;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class ScheduleDetailsActivity extends BaseActivity {

    public static final String KEY_SCHEDULE_ITEM = "KEY_SCHEDULE_ITEM";
    private ScheduleModelItem mScheduleModelItem;


    private FloatingActionButton mUpdateScheduleFloatingActionButton;
    private TextView mClientNameTextView;
    private TextView mStartDateTimeTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mEndDateTimeTextView;
    private TextView mAddressTextView;
    private TextView mDurationTextView;
    private RelativeLayout mTutorialRelativeLayout;

    private String mAddress = "N/A";
    private String mDuration = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SharedPreferencesManager.getInstance(getApplicationContext()).getBooleanValue(AllSharedPreferencesKeys.KEY_IS_UPDATE_THIS_SCHEDULE_TUTORIAL_SHOWN, false)) {
            SharedPreferencesManager.getInstance(getApplicationContext()).setBooleanValue(AllSharedPreferencesKeys.KEY_IS_UPDATE_THIS_SCHEDULE_TUTORIAL_SHOWN, true);
            mTutorialRelativeLayout.setVisibility(View.VISIBLE);
        }

        if (getIntent() != null) {
            mScheduleModelItem = getIntent().getParcelableExtra(ScheduleDetailsActivity.KEY_SCHEDULE_ITEM);

            mClientNameTextView.setText(mScheduleModelItem.getClientName());
            mStartDateTimeTextView.setText(DateTimeUtils.getDateTimeFromMilliSeconds(mScheduleModelItem.getStartDateTimeMillis(), DateTimeUtils.DATE_TIME_FORMAT));
            mEndDateTimeTextView.setText(DateTimeUtils.getDateTimeFromMilliSeconds(mScheduleModelItem.getEndDateTimeMillis(), DateTimeUtils.DATE_TIME_FORMAT));

            if (mScheduleModelItem.getLatitude() > 0 && mScheduleModelItem.getLongitude() > 0) {
                mLatitudeTextView.setText(mScheduleModelItem.getLatitude().toString());
                mLongitudeTextView.setText(mScheduleModelItem.getLongitude().toString());
                MapUtils.getAddressFromLocation(mScheduleModelItem.getLatitude(), mScheduleModelItem.getLongitude(),
                        ScheduleDetailsActivity.this, new GeocoderHandler());
            } else {
                mLatitudeTextView.setText("N/A");
                mLongitudeTextView.setText("N/A");
                mAddressTextView.setText("N/A");
            }
            mDuration = DateTimeUtils.getDurationFromMilliSecondse(mScheduleModelItem.getEndDateTimeMillis() - mScheduleModelItem.getStartDateTimeMillis(), DateTimeUtils.DURATION_FORMAT);
            mDurationTextView.setText(mDuration);
        }
    }

    @Override
    void setContentView() {
        setContentView(R.layout.activity_schedule_details);
    }

    @Override
    void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_schedule_details_parent_tb);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    void initializeEditTextComponents() {

    }

    @Override
    void initializeButtonComponents() {
        mUpdateScheduleFloatingActionButton = (FloatingActionButton) findViewById(R.id.activity_schedule_details_update_schedule_fab);
    }

    @Override
    void initializeTextViewComponents() {
        mClientNameTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_client_name_value_tv);
        mStartDateTimeTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_start_date_time_value_tv);
        mLatitudeTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_latitude_value_tv);
        mLongitudeTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_longitude_value_tv);
        mDurationTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_duration_value_tv);
        mEndDateTimeTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_end_date_time_value_tv);
        mAddressTextView = (TextView) findViewById(R.id.activity_schedule_details_content_layout_address_value_tv);
    }

    @Override
    void initializeOtherViewComponents() {

        mTutorialRelativeLayout = (RelativeLayout) findViewById(R.id.activity_schedule_details_tutorial_rl);
    }

    @Override
    void initializeViewComponentsEventListeners() {
        mUpdateScheduleFloatingActionButton.setOnClickListener(this);
        mTutorialRelativeLayout.setOnClickListener(this);
    }

    @Override
    void removeViewComponentsEventListeners() {
        mUpdateScheduleFloatingActionButton.setOnClickListener(null);
        mTutorialRelativeLayout.setOnClickListener(null);
    }

    @Override
    void exitThisWithAnimation() {
        finish();
        overridePendingTransition(R.anim.trans_top_in,
                R.anim.trans_top_out);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            exitThisWithAnimation(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.activity_schedule_details_update_schedule_fab:
                    // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    // .setAction("Action", null).show();
                    Intent intent = new Intent(ScheduleDetailsActivity.this, UpdateScheduleActivity.class);
                    intent.putExtra(UpdateScheduleActivity.KEY_SCHEDULE_ITEM, mScheduleModelItem);
                    startNextWithAnimation(intent, true);
                    break;
                case R.id.activity_schedule_details_tutorial_rl:
                    mTutorialRelativeLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        exitThisWithAnimation();
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            mAddress = locationAddress;
            mAddressTextView.setText(mAddress);
        }
    }
}