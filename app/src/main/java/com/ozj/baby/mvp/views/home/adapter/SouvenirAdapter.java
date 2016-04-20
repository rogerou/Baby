package com.ozj.baby.mvp.views.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Administrator on 2016/4/20.
 */
public class SouvenirAdapter extends RecyclerView.Adapter<SouvenirAdapter.ViewHolder> {


    RealmResults<Souvenir> mList;
    Context mContext;


    public SouvenirAdapter(RealmResults<Souvenir> List, Context context) {
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
        holder.tvAuthor.setText(mList.get(position).getAuthor().getNick());
        holder.txtTime.setText(getTime(mList.get(position).getTimeStamp()));
        Glide.with(mContext).load(mList.get(position).getPicture()).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivSouvenirPic);
        Glide.with(mContext).load(mList.get(position).getAuthor().getAvatar()).bitmapTransform(new CropCircleTransformation(mContext)).into(holder.ivSpeaker);
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
                    isLikeSouvenir(SouvenirDao.SOUVENIR_ISLIKEME, true);
                } else {
                    isLikeSouvenir(SouvenirDao.SOUVENIR_ISLIKEOTHER, true);
                }
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                if (isMySouvenir(holder.getAdapterPosition())) {
                    isLikeSouvenir(SouvenirDao.SOUVENIR_ISLIKEME, false);

                } else {
                    isLikeSouvenir(SouvenirDao.SOUVENIR_ISLIKEOTHER, false);
                }
            }
        });

    }

    private void isLikeSouvenir(String like, boolean islike) {
        AVObject mySouvenir = new AVObject(SouvenirDao.TABLENAME);
        mySouvenir.put(like, islike);
        mySouvenir.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Logger.d("自己点赞成功和不取消赞成功");
                } else {
                    Logger.e("赞失败");
                }

            }
        });
    }

    private boolean isMySouvenir(int position) {
        return mList.get(position).getAuthor().getID().equals(AVUser.getCurrentUser().getObjectId());
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

    private String getTime(long timestamp) {
        return DateUtils.formatDateTime(mContext, timestamp, DateUtils.FORMAT_SHOW_TIME);
    }
}
