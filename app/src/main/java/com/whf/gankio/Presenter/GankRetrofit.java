package com.whf.gankio.Presenter;

import com.whf.gankio.Model.Constant;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by WHF on 2016/7/17.
 */
public class GankRetrofit {

    private static Retrofit gankRetrofit=null;

    public static Retrofit getRetrofit(){
        if(gankRetrofit==null){
            synchronized (GankRetrofit.class){
                if(gankRetrofit==null){
                    gankRetrofit=new Retrofit.Builder().baseUrl(Constant.GANK_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .build();
                }
            }
        }
        return gankRetrofit;
    }

}
