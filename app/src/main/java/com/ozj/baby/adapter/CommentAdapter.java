package com.ozj.baby.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;

import java.util.List;

/**
 * Created by Rogerou on 2016/5/25.
 * <p>
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Comment> mList;
    Context mContext;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.mList = comments;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    private Comment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
