package com.ozj.baby.mvp.model.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/4/17.
 */
public class User extends RealmObject {

    private String nick;
    private String avatar;
    private String city;
    private String username;
    @PrimaryKey
    private String ID;
    private String loverId;
    private Long timestamp;
    

    public User getAnotherUser() {
        return anotherUser;
    }

    public void setAnotherUser(User anotherUser) {
        this.anotherUser = anotherUser;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    private User anotherUser;


}
