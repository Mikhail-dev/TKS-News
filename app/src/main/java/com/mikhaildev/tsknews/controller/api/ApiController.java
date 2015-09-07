package com.mikhaildev.tsknews.controller.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaildev.tsknews.R;
import com.mikhaildev.tsknews.exception.ApiException;
import com.mikhaildev.tsknews.exception.NetworkConnectionException;
import com.mikhaildev.tsknews.model.Headline;
import com.mikhaildev.tsknews.model.News;
import com.mikhaildev.tsknews.util.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;


public class ApiController {

    private static final String APP_URL    = "https://api.tinkoff.ru";
    private static final String URL_HEADLINES = APP_URL + "/v1/news";
    private static final String URL_NEWS = APP_URL + "/v1/news_content?";

    private static final Object lock = new Object();
    private static ApiController instance;

    private OkHttpClient client;


    public static ApiController getInstance() {
        if (instance==null) {
            synchronized (lock) {
                if (instance==null)
                    instance = new ApiController();
            }
        }
        return instance;
    }

    private ApiController() {
        client = new OkHttpClient();
    }


    /**
     * Returns massive of {@link Headline}
     * @param context
     * @return Headline[]
     * @throws IOException
     */
    public Headline[] getHeadlines(Context context) throws IOException {
        if (!Utils.isThereInternetConnection(context))
            throw new NetworkConnectionException();

        Request request = getDefaultRequest(URL_HEADLINES);

        Response response = client.newCall(request).execute();
        String data = response.body().string();
        Type type = new TypeToken<Headline[]>() { }.getType();
        Headline[] headlines = new Headline[0];
        try {
            headlines = new Gson().fromJson(new JSONObject(data).getString("payload"), type);
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return headlines;
    }

    /**
     * Returns {@link News}
     * @param context
     * @return News
     * @throws IOException
     */
    public News getNews(Context context, long newsId) throws IOException {
        if (!Utils.isThereInternetConnection(context))
            throw new NetworkConnectionException();

        Request request = getDefaultRequest(URL_NEWS + "id=" + newsId);

        Response response = client.newCall(request).execute();
        String data = response.body().string();
        News news = null;

        try {
            String newsData = new JSONObject(data).getString("payload");
            news = new Gson().fromJson(newsData, News.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (news==null)
            throw new ApiException(R.string.news_not_found);

        return news;
    }

    private Request getDefaultRequest(String url) {
        return new Request.Builder()
                .url(url)
                .build();
    }

    private IOException getException(int responseCode) {
        return getException(null, responseCode);
    }

    private IOException getException(Exception e, int responseCode) {
        if (e!=null
                && (e.getClass().equals(java.net.UnknownHostException.class) || e.getClass().equals(java.net.SocketTimeoutException.class))) {
            return new NetworkConnectionException();
        } else if (HttpURLConnection.HTTP_INTERNAL_ERROR == responseCode) {
            return new ApiException(new Exception("Response code " + responseCode), R.string.server_error, responseCode);
        } else {
            return new ApiException(new Exception("Response code " + responseCode), R.string.error_code_frmt, responseCode);
        }
    }
}
