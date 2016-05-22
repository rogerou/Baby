package com.ozj.baby.event;

/**
 * Created by Administrator on 2016/5/1.
 */
public class HxDisconnectEvent {

    public static final int USER_REMOVED = 0;
    public static final int USER_LOGIN_ANOTHER_DEVICE = 1;
    public static final int CANNOTCONNECTTOHX = 2;
    public static final int NONETWORK = 3;
    private int event;

    public HxDisconnectEvent(int i) {
        this.event = i;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

}
