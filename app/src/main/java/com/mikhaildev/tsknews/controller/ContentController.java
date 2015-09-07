package com.mikhaildev.tsknews.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mikhaildev.tsknews.controller.api.ApiController;
import com.mikhaildev.tsknews.database.HeadlineTable;
import com.mikhaildev.tsknews.database.TksNewsProvider;
import com.mikhaildev.tsknews.model.Headline;
import com.mikhaildev.tsknews.model.News;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ContentController {

    private static ContentController instance;
    private static final Object lock = new Object();


    private ContentController() {

    }

    public static ContentController getInstance() {
        if (instance==null) {
            synchronized (lock) {
                if (instance==null)
                    instance = new ContentController();
            }
        }
        return instance;
    }

    public boolean updateHeadlines(Context context) throws IOException {
        Headline[] headlines = ApiController.getInstance().getHeadlines(context);

        if (headlines==null || headlines.length==0)
            return true;

        Set<Long> availableHeadlineIds = new HashSet();
        Cursor cursor = context.getContentResolver().query(TksNewsProvider.CONTENT_URI, HeadlineTable.PROJECTION, null, null, null);
        if (cursor!=null && cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(HeadlineTable.COLUMN_ID));
                availableHeadlineIds.add(id);
            }
            cursor.close();
        }

        List<ContentValues> values = new ArrayList<>();
        for (int i = 0; i < headlines.length; i++) {
            /* new instance - it's expensive operation for android, so we don't create excess objects */
            if (!availableHeadlineIds.contains(headlines[i].getId())) {
                ContentValues value = new ContentValues();
                value.put(HeadlineTable.COLUMN_ID, headlines[i].getId());
                value.put(HeadlineTable.COLUMN_NAME, headlines[i].getName());
                value.put(HeadlineTable.COLUMN_TEXT, headlines[i].getText());
                value.put(HeadlineTable.COLUMN_PUBLICATION_DATE, headlines[i].getPublicationDate());
                value.put(HeadlineTable.COLUMN_BANK_INFO_TYPE_ID, headlines[i].getBankInfoTypeId());
                values.add(value);
            }
        }

        if (values.size()>0)
            context.getContentResolver().bulkInsert(TksNewsProvider.CONTENT_URI, values.toArray(new ContentValues[values.size()]));
        return true;
    }

    public News getNews(Context context, long newsId) throws IOException {
        return ApiController.getInstance().getNews(context, newsId);
    }
}
