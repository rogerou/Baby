package com.ozj.baby.event;

import com.ozj.baby.mvp.model.bean.Comment;

import java.util.List;

/**
 * Created by YX201603-6 on 2016/5/25.
 */
public class AddCommentsEvent {

    public List<Comment> mCommentList;
    public boolean isRefresh;

    public AddCommentsEvent(List<Comment> commentList, boolean isRefresh) {
        this.mCommentList = commentList;
        this.isRefresh = isRefresh;
    }

}
