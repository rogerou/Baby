package com.ozj.baby.event;

import android.support.annotation.Nullable;

import com.ozj.baby.mvp.model.bean.Comment;

import java.util.List;

/**
 * Created by YX201603-6 on 2016/5/25.
 */
public class CommentsEvent {
    public List<Comment> mCommentList;
    public Comment mComment;
    public int mAction;

    public CommentsEvent(@Nullable Comment comment, @Nullable List<Comment> commentList, int action) {
        mComment = comment;
        mCommentList = commentList;
        mAction = action;
    }
}
