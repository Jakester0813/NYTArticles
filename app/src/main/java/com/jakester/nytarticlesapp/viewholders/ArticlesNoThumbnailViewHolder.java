package com.jakester.nytarticlesapp.viewholders;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.databinding.ArticlesLayoutBinding;
import com.jakester.nytarticlesapp.databinding.ArticlesNoThumbnailBinding;
import com.jakester.nytarticlesapp.models.Article;

/**
 * Created by Jake on 9/23/2017.
 */

public class ArticlesNoThumbnailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TextView mHeadline, mSnippet;
    Article mArticle;
    Context mContext;

    public ArticlesNoThumbnailViewHolder(View view, Context pContext) {
        super(view);
        mContext = pContext;
        this.mHeadline = (TextView) view.findViewById(R.id.tv_headline);
        this.mSnippet = (TextView) view.findViewById(R.id.tv_snippet);
    }

    public void bind(Article pArticle){
        mArticle = pArticle;
        if(mArticle.getHeadline().getMain() != null){
            mHeadline.setText(mArticle.getHeadline().getMain());
        }
        else{
            mHeadline.setText(mArticle.getHeadline().getPrintHeadline());
        }
        mSnippet.setText(mArticle.getSnippet());

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mArticle.getWebUrl());

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_name);
        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setActionButton(bitmap, "Share Article", pendingIntent, true);
        CustomTabsIntent customIntent = builder.build();
        customIntent.launchUrl(mContext, Uri.parse(mArticle.getWebUrl()));
    }
}