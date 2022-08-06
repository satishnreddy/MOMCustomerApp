package com.mom.momcustomerapp.controllers.updateapp;
/*
 * Created by nishant on 13/07/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class WhatsNewModel implements Parcelable {
    private int code;
    private String name;
    private String changelog;

    public WhatsNewModel() {
    }

    public WhatsNewModel(int code, String name, String changelog) {
        this.code = code;
        this.name = name;
        this.changelog = changelog;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.name);
        dest.writeString(this.changelog);
    }

    protected WhatsNewModel(Parcel in) {
        this.code = in.readInt();
        this.name = in.readString();
        this.changelog = in.readString();
    }

    public static final Parcelable.Creator<WhatsNewModel> CREATOR = new Parcelable.Creator<WhatsNewModel>() {
        @Override
        public WhatsNewModel createFromParcel(Parcel source) {
            return new WhatsNewModel(source);
        }

        @Override
        public WhatsNewModel[] newArray(int size) {
            return new WhatsNewModel[size];
        }
    };
}
