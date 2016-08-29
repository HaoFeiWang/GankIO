package com.whf.gankio.View.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.whf.gankio.Model.GankBean;
import com.whf.gankio.Presenter.Adapters.PlayAdapters;
import com.whf.gankio.Presenter.GankRetrofit;
import com.whf.gankio.Presenter.GankService;
import com.whf.gankio.R;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PlayActivity extends AppCompatActivity {

    private EasyRecyclerView mEasyRecyclerView;
    private RecyclerArrayAdapter<GankBean.Result> mAdapter;
    private int mPage=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mEasyRecyclerView = (EasyRecyclerView) findViewById(R.id.recyclerView_play);
        mEasyRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mAdapter= new PlayAdapters(this);
        mEasyRecyclerView.setAdapter(mAdapter);

        getData(mPage);

    }

    private void getData(int page){
        GankRetrofit.getRetrofit().create(GankService.class)
                .getGank("福利",20+"",page+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GankBean gankBean) {
                        mAdapter.addAll(gankBean.getResults());
                    }
                });
    }
}
