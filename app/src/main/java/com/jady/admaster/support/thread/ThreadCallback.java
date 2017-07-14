package com.jady.admaster.support.thread;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @Description: Created by jadyli on 2017/4/27.
 */
public interface ThreadCallback {
    public void runOperate() throws InterruptedException, ExecutionException, IOException;

    public void onError(Throwable e);

    public void onCompleted();
}
