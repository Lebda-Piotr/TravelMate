package com.example.travelmate;

import android.os.Parcel;
import android.os.Parcelable;

public class GeoPointWrapper implements Parcelable {
    private double latitude;
    private double longitude;

    public GeoPointWrapper(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Metody Parcelable
    protected GeoPointWrapper(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<GeoPointWrapper> CREATOR = new Creator<GeoPointWrapper>() {
        @Override
        public GeoPointWrapper createFromParcel(Parcel in) {
            return new GeoPointWrapper(in);
        }

        @Override
        public GeoPointWrapper[] newArray(int size) {
            return new GeoPointWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
