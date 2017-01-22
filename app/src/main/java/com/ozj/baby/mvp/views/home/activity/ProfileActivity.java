package com.ozj.baby.mvp.views.home.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.base.BaseActivity;
import com.ozj.baby.mvp.presenter.home.impl.ProfilePresenterImpl;
import com.ozj.baby.mvp.views.home.IProfileView;
import com.ozj.baby.widget.ChoosePicDialog;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ProfileActivity extends BaseActivity implements IProfileView {
    @Inject
    ProfilePresenterImpl mProfilePresenterImpl;
    @Inject
    ChoosePicDialog mDialog;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.btn_change_avatar)
    Button btnChangeAvatar;
    @BindView(R.id.nick_input)
    TextInputLayout nickInput;
    @BindView(R.id.sex_input)
    TextInputLayout sexInput;
    @BindView(R.id.city_input)
    TextInputLayout cityInput;
    @BindView(R.id.lover_input)
    TextInputLayout loverInput;

    @Override
    protected void initData() {
        mProfilePresenterImpl.initData(ivAvatar, nickInput, loverInput, cityInput, sexInput);
    }

    @Override
    public void initDagger() {
        mActivityComponent.inject(this);
    }

    @Override
    public void initContentView() {
        setContentView(R.layout.activity_edit_profile);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void initViewsAndListener() {


    }

    @Override
    public void initPresenter() {
        mProfilePresenterImpl.attachView(this);


    }

    @Override
    public void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_commit:
                String nick = nickInput.getEditText().getText().toString();
                String loverusername = loverInput.getEditText().getText().toString();
                String city = cityInput.getEditText().getText().toString();
                String sex = sexInput.getEditText().getText().toString();
                mProfilePresenterImpl.commit(nick, loverusername, city, sex);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        return true;
    }

    @Override
    public void ShowPicChoiceDialog() {
        mDialog.show();

    }

    @Override
    public void HidePicChoiceDialog() {
        mDialog.dismiss();
    }

    @Override
    public void setResultCode() {
        setResult(RESULT_OK);
    }


    @OnClick(R.id.btn_change_avatar)
    public void onClick() {
        mDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource imageSource, int i) {
                mProfilePresenterImpl.onError();

            }

            @Override
            public void onImagePicked(File file, EasyImage.ImageSource imageSource, int i) {
                mProfilePresenterImpl.HandleReturnPic(file);
            }

            @Override
            public void onCanceled(EasyImage.ImageSource imageSource, int i) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                mProfilePresenterImpl.onCanceled(imageSource);

            }
        });

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            mProfilePresenterImpl.UpLoadAvatar(ivAvatar, data);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            mProfilePresenterImpl.onError();
            Throwable throwable = UCrop.getError(data);
            if (throwable != null) {
                Logger.e(throwable.getMessage());
            }
        }
    }


}
