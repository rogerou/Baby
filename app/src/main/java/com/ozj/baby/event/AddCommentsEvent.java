package com.ozj.baby.event;

import com.ozj.baby.mvp.model.bean.Comment;

import java.util.List;

/**
 * Created by YX201603-6 on 2016/5/25.
 */
public class AddCommentsEvent {

    public List<Comment> mCommentList;
    public boolean isRefresh;
    public boolean isList;
    public Comment mComment;

    public AddCommentsEvent(Comment comment, boolean isRefresh, boolean isList) {
        this.isRefresh = isRefresh;
        mComment = comment;
        this.isList = isList;
    }

    public AddCommentsEvent(List<Comment> commentList, boolean isRefresh, boolean isList) {
        this.mCommentList = commentList;
        this.isRefresh = isRefresh;
        this.isList = isList;

    }

}
