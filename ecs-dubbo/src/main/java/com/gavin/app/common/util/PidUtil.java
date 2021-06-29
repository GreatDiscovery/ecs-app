package com.gavin.app.common.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * @author: Gavin
 * @date: 2021/6/29 11:09
 * @description:
 */
public class PidUtil {
    private static int PID = -1;

    public static int getPid() {
        if (PID < 0) {
            try {
                RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                String name = runtime.getName(); // format: "pid@hostname"
                PID = Integer.parseInt(name.substring(0, name.indexOf('@')));
            } catch (Throwable e) {
                PID = 0;
            }
        }
        return PID;
    }

    public static void main(String[] args) {
        System.out.println(getPid());
    }
}
