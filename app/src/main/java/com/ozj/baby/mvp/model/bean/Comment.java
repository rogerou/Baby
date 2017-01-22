package com.ozj.baby.mvp.model.bean;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;
import com.hyphenate.easeui.domain.User;
import com.ozj.baby.mvp.model.dao.CommentDao;

/**
 * Created by YX201603-6 on 2016/5/25.
 */
@AVClassName("Comment")
public class Comment extends AVObject implements Cloneable {

    private User auhtor;
    private User reply;
    private String mComment;
    private Souvenir mSouvenir;

    public Souvenir getSouvenir() {
        return (Souvenir) get(CommentDao.SOUVENIR);
    }

    public void setSouvenir(Souvenir souvenir) {
        put(CommentDao.SOUVENIR, souvenir);
    }

    public User getAuhtor() {
        return (User) get(CommentDao.AUTHOR);
    }

    public void setAuhtor(User auhtor) {
        put(CommentDao.AUTHOR, auhtor);
    }

    public User getReply() {
        return (User) get(CommentDao.REPLY_TO);
    }

    public void setReply(User replyTo) {
        put(CommentDao.REPLY_TO, replyTo);
    }

    public String getComment() {
        return getString(CommentDao.COMMENT);
    }

    public void setComment(String comment) {
        put(CommentDao.COMMENT, comment);
    }

    @Override
    public Comment clone() {
        try {
            return (Comment) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
