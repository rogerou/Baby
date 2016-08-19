package com.ozj.baby.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.domain.UserDao;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.orhanobut.logger.Logger;
import com.ozj.baby.R;
import com.ozj.baby.mvp.model.bean.Souvenir;
import com.ozj.baby.mvp.views.home.activity.CommentActivity;
import com.ozj.baby.mvp.views.home.activity.DetailImageActivity;
import com.ozj.baby.util.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SouvenirAdapter extends RecyclerView.Adapter<SouvenirAdapter.ViewHolder> {

    final List<Souvenir> mList;
    final Context mContext;
    OnItemLongClickListener mOnItemLongClickListener;

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
        holder.txtTime.setText(com.hyphenate.util.DateUtils.getTimestampString(mList.get(position).getCreatedAt()));
        holder.tv_comment.setText(String.valueOf(mList.get(position).getCommentcount()));
        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("moment", mList.get(holder.getAdapterPosition()).getObjectId());
                intent.putExtra("position", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });
        Glide.with(mContext).load(mList.get(position).getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).crossFade().into(holder.ivSouvenirPic);
        Glide.with(mContext).load(mList.get(position).getAuthor().getString(UserDao.AVATARURL)).bitmapTransform(new CropCircleTransformation(mContext)).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivSpeaker);
        holder.ivSouvenirPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailImageActivity.class);
                ArrayList<String> list = new ArrayList<>();
                list.add(mList.get(holder.getAdapterPosition()).getPicture());
                intent.putStringArrayListExtra("imgurl", list);
                intent.putExtra("index", holder.getAdapterPosition());
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, v, mList.get(holder.getAdapterPosition()).getPicture());
                mContext.startActivity(intent, optionsCompat.toBundle());
            }
        });
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

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
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

//        
    }

    private boolean isMySouvenir(int position) {
        return mList.get(position).getAuthorId().equals(AVUser.getCurrentUser().getObjectId());
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_souvenir_pic)
        ImageView ivSouvenirPic;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.iv_speaker)
        ImageView ivSpeaker;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.like_button)
        LikeButton likeButton;
        @BindView(R.id.tv_comment)
        TextView tv_comment;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
