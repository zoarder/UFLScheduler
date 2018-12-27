package me.muktadir.uflscheduler.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.database.ScheduleDataSource;
import me.muktadir.uflscheduler.models.ScheduleModelItem;
import me.muktadir.uflscheduler.utilities.DateTimeUtils;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleListItemViewHolder> {

    private ArrayList<ScheduleModelItem> mScheduleModelItems;

    public ScheduleListAdapter(ArrayList<ScheduleModelItem> scheduleModelItems) {
        this.mScheduleModelItems = scheduleModelItems;
    }

    @Override
    public ScheduleListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.schedule_list_item_layout, viewGroup, false);
        return new ScheduleListItemViewHolder(view);
    }

    public void notifyScheduleDataSetChanged(Context context) {
        ScheduleDataSource scheduleDataSource = new ScheduleDataSource(context);
        scheduleDataSource.open();
        mScheduleModelItems = scheduleDataSource.getAllScheduleItem();
        scheduleDataSource.close();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ScheduleListItemViewHolder viewHolder, int i) {
        ScheduleModelItem scheduleModelItem = mScheduleModelItems.get(i);
        viewHolder.mClientNameTextView.setText(scheduleModelItem.getClientName());
        viewHolder.mStartTimeTextView.setText(DateTimeUtils.getDateTimeFromMilliSeconds(scheduleModelItem.getStartDateTimeMillis(), DateTimeUtils.DATE_TIME_FORMAT));
        viewHolder.mEndTimeTextView.setText(DateTimeUtils.getDateTimeFromMilliSeconds(scheduleModelItem.getEndDateTimeMillis(), DateTimeUtils.DATE_TIME_FORMAT));
    }

    @Override
    public int getItemCount() {
        return mScheduleModelItems.size();
    }

    @Override
    public long getItemId(int position) {
        return mScheduleModelItems.get(position).getScheduleID();
    }

    public ScheduleModelItem getScheduleModelItem(int position) {
        return mScheduleModelItems.get(position);
    }

    public class ScheduleListItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mClientNameTextView;
        private TextView mStartTimeTextView;
        private TextView mEndTimeTextView;

        public ScheduleListItemViewHolder(View view) {
            super(view);

            mClientNameTextView = (TextView) view.findViewById(R.id.schedule_list_item_layout_client_name_value_tv);
            mStartTimeTextView = (TextView) view.findViewById(R.id.schedule_list_item_layout_start_date_time_value_tv);
            mEndTimeTextView = (TextView) view.findViewById(R.id.schedule_list_item_layout_end_date_time_value_tv);
        }
    }

}
