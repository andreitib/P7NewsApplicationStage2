package com.example.android.p7newsapplicationstage2;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Helper methods related to requesting and receiving news data from the Guardian json.
 */
public final class QueryUtils {

    /**
     * Tag for log message
     */
    private static final String LOD_TAG = QueryUtils.class.getSimpleName();

    /**
     * shortcuts used for json responses
     */
    private static final String json_tags = "tags";
    private static final String json_publicationDate = "webPublicationDate";
    private static final String json_title = "webTitle";
    private static final String json_sectionName = "sectionName";
    private static final String json_response = "response";
    private static final String json_results = "results";
    private static final String json_url = "webUrl";
    private static final String json_requestmethod = "GET";
    private static final int the_index = 0;
    private static final int jsnon_readtimeout=10000;
    private static final int json_connectiontimeout=15000;

    /**
     * building and manipulating my uri url requests
     */
    public static List<Article> fetchArcticlesAppData(String requestUrl) {
        //Create URL object
        URL url = createUrl(requestUrl);
        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOD_TAG, "There is a problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link NewsApp}s
        // Return the list of {@link NewsApp}s
        return extractFeatureFromJson(jsonResponse);
    }

    /**
     * Return new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOD_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Date Helper
     */
    private static String formatDate(String dateData) {
        String guardianJsonDateFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat jsonDateFormatter = new SimpleDateFormat(guardianJsonDateFormat, Locale.UK);
        try {
            Date jsonDateToParse = jsonDateFormatter.parse(dateData);
            String resultDate = "MMM d, yyy";
            SimpleDateFormat resultDateFormatter = new SimpleDateFormat(resultDate, Locale.UK);
            return resultDateFormatter.format(jsonDateToParse);
        } catch (ParseException e) {
            Log.e(LOD_TAG, "There is an error Formatting the Json Date", e);
            return "";
        }
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(jsnon_readtimeout);
            urlConnection.setConnectTimeout(json_connectiontimeout);
            urlConnection.setRequestMethod(json_requestmethod);
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOD_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOD_TAG, "Problem retrieving the Article News Json result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * <p>
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Article} objects that has been built up from
     * <p>
     * parsing the given JSON response.
     */
    private static List<Article> extractFeatureFromJson(String newsAppJson) {
        // Create an empty ArrayList that we can start adding newsApps to
        List<Article> newsApps = new ArrayList<>();
        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            //Create a JSONObject from the JSON response string
            JSONObject jsonResponse = new JSONObject(newsAppJson);
            JSONObject jsonResults = jsonResponse.getJSONObject(json_response);
            // Extract the JSONArray associated with the key called "results",
            JSONArray resultsArray = jsonResults.getJSONArray(json_results);
            // For each article news in the newsAppArray, create an {@link NewsApp} object
            for (int i = 0; i < resultsArray.length(); i++) {
                //Get a single article news at position i within the list of newsApps
                JSONObject currentArticlesApp = resultsArray.getJSONObject(i);
                // Extract the value for the key called "sectionName"
                String secName = currentArticlesApp.getString(json_sectionName);

                // Extract the value for the key called "webPublicationDate"
                String originalPublicationDate = currentArticlesApp.getString(json_publicationDate);

                //Format publication date
                Date publicationDate = null;
                try {
                    publicationDate = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")).parse(originalPublicationDate);
                } catch (Exception e) {
                    Log.e("QueryUtils", "Problem parsing the news date", e);
                }

                // Extract the value for the key called "webTitle"
                String artTitle = currentArticlesApp.getString(json_title);
                //Extract the value for the key called"webUrl"
                String url = currentArticlesApp.getString(json_url);

                //Extract the value of the author
                String articleAuthor = null;
                JSONArray tagsArray = currentArticlesApp.getJSONArray(json_tags);
                if (!tagsArray.isNull(the_index)) {
                    JSONObject currentStoryTags = tagsArray.getJSONObject(the_index);
                    if (currentArticlesApp.has(json_title)) {
                        articleAuthor = currentStoryTags.getString(json_title);
                    }
                }
                // Create a new {@link NewsApp} object with the artTitle, secName,publicationDate,articleAuthor and url.
                Article JSONarcticles = new Article(artTitle, secName,publicationDate, articleAuthor, url);
                // and url from the JSON response.
                newsApps.add(JSONarcticles);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOD_TAG, "Problem parsing the ArcticlesApp JSON results", e);
        }
        //Return the list of newsApps
        return newsApps;
    }

}