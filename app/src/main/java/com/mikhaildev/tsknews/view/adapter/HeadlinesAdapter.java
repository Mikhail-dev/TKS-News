package com.mikhaildev.tsknews.view.adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikhaildev.tsknews.R;
import com.mikhaildev.tsknews.database.HeadlineTable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HeadlinesAdapter extends CursorRecyclerAdapter {

    public interface HeadlineListener {
        void onHeadlineClicked(long headlineId);
    }

    private HeadlineListener mCallback;

    public HeadlinesAdapter(Cursor cursor, HeadlineListener callback) {
        super(cursor);
        mCallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_headline, parent, false);
        return new HeadlineViewHolder(v);
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        if (holder instanceof HeadlineViewHolder) {
            HeadlineViewHolder headlineHolder = (HeadlineViewHolder) holder;
            String text = cursor.getString(cursor.getColumnIndex(HeadlineTable.COLUMN_TEXT));
            long id  = cursor.getInt(cursor.getColumnIndex(HeadlineTable.COLUMN_ID));
            headlineHolder.setHeadlineId(id);
            headlineHolder.headlineTV.setText(Html.fromHtml(text));
        }
    }


    class HeadlineViewHolder extends RecyclerView.ViewHolder  {

        private long headlineId;

        @Bind(R.id.headline_tv) TextView headlineTV;
        @Bind(R.id.headline) View headline;

        HeadlineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setHeadlineId(long id) {
            headlineId = id;
        }

        @OnClick(R.id.headline)
        public void onHeadlineClicked() {
            mCallback.onHeadlineClicked(headlineId);
        }
    }
}
