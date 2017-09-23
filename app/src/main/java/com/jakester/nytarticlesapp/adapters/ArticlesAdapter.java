package com.jakester.nytarticlesapp.adapters;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.models.Article;
import com.jakester.nytarticlesapp.viewholders.ArticlesNoThumbnailViewHolder;
import com.jakester.nytarticlesapp.viewholders.ArticlesViewHolder;

import java.util.List;

/**
 * Created by Jake on 9/19/2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter {

    Context mContext;
    List<Article> mArticles;
    private final int ARTICLE = 0, ARTICLE_NO_IMAGE = 1;

    public ArticlesAdapter(Context pContext, List<Article> pArticles){
        mContext = pContext;
        mArticles = pArticles;
    }

    public void addList(List<Article> items){
        mArticles.addAll(items);
        notifyDataSetChanged();
    }

    public void clearList(){
        mArticles.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case ARTICLE:
                View v1 = inflater.inflate(R.layout.articles_layout, viewGroup, false);
                viewHolder = new ArticlesViewHolder(v1, mContext);
                break;
            case ARTICLE_NO_IMAGE:
                View v2 = inflater.inflate(R.layout.articles_no_thumbnail, viewGroup, false);
                viewHolder = new ArticlesNoThumbnailViewHolder(v2, mContext);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Article article = mArticles.get(position);
        switch (holder.getItemViewType()){
            case ARTICLE:
                ArticlesViewHolder articlesViewHolder = (ArticlesViewHolder) holder;
                articlesViewHolder.bind(article);
                break;
            case ARTICLE_NO_IMAGE:
                ArticlesNoThumbnailViewHolder articlesNoThumb = (ArticlesNoThumbnailViewHolder) holder;
                articlesNoThumb.bind(article);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    @Override
    public int getItemViewType(int position) {
        mArticles.get(position).setImageUrl();
        if (mArticles.get(position).getImageUrl() != null) {
            return ARTICLE;
        } else {
            return ARTICLE_NO_IMAGE;
        }
    }
}
