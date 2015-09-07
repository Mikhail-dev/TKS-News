package com.mikhaildev.tsknews.ui.activity;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaildev.tsknews.R;
import com.mikhaildev.tsknews.controller.NewsLoader;
import com.mikhaildev.tsknews.exception.ApiException;
import com.mikhaildev.tsknews.exception.NetworkConnectionException;
import com.mikhaildev.tsknews.model.AsyncResult;
import com.mikhaildev.tsknews.model.News;
import com.mikhaildev.tsknews.util.DateUtils;
import com.mikhaildev.tsknews.util.StringUtils;

import java.util.Date;

import butterknife.Bind;

/**
 * Created by E.Mikhail on 07.09.2015.
 */
public class DetailActivity extends BaseActivity implements LoaderManager.LoaderCallbacks {

    private final int LOADER_ID_NEWS_GET_CONTENT = 1;
    private final String NEWS_ID = "news_id";

    private long newsId = -1;

    @Bind(R.id.title) TextView title;
    @Bind(R.id.date) TextView date;
    @Bind(R.id.content) TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().getExtras().containsKey(StringUtils.EXTRA_NEWS_ID)) {
            Toast.makeText(this, getString(R.string.news_not_found), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        newsId = getIntent().getExtras().getLong(StringUtils.EXTRA_NEWS_ID);
        getLayoutResource();
        getSupportLoaderManager().initLoader(LOADER_ID_NEWS_GET_CONTENT, null, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.a_detail;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong(NEWS_ID, newsId);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        newsId = savedInstanceState.getLong(NEWS_ID);
    }

    @Override
    public Loader onCreateLoader(int loaderId, Bundle bundle) {
        showProgressDialog();
        switch (loaderId) {
            case LOADER_ID_NEWS_GET_CONTENT:
                return new NewsLoader(this, newsId);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        hideProgressDialog();
        switch (loader.getId()) {
            case LOADER_ID_NEWS_GET_CONTENT:
                handleData(data);
                break;
        }
    }

    private void handleData(Object data) {
        AsyncResult<News> result = (AsyncResult<News>) data;
        if (result.getData() != null) {
            News news = result.getData();
            title.setText(Html.fromHtml(news.getHeadline().getText()));
            date.setText(DateUtils.formatShortDate(new Date(news.getHeadline().getPublicationDate())));
            content.setText(Html.fromHtml(news.getContent()));
        } else {
            handleException(result.getException());
            finish();
        }
    }

    private void handleException(Exception e) {
        if (e.getClass().equals(NetworkConnectionException.class)) {
            Toast.makeText(this, R.string.internet_connection_error, Toast.LENGTH_LONG).show();
        } else if (e.getClass().equals(ApiException.class)) {
            ApiException apiException = (ApiException) e;
            Toast.makeText(this, getString(apiException.getMessageResourceId()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
}
