package com.gavin.app.json.attach;

import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

/**
 * @author gavin
 * @date 2021/10/19 下午11:32
 */
public class AttachAgent {
    private static Logger LOGGER = LoggerFactory.getLogger(AttachAgent.class);

    public static void main(String[] args) {
        run(args);
    }

    public static void run(String[] args) {
        String agentFilePath = "/Users/gavin/Documents/github/ecs-app/ecs-app/out/ecs-util.jar";
        String applicationName = "com.gavin.app.json.attach.DemoApplication";

        //iterate all jvms and get the first one that matches our application name
        Optional<String> jvmProcessOpt = Optional.ofNullable(VirtualMachine.list()
                .stream()
                .filter(jvm -> {
                    LOGGER.info("jvm:{}", jvm.displayName());
                    return jvm.displayName().contains(applicationName);
                })
                .findFirst().get().id());

        if (!jvmProcessOpt.isPresent()) {
            LOGGER.error("Target Application not found");
            return;
        }
        File agentFile = new File(agentFilePath);
        try {
            String jvmPid = jvmProcessOpt.get();
            LOGGER.info("Attaching to target JVM with PID: " + jvmPid);
            VirtualMachine jvm = VirtualMachine.attach(jvmPid);
            jvm.loadAgent(agentFile.getAbsolutePath());
            jvm.detach();
            LOGGER.info("Attached to target JVM and loaded Java agent successfully");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
