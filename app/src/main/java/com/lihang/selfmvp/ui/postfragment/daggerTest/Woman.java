package com.lihang.selfmvp.ui.postfragment.daggerTest;


import javax.inject.Inject;

/**
 * Created by leo
 * on 2019/9/5.
 */
public class Woman {
    @Inject
    Soul soul;

    @Inject
    public Woman() {

    }

    public Soul getSoul() {
        return soul;
    }

    public void setSoul(Soul soul) {
        this.soul = soul;
    }
}
