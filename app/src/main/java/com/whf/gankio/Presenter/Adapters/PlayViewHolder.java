package com.whf.gankio.Presenter.Adapters;


import android.view.ViewGroup;
import android.widget.ImageView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.whf.gankio.Model.GankBean;
import com.whf.gankio.R;


/**
 * Created by WHF on 2016/8/29.
 */
public class PlayViewHolder extends BaseViewHolder<GankBean.Result>{

    private ImageView mImageView;
    private ImageLoader mImageLoader;

    public PlayViewHolder(ViewGroup itemView,ImageLoader mImageLoader) {
        super(itemView,R.layout.item_play);
        mImageView = $(R.id.item_imageView);
        this.mImageLoader=mImageLoader;
    }

    @Override
    public void setData(GankBean.Result data) {
        super.setData(data);
        mImageView.setTag(data.getUrl());
        if(data.getUrl()==mImageView.getTag()){
            mImageLoader.displayImage(data.getUrl(),mImageView);

        }

    }
}
