package com.mikhaildev.tsknews.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.mikhaildev.tsknews.R;
import com.mikhaildev.tsknews.controller.HeadlinesLoader;
import com.mikhaildev.tsknews.database.HeadlineTable;
import com.mikhaildev.tsknews.database.TksNewsProvider;
import com.mikhaildev.tsknews.exception.ApiException;
import com.mikhaildev.tsknews.exception.NetworkConnectionException;
import com.mikhaildev.tsknews.model.AsyncResult;
import com.mikhaildev.tsknews.ui.activity.DetailActivity;
import com.mikhaildev.tsknews.util.StringUtils;
import com.mikhaildev.tsknews.view.DividerItemDecoration;
import com.mikhaildev.tsknews.view.adapter.HeadlinesAdapter;

import butterknife.Bind;


public class HeadlinesFragment extends BaseFragment
        implements
            SwipeRefreshLayout.OnRefreshListener,
            LoaderManager.LoaderCallbacks,
            HeadlinesAdapter.HeadlineListener {

    private final int LOADER_ID_UPDATE_HEADLINES = 1;
    private final int LOADER_ID_GET_HEADLINES = 2;

    @Bind(R.id.news_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.swipe_container) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.info) View info;

    private HeadlinesAdapter adapter;

    public HeadlinesFragment() {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.f_headlines;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        getActivity().getSupportLoaderManager().initLoader(LOADER_ID_GET_HEADLINES, null, this);
    }

    @Override
    public Loader onCreateLoader(int loaderId, Bundle bundle) {
        switch (loaderId) {
            case LOADER_ID_UPDATE_HEADLINES:
                return new HeadlinesLoader(getActivity());
            case LOADER_ID_GET_HEADLINES:
                return new CursorLoader(getActivity(),
                        TksNewsProvider.CONTENT_URI,
                        HeadlineTable.PROJECTION,
                        null,
                        null,
                        HeadlineTable.DEFAULT_SORT_ORDER
                );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        switch (loader.getId()) {
            case LOADER_ID_UPDATE_HEADLINES:
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                handleData(data);
                break;
            case LOADER_ID_GET_HEADLINES:
                Cursor cursor = (Cursor) data;
                if (cursor!=null && cursor.getCount()>0)
                    info.setVisibility(View.GONE);
                if (adapter==null) {
                    adapter = new HeadlinesAdapter(cursor, this);
                    recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.swapCursor(cursor);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }

    private void handleData(Object data) {
        AsyncResult<Boolean> result = (AsyncResult<Boolean>) data;
        if (result.getData() != null) {
            boolean success = result.getData();
            info.setVisibility(View.GONE);
            //nothing to do. we automatically update data in cursorAdapter
        } else {
            handleException(result.getException());
        }
    }

    private void handleException(Exception e) {
        if (e.getClass().equals(NetworkConnectionException.class)) {
            Toast.makeText(getActivity(), R.string.internet_connection_error, Toast.LENGTH_LONG).show();
        } else if (e.getClass().equals(ApiException.class)) {
            ApiException apiException = (ApiException) e;
            Toast.makeText(getActivity(), getString(apiException.getMessageResourceId()), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {
        if (adapter!=null) adapter.swapCursor(null);
    }

    @Override
    public void onRefresh() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID_UPDATE_HEADLINES, null, this);
    }

    @Override
    public void onHeadlineClicked(long headlineId) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(StringUtils.EXTRA_NEWS_ID, headlineId);
        startActivity(intent);
    }
}
