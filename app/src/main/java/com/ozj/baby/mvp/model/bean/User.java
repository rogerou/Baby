package com.ozj.baby.mvp.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.avos.avoscloud.AVUser;
import com.ozj.baby.mvp.model.dao.UserDao;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Administrator on 2016/4/17.
 */
public class User extends RealmObject implements Parcelable {

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
        if (user.getString(UserDao.LOVERID) != null) {
            this.anotherUserID = user.getString(UserDao.LOVERID);
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
        dest.writeString(this.ID);
        dest.writeLong(this.createTime);
        dest.writeString(this.sex);
        dest.writeString(this.anotherUserID);
    }

    protected User(Parcel in) {
        this.nick = in.readString();
        this.avatar = in.readString();
        this.city = in.readString();
        this.loverusername = in.readString();
        this.ID = in.readString();
        this.createTime = in.readLong();
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
}
