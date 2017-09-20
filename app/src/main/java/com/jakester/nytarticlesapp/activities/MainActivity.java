package com.jakester.nytarticlesapp.activities;

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

import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.adapters.ArticlesAdapter;
import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.network.RestClient;
import com.jakester.nytarticlesapp.util.APIUtility;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    RecyclerView mArticlesRecycler;
    StaggeredGridLayoutManager mLayoutManager;
    ArticlesAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArticlesRecycler = (RecyclerView) findViewById(R.id.rv_articles);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mArticlesRecycler.setLayoutManager(mLayoutManager);

    }

    public void getArticles(String query){
        NYTArticlesService service = APIUtility.getArticleService();
        service.getArticles(query).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                mAdapter = new ArticlesAdapter(MainActivity.this, response.body().getArticlesResponse().getArticles());
                mArticlesRecycler.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {

                Log.d("","");
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
                getArticles(query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
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
}
