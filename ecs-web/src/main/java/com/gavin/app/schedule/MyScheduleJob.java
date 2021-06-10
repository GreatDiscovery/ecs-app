package com.gavin.app.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author gavin
 * @date 2021/6/2 下午10:25
 */
@Component
public class MyScheduleJob {

    @Scheduled(fixedRate = 10000)
    public void schedulePrint() {
//        System.out.println("hello world");
    }
}
