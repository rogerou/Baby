package com.ozj.baby.mvp.model.bean;

import com.avos.avoscloud.AVUser;
import com.ozj.baby.mvp.model.dao.UserDao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/4/17.
 */
public class User extends RealmObject {

    private String nick;
    private String avatar;
    private String city;
    private String loverusername;
    @PrimaryKey
    private String ID;
    private long createTime;
    private String sex;
    private String anotherUserID;


    public User(AVUser user) {
        if (user.getString(UserDao.AVATARURL) != null) {
            avatar = user.getString(UserDao.AVATARURL);
        }
        if (user.getString(UserDao.CITY) != null) {
            this.city = user.getString(UserDao.CITY);
        }
        if (user.getString(UserDao.LOVERUSERNAME) != null) {
            this.loverusername = user.getString(UserDao.LOVERUSERNAME);
        }
        if (user.getString(UserDao.SEX) != null) {
            this.sex = user.getString(UserDao.SEX);
        }
        if (user.getObjectId() != null) {
            this.ID = user.getObjectId();
        }
        if (user.getString(UserDao.NICK) != null) {
            this.nick = user.getString(UserDao.NICK);
        }
        this.createTime = user.getCreatedAt().getTime();
        if (user.getString(UserDao.LOVER) != null) {
            this.anotherUserID = user.getString(UserDao.LOVER);
        }
    }

    public User() {

    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getLoverusername() {
        return loverusername;
    }

    public void setLoverusername(String loverusername) {
        this.loverusername = loverusername;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAnotherUserID() {
        return anotherUserID;
    }

    public void setAnotherUserID(String anotherUserID) {
        this.anotherUserID = anotherUserID;
    }
}
