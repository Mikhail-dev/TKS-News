package com.mikhaildev.tsknews.util;


public class StringUtils {

    public static final String HEADLINE_FRAGMENT = "headline_fragment";
    public static final String EXTRA_NEWS_ID = "extra_news_id";

    private StringUtils() {
        //Empty
    }

    public static boolean isNullOrEmpty(final String string) {
        return string == null || string.length()==0;
    }
}
