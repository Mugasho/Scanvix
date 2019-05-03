package com.scriptfloor.scanvix.model;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.scriptfloor.scanvix.fragment.ContentFragment;

import java.util.ArrayList;

/**
 * Created by LINCOLN on 3/11/2019.
 */

public class ContentPage extends Page {
    public ContentPage(ModelCallbacks callbacks, String title) {
        super(callbacks, title);
    }

    @Override
    public Fragment createFragment() {
        return ContentFragment.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        dest.add(new ReviewItem(getTitle(), mData.getString(SIMPLE_DATA_KEY), getKey()));

    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(SIMPLE_DATA_KEY));
    }

    public ContentPage setValue(String value) {
        mData.putString(SIMPLE_DATA_KEY, value);
        return this;
    }
}
