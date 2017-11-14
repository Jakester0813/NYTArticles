package com.jakester.nytarticlesapp.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.adapters.ArticlesAdapter;
import com.jakester.nytarticlesapp.databinding.ActivityMainBinding;
import com.jakester.nytarticlesapp.fragments.FilterDialogFragment;
import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.listener.EndlessScrollListener;
import com.jakester.nytarticlesapp.managers.InternetManager;
import com.jakester.nytarticlesapp.models.Article;
import com.jakester.nytarticlesapp.managers.FiltersManager;
import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.util.APIUtility;
import com.jakester.nytarticlesapp.util.NYTConstants;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener{

    RecyclerView mArticlesRecycler;
    StaggeredGridLayoutManager mLayoutManager;
    ArticlesAdapter mAdapter;
    private EndlessScrollListener scrollListener;
    ActivityMainBinding binding;
    static String mQuery = NYTConstants.EMPTY_STRING;
    AlertDialog noInternetDialog;
    AlertDialog noArticlesDialog;
    ProgressDialog mProgress;
    boolean mFromWebPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mArticlesRecycler = binding.rvArticles;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }

        mArticlesRecycler.setLayoutManager(mLayoutManager);
        scrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if(InternetManager.getInstance(MainActivity.this).isInternetAvailable()) {
                    makeArticlesCall(mQuery, page);
                }
                else{
                    noInternetDialog.show();
                }
            }
        };

        mAdapter = new ArticlesAdapter(MainActivity.this, new ArrayList<Article>());
        mArticlesRecycler.setAdapter(mAdapter);
        mArticlesRecycler.addOnScrollListener(scrollListener);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage(NYTConstants.PROGRESS_DIALOG_MESSAGE);

    }

    @Override
    public void onResume(){
        super.onResume();
        noInternetDialog = InternetManager.getInstance(this).noInternetDialog();
        noArticlesDialog = InternetManager.getInstance(this).noArticlesDialog();
        if(!mQuery.equals(NYTConstants.EMPTY_STRING) && !InternetManager.getInstance(this).getFromWeb()) {
            mProgress.show();
            makeArticlesCall(mQuery, 0);
        }
        else if (InternetManager.getInstance(this).getFromWeb()){
            InternetManager.getInstance(this).switchFromWeb(false);
        }
    }

    public void getArticles(String query, int page, String date, String sortBy, String newsDesk){
        NYTArticlesService service = APIUtility.getArticleService();

        Observable<Response> articles = service.getArticles(query, page, date, sortBy, newsDesk);

        articles.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override public void onError(Throwable e) {
                        e.printStackTrace();
                        if(mProgress.isShowing()){
                            mProgress.hide();
                        }
                        noArticlesDialog.show();
                    }
                    @Override public void onNext(Response response) {
                        if(mProgress.isShowing()){
                            mProgress.hide();
                        }
                        if (response != null){
                            ArrayList<Article> articles = (ArrayList<Article>) response.getArticlesResponse().getArticles();
                            articles = Lists.newArrayList(Iterables.filter(articles,
                                            new Predicate<Article>() {
                                @Override
                                public boolean apply(Article input) {
                                    if (input != null) {
                                        return true;
                                    }
                                    return false;
                                }
                            }));
                            mAdapter.addList(articles);
                        }
                        else{
                            if(page != 1)
                                noArticlesDialog.show();
                        }
                    }
                });
        /*service.getArticles(query, page, date, sortBy, newsDesk).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {

            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                noArticlesDialog.show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(InternetManager.getInstance(MainActivity.this).isInternetAvailable()) {
                    if(mAdapter != null) {
                        mAdapter.clearList();
                        scrollListener.resetState();
                    }
                    mQuery = query;
                    mProgress.show();
                    makeArticlesCall(mQuery, 0);
                    searchView.clearFocus();
                }
                else{
                    noInternetDialog.show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_settings:
                showFiltersDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFiltersDialog() {
        FragmentManager fm = this.getSupportFragmentManager();
        FilterDialogFragment filterDialog = FilterDialogFragment.newInstance(NYTConstants.SETTINGS);
        filterDialog.show(fm,NYTConstants.FRAGMENT_SETTINGS);
    }



    @Override
    public void onFinishFilterDialog() {
        if(InternetManager.getInstance(this).isInternetAvailable()) {
            mProgress.show();
            if(mAdapter != null) {
                mAdapter.clearList();
                scrollListener.resetState();
            }
            makeArticlesCall(mQuery, 0);
        }
        else{
            noInternetDialog.show();
        }
    }

    public void makeArticlesCall(String q, int page){
        String beginDate = FiltersManager.getInstance(this).getFilterDate();
        if(beginDate != null){
            try {
                beginDate = URLEncoder.encode(beginDate, NYTConstants.UTF8);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String sortFilter = FiltersManager.getInstance(this).getSortFilter();
        if(sortFilter != null){
            try {
                sortFilter = URLEncoder.encode(sortFilter, NYTConstants.UTF8);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String newDesks = FiltersManager.getInstance(this).getNewsDeskFilter();
        if(newDesks != null){
            try {
                newDesks = URLEncoder.encode(newDesks, NYTConstants.UTF8);
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        getArticles(q,page,beginDate,sortFilter,newDesks);
    }
}
