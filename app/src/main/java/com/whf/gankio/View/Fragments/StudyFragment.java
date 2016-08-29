package com.whf.gankio.View.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.whf.gankio.Model.Constant;
import com.whf.gankio.Model.GankBean;
import com.whf.gankio.Presenter.Adapters.StudyAdapters;
import com.whf.gankio.Presenter.GankRetrofit;
import com.whf.gankio.Presenter.GankService;
import com.whf.gankio.R;
import com.whf.gankio.View.Activitys.StudyActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StudyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RecyclerArrayAdapter.OnLoadMoreListener {

    private EasyRecyclerView mEasyRecyclerView;
    private FloatingActionButton mFloatingActionButton;
    private StudyAdapters mStudyAdapters;
    private String mType;
    private Context mContext;
    private Handler mHandler=new Handler();
    private int mPage=1;
    private boolean mIsLoadMore=false;

    public static StudyFragment getInstance(String type){
        Bundle bundle=new Bundle();
        bundle.putString("type",type);
        StudyFragment studyFragment=new StudyFragment();
        studyFragment.setArguments(bundle);
        return studyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mType = bundle.getString("type");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_study, container, false);
        mEasyRecyclerView= (EasyRecyclerView) view.findViewById(R.id.study_easyrecyclerview);
        mFloatingActionButton= (FloatingActionButton) view.findViewById(R.id.floatingactionbutton);

        initRecyclerView();
        getData(1);
        initFloatingActionButton();

        return view;
    }

    private void initRecyclerView() {

        mEasyRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mStudyAdapters=new StudyAdapters(mContext);
        mEasyRecyclerView.setAdapterWithProgress(mStudyAdapters);

        mEasyRecyclerView.setRefreshListener(this);
        mStudyAdapters.setMore(R.layout.load_more,this);

        mStudyAdapters.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                GankBean.Result result=mStudyAdapters.getItem(position);
                String url=result.getUrl();
                String desc=result.getDesc();

                Intent intent=new Intent(mContext, StudyActivity.class);
                intent.putExtra("url",url);
                intent.putExtra("desc",desc);

                startActivity(intent);
            }
        });
    }

    private void initFloatingActionButton() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEasyRecyclerView.scrollToPosition(0);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        putCache();
    }

    private void getData(int page){
        GankRetrofit.getRetrofit().create(GankService.class)
                .getGank(mType,20+"",page+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankBean>() {
                    @Override
                    public void onCompleted() {
                        mIsLoadMore=false;
                        mPage++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        getCache();
                        mPage=2;
                        if(mIsLoadMore){
                            mEasyRecyclerView.scrollToPosition(mStudyAdapters.getCount()-1);
                            mIsLoadMore=false;
                        }
                        Snackbar.make(mEasyRecyclerView,"网络异常，加载新数据失败",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(GankBean gankBean) {
                        List<GankBean.Result> results=gankBean.getResults();
                        mStudyAdapters.addAll(results);
                    }
                });
    }

    /**
     * 将EasyRecycleView中的数据缓存在本地
     */
    private void putCache(){
        ObjectOutputStream objectOutputStream=null;
        FileOutputStream fileOutputStream=null;
        try {
            String outPath=getOutPath();
            fileOutputStream=new FileOutputStream(outPath);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            List<GankBean.Result> results=mStudyAdapters.getAllData();
            objectOutputStream.writeObject(results);
            Log.e("results","pubCache()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地缓存的数据
     */
    private void getCache(){
        ObjectInputStream objectInputStream=null;
        FileInputStream fileInputStream=null;
        try {
            String outPath=getOutPath();
            fileInputStream=new FileInputStream(outPath);
            objectInputStream = new ObjectInputStream(fileInputStream);
            List<GankBean.Result> results= (List<GankBean.Result>) objectInputStream.readObject();
            mStudyAdapters.clear();
            mStudyAdapters.addAll(results);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取数据缓存的路径
     * @return
     */
    private String getOutPath(){
        File cacheFile = mContext.getCacheDir();
        String outPath=null;
        Log.e("cacheFile",cacheFile.toString());
        if(mType==Constant.ANDROID_TYPE_NAME){
            outPath=cacheFile+"/"+Constant.ANDROID_CACHE_NAME;
        }else if(mType==Constant.iOS_TYPE_NAME){
            outPath=cacheFile+"/"+Constant.IOS_CACHE_NAME;
        }
        return outPath;
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mStudyAdapters.clear();
                getData(1);
            }
        },1000);
     }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsLoadMore=true;
                getData(mPage);
            }
        },1000);
    }
}
