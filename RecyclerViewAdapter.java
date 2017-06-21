package com.napps.napps.newtrailers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Rewan on 2017-06-08.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder> {
    private static final String TAG = "YTRecyclerViewAdapt";
    private List<Data> mList;
    private Context mContext;
    private Type mType;

    public RecyclerViewAdapter(Context context, List<Data> dataList) {
        mContext = context;
        mList = dataList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Called by the layout manager when it needs a new view
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browse, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Called by the layout manager when it wants new data in an existing row

        if ((mList == null) || (mList.size() == 0)) {
            holder.thumbnail.setImageResource(R.drawable.placeholder);
            holder.title.setText(R.string.empty_photo);
            holder.description.setText("");
        } else {
            String imageUrl = "";
            Data dataItem = mList.get(position);
            Log.d(TAG, "onBindViewHolder: " + dataItem.getTitle() + " --> " + position);
            if (mType == Type.YouTube) {
                imageUrl = "https://img.youtube.com/vi/" + dataItem.getID() + "/mqdefault.jpg";
            } else if (mType == Type.Netflix) {
                imageUrl = dataItem.getID();
            }

            Picasso.with(mContext).load(imageUrl)
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(holder.thumbnail);

            holder.title.setText(dataItem.getTitle());
            holder.description.setText(dataItem.getDescription());

        }
    }

    @Override
    public int getItemCount() {
        return ((mList != null) && (mList.size() != 0) ? mList.size() : 1);
    }

    void loadNewMovie(List<Data> newData, Type mType) {
        mList = newData;
        notifyDataSetChanged();
        this.mType = mType;
    }

    public Data getMovie(int position) {
        return ((mList != null) && (mList.size() != 0) ? mList.get(position) : null);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;
        TextView description = null;

        public ImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "ImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
            this.description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}