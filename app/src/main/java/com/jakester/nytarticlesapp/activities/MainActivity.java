package com.jakester.nytarticlesapp.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.adapters.ArticlesAdapter;
import com.jakester.nytarticlesapp.fragments.FilterDialogFragment;
import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.listener.EndlessScrollListener;
import com.jakester.nytarticlesapp.models.Article;
import com.jakester.nytarticlesapp.models.FiltersManager;
import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.util.APIUtility;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener{

    RecyclerView mArticlesRecycler;
    StaggeredGridLayoutManager mLayoutManager;
    ArticlesAdapter mAdapter;
    private EndlessScrollListener scrollListener;
    String mQuery = "";
    int mPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArticlesRecycler = (RecyclerView) findViewById(R.id.rv_articles);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mArticlesRecycler.setLayoutManager(mLayoutManager);
        scrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                makeArticlesCall(mQuery, page);
            }
        };

        mAdapter = new ArticlesAdapter(MainActivity.this, new ArrayList<Article>());
        mArticlesRecycler.setAdapter(mAdapter);
        mArticlesRecycler.addOnScrollListener(scrollListener);

    }

    public void getArticles(String query, int page, String date, String sortBy, String newsDesk){
        NYTArticlesService service = APIUtility.getArticleService();
        service.getArticles(query, page, date, sortBy, newsDesk).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if (response.body() != null){
                    mAdapter.addList(response.body().getArticlesResponse().getArticles());
                }
                else{
                    Toast.makeText(MainActivity.this,"No articles returned!!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                Log.e("Error",t.getLocalizedMessage());
            }
        });
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
                if(mAdapter != null) {
                    mAdapter.clearList();
                    scrollListener.resetState();
                }
                mQuery = query;
                makeArticlesCall(mQuery, 0);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void makeArticlesCall(String q, int page){
        String beginDate = FiltersManager.getInstance(this).getFilterDate();
        if(beginDate != null){
            try {
                beginDate = URLEncoder.encode(beginDate, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String sortFilter = FiltersManager.getInstance(this).getSortFilter();
        if(sortFilter != null){
            try {
                sortFilter = URLEncoder.encode(sortFilter, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        String newDesks = FiltersManager.getInstance(this).getNewsDeskFilter();
        if(newDesks != null){
            try {
                newDesks = URLEncoder.encode(newDesks, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        getArticles(q,page,beginDate,sortFilter,newDesks);
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
        FilterDialogFragment filterDialog = FilterDialogFragment.newInstance("Settings");
        filterDialog.show(fm,"fragment_settings");
    }

    @Override
    public void onFinishFilterDialog() {
        makeArticlesCall(mQuery, 0);
    }
}
