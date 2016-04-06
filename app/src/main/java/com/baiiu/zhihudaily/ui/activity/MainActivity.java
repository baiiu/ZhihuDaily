package com.baiiu.zhihudaily.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.baiiu.zhihudaily.R;
import com.baiiu.zhihudaily.adapter.DailyNewsAdapter;
import com.baiiu.zhihudaily.base.BaseActivity;
import com.baiiu.zhihudaily.net.DailyClient;
import com.baiiu.zhihudaily.net.http.RequestCallBack;
import com.baiiu.zhihudaily.pojo.Daily;
import com.baiiu.zhihudaily.util.LogUtil;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {


    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private DailyNewsAdapter dailyNewsAdapter;
    private String mCurrentDate;

    @Override
    public int provideLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initOnCreate(Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        dailyNewsAdapter = new DailyNewsAdapter(this, this);
        recyclerView.setAdapter(dailyNewsAdapter);

        refreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    refreshLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    refreshLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }

                refreshLayout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        mCurrentDate = "";
        loadData();
    }

    private void loadData() {
        DailyClient.getLatestNews(volleyTag, dailyRequest);
    }


    public void loadMore() {
        DailyClient.getBeforeNews(volleyTag, mCurrentDate, dailyRequest);
    }

    private RequestCallBack<Daily> dailyRequest = new RequestCallBack<Daily>() {

        @Override
        public void onSuccess(Daily response) {
            if (TextUtils.isEmpty(mCurrentDate)) {
                refreshLayout.setRefreshing(false);
                dailyNewsAdapter.setDaily(response, true);
            } else {
                dailyNewsAdapter.setDaily(response, false);
            }

            mCurrentDate = response.date;
        }

        @Override
        public void onFailure(int statusCode, String errorString) {
            LogUtil.d(statusCode + ", " + errorString);

            if (TextUtils.isEmpty(mCurrentDate)) {
                refreshLayout.setRefreshing(false);
                // TODO: 16/4/6  空页面或者错误页面
            } else {
                dailyNewsAdapter.bindFooter(null);
            }
        }
    };


    @OnClick(R.id.fab)
    public void onClick() {
        Snackbar.make(refreshLayout, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @Override
    public void onClick(View v) {
        String id = "";
        switch (v.getId()) {
            case R.id.item_news:
                id = (String) v.getTag();
                break;
            case R.id.item_topic_news:
                id = (String) v.getTag(R.id.item_topic_news);
                break;
        }

        startActivity(NewsDetailActivity.instance(this, id));
    }

    //=====================Menu===================================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
