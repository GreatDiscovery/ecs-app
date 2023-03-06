package com.gavin.app.shell;

import org.junit.Test;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 测试本地执行shell命令
 *
 * @author QiangZhi
 * @date 2023/3/6 09:29
 */
public class JavaShellTest {
    @Test
    public void shellTest() {
        byte[] buffer = new byte[10240];
        List<String> cmdList = new ArrayList<>(Arrays.asList("/bin/bash", "-c", "/usr/bin/ssh root@192.168.1.2 \"pwd\""));
        ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
        int runningStatus = 0;
        String s = "";
        try {
            Process p = processBuilder.start();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }
            try {
                runningStatus = p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("执行失败");
            e.printStackTrace();
        }
    }
}
