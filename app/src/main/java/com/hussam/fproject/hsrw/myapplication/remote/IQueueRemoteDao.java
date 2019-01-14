package com.hussam.fproject.hsrw.myapplication.remote;

import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.network.HttpCall;

import java.util.List;

public interface IQueueRemoteDao {

    HttpCall<List<Queues>> getQueuesList();

}