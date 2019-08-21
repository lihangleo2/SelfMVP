package com.lihang.selfmvp.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by leo
 * on 2019/8/14.
 */
public class GankFatherBean implements Serializable {
    private boolean error;
    private ArrayList<GankBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ArrayList<GankBean> getResults() {
        return results;
    }

    public void setResults(ArrayList<GankBean> results) {
        this.results = results;
    }
}
