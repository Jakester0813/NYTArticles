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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.databinding.ArticlesLayoutBinding;
import com.jakester.nytarticlesapp.models.Article;

/**
 * Created by Jake on 9/23/2017.
 */

public class ArticlesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView mArticleImage;
    TextView mHeadline, mSnippet;
    Article mArticle;
    Context mContext;

    public ArticlesViewHolder(View view, Context pContext){
        super(view);
        this.mArticleImage = (ImageView) view.findViewById(R.id.iv_image);
        this.mHeadline = (TextView) view.findViewById(R.id.tv_headline);
        this.mSnippet = (TextView) view.findViewById(R.id.tv_snippet);
        this.mContext = pContext;
    }

    public void bind(Article pArticle){
        mArticle = pArticle;
        mArticle.setImageUrl();
        if(mArticle.getImageUrl() != null) {
            Glide.with(mContext).load(mArticle.getImageUrl()).into(mArticleImage);
            mArticleImage.setVisibility(View.VISIBLE);
        }
        mHeadline.setText(mArticle.getHeadline().getMain());
        mSnippet.setText(mArticle.getSnippet());

    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, mArticle.getWebUrl());

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_action_name);
        int requestCode = 100;
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setActionButton(bitmap,"Share Article", pendingIntent, true);
        CustomTabsIntent customIntent = builder.build();
        customIntent.launchUrl(mContext, Uri.parse(mArticle.getWebUrl()));
    }
}
