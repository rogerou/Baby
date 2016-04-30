package com.ozj.baby.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVUser;
import com.ozj.baby.mvp.model.dao.UserDao;

/**
 * Created by Administrator on 2016/4/17.
 */
public class User extends AVUser implements Parcelable {
    private String nick;
    private String avatar;
    private String city;
    private String loverusername;
    private String sex;
    private String anotherUserID;
    private String InstallationId;
    private String LoverInstallationId;

    public long getLovetimeStamp() {
        return getLong(UserDao.LOVETIMESTAMP);
    }

    public void setLovetimeStamp(long lovetimeStamp) {
        put(UserDao.LOVETIMESTAMP, lovetimeStamp);
    }

    private long lovetimeStamp;


    public User() {

    }
//    public User(AVUser user) {
//        if (user.getString(UserDao.AVATARURL) != null) {
//            avatar = user.getString(UserDao.AVATARURL);
//        }
//        if (user.getString(UserDao.CITY) != null) {
//            this.city = user.getString(UserDao.CITY);
//        }
//        if (user.getString(UserDao.LOVERUSERNAME) != null) {
//            this.loverusername = user.getString(UserDao.LOVERUSERNAME);
//        }
//        if (user.getString(UserDao.SEX) != null) {
//            this.sex = user.getString(UserDao.SEX);
//        }
//        if (user.getObjectId() != null) {
//            this.ID = user.getObjectId();
//        }
//        if (user.getString(UserDao.NICK) != null) {
//            this.nick = user.getString(UserDao.NICK);
//        }
//        this.createTime = user.getCreatedAt().getTime();
//        if (user.getString(UserDao.LOVERID) != null) {
//            this.anotherUserID = user.getString(UserDao.LOVERID);
//        }
//    }
//
//    public User() {
//
//    }

    public String getLoverusername() {
        return getString(UserDao.LOVERUSERNAME);
    }

    public void setLoverusername(String loverusername) {
        put(UserDao.LOVERUSERNAME, loverusername);
    }

    public String getCity() {
        return getString(UserDao.CITY);
    }

    public void setCity(String city) {
        put(UserDao.CITY, city);
    }


    public String getNick() {
        return getString(UserDao.NICK);
    }

    public void setNick(String nick) {
        put(UserDao.NICK, nick);
    }

    public String getAvatar() {
        return getString(UserDao.AVATARURL);
    }

    public void setAvatar(String avatar) {
        put(UserDao.AVATARURL, avatar);
    }


    public String getSex() {
        return getString(UserDao.SEX);
    }

    public void setSex(String sex) {
        put(UserDao.SEX, sex);
    }

    public String getAnotherUserID() {
        return getString(UserDao.LOVERID);
    }

    public void setAnotherUserID(String anotherUserID) {
        put(UserDao.LOVERID, anotherUserID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nick);
        dest.writeString(this.avatar);
        dest.writeString(this.city);
        dest.writeString(this.loverusername);
        dest.writeString(this.sex);
        dest.writeString(this.anotherUserID);
    }

    protected User(Parcel in) {
        this.nick = in.readString();
        this.avatar = in.readString();
        this.city = in.readString();
        this.loverusername = in.readString();
        this.sex = in.readString();
        this.anotherUserID = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getInstallationId() {
        return getString(UserDao.INSTALLATIONID);
    }

    public void setInstallationId(String installationId) {
        put(UserDao.INSTALLATIONID, installationId);
    }

    public String getLoverInstallationId() {
        return getString(UserDao.LOVERINSTALLATIONID);
    }

    public void setLoverInstallationId(String loverInstallationId) {
        put(UserDao.LOVERINSTALLATIONID, loverInstallationId);
    }
}
