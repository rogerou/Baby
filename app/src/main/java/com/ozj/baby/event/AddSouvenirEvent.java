package com.ozj.baby.event;

import com.ozj.baby.mvp.model.bean.Souvenir;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class AddSouvenirEvent {

    private Souvenir msouvenir;

    private List<Souvenir> mlist;

    private boolean isRefresh;

    private boolean isList;

    public AddSouvenirEvent(boolean isRefresh, boolean isList, Souvenir souvenir) {
        this.isList = isList;
        this.isRefresh = isRefresh;
        this.msouvenir = souvenir;
    }

    public AddSouvenirEvent(boolean isRefresh, boolean isList, List<Souvenir> souvenirList) {
        this.isRefresh = isRefresh;
        this.isList = isList;
        this.mlist = souvenirList;
    }

    public Souvenir getSouvenir() {
        return msouvenir;
    }

    public void setSouvenir(Souvenir souvenir) {
        this.msouvenir = souvenir;
    }

    public List<Souvenir> getMlist() {
        return mlist;
    }

    public void setMlist(List<Souvenir> mlist) {
        this.mlist = mlist;
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public boolean isList() {
        return isList;
    }

}
