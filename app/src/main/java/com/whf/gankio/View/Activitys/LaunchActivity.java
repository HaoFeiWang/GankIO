package com.whf.gankio.View.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.whf.gankio.Model.Constant;
import com.whf.gankio.Model.GankBean;
import com.whf.gankio.Presenter.GankRetrofit;
import com.whf.gankio.Presenter.GankService;
import com.whf.gankio.R;
import com.whf.gankio.Utils.ImageLoaderUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LaunchActivity extends AppCompatActivity{

    private ImageView mImageView;
    private TextView mTextView;
    private Subscription subscription=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch);

        initView();
        initData();
        initListener();
    }

    /**
     * 记得要取消订阅，不然可能发生内存泄露
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscription!=null&&!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.launch_image);
        mTextView = (TextView) findViewById(R.id.launch_count_text);
    }

    private void initData() {
        GankRetrofit.getRetrofit().create(GankService.class)
                .getGank("福利","1","1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankBean>() {
                    @Override
                    public void onCompleted() {
                        startImageAnimation();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ImageLoaderUtil.getImageLoader(LaunchActivity.this,ImageLoaderUtil.LAUNCH_IMAGES)
                                .displayImage(Constant.LAUNCH_IMAGE_URL,mImageView);
                        startImageAnimation();
                    }

                    @Override
                    public void onNext(GankBean gankBean) {
                        Log.e("test","onNext(gankBean)");
                        ImageLoaderUtil.getImageLoader(LaunchActivity.this,ImageLoaderUtil.LAUNCH_IMAGES)
                                .displayImage(gankBean.getResults().get(0).getUrl(),mImageView);
                    }
                });
    }

    private void startImageAnimation(){
        Animation scaleAnim=AnimationUtils.loadAnimation(this,R.anim.launch_image_scale);
        scaleAnim.setFillAfter(true);
        scaleAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //mTimerHandler.sendEmptyMessageDelayed(TIME_TAG,1000);
                subscription=Observable.interval(1,1,TimeUnit.SECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Long>() {
                            @Override
                            public void onCompleted() {}
                            @Override
                            public void onError(Throwable e) {}
                            @Override
                            public void onNext(Long aLong) {
                                Log.e("test",aLong+"");
                                mTextView.setText(4-aLong+" 跳过");
                            }
                        });
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jumpToMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        mImageView.startAnimation(scaleAnim);
    }

    private void initListener(){
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jumpToMain();
            }
        });
    }

    private void jumpToMain(){
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        this.finish();
    }

//    private class TimerHandler extends Handler{
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if(msg.what==TIME_TAG){
//                mTextView.setText(mTimeCount + " 跳过");
//                if(--mTimeCount>0){
//                    mTimerHandler.sendEmptyMessageDelayed(TIME_TAG,1000);
//                }
//
//            }
//        }
//    }
}
