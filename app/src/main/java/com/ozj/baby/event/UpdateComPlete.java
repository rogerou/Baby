package com.ozj.baby.event;

/**
 * Created by Administrator on 2016/4/23.
 */
public class UpdateComPlete {
    private boolean isComPlete;

    public UpdateComPlete(boolean isComPlete) {
        this.isComPlete = isComPlete;
    }

    public boolean isComPlete() {
        return isComPlete;
    }

    public void setComPlete(boolean comPlete) {
        isComPlete = comPlete;
    }
}
