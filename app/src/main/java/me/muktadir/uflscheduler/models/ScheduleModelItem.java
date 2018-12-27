package me.muktadir.uflscheduler.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ZOARDER AL MUKTADIR on 11/21/2016.
 */

public class ScheduleModelItem implements Parcelable {

    private int mScheduleID;
    private String mClientName;
    private long mStartDateTimeMillis;
    private long mEndDateTimeMillis;
    private Double mLatitude;
    private Double mLongitude;

    public ScheduleModelItem() {
        mScheduleID = -1;
        mClientName = "";
        mStartDateTimeMillis = 0;
        mEndDateTimeMillis = 0;
        mLatitude = 0.0;
        mLongitude = 0.0;
    }

    public ScheduleModelItem(int mScheduleID, String mClientName, long mStartDateTimeMillis, long mEndDateTimeMillis, Double mLatitude, Double mLongitude) {
        this.mScheduleID = mScheduleID;
        this.mClientName = mClientName;
        this.mStartDateTimeMillis = mStartDateTimeMillis;
        this.mEndDateTimeMillis = mEndDateTimeMillis;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
    }

    public int getScheduleID() {
        return mScheduleID;
    }

    public void setScheduleID(int mScheduleID) {
        this.mScheduleID = mScheduleID;
    }

    public String getClientName() {
        return mClientName;
    }

    public void setClientName(String mClientName) {
        this.mClientName = mClientName;
    }

    public long getStartDateTimeMillis() {
        return mStartDateTimeMillis;
    }

    public void setStartDateTimeMillis(long mStartDateTimeMillis) {
        this.mStartDateTimeMillis = mStartDateTimeMillis;
    }

    public long getEndDateTimeMillis() {
        return mEndDateTimeMillis;
    }

    public void setEndDateTimeMillis(long mEndDateTimeMillis) {
        this.mEndDateTimeMillis = mEndDateTimeMillis;
    }

    public Double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(Double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public Double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(Double mLongitude) {
        this.mLongitude = mLongitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleModelItem)) return false;

        ScheduleModelItem that = (ScheduleModelItem) o;

        if (getScheduleID() != that.getScheduleID()) return false;
        if (getStartDateTimeMillis() != that.getStartDateTimeMillis()) return false;
        if (getEndDateTimeMillis() != that.getEndDateTimeMillis()) return false;
        if (!getClientName().equals(that.getClientName())) return false;
        if (!getLatitude().equals(that.getLatitude())) return false;
        return getLongitude().equals(that.getLongitude());

    }

    @Override
    public int hashCode() {
        int result = getScheduleID();
        result = 31 * result + getClientName().hashCode();
        result = 31 * result + (int) (getStartDateTimeMillis() ^ (getStartDateTimeMillis() >>> 32));
        result = 31 * result + (int) (getEndDateTimeMillis() ^ (getEndDateTimeMillis() >>> 32));
        result = 31 * result + getLatitude().hashCode();
        result = 31 * result + getLongitude().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ScheduleModelItem{" +
                "mScheduleID=" + mScheduleID +
                ", mClientName='" + mClientName + '\'' +
                ", mStartDateTimeMillis=" + mStartDateTimeMillis +
                ", mEndDateTimeMillis=" + mEndDateTimeMillis +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude + '\'' +
                '}';
    }

    protected ScheduleModelItem(Parcel in) {
        mScheduleID = in.readInt();
        mClientName = in.readString();
        mStartDateTimeMillis = in.readLong();
        mEndDateTimeMillis = in.readLong();
        mLatitude = in.readByte() == 0x00 ? null : in.readDouble();
        mLongitude = in.readByte() == 0x00 ? null : in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mScheduleID);
        dest.writeString(mClientName);
        dest.writeLong(mStartDateTimeMillis);
        dest.writeLong(mEndDateTimeMillis);
        if (mLatitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mLatitude);
        }
        if (mLongitude == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeDouble(mLongitude);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ScheduleModelItem> CREATOR = new Parcelable.Creator<ScheduleModelItem>() {
        @Override
        public ScheduleModelItem createFromParcel(Parcel in) {
            return new ScheduleModelItem(in);
        }

        @Override
        public ScheduleModelItem[] newArray(int size) {
            return new ScheduleModelItem[size];
        }
    };
}