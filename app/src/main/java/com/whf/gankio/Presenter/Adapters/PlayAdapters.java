package com.whf.gankio.Presenter.Adapters;


import android.content.Context;
import android.view.ViewGroup;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.whf.gankio.Model.GankBean;
import com.whf.gankio.Utils.ImageLoaderUtil;


/**
 * Created by WHF on 2016/8/29.
 */
public class PlayAdapters extends RecyclerArrayAdapter<GankBean.Result> {

    private ImageLoader mImageLoader;

    public PlayAdapters(Context context) {
        super(context);
        mImageLoader= ImageLoaderUtil.getImageLoader(context,ImageLoaderUtil.GRID_IMAGES);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayViewHolder(parent,mImageLoader);
    }
}
