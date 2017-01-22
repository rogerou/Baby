package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.presenter.home.impl.AboutPresenterImpl;
import com.ozj.baby.mvp.views.home.IAboutView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by YX201603-6 on 2016/5/3.
 */
public class AboutActivity extends BaseActivity implements IAboutView {

    @Inject
    AboutPresenterImpl mAboutPresenter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_github)
    TextView tvGithub;

    @Override
    protected void initData() {
        
    }

    @Override
    public void initDagger() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_about);
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void initViewsAndListener() {
        tvAuthor.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvGithub.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvAuthor.getPaint().setAntiAlias(true);
        tvGithub.getPaint().setAntiAlias(true);

    }

    @Override
    public void initPresenter() {
        mAboutPresenter.attachView(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.check_version) {
            mAboutPresenter.checkVersion();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }

    @Override
    public void toWebView(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);

    }


    @OnClick({R.id.tv_author, R.id.tv_github})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_author:
                toWebView("https://github.com/rogerou");
                break;
            case R.id.tv_github:
                toWebView(tvGithub.getText().toString());
                break;

            default:
                break;

        }
    }
}
