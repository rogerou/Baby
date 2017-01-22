package com.ozj.baby.mvp.views.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.avos.avoscloud.AVAnalytics;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ozj.baby.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by YX201603-6 on 2016/4/27.
 */
public class DetailFragment extends Fragment implements RequestListener<String, GlideDrawable> {


    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    private String mImgUrl;
    private Unbinder unbinder;

    public static DetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("mImgUrl", url);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImgUrl = getArguments().getString("mImgUrl");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity()).load(mImgUrl).diskCacheStrategy(DiskCacheStrategy.ALL).listener(this).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        AVAnalytics.onFragmentStart("DetailFragment");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_zoom, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onFragmentEnd("DetailFragment");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        return true;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        ivPhoto.setImageDrawable(resource);

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public ImageView getSharedElement() {
        return ivPhoto;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.iv_photo)
    public void onClick() {
        getActivity().onBackPressed();
    }
}
