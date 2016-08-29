package com.whf.gankio.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.Telephony;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by WHF on 2016/7/17.
 */
public class ImageLoaderUtil {

    public static final int LAUNCH_IMAGES=0x1;
    public static final int GRID_IMAGES=0x2;

    public static ImageLoader getImageLoader(Context context,int mode){

        ImageLoader imageLoader=ImageLoader.getInstance();
        ImageLoaderConfiguration config=null;
        DisplayImageOptions options=null;

        if(mode==LAUNCH_IMAGES){
            options=new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(false)
                    .build();
            config=new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(2)
                    .diskCacheFileCount(3)
                    .defaultDisplayImageOptions(options)
                    .build();
        }else{
            options=new DisplayImageOptions.Builder()
                    .cacheOnDisk(true)
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            config=new ImageLoaderConfiguration.Builder(context)
                    .threadPoolSize(3)
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .memoryCache(new LruMemoryCache(2*1024*1024))
                    .diskCacheFileCount(40)
                    .defaultDisplayImageOptions(options)
                    .build();
        }
        imageLoader.init(config);
        return imageLoader;
    }

}
