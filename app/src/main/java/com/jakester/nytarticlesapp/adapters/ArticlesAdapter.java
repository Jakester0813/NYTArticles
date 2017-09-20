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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.models.Article;
import com.jakester.nytarticlesapp.models.Multimedia;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Jake on 9/19/2017.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticlesViewHolder> {

    Context mContext;
    List<Article> mArticles;

    public ArticlesAdapter(Context pContext, List<Article> pArticles){
        mContext = pContext;
        mArticles = pArticles;
    }

    @Override
    public ArticlesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View articlesView = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles_layout,parent,false);
        ArticlesViewHolder viewHolder = new ArticlesViewHolder(articlesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ArticlesViewHolder holder, int position) {
        holder.bind(mArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ArticlesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView mArticleImage;
        TextView mHeadLine;
        TextView mSnippet;


        public ArticlesViewHolder(View view){
            super(view);
            mArticleImage = (ImageView) view.findViewById(R.id.iv_image);
            mHeadLine = (TextView) view.findViewById(R.id.tv_headline);
            mSnippet = (TextView) view.findViewById(R.id.tv_snippet);
            view.setOnClickListener(this);
        }

        public void bind(Article article){
            if(article.getImages() != null) {
                String url = article.getImages().get(article.getImages().size()-1).mGetUrl();
                Glide.with(mContext).load(url).into(mArticleImage);
            }
            mHeadLine.setText(article.getHeadline().getMain());
            mSnippet.setText(article.getSnippet());


        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Article article = mArticles.get(position);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, article.getWebUrl());

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_share);
            int requestCode = 100;
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setActionButton(bitmap,"Share Article", pendingIntent, true);
            CustomTabsIntent customIntent = builder.build();
            customIntent.launchUrl(mContext, Uri.parse(article.getWebUrl()));
        }
    }
}
