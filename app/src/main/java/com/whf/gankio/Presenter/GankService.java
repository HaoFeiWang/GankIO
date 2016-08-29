package com.whf.gankio.Presenter;

import com.whf.gankio.Model.GankBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by WHF on 2016/7/17.
 */
public interface GankService {
    @GET("api/data/{type}/{count}/{page}")
    Observable<GankBean> getGank(@Path("type") String type,
                                 @Path("count") String count,
                                 @Path("page") String page);
}
