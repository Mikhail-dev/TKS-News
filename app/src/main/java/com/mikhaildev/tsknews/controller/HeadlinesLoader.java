package com.mikhaildev.tsknews.controller;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.mikhaildev.tsknews.model.AsyncResult;


public class HeadlinesLoader extends AsyncTaskLoader<AsyncResult<Boolean>> {

    private AsyncResult<Boolean> mResult;


    public HeadlinesLoader(Context context) {
        super(context);
    }

    @Override
    public AsyncResult<Boolean> loadInBackground() {
        AsyncResult<Boolean> data = new AsyncResult<Boolean>();
        try {
            data.setData(ContentController.getInstance().updateHeadlines(getContext()));
        } catch (Exception e) {
            data.setException(e);
        }
        return data;
    }

    @Override
    public void deliverResult(AsyncResult<Boolean> result) {
        mResult = result;
        if (isStarted()) {
            super.deliverResult(result);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult);
        }

        if (takeContentChanged() || mResult == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        mResult = null;
    }
}
