package com.gavin.app;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author gavin
 * @date 2019-12-17 22:42
 */
@EnableSwagger2
@EnableScheduling
@SpringBootApplication
public class Application {

    private static ExecutorService downloadExecutors = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(20), new ThreadFactoryBuilder().setNameFormat("download-pool-%d").build());

    @PreDestroy
    private void shutDownHook() {
        System.out.println("注册钩子");
        System.out.println(downloadExecutors);
        downloadExecutors.shutdown();
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            try {
//                System.out.println("注册钩子");
//                System.out.println(downloadExecutors);
////                downloadExecutors.shutdown();
//                if (!downloadExecutors.awaitTermination(10, TimeUnit.SECONDS)) {
//
//                    List droppedTasks = downloadExecutors.shutdownNow();
//                    if (droppedTasks != null && droppedTasks.size() > 0) {
//                        System.out.println("丢弃任务:" + droppedTasks.size());
//                    }
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, "shutdownThread"));
    }

    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        // 返回的调用状态码，比如0成功，1失败等
        return () -> 42;
    }

    public static void main(String[] args) {
        downloadExecutors.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                    System.out.println("任务结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ConfigurableApplicationContext context = SpringApplication.run(Application.class);
        SpringApplication.exit(context, () -> 402);
    }
}
