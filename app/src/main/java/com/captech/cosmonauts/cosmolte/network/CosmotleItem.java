package com.captech.cosmonauts.cosmolte.network;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jvazquez on 7/26/16.
 */
public class CosmotleItem implements Parcelable{

    private String name;
    private int count;

    public CosmotleItem(){

    }

    public CosmotleItem(Parcel parcel){
        name = parcel.readString();
        count = parcel.readInt();
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
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
        dest.writeInt(count);
    }

    public static final Creator<CosmotleItem> CREATOR = new Creator<CosmotleItem>() {
        @Override
        public CosmotleItem createFromParcel(Parcel in) {
            return new CosmotleItem(in);
        }

        @Override
        public CosmotleItem[] newArray(int size) {
            return new CosmotleItem[size];
        }
    };


}
