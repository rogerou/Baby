package com.ozj.baby.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;

import java.util.List;

/**
 * Created by Seveb on 2016/5/25.
 * <p>
 * 评论界面根据Type来判断的的Adapter
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    List<Comment> mList;
    Souvenir mSouvenir;
    Context mContext;

    public CommentAdapter(List<Comment> comments, Souvenir souvenir, Context context) {
        this.mList = comments;
        this.mSouvenir = souvenir;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private Comment getItem(int position) {
        return mList.get(position - 1);
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }


}
