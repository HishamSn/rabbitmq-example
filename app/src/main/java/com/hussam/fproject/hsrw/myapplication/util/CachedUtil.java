package com.hussam.fproject.hsrw.myapplication.util;

import com.hussam.fproject.hsrw.myapplication.model.Queues;

import java.util.ArrayList;
import java.util.List;

public class CachedUtil {

  public  List<Queues> queueList;
  public  List<String> queueNameList;

    public static CachedUtil instance;

    public CachedUtil() {
        queueList = new ArrayList<>();
        queueNameList = new ArrayList<>();

    }

    public static CachedUtil getInstance() {
        if (instance == null) {
            instance = new CachedUtil();
        }
        return instance;
    }

}
