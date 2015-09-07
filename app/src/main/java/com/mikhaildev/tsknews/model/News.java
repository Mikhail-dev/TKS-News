package com.mikhaildev.tsknews.model;

/**
 * Created by E.Mikhail on 07.09.2015.
 */
public class News {

    private Headline title;
    private String content;

    public Headline getHeadline() {
        return title;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "News{" +
                "title=" + title +
                ", content='" + content + '\'' +
                '}';
    }
}
