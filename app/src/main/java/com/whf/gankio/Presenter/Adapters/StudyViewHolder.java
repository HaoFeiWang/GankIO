package com.whf.gankio.Presenter.Adapters;

import android.view.ViewGroup;
import android.widget.TextView;

import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.whf.gankio.Model.GankBean;
import com.whf.gankio.R;
import com.whf.gankio.Utils.TimeUtil;

/**
 * Created by WHF on 2016/7/18.
 */
public class StudyViewHolder extends BaseViewHolder<GankBean.Result> {

    private TextView mTitleText,mTypeText,mAuthorText,mDateText;

    public StudyViewHolder(ViewGroup viewGroup) {
        super(viewGroup, R.layout.item_study);
        initView();
    }

    private void initView() {
        mTitleText = $ (R.id.study_title);
        mAuthorText = $ (R.id.study_author);
        mTypeText = $ (R.id.study_type);
        mDateText = $ (R.id.study_date);
    }

    @Override
    public void setData(GankBean.Result data) {
        super.setData(data);
        mTitleText.setText(data.getDesc());
        mTypeText.setText(data.getType());
        mAuthorText.setText(data.getWho()==null?"佚名":data.getWho());
        mDateText.setText(TimeUtil.getFormatTime(data.getCreatedAt()));
    }
}
