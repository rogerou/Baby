package com.ozj.baby.mvp.views.home.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ZoomImageActivity extends BaseActivity {


    @Bind(R.id.iv_photo)
    ImageView ivPhoto;
    @Bind(R.id.tv_detail)
    TextView tvDetail;

    PhotoViewAttacher mAttacher;

    @Override
    public void initDagger() {

    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_zoomimage);
        StatusBarUtil.setTranslucent(this);
        
        mAttacher = new PhotoViewAttacher(ivPhoto);
    }

    @Override
    public void initViewsAndListener() {

    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initToolbar() {

    }

}
