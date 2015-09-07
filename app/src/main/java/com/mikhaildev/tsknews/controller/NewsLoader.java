package com.mikhaildev.tsknews.controller;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.mikhaildev.tsknews.model.AsyncResult;
import com.mikhaildev.tsknews.model.News;


public class NewsLoader extends AsyncTaskLoader<AsyncResult<News>> {

    private AsyncResult<News> mResult;
    private long mNewsId;

    public NewsLoader(Context context, long newsId) {
        super(context);
        mNewsId = newsId;
    }

    @Override
    public AsyncResult<News> loadInBackground() {
        AsyncResult<News> data = new AsyncResult<News>();
        try {
            data.setData(ContentController.getInstance().getNews(getContext(), mNewsId));
        } catch (Exception e) {
            data.setException(e);
        }
        return data;
    }

    @Override
    public void deliverResult(AsyncResult<News> result) {
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
