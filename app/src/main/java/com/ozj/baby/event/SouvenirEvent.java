package com.ozj.baby.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ozj.baby.mvp.model.bean.Souvenir;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class SouvenirEvent {

    public Souvenir mSouvenir;

    public List<Souvenir> mlist;

    public int mAction;

    public SouvenirEvent(@Nullable Souvenir souvenir, @Nullable List<Souvenir> list, @NonNull int action) {
        this.mSouvenir = souvenir;
        this.mAction = action;
        this.mlist = list;
    }

}
