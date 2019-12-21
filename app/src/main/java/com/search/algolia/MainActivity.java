package com.search.algolia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.search.algolia.binders.StoriesBinder;
import com.search.algolia.models.Hits;
import com.search.algolia.networklayer.AlgoliaApi;
import com.search.algolia.networklayer.IAlogiaService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mStoriesPicked;
    private TextView retryTV;
    private int pageNum = 1;
    private boolean canLoadData = true;
    private boolean hardReset = false;
    private int storySelectCount = 0;
    private IAlogiaService algoliaService;
    private StoriesBinder viewBinderData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.storiesContainerSRL);
        mRecyclerView = (RecyclerView) findViewById(R.id.storiesRV);
        mProgressBar = (ProgressBar) findViewById(R.id.loadingStoriesPB);
        mStoriesPicked = (TextView) findViewById(R.id.selectedStoriesCountTV);
        retryTV = (TextView) findViewById(R.id.retryTV );
        retryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                retryTV.setVisibility(View.GONE);
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                loadData();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                hardReset = true;
                pageNum  = 1;
                loadData();
            }

        });


        algoliaService = AlgoliaApi.getClient().create(IAlogiaService.class);
        viewBinderData = new StoriesBinder(getApplicationContext(), new ArrayList<Hits.Story>()) {
            @Override
            public void onSwitchToggle(boolean state) {
                updateStoriesCounter(state);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.setAdapter(viewBinderData);

        initScrollListener();
        loadData();
    }


    /**
     *
     */
    private void loadData() {
        Call<Hits> call = algoliaService.getStories("story", pageNum);
        call.enqueue(new Callback<Hits>() {

            @Override
            public void onResponse(Call<Hits> call, Response<Hits> response) {

                if(hardReset){
                    viewBinderData.resetStories();
                    hardReset = false;
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                Hits hits = response.body();
                viewBinderData.addStories(hits.getStories());
                canLoadData = true;
                mProgressBar.setVisibility(View.GONE );
            }

            @Override
            public void onFailure(Call<Hits> call, Throwable t) {
                canLoadData = true;
                mProgressBar.setVisibility(View.GONE);
                retryTV.setVisibility(View.VISIBLE);
                mSwipeRefreshLayout.setVisibility(View.GONE);
            }
        });
    }


    private void initScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(canLoadData){
                    //bottom of list!
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewBinderData.getItemCount() - 1) {
                        pageNum++;
                        canLoadData = false;
                        mProgressBar.setVisibility(View.VISIBLE);
                        loadData();

                    }
                }
            }
        });
    }

    private void updateStoriesCounter(boolean state) {
        if(state){
            storySelectCount++;
        }else{
            storySelectCount--;
        }

        if(storySelectCount==0){
            mStoriesPicked.setText("");
        }else{
            mStoriesPicked.setText("("+storySelectCount+")");
        }
    }

}
