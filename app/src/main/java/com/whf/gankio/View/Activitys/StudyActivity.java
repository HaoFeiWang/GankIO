package com.whf.gankio.View.Activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.whf.gankio.R;
import com.whf.gankio.View.me.fichardu.circleprogress.CircleProgress;

import java.lang.reflect.Method;

public class StudyActivity extends AppCompatActivity {

    private WebView mWebView;
    private Toolbar mToolbar;
    private CircleProgress mCircleProgress;
    private FrameLayout mFrameLayout;
    private String uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        mWebView= (WebView) findViewById(R.id.webView);
        mToolbar= (Toolbar) findViewById(R.id.study_toolbar);
        mCircleProgress= (CircleProgress) findViewById(R.id.progress);
        mFrameLayout = (FrameLayout) findViewById(R.id.progress_framelayout);

        Intent intent=getIntent();
        uri=intent.getStringExtra("url");
        String desc=intent.getStringExtra("desc");

        mToolbar.setTitleTextAppearance(this,R.style.StudyActivityTitleStyle);//修改toolbar标题字体样式
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(desc);

        initWebView();
        mWebView.loadUrl(uri);
        mCircleProgress.startAnim();

    }

    private void initWebView() {
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if(newProgress==100){
                    mCircleProgress.stopAnim();
                    mFrameLayout.setVisibility(View.GONE);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_study,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_study_refresh:
                mWebView.reload();
                break;
            case R.id.menu_study_share:
                break;
            case R.id.menu_study_browser:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        setOverflowIconVisible(menu);
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * 显示OverflowMenu中菜单项的图标
     * @param menu
     */
    private void setOverflowIconVisible(Menu menu) {
        if ( menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.d("OverflowIconVisible", e.getMessage());
                }
            }
        }
    }
}
