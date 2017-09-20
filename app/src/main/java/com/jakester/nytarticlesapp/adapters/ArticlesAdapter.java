package com.jakester.nytarticlesapp.adapters;

import android.content.Context;
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

    public class ArticlesViewHolder extends RecyclerView.ViewHolder {
        ImageView mArticleImage;
        TextView mHeadLine;
        TextView mSnippet;


        public ArticlesViewHolder(View view){
            super(view);
            mArticleImage = (ImageView) view.findViewById(R.id.iv_image);
            mHeadLine = (TextView) view.findViewById(R.id.tv_headline);
            mSnippet = (TextView) view.findViewById(R.id.tv_snippet);
        }

        public void bind(Article article){
            Multimedia media = article.getMultimedia();
            if(media != null) {
                String url = media.getImages().get(media.getImages().size()-1).mGetUrl();
                Glide.with(mContext).load(url).into(mArticleImage);
            }
            mHeadLine.setText(article.getHeadline().getMain());
            mSnippet.setText(article.getSnippet());
        }
    }
}
