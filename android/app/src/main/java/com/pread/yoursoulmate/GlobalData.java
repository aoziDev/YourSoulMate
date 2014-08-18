package com.pread.yoursoulmate;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hshan on 2014. 8. 16..
 */
public class GlobalData extends Application {
    private List<String> resultList;

    @Override
    public void onCreate() {
        init();
        super.onCreate();
    }

    private void init() {
        resultList = new ArrayList<String>();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void setResultList(List<String> resultList) {
        this.resultList = resultList;
    }

    public List<String> getResultList() {
        return resultList;
    }
}
