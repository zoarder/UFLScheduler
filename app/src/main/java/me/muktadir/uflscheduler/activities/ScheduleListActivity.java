package me.muktadir.uflscheduler.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.adapters.ScheduleListAdapter;
import me.muktadir.uflscheduler.database.ScheduleDataSource;
import me.muktadir.uflscheduler.models.ScheduleModelItem;
import me.muktadir.uflscheduler.utilities.AllSharedPreferencesKeys;
import me.muktadir.uflscheduler.utilities.SharedPreferencesManager;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class ScheduleListActivity extends BaseActivity {

    public static final String KEY_SCHEDULE_LIST = "KEY_SCHEDULE_LIST";

    private ArrayList<ScheduleModelItem> mScheduleModelItems = new ArrayList<>();
    private RecyclerView mScheduleListRecyclerView;
    private CoordinatorLayout mParentCoordinatorLayout;
    private ScheduleListAdapter mScheduleListRecyclerViewAdapter;
    private FloatingActionButton mAddScheduleFloatingActionButton;
    private RelativeLayout mTutorialRelativeLayout;
    private TextView mEmptyScheduleListMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SharedPreferencesManager.getInstance(getApplicationContext()).getBooleanValue(AllSharedPreferencesKeys.KEY_IS_CREATE_NEW_SCHEDULE_TUTORIAL_SHOWN, false)) {
            SharedPreferencesManager.getInstance(getApplicationContext()).setBooleanValue(AllSharedPreferencesKeys.KEY_IS_CREATE_NEW_SCHEDULE_TUTORIAL_SHOWN, true);
            mTutorialRelativeLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScheduleDataSource scheduleDataSource = new ScheduleDataSource(ScheduleListActivity.this);
        scheduleDataSource.open();
        if (scheduleDataSource.getSize() > 0) {
            mScheduleListRecyclerView.setVisibility(View.VISIBLE);
            mEmptyScheduleListMessageTextView.setVisibility(View.GONE);
        } else {
            mScheduleListRecyclerView.setVisibility(View.GONE);
            mEmptyScheduleListMessageTextView.setVisibility(View.VISIBLE);
        }
        scheduleDataSource.close();

        if (mScheduleListRecyclerViewAdapter != null) {
            mScheduleListRecyclerViewAdapter.notifyScheduleDataSetChanged(ScheduleListActivity.this);
        }
    }

    @Override
    void setContentView() {
        setContentView(R.layout.activity_schedule_list);
    }

    @Override
    void setupActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_schedule_list_parent_tb);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        }
    }

    @Override
    void initializeEditTextComponents() {

    }

    @Override
    void initializeButtonComponents() {
        mAddScheduleFloatingActionButton = (FloatingActionButton) findViewById(R.id.activity_schedule_list_add_schedule_fab);
    }

    @Override
    void initializeTextViewComponents() {
        mEmptyScheduleListMessageTextView = (TextView) findViewById(R.id.activity_schedule_list_content_layout_empty_schedule_list_message_tv);
    }

    @Override
    void initializeOtherViewComponents() {
        mParentCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_schedule_list_parent_cl);
        mTutorialRelativeLayout = (RelativeLayout) findViewById(R.id.activity_schedule_list_tutorial_rl);
        mScheduleListRecyclerView = (RecyclerView) findViewById(R.id.activity_schedule_list_content_layout_schedule_list_rv);
        mScheduleListRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mScheduleListRecyclerView.setLayoutManager(layoutManager);
        mScheduleListRecyclerViewAdapter = new ScheduleListAdapter(mScheduleModelItems);
        mScheduleListRecyclerView.setAdapter(mScheduleListRecyclerViewAdapter);
    }

    @Override
    void initializeViewComponentsEventListeners() {
        mScheduleListRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
//                    Snackbar.make(child, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();

                    Intent intent = new Intent(ScheduleListActivity.this, ScheduleDetailsActivity.class);
                    intent.putExtra(ScheduleDetailsActivity.KEY_SCHEDULE_ITEM, mScheduleListRecyclerViewAdapter.getScheduleModelItem(position));
                    startActivity(intent);
                    overridePendingTransition(R.anim.trans_left_in,
                            R.anim.trans_left_out);
                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mAddScheduleFloatingActionButton.setOnClickListener(this);
        mTutorialRelativeLayout.setOnClickListener(this);
    }

    @Override
    void removeViewComponentsEventListeners() {
        mScheduleListRecyclerView.addOnItemTouchListener(null);
        mAddScheduleFloatingActionButton.setOnClickListener(null);
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
    public void onBackPressed() {
        exitFromApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_exit) {
            exitFromApp();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    public void exitFromApp() {
        final SweetAlertDialog sDialog = new SweetAlertDialog(ScheduleListActivity.this, SweetAlertDialog.WARNING_MODIFIED_TYPE);
        sDialog.setTitleText(getResources().getString(R.string.title_exit_dialog));
        sDialog.setTitle("sDialog");
        sDialog.setConfirmText(getResources().getString(R.string.dialog_confirm_text));
        sDialog.setCancelText(getResources().getString(R.string.dialog_cancel_text));
        sDialog.setCancelable(false);
        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog mDialog) {
                mDialog.dismiss();
                exitThisWithAnimation();
            }
        });
        sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog mDialog) {
                mDialog.cancel();
            }
        });
        sDialog.show();
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.activity_schedule_list_add_schedule_fab:
                    // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    // .setAction("Action", null).show();
                    Intent intent = new Intent(ScheduleListActivity.this, CreateScheduleActivity.class);
                    startNextWithAnimation(intent, false);
                    break;
                case R.id.activity_schedule_list_tutorial_rl:
                    mTutorialRelativeLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }
}
