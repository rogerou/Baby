package com.ozj.baby.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.ozj.baby.R;
import com.ozj.baby.di.scope.ContextLife;

import javax.inject.Inject;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by Administrator on 2016/4/21.
 */
public class ChoosePicDialog implements View.OnClickListener {
    Button btnPicture;
    Button btnTakephoto;
    Button btnCancel;
    final Dialog mDialog;
    final Activity mContext;

    @Inject
    public ChoosePicDialog(Activity context) {
        mContext = context;
        mDialog = new Dialog(mContext, R.style.transparentFrameWindowStyle);
        initDialog();
        initDialogViews();
    }

    private void initDialog() {
        View view = mContext.getLayoutInflater().inflate(R.layout.photo_choose_dialog, null);
        btnPicture = (Button) view.findViewById(R.id.btn_picture);
        btnTakephoto = (Button) view.findViewById(R.id.btn_takephoto);
        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        mDialog.setContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        Window window = mDialog.getWindow();
        // 设置显示动画
        window.setWindowAnimations(R.style.main_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = mContext.getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        // 设置显示位置
        mDialog.onWindowAttributesChanged(wl);
        // 设置点击外围解散
        mDialog.setCanceledOnTouchOutside(true);
    }

    private void initDialogViews() {
        btnCancel.setOnClickListener(this);
        btnPicture.setOnClickListener(this);
        btnTakephoto.setOnClickListener(this);

    }

    public void show() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        } else {
            dismiss();
        }
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_picture:
                EasyImage.openGallery(mContext, 0);
                break;
            case R.id.btn_takephoto:
                EasyImage.openCamera(mContext, 0);

                break;
            case R.id.btn_cancel:
            default:
                break;
        }
        dismiss();

    }
}
