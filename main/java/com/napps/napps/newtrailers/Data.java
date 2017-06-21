package com.napps.napps.newtrailers;

/**
 * Created by Rewan on 2017-06-08.
 */

class Data {

    private String mTitle;
    private String mID;
    private String mDescription;

    public Data(String mTitle, String mID, String mDescription) {
        this.mTitle = mTitle;
        this.mID = mID;
        this.mDescription = mDescription;
    }
    public Data(String mTitle, String mVideoID) {
        this.mTitle = mTitle;
        this.mID = mVideoID;
        this.mDescription = "";
    }
    public Data(){
        this.mTitle = "";
        this.mID = "";
        this.mDescription = "";

    }

     String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    @Override
    public String toString() {
        return "Data{" +
                "mTitle='" + mTitle + '\'' +
                ", mID='" + mID + '\'' +
                ", mDescription='" + mDescription + '\'' +
                '}';
    }
}
