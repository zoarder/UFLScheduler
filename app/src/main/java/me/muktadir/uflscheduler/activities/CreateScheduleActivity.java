package me.muktadir.uflscheduler.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.database.DataSourceException;
import me.muktadir.uflscheduler.database.ScheduleDataSource;
import me.muktadir.uflscheduler.models.ScheduleModelItem;
import me.muktadir.uflscheduler.services.GPSTrackerService;
import me.muktadir.uflscheduler.utilities.CommonUtils;
import me.muktadir.uflscheduler.utilities.DateTimeUtils;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class CreateScheduleActivity extends BaseActivity {

    private static final String TAG = CreateScheduleActivity.class.getSimpleName();

    private TextInputLayout mClientNameTextInputLayout;
    private TextInputLayout mStartDateTextInputLayout;
    private TextInputLayout mStartTimeTextInputLayout;
    private TextInputLayout mEndDateTextInputLayout;
    private TextInputLayout mEndTimeTextInputLayout;
    private EditText mClientNameEditText;
    private EditText mStartDateEditText;
    private EditText mStartTimeEditText;
    private EditText mEndDateEditText;
    private EditText mEndTimeEditText;
    private ImageButton mStartDateImageButton;
    private ImageButton mEndDateImageButton;
    private ImageButton mStartTimeImageButton;
    private ImageButton mEndTimeImageButton;
    private TextView mCreateScheduleTextView;
    GPSTrackerService gpsTrackerService;
    private Location mLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gpsTrackerService = new GPSTrackerService(CreateScheduleActivity.this);
    }

    @Override
    void setContentView() {
        setContentView(R.layout.activity_create_schedule);
    }

    @Override
    void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_create_schedule_parent_tb);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean allPermissionGranted = false;
        if (requestCode == GPSTrackerService.REQUEST_LOCATION) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = true;
                    } else {
                        allPermissionGranted = false;
                    }
                }

                if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        allPermissionGranted = true;
                    } else {
                        allPermissionGranted = false;
                    }
                }
            }
        }

        if (allPermissionGranted) {
            mLocation = gpsTrackerService.getLocation();
        }
    }

    @Override
    void initializeEditTextComponents() {
        mClientNameTextInputLayout = (TextInputLayout) findViewById(R.id.activity_create_schedule_content_layout_client_name_til);
        mStartDateTextInputLayout = (TextInputLayout) findViewById(R.id.activity_create_schedule_content_layout_start_date_til);
        mStartTimeTextInputLayout = (TextInputLayout) findViewById(R.id.activity_create_schedule_content_layout_start_time_til);
        mEndDateTextInputLayout = (TextInputLayout) findViewById(R.id.activity_create_schedule_content_layout_end_date_til);
        mEndTimeTextInputLayout = (TextInputLayout) findViewById(R.id.activity_create_schedule_content_layout_end_time_til);

        mClientNameEditText = (EditText) findViewById(R.id.activity_create_schedule_content_layout_client_name_et);
        mStartDateEditText = (EditText) findViewById(R.id.activity_create_schedule_content_layout_start_date_et);
        mStartTimeEditText = (EditText) findViewById(R.id.activity_create_schedule_content_layout_start_time_et);
        mEndDateEditText = (EditText) findViewById(R.id.activity_create_schedule_content_layout_end_date_et);
        mEndTimeEditText = (EditText) findViewById(R.id.activity_create_schedule_content_layout_end_time_et);
    }

    @Override
    void initializeButtonComponents() {
        mStartDateImageButton = (ImageButton) findViewById(R.id.activity_create_schedule_content_layout_start_date_picker_ib);
        mEndDateImageButton = (ImageButton) findViewById(R.id.activity_create_schedule_content_layout_end_date_picker_ib);
        mStartTimeImageButton = (ImageButton) findViewById(R.id.activity_create_schedule_content_layout_start_time_picker_ib);
        mEndTimeImageButton = (ImageButton) findViewById(R.id.activity_create_schedule_content_layout_end_time_picker_ib);
    }

    @Override
    void initializeTextViewComponents() {
        mCreateScheduleTextView = (TextView) findViewById(R.id.activity_create_schedule_content_layout_button_tv);
    }

    @Override
    void initializeOtherViewComponents() {

    }

    @Override
    void initializeViewComponentsEventListeners() {
        mStartDateEditText.setOnClickListener(this);
        mStartTimeEditText.setOnClickListener(this);
        mEndDateEditText.setOnClickListener(this);
        mEndTimeEditText.setOnClickListener(this);
        mStartDateImageButton.setOnClickListener(this);
        mEndDateImageButton.setOnClickListener(this);
        mStartTimeImageButton.setOnClickListener(this);
        mEndTimeImageButton.setOnClickListener(this);
        mCreateScheduleTextView.setOnClickListener(this);

        mEndTimeEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    submitForm();
                }
                return true;
            }
        });
    }

    @Override
    void removeViewComponentsEventListeners() {
        mStartDateEditText.setOnClickListener(null);
        mStartTimeEditText.setOnClickListener(null);
        mEndDateEditText.setOnClickListener(null);
        mEndTimeEditText.setOnClickListener(null);
        mStartDateImageButton.setOnClickListener(null);
        mEndDateImageButton.setOnClickListener(null);
        mStartTimeImageButton.setOnClickListener(null);
        mEndTimeImageButton.setOnClickListener(null);
        mCreateScheduleTextView.setOnClickListener(null);
        mEndTimeEditText.setOnEditorActionListener(null);
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

    private void submitForm() {
        if (!validateClientName()) {
            return;
        }

        if (!validateStartDate()) {
            return;
        }

        if (!validateStartTime()) {
            return;
        }

        if (!validateEndDate()) {
            return;
        }

        if (!validateEndTime()) {
            return;
        }

        if (!validateStartDateTime()) {
            return;
        }

        if (!validateEndDateTime()) {
            return;
        }

        if (!validateScheduleDuration()) {
            return;
        }

        ScheduleDataSource scheduleDataSource = new ScheduleDataSource(CreateScheduleActivity.this);
        scheduleDataSource.open();

        ScheduleModelItem scheduleModelItem = new ScheduleModelItem();
        scheduleModelItem.setClientName(mClientNameEditText.getText().toString().trim());
        String startDateTime = mStartDateEditText.getText().toString().trim() + ", " + mStartTimeEditText.getText().toString().trim();
        String endDateTime = mEndDateEditText.getText().toString().trim() + ", " + mEndTimeEditText.getText().toString().trim();
        scheduleModelItem.setStartDateTimeMillis(DateTimeUtils.getMilliSecondsFromDateTime(startDateTime, DateTimeUtils.DATE_TIME_FORMAT));
        scheduleModelItem.setEndDateTimeMillis(DateTimeUtils.getMilliSecondsFromDateTime(endDateTime, DateTimeUtils.DATE_TIME_FORMAT));


        if (mLocation != null) {
            scheduleModelItem.setLatitude(mLocation.getLatitude());
            scheduleModelItem.setLongitude(mLocation.getLongitude());
        }

        boolean success = false;
        try {
            success = scheduleDataSource.isNotConflictWithPreviousSchedule(scheduleModelItem);
            if (success) {
                scheduleDataSource.insertScheduleModelItem(scheduleModelItem);
            } else {
                CommonUtils.showErrorDialog(CreateScheduleActivity.this, getString(R.string.conflict_error_title), getString(R.string.conflict_error_message), getResources().getString(R.string.ok_title));
            }
        } catch (DataSourceException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        } finally {
            scheduleDataSource.close();
            if (success) {
                exitThisWithAnimation();
            }
        }
    }

    private boolean validateEndDateTime() {
//        Toast.makeText(CreateScheduleActivity.this, "validateEndDateTime", Toast.LENGTH_SHORT).show();
        String endDateTime = mEndDateEditText.getText().toString().trim() + ", " + mEndTimeEditText.getText().toString().trim();
        long endMilliseconds = DateTimeUtils.getMilliSecondsFromDateTime(endDateTime, DateTimeUtils.DATE_TIME_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (endMilliseconds < calendar.getTimeInMillis()) {
            CommonUtils.showErrorDialog(CreateScheduleActivity.this, getString(R.string.date_time_error_title), getString(R.string.end_date_time_error_message), getResources().getString(R.string.ok_title));
            return false;
        }
        return true;
    }

    private boolean validateStartDateTime() {
//        Toast.makeText(CreateScheduleActivity.this, "validateStartDateTime", Toast.LENGTH_SHORT).show();
        String startDateTime = mStartDateEditText.getText().toString().trim() + ", " + mStartTimeEditText.getText().toString().trim();
        long startMilliseconds = DateTimeUtils.getMilliSecondsFromDateTime(startDateTime, DateTimeUtils.DATE_TIME_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (startMilliseconds < calendar.getTimeInMillis()) {
            CommonUtils.showErrorDialog(CreateScheduleActivity.this, getString(R.string.date_time_error_title), getString(R.string.start_date_time_error_message), getResources().getString(R.string.ok_title));
            return false;
        }
        return true;
    }

    private boolean validateScheduleDuration() {
//        Toast.makeText(CreateScheduleActivity.this, "validateScheduleDuration", Toast.LENGTH_SHORT).show();
        String startDateTime = mStartDateEditText.getText().toString().trim() + ", " + mStartTimeEditText.getText().toString().trim();
        String endDateTime = mEndDateEditText.getText().toString().trim() + ", " + mEndTimeEditText.getText().toString().trim();
        long startMilliseconds = DateTimeUtils.getMilliSecondsFromDateTime(startDateTime, DateTimeUtils.DATE_TIME_FORMAT);
        long endMilliseconds = DateTimeUtils.getMilliSecondsFromDateTime(endDateTime, DateTimeUtils.DATE_TIME_FORMAT);

        if (startMilliseconds > endMilliseconds) {
            CommonUtils.showErrorDialog(CreateScheduleActivity.this, getString(R.string.date_time_error_title), getString(R.string.schedule_duration_error_message), getResources().getString(R.string.ok_title));
            return false;
        }
        return true;
    }

    private boolean validateEndTime() {
//        Toast.makeText(CreateScheduleActivity.this,"validateEndTime",Toast.LENGTH_SHORT).show();
        if (mEndTimeEditText.getText().toString().trim().isEmpty()) {
            mEndTimeTextInputLayout.setError(getString(R.string.enter_end_time_empty));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mEndTimeEditText);
            return false;
        } else if (!DateTimeUtils.isValidDateTimeFormat(mEndTimeEditText.getText().toString().trim(), DateTimeUtils.TIME_FORMAT)) {
            mEndTimeTextInputLayout.setError(getString(R.string.enter_invalid_time));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mEndTimeEditText);
            return false;
        } else {
            mEndTimeTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateEndDate() {
//        Toast.makeText(CreateScheduleActivity.this,"validateEndDate",Toast.LENGTH_SHORT).show();
        if (mEndDateEditText.getText().toString().trim().isEmpty()) {
            mEndDateTextInputLayout.setError(getString(R.string.enter_end_date_empty));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mEndDateEditText);
            return false;
        } else if (!DateTimeUtils.isValidDateTimeFormat(mEndDateEditText.getText().toString().trim(), DateTimeUtils.DATE_FORMAT)) {
            mEndDateTextInputLayout.setError(getString(R.string.enter_invalid_date));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mEndDateEditText);
            return false;
        } else {
            mEndDateTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateStartTime() {
//        Toast.makeText(CreateScheduleActivity.this,"validateStartTime",Toast.LENGTH_SHORT).show();
        if (mStartTimeEditText.getText().toString().trim().isEmpty()) {
            mStartTimeTextInputLayout.setError(getString(R.string.enter_start_time_empty));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mStartTimeEditText);
            return false;
        } else if (!DateTimeUtils.isValidDateTimeFormat(mStartTimeEditText.getText().toString().trim(), DateTimeUtils.TIME_FORMAT)) {
            mStartTimeTextInputLayout.setError(getString(R.string.enter_invalid_time));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mStartTimeEditText);
            return false;
        } else {
            mStartTimeTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateStartDate() {
//        Toast.makeText(CreateScheduleActivity.this,"validateStartDate",Toast.LENGTH_SHORT).show();
        if (mStartDateEditText.getText().toString().trim().isEmpty()) {
            mStartDateTextInputLayout.setError(getString(R.string.enter_start_date_empty));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mStartDateEditText);
            return false;
        } else if (!DateTimeUtils.isValidDateTimeFormat(mStartDateEditText.getText().toString().trim(), DateTimeUtils.DATE_FORMAT)) {
            mStartDateTextInputLayout.setError(getString(R.string.enter_invalid_date));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mStartDateEditText);
            return false;
        } else {
            mStartDateTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateClientName() {
//        Toast.makeText(CreateScheduleActivity.this,"validateClientName",Toast.LENGTH_SHORT).show();
        if (mClientNameEditText.getText().toString().trim().isEmpty()) {
            mClientNameTextInputLayout.setError(getString(R.string.enter_client_name_empty));
            CommonUtils.requestFocus(CreateScheduleActivity.this, mClientNameEditText);
            return false;
        } else {
            mClientNameTextInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        CommonUtils.hideSoftInputMode(CreateScheduleActivity.this, view);
        if (view != null) {
            switch (view.getId()) {
                case R.id.activity_create_schedule_content_layout_start_date_picker_ib:
                    mStartDateEditText.requestFocus();
                case R.id.activity_create_schedule_content_layout_start_date_et:
                    DateTimeUtils.setDateFromDatePicker(CreateScheduleActivity.this, year, month, day, DateTimeUtils.DATE_FORMAT, mStartDateEditText);
                    break;
                case R.id.activity_create_schedule_content_layout_end_date_picker_ib:
                    mEndDateEditText.requestFocus();
                case R.id.activity_create_schedule_content_layout_end_date_et:
                    DateTimeUtils.setDateFromDatePicker(CreateScheduleActivity.this, year, month, day, DateTimeUtils.DATE_FORMAT, mEndDateEditText);
                    break;
                case R.id.activity_create_schedule_content_layout_start_time_picker_ib:
                    mStartTimeEditText.requestFocus();
                case R.id.activity_create_schedule_content_layout_start_time_et:
                    DateTimeUtils.setTimeFromTimePicker(CreateScheduleActivity.this, hour, minute, second, DateTimeUtils.TIME_FORMAT, mStartTimeEditText);
                    break;
                case R.id.activity_create_schedule_content_layout_end_time_picker_ib:
                    mEndTimeEditText.requestFocus();
                case R.id.activity_create_schedule_content_layout_end_time_et:
                    DateTimeUtils.setTimeFromTimePicker(CreateScheduleActivity.this, hour, minute, second, DateTimeUtils.TIME_FORMAT, mEndTimeEditText);
                    break;
                case R.id.activity_create_schedule_content_layout_button_tv:
                    submitForm();
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        exitThisWithAnimation();
    }
}