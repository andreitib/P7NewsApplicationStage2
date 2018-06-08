package com.example.android.p7newsapplicationstage2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link ArticleAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link Article} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context of the app
     * @param newsfeed is the list of newsfeed, which is the data source of the adapter
     */
    public ArticleAdapter(Context context, List<Article> newsfeed) {
        super(context, 0, newsfeed);
    }
    /**
     * Returns a list item view that displays information about every news at the given position
     * in the list of newsfeeds.
     */
    static class ViewHolder {
        TextView  titlearticle;
        TextView  sectionarticle;
        TextView  authorarticle;
        TextView  articledate=null;
        TextView  articletime=null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.articles_list_item, parent, false);
            holder = new ViewHolder();
            holder.titlearticle = (TextView) convertView.findViewById(R.id.article_title);
            holder.sectionarticle = (TextView) convertView.findViewById(R.id.article_section);
            holder.articledate = (TextView)convertView.findViewById(R.id.article_date);
            holder.articletime =(TextView) convertView.findViewById(R.id.article_time);
            holder.authorarticle = (TextView) convertView.findViewById(R.id.author);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Find the news at the given position in the list of newsfeeds
        Article currentArticle = getItem(position);
        //ID article_title
        assert currentArticle != null;
        holder.titlearticle.setText(currentArticle.getArticle_title());

        //ID article_section
        holder.sectionarticle.setText(currentArticle.getArticle_section());

        // ID date
        // Format the date string
        String formattedDate = formatDate(currentArticle.getDate()).concat(",");
        // Display the date of the current earthquake in that TextView
        holder.articledate.setText(formattedDate);

        // ID time
        // Format the time string
        String formattedTime = formatTime(currentArticle.getDate());
        // Display the time of the current earthquake in that TextView
        holder.articletime.setText(formattedTime);

        //ID author
        holder.authorarticle .setText(currentArticle.getAuthor());
        return convertView;
    }

    /**
     * Return the formatted date string from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }


}
