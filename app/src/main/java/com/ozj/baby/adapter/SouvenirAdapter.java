package com.ozj.baby.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.model.dao.SouvenirDao;
import com.ozj.baby.mvp.model.dao.UserDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SouvenirAdapter extends RecyclerView.Adapter<SouvenirAdapter.ViewHolder> {

    List<Souvenir> mList;
    Context mContext;

    public SouvenirAdapter(@NonNull List<Souvenir> List, Context context) {
        this.mList = List;
        this.mContext = context;
        com.orhanobut.logger.Logger.init(this.getClass().getSimpleName());

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_souvenir_fragment, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvContent.setText(mList.get(position).getContent());
        holder.tvAuthor.setText(mList.get(position).getAuthor().getString(UserDao.NICK));
        holder.txtTime.setText(getTime(mList.get(position).getCreatedAt()));
        Glide.with(mContext).load(mList.get(position).getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().thumbnail((float) 0.8).into(holder.ivSouvenirPic);
        Glide.with(mContext).load(mList.get(position).getAuthor().getString(UserDao.AVATARURL)).bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivSpeaker);
        if (isMySouvenir(position)) {
            if (mList.get(position).isLikedMine()) {
                holder.likeButton.setLiked(true);
            } else {
                holder.likeButton.setLiked(false);
            }

        } else {
            if (mList.get(position).isLikedOther()) {
                holder.likeButton.setLiked(true);
            } else {
                holder.likeButton.setLiked(false);
            }
        }
        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                if (isMySouvenir(holder.getAdapterPosition())) {
                    isLikeSouvenir(holder, true, true);
                } else {
                    isLikeSouvenir(holder, false, true);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                if (isMySouvenir(holder.getAdapterPosition())) {
                    isLikeSouvenir(holder, true, false);

                } else {
                    isLikeSouvenir(holder, false, false);
                }
            }
        });

    }

    private void isLikeSouvenir(ViewHolder holder, boolean isMine, boolean islike) {
        if (isMine) {
            mList.get(holder.getAdapterPosition()).setLikedMine(islike);
        } else {
            mList.get(holder.getAdapterPosition()).setLikedOther(islike);
        }
        mList.get(holder.getAdapterPosition()).saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Logger.d("点赞成功");
                } else {
                    Logger.e("点赞失败");
                }
            }
        });

//        mySouvenir.put(like, islike);
//        mySouvenir.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e == null) {
//                    Logger.d("自己点赞成功和不取消赞成功");
//                } else {
//                    Logger.e("赞失败");
//                }
//
//            }
//        });
    }

    private boolean isMySouvenir(int position) {
        return mList.get(position).getAuthorId().equals(AVUser.getCurrentUser().getObjectId());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.iv_souvenir_pic)
        ImageView ivSouvenirPic;
        @Bind(R.id.txt_time)
        TextView txtTime;
        @Bind(R.id.iv_speaker)
        ImageView ivSpeaker;
        @Bind(R.id.tv_author)
        TextView tvAuthor;
        @Bind(R.id.like_button)
        LikeButton likeButton;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

    private String getTime(Date timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(timestamp);
    }
}
