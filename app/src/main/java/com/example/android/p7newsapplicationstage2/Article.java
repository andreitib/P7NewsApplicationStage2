package com.example.android.p7newsapplicationstage2;

import java.util.Date;

/**
 * An {@link Article} object contains information related to a single news.
 */
public class Article {
    /**
     * Article title for news
     */
    private String mTitle;
    /**
     * Article section for the news
     */
    private String mSection_Article;
    /**
     * Date of the article news
     */
    private Date mPublicationDate;
    /**
     * Author of the article news
     */
    private String mNameAuthor;
    /**
     * Website URL of the article news
     */
    private String mUrl;

    /**
     * Constructs a new {@link Article} object
     *
     * @param articleTitle   is the title of the article
     * @param articleSection is the Section of the article news
     * @param arcticleDate   is the date of publication of the news on the GUARDIAN website
     * @param arcticleAuthor is the author of the article
     * @param Url            is the url of the article news
     */
    Article(String articleTitle, String articleSection, Date arcticleDate, String arcticleAuthor, String Url) {
        mTitle = articleTitle;
        mSection_Article = articleSection;
        mPublicationDate = arcticleDate;
        mNameAuthor = arcticleAuthor;
        mUrl = Url;
    }

    /**
     * Returns the article title of the Guardian Newspaper JSON news
     */
    public String getArticle_title() {
        return mTitle;
    }

    /**
     * Returns the article section of the article news
     */
    public String getArticle_section() {
        return mSection_Article;
    }

    /**
     * Returns the date of the article news
     */
    public Date getDate() {
        return mPublicationDate;
    }

    /**
     * Returns the author of the article
     */
    public String getAuthor() {
        return mNameAuthor;
    }

    /**
     * Returns the website url of the article news
     */
    public String getUrl() {
        return mUrl;
    }

}
