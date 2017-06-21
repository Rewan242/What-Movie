package com.napps.napps.newtrailers;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rewan on 2017-06-08.
 */
enum Type {
    YouTube, Netflix
}

class GetXmlData extends AsyncTask<String, Void, List<Data>> implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetXmlData";

    private List<Data> mDataList = null;
    private Type mType;
    private final OnDataAvailable mCallBack;
    private String mFeedSource = "";

    interface OnDataAvailable {
        void onDataAvailable(List<Data> data, DownloadStatus status, Type mType);
    }

    public GetXmlData(OnDataAvailable callBack, String feedSource) {
        Log.d(TAG, "GetFlickrJsonData called");
        mCallBack = callBack;
        mFeedSource = feedSource;
    }

    @Override
    protected void onPostExecute(List<Data> movies) {
        Log.d(TAG, "onPostExecute starts");

        if (mCallBack != null) {
            mCallBack.onDataAvailable(mDataList, DownloadStatus.OK, mType);
        }
        Log.d(TAG, "onPostExecute ends");
    }

    @Override
    protected List<Data> doInBackground(String... params) {
        Log.d(TAG, "doInBackground starts");
        GetRawData getRawData = new GetRawData(this);
        getRawData.runInSameThread(params[0]);
        Log.d(TAG, "doInBackground ends");
        return mDataList;
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {
        Log.d(TAG, "onDownloadComplete starts. Status = " + status);
        if (status == DownloadStatus.OK) {
            mDataList = new ArrayList<>();
            boolean inEntry = false;
            String textValue = "";

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(data));
                int eventType = xpp.getEventType();

                if (mFeedSource.equals("youtube")) {
                    mType = Type.YouTube;
                    String title = "";
                    String videoID = "";
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = xpp.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
//                        Log.d(TAG, "parse: Starting tag for " + tagName);
                                if ("entry".equalsIgnoreCase(tagName)) {
                                    inEntry = true;
                                }
                                if ("thumbnail".equalsIgnoreCase(tagName)) {
                                    textValue = xpp.getAttributeValue(null, "url");
                                    Log.d(TAG, "onDownloadComplete " + textValue);
                                }
                                break;

                            case XmlPullParser.TEXT:
                                textValue = xpp.getText();
                                break;

                            case XmlPullParser.END_TAG:
//                        Log.d(TAG, "parse: Ending tag for " + tagName);
                                if (inEntry) {
                                    if ("title".equalsIgnoreCase(tagName)) {
                                        title = textValue;
                                    } else if ("videoId".equalsIgnoreCase(tagName)) {
                                        videoID = textValue;
                                    } else if ("entry".equalsIgnoreCase(tagName)) {
                                        Data dataObject = new Data(title, videoID);
                                        mDataList.add(dataObject);
//                                    Log.d(TAG, "onDownloadComplete " + dataObject.toString());
                                        inEntry = false;
                                    }
                                }
                                break;
                            default:
                        }
                        eventType = xpp.next();
                    }

                } else if (mFeedSource.equals("netflix")) {
//                    Parsing data from netflix rss feed
                    mType = Type.Netflix;
                    String title = "";
                    String imageID = "";
                    String description = "";
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        String tagName = xpp.getName();
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                if ("item".equalsIgnoreCase(tagName)) {
                                    inEntry = true;
                                }
                                break;

                            case XmlPullParser.TEXT:
                                textValue = xpp.getText();
                                break;

                            case XmlPullParser.END_TAG:
                                if (inEntry) {
                                    if ("title".equalsIgnoreCase(tagName)) {
                                        title = textValue;
                                    } else if ("link".equalsIgnoreCase(tagName)) {
                                       imageID = textValue;
                                        imageID = imageID.substring(imageID.lastIndexOf('/') + 1);
                                   imageID = "https://secure.netflix.com/us/boxshots/ghd/" + imageID + ".jpg";
                                    } else if ("description".equalsIgnoreCase(tagName)) {
                                        Log.d(TAG, "Description start ");
                                        description = textValue;
                                        description = description.substring(description.lastIndexOf('>') + 1);
                                    } else if ("item".equalsIgnoreCase(tagName)) {
                                        Data dataObject = new Data(title, imageID, description);
                                        mDataList.add(dataObject);
                                        Log.d(TAG, "onDownloadComplete " + dataObject.toString());
                                        inEntry = false;
                                    }
                                }
                                break;
                            default:
                        }
                        eventType = xpp.next();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
