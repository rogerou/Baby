package com.ozj.baby.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.util.DateUtils;
import com.ozj.baby.R;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.util.OnItemClickListener;
import com.ozj.baby.util.OnItemLongClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Rogerou on 2016/5/25.
 * <p>
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewholder> {
    final List<Comment> mList;
    final Context mContext;
    OnItemClickListener mOnItemClickListener;
    OnItemLongClickListener mOnItemLongClickListener;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.mList = comments;
        this.mContext = context;
    }


    @Override
    public CommentViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(CommentViewholder holder, int position) {
        Comment comment = mList.get(position);
        holder.mTvNick.setText(comment.getAuhtor().getNick());
        holder.mTvTime.setText(DateUtils.getTimestampString(comment.getCreatedAt()));
        Glide.with(mContext).load(comment.getAuhtor().getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).bitmapTransform(new CropCircleTransformation(mContext)).crossFade().into(holder.mIvAvatar);
        if (mList.get(position).getReply() == null) {
            holder.mTvComment.setText(comment.getComment());
        } else {
            holder.mTvComment.setText(String.format("回复 %s 的评论：%s", comment.getReply().getNick(), comment.getComment()));
        }
    }


    private Comment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setItemOnclicklistener(OnItemClickListener onclicklistener) {
        this.mOnItemClickListener = onclicklistener;
    }

    public void setItemOnLongClickListener(OnItemLongClickListener onClickListener) {
        this.mOnItemLongClickListener = onClickListener;
    }

    class CommentViewholder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_avatar)
        ImageView mIvAvatar;
        @BindView(R.id.tv_nick)
        TextView mTvNick;
        @BindView(R.id.tv_comment)
        TextView mTvComment;
        @BindView(R.id.tv_time)
        TextView mTvTime;


        public CommentViewholder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(v, getAdapterPosition());
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
