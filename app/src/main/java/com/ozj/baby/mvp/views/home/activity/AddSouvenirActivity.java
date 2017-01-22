package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.presenter.home.impl.AddSouvenirImpl;
import com.ozj.baby.mvp.views.home.IAddSouvenirView;
import com.ozj.baby.widget.ChoosePicDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import butterknife.BindView;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by Administrator on 2016/4/21.
 */
public class AddSouvenirActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, IAddSouvenirView {


    @Inject
    ChoosePicDialog mDialog;
    @Inject
    AddSouvenirImpl mAddSouenirPresenter;
    File imgfile;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_album)
    ImageView ivAlbum;


    @Override
    protected void initData() {

    }

    @Override
    public void initDagger() {
        mActivityComponent.inject(this);

    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_addsouvenir);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void initViewsAndListener() {

    }

    @Override
    public void initPresenter() {
        mAddSouenirPresenter.attachView(this);
    }

    @Override
    public void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_souvenir_action, menu);

        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_commit:
                String content = etContent.getText().toString();
                mAddSouenirPresenter.commit(content, imgfile);
                break;
            case R.id.fetch_picture:
                showDialog();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void showDialog() {
        mDialog.show();
    }

    @Override
    public void hideDialog() {
        mDialog.dismiss();
    }

    @Override
    public void setResultCode(Intent intent) {
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {
                showToast("出错啦，请重新试试");
            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                UCrop.of(Uri.fromFile(file), Uri.fromFile(file))
                        .withMaxResultSize(800, 800).start(AddSouvenirActivity.this);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {
                if (imageSource == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(AddSouvenirActivity.this);
                    if (photoFile != null) {
                        photoFile.delete();
                    }

                }
            }
        });
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            try {
                imgfile = new File(new URI(UCrop.getOutput(data).toString()));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            Glide.with(this).load(imgfile).into(ivAlbum);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            showToast("出错啦，请稍后再试");
            Throwable throwable = UCrop.getError(data);
            if (throwable != null) {
                Logger.e(throwable.getMessage());
            }
        }

    }

}
