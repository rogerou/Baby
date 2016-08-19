package com.ozj.baby.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ozj.baby.R;
import com.ozj.baby.mvp.model.bean.Gallery;
import com.ozj.baby.util.OnItemClickListener;
import com.ozj.baby.util.OnItemLongClickListener;
import com.ozj.baby.widget.RatioImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final List<Gallery> mList;
    private final Context mContext;
    private OnItemClickListener mlistener;
    private OnItemLongClickListener mOnItemLongClickListener;

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
        Glide.with(mContext).load(gallery.getImgUrl()).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivGallery);
        if (gallery.getHeight() != 0 && gallery.getWidth() != 0) {
            holder.ivGallery.setOriginalSize(gallery.getWidth(), gallery.getHeight());
        }
    }


    public void setOnItemActionListener(OnItemClickListener listener) {
        this.mlistener = listener;

    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_gallery)
        RatioImageView ivGallery;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.onClick(v, getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemLongClickListener.onLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }
    }

}
