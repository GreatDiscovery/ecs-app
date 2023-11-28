package com.gavin.app.sls;

/**
 * 参考： https://help.aliyun.com/document_detail/175624.html?spm=a2c4g.171781.0.0.34e053baoFPsvy
 * @author QiangZhi
 * @date 2023/4/24 17:21
 */

//import com.aliyun.openservices.aliyun.log.producer.Callback;
//import com.aliyun.openservices.aliyun.log.producer.LogProducer;
//import com.aliyun.openservices.aliyun.log.producer.Producer;
//import com.aliyun.openservices.aliyun.log.producer.ProducerConfig;
//import com.aliyun.openservices.aliyun.log.producer.ProjectConfig;
//import com.aliyun.openservices.aliyun.log.producer.Result;
//import com.aliyun.openservices.aliyun.log.producer.errors.ProducerException;
import com.aliyun.openservices.log.common.LogItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MetricStoreProducer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(MetricStoreProducer.class);
//    private static final Random random = new Random();
//
//    public static void main(String[] args) throws InterruptedException {
//        final String project = "";
//        final String logstore = "";
//        final String endpoint = "your-endpoint";
//        final String accessKeyId = "";
//        final String accessKeySecret = "";
//        int sendThreadCount = 8;
//        final int times = 10;
//        LOGGER.info(
//                "project={}, logstore={}, endpoint={}, sendThreadCount={}, times={}",
//                project, logstore, endpoint, sendThreadCount, times);
//        ExecutorService executorService = Executors.newFixedThreadPool(sendThreadCount);
//        ProducerConfig producerConfig = new ProducerConfig();
//        producerConfig.setBatchSizeThresholdInBytes(3 * 1024 * 1024);
//        producerConfig.setBatchCountThreshold(40960);
//
//        final Producer producer = new LogProducer(producerConfig);
//        producer.putProjectConfig(new ProjectConfig(project, endpoint, accessKeyId, accessKeySecret));
//
//        final AtomicInteger completedCount = new AtomicInteger(0);
//        LOGGER.info("Test started.");
//        long t1 = System.currentTimeMillis();
//        final Map<String, String> labels = new HashMap<String, String>();
//        labels.put("test_k", "test_v");
//        for (int i = 0; i < sendThreadCount; ++i) {
//            executorService.submit(
//                    new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                for (int i = 0; i < times; ++i) {
//                                    int r = random.nextInt(times);
//                                    producer.send(project, logstore, generateTopic(r), generateSource(r),
//                                            buildLogItem("test_metric", labels, i),
//                                            new Callback() {
//                                                @Override
//                                                public void onCompletion(Result result) {
//                                                    completedCount.incrementAndGet();
//                                                    if (!result.isSuccessful()) {
//                                                        LOGGER.error(
//                                                                "Failed to send log, project={}, logstore={}, result={}",
//                                                                project,
//                                                                logstore,
//                                                                result);
//                                                    }
//                                                }
//                                            });
//                                }
//                            } catch (Exception e) {
//                                LOGGER.error("Failed to send log, e=", e);
//                            }
//                        }
//                    });
//        }
//        while (completedCount.get() < sendThreadCount * times) {
//            Thread.sleep(100);
//        }
//        long t2 = System.currentTimeMillis();
//        LOGGER.info("Test end.");
//        LOGGER.info("======Summary======");
//        LOGGER.info("Total count " + sendThreadCount * times + ".");
//        long timeCost = t2 - t1;
//        LOGGER.info("Time cost " + timeCost + " millis");
//        try {
//            producer.close();
//        } catch (ProducerException e) {
//            LOGGER.error("Failed to close producer, e=", e);
//        }
//        executorService.shutdown();
//    }

    private static String generateTopic(int r) {
        return "topic-" + r % 5;
    }

    private static String generateSource(int r) {
        return "source-" + r % 10;
    }

    /**
     * @param metricName: the metric name, eg: http_requests_count
     * @param labels:     labels map, eg: {'idc': 'idc1', 'ip': '192.0.2.0', 'hostname': 'appserver1'}
     * @param value:      double value, eg: 1.234
     * @return LogItem
     */
    public static LogItem buildLogItem(String metricName, Map<String, String> labels, double value) {
        String labelsKey = "__labels__";
        String timeKey = "__time_nano__";
        String valueKey = "__value__";
        String nameKey = "__name__";
        LogItem logItem = new LogItem();
        int timeInSec = (int) (System.currentTimeMillis() / 1000);
        logItem.SetTime(timeInSec);
        logItem.PushBack(timeKey, timeInSec + "000000");
        logItem.PushBack(nameKey, metricName);
        logItem.PushBack(valueKey, String.valueOf(value));

        // 按照字典序对labels排序, 如果您的labels已排序, 请忽略此步骤。
        TreeMap<String, String> sortedLabels = new TreeMap<String, String>(labels);
        StringBuilder labelsBuilder = new StringBuilder();

        boolean hasPrev = false;
        for (Map.Entry<String, String> entry : sortedLabels.entrySet()) {
            if (hasPrev) {
                labelsBuilder.append("|");
            }
            hasPrev = true;
            labelsBuilder.append(entry.getKey());
            labelsBuilder.append("#$#");
            labelsBuilder.append(entry.getValue());
        }
        logItem.PushBack(labelsKey, labelsBuilder.toString());
        return logItem;
    }
}
