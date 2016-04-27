package com.ozj.baby.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ozj.baby.R;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.util.OnItemClickListener;
import com.ozj.baby.widget.RatioImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<Gallery> mList;
    private Context mContext;

    private OnItemClickListener mlistener;

    public GalleryAdapter(List<Gallery> list, Activity activity) {
        mList = list;
        mContext = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Gallery gallery = mList.get(position);
//        holder.tvTime.setText(getTime(mList.get(position).getCreatedAt()));
//        Glide.with(mContext).load(mList.get(position).getUser().getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(mContext)).crossFade().into(holder.ivAvatar);
        Glide.with(mContext).load(gallery.getImgUrl()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivGallery);
        if (gallery.getHeight() != 0 && gallery.getWidth() != 0) {
            holder.ivGallery.setOriginalSize(gallery.getWidth(), gallery.getHeight());
        }
    }


    public void setOnItemActionListener(OnItemClickListener listener) {
        this.mlistener = listener;

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_gallery)
        RatioImageView ivGallery;
//        @Bind(R.id.tv_time)
//        TextView tvTime;
//        @Bind(R.id.iv_avatar)
//        RatioImageView ivAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(v, getAdapterPosition());
                }
            });

        }
    }

//    private String getTime(Date timestamp) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
//        return simpleDateFormat.format(timestamp);
//    }
}
