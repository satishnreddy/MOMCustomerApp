package com.mom.momcustomerapp.controllers.updateapp;
/*
 * Created by nishant on 13/07/17.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VersionModel implements Parcelable {
    public static final Parcelable.Creator<VersionModel> CREATOR = new Parcelable.Creator<VersionModel>() {
        @Override
        public VersionModel createFromParcel(Parcel source) {
            return new VersionModel(source);
        }

        @Override
        public VersionModel[] newArray(int size) {
            return new VersionModel[size];
        }
    };
    private int currentVersion;
    private int forcedVersions;
    private String currentVersionName;
    private String url;
    private List<WhatsNewModel> whatsnew;

    public VersionModel() {
    }

    public VersionModel(int currentVersion, int forcedVersions, String currentVersionName, String url, List<WhatsNewModel> whatsnew) {
        this.currentVersion = currentVersion;
        this.forcedVersions = forcedVersions;
        this.currentVersionName = currentVersionName;
        this.url = url;
        this.whatsnew = whatsnew;
    }

    protected VersionModel(Parcel in) {
        this.currentVersion = in.readInt();
        this.forcedVersions = in.readInt();
        this.currentVersionName = in.readString();
        this.url = in.readString();
        this.whatsnew = new ArrayList<WhatsNewModel>();
        in.readList(this.whatsnew, WhatsNewModel.class.getClassLoader());
    }

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getForcedVersions() {
        return forcedVersions;
    }

    public void setForcedVersions(int forcedVersions) {
        this.forcedVersions = forcedVersions;
    }

    public String getCurrentVersionName() {
        return currentVersionName;
    }

    public void setCurrentVersionName(String currentVersionName) {
        this.currentVersionName = currentVersionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<WhatsNewModel> getWhatsnew() {
        return whatsnew;
    }

    public void setWhatsnew(List<WhatsNewModel> whatsnew) {
        this.whatsnew = whatsnew;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentVersion);
        dest.writeInt(this.forcedVersions);
        dest.writeString(this.currentVersionName);
        dest.writeString(this.url);
        dest.writeList(this.whatsnew);
    }
}
