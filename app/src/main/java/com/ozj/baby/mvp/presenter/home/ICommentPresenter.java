package com.ozj.baby.mvp.presenter.home;

import com.ozj.baby.base.BasePresenter;
import com.ozj.baby.mvp.model.bean.Comment;
import com.ozj.baby.mvp.model.bean.Souvenir;

/**
 * Created by YX201603-6 on 2016/5/25.
 */
public interface ICommentPresenter extends BasePresenter {

    void fetchAllComments(Souvenir souvenir, int page, int size);

    void Comment(Comment comment, int position);

    void deleteComment(Comment comment);

}
