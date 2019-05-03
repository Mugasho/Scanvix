package com.scriptfloor.scanvix.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.scriptfloor.scanvix.R;
import com.scriptfloor.scanvix.model.AnswerModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LINCOLN on 4/7/2019.
 */

public class ReviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<AnswerModel> mCurrentReviewItems;
    public ReviewAdapter(Context context, ArrayList<AnswerModel> mCurrentReviewItems) {
        this.context=context;
        this.mCurrentReviewItems=mCurrentReviewItems;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public Object getItem(int position) {
        return mCurrentReviewItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mCurrentReviewItems.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup container) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View rootView = inflater.inflate(R.layout.list_item_review, container, false);

        AnswerModel answer = mCurrentReviewItems.get(position);
        float[] arr={(answer.getHighest()*100)/3,(answer.getHigher()*100)/8,(answer.getHigh()*100)/9,(answer.getLower()*100)/11, (answer.getLowest()*100)/1};

        String value = largest(arr)+"";
        if (TextUtils.isEmpty(value)) {
            value = "(None)";
        }
        ((TextView) rootView.findViewById(android.R.id.text1)).setText(answer.date_added+": Review");
        ((TextView) rootView.findViewById(android.R.id.text2)).setText(value);
        return rootView;
    }

    @Override
    public int getCount() {
        return mCurrentReviewItems.size();
    }

    // Method to find maximum in arr[]
    static float largest(float[]arr)
    {
        int i;

        // Initialize maximum element
        float max = arr[0];

        // Traverse array elements from second and
        // compare every element with current max
        for (i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];

        return max;
    }
}
