package com.whf.gankio.Presenter.Adapters;

import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.whf.gankio.Model.GankBean;

/**
 * Created by WHF on 2016/7/18.
 */
public class StudyAdapters extends RecyclerArrayAdapter<GankBean.Result>{

    public StudyAdapters(Context context) {
        super(context);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new StudyViewHolder(parent);
    }

}
