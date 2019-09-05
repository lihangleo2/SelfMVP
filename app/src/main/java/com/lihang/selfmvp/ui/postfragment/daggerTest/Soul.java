package com.lihang.selfmvp.ui.postfragment.daggerTest;

import javax.inject.Inject;

/**
 * Created by leo
 * on 2019/9/5.
 */
public class Soul {
    private int money = 100;
    @Inject
    public Soul() {

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
