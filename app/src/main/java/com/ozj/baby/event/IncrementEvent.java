package com.ozj.baby.event;

/**
 * Created by YX201603-6 on 2016/5/26.
 */
public class IncrementEvent {
    public int position;
    public int count;

    public IncrementEvent(int count, int position) {
        this.count = count;
        this.position = position;
    }


}
