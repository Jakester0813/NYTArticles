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

import com.jakester.nytarticlesapp.R;
import com.jakester.nytarticlesapp.adapters.ArticlesAdapter;
import com.jakester.nytarticlesapp.fragments.FilterDialogFragment;
import com.jakester.nytarticlesapp.interfaces.NYTArticlesService;
import com.jakester.nytarticlesapp.models.FiltersManager;
import com.jakester.nytarticlesapp.models.Response;
import com.jakester.nytarticlesapp.util.APIUtility;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener{

    RecyclerView mArticlesRecycler;
    StaggeredGridLayoutManager mLayoutManager;
    ArticlesAdapter mAdapter;
    String mQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mArticlesRecycler = (RecyclerView) findViewById(R.id.rv_articles);
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mArticlesRecycler.setLayoutManager(mLayoutManager);

    }

    public void getArticles(String query, String date, String sortBy, String newsDesk){
        NYTArticlesService service = APIUtility.getArticleService();
        service.getArticles(query, date, sortBy, newsDesk).enqueue(new Callback<Response>() {
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
                mQuery = query;
                makeArticlesCall(mQuery);
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

    public void makeArticlesCall(String q){
        String beginDate = FiltersManager.getInstance(this).getFilterDate();
        String sortFilter = FiltersManager.getInstance(this).getSortFilter();
        String newDesks = FiltersManager.getInstance(this).getNewsDeskFilter();
        getArticles(q,beginDate,sortFilter,newDesks);
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
        makeArticlesCall(mQuery);
    }
}
