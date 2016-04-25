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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by YX201603-6 on 2016/4/25.
 */
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<Gallery> mList;
    private Context mContext;

    private OnItemActionListener mlistener;

    public GalleryAdapter(List list, Activity activity) {
        mList = list;
        mContext = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_gallery, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Glide.with(mContext).load(mList.get(position)).crossFade().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivGallery);
        if (mlistener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mlistener.OnItemClickListener(v, holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return mlistener.OnItemLongClick(v, holder.getAdapterPosition());
                }
            });

        }
    }

    public interface OnItemActionListener {
        void OnItemClickListener(View view, int position);

        boolean OnItemLongClick(View view, int position);

    }


    public void setOnItemActionListener(OnItemActionListener listener) {
        this.mlistener = listener;

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_gallery)
        ImageView ivGallery;
        @Bind(R.id.tv_time)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getTime(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(new Date(timestamp));
    }
}
