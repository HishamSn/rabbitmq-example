package com.hussam.fproject.hsrw.myapplication.remote;

import com.hussam.fproject.hsrw.myapplication.constant.AppConstant;
import com.hussam.fproject.hsrw.myapplication.model.BaseGenericWrapper;
import com.hussam.fproject.hsrw.myapplication.model.Queues;
import com.hussam.fproject.hsrw.myapplication.network.HttpCall;
import com.hussam.fproject.hsrw.myapplication.network.HttpHelper;

import java.util.List;

import retrofit2.http.GET;

public class QueueRemoteDao implements IQueueRemoteDao {
    private static QueueRemoteDao instance;
    private QueueClient queueClient;

    public QueueRemoteDao() {
        queueClient = HttpHelper.getInstance().create(QueueClient.class);
    }

    public static QueueRemoteDao getInstance() {
        if (instance == null) {
            instance = new QueueRemoteDao();
        }
        return instance;
    }

    @Override
    public HttpCall<List<Queues>> getQueuesList() {
        return queueClient.getQueuesList();
    }


    private interface QueueClient {
        @GET(AppConstant.GET_QUEUES)
        HttpCall<List<Queues>> getQueuesList();
    }
}
