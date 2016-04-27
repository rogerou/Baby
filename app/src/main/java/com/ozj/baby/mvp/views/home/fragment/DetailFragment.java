package com.ozj.baby.mvp.views.home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ozj.baby.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by YX201603-6 on 2016/4/27.
 */
public class DetailFragment extends Fragment implements RequestListener<String, GlideDrawable> {


    @Bind(R.id.iv_photo)
    PhotoView ivPhoto;
    @Bind(R.id.tv_time)
    TextView tvTime;
    private String imgurl;

    public static DetailFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("imgurl", url);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imgurl = getArguments().getString("imgurl");
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Glide.with(getActivity()).load(imgurl).diskCacheStrategy(DiskCacheStrategy.ALL).listener(this).into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detail_zoom, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getActivity().supportStartPostponedEnterTransition();


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
        ButterKnife.unbind(this);
    }
}
