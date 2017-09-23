package com.jakester.nytarticlesapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.jakester.nytarticlesapp.databinding.ActivityMainBinding;
import com.jakester.nytarticlesapp.fragments.FilterDialogFragment;
import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.listener.EndlessScrollListener;
import com.jakester.nytarticlesapp.models.Article;
import com.jakester.nytarticlesapp.models.FiltersManager;
import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.util.APIUtility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener{

    RecyclerView mArticlesRecycler;
    StaggeredGridLayoutManager mLayoutManager;
    ArticlesAdapter mAdapter;
    private EndlessScrollListener scrollListener;
    ActivityMainBinding binding;
    String mQuery = "";
    int mPage = 0;
    AlertDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mArticlesRecycler = binding.rvArticles;
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mArticlesRecycler.setLayoutManager(mLayoutManager);
        scrollListener = new EndlessScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if(isNetworkAvailable() && isOnline()) {
                    makeArticlesCall(mQuery, 0);
                }
                else{
                    noInternetDialog.show();
                }
            }
        };

        mAdapter = new ArticlesAdapter(MainActivity.this, new ArrayList<Article>());
        mArticlesRecycler.setAdapter(mAdapter);
        mArticlesRecycler.addOnScrollListener(scrollListener);

    }

    @Override
    public void onResume(){
        super.onResume();
        noInternetDialog = noInternetDialog();
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
                if(isNetworkAvailable() && isOnline()) {
                    if(mAdapter != null) {
                        mAdapter.clearList();
                        scrollListener.resetState();
                    }
                    mQuery = query;
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

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

    @Override
    public void onFinishFilterDialog() {
        if(isNetworkAvailable() && isOnline()) {
            makeArticlesCall(mQuery, 0);
        }
        else{
            noInternetDialog.show();
        }
    }

    private AlertDialog noInternetDialog(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("No Internet");
        builder1.setMessage("It seems that you are not connected to the internet. Make sure that you are connected before");
        builder1.setCancelable(true);
        builder1.setNeutralButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        return builder1.create();
    }
}
