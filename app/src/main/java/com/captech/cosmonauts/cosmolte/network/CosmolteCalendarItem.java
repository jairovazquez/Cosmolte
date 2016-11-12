package com.captech.cosmonauts.cosmolte.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jvazquez on 8/5/16.
 */
public class CosmolteCalendarItem implements Parcelable, Comparable<CosmolteCalendarItem> {

    private String name;
    private int month;
    private int date;

    public CosmolteCalendarItem() {
    }

    public CosmolteCalendarItem(Parcel parcel) {
        name = parcel.readString();
        month = parcel.readInt();
        date = parcel.readInt();
    }

    public int getDay() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(month);
        dest.writeInt(date);
    }

    public static final Creator<CosmolteCalendarItem> CREATOR = new Creator<CosmolteCalendarItem>() {
        @Override
        public CosmolteCalendarItem createFromParcel(Parcel in) {
            return new CosmolteCalendarItem(in);
        }

        @Override
        public CosmolteCalendarItem[] newArray(int size) {
            return new CosmolteCalendarItem[size];
        }
    };

    @Override
    public int compareTo(CosmolteCalendarItem another) {
        if (this.getDay() == another.getDay() && this.getMonth() == another.getMonth()) {
            return 0;
        } else if( this.getMonth() == another.getMonth() && this.getDay() != another.getDay()){
            return another.getDay() - this.getDay();
        } else if( this.getMonth() != another.getMonth() ){
            return another.getMonth() - this.getMonth();
        }

        return 0;
    }
}
