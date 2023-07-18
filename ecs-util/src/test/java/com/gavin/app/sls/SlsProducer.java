package com.gavin.app.sls;

import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.LogItem;
import com.aliyun.openservices.log.common.LogStore;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.CreateLogStoreResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 参考 https://github.com/aliyun/aliyun-log-java-sdk
 * @author QiangZhi
 * @date 2023/4/24 16:11
 */
public class SlsProducer {

    public static void main(String[] args) throws Exception {
        String accessId = "your_access_id";
        String accessKey = "your_access_key";
        String host = "your_endpoint";
        Client client = new Client(host, accessId, accessKey);

        String project = "your_project_name";
        String logstore = "your_logstore";
        int ttl_in_day = 3;
        int shard_count = 10;

        LogStore store = new LogStore(logstore, ttl_in_day, shard_count);
        CreateLogStoreResponse res = client.CreateLogStore(project, store);

        int numLogGroup = 10;
        /**
         * 向log service发送一个日志包，每个日志包中，有2行日志
         */
        for (int i = 0; i < numLogGroup; i++) {
            List<LogItem> logGroup = new ArrayList<LogItem>();
            LogItem logItem = new LogItem((int) (new Date().getTime() / 1000));
            logItem.PushBack("level", "info");
            logItem.PushBack("name", String.valueOf(i));
            logItem.PushBack("message", "it's a test message");
            logGroup.add(logItem);
            LogItem logItem2 = new LogItem((int) (new Date().getTime() / 1000));
            logItem2.PushBack("level", "error");
            logItem2.PushBack("name", String.valueOf(i));
            logItem2.PushBack("message", "it's a test message");
            logGroup.add(logItem2);

            try {
                client.PutLogs(project, logstore, "health", logGroup, "");
            } catch (LogException e) {
                System.out.println("error code :" + e.GetErrorCode());
                System.out.println("error message :" + e.GetErrorMessage());
                System.out.println("error requestId :" + e.GetRequestId());
                throw e;
            }

        }
    }
}
