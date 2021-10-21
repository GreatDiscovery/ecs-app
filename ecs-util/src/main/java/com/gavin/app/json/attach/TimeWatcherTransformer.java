package com.gavin.app.json.attach;

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


/**
 * 替换类
 * @author gavin
 * @date 2021/10/21 上午12:04
 */
public class TimeWatcherTransformer implements ClassFileTransformer {
    private static Logger LOGGER = LoggerFactory.getLogger(TimeWatcherTransformer.class);

    private String targetClassName;
    private ClassLoader targetClassLoader;
    private String method;

    public TimeWatcherTransformer(String targetClassName, ClassLoader targetClassLoader, String method) {
        this.targetClassName = targetClassName;
        this.targetClassLoader = targetClassLoader;
        this.method = method;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] byteCode = classfileBuffer;
        String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/"); //replace . with /
        if (!className.equals(finalTargetClassName)) {
            return byteCode;
        }

        if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {
            LOGGER.info("[Agent] Transforming class DemoApplication");
            try {
                ClassPool cp = ClassPool.getDefault();
                CtClass cc = cp.get(targetClassName);
                CtMethod m = cc.getDeclaredMethod(method);
                m.addLocalVariable("startTime", CtClass.longType);
                m.insertBefore("startTime = System.currentTimeMillis();");

                StringBuilder endBlock = new StringBuilder();

                m.addLocalVariable("endTime", CtClass.longType);
                m.addLocalVariable("opTime", CtClass.longType);
                endBlock.append("endTime = System.currentTimeMillis();");
                endBlock.append("opTime = (endTime-startTime)/1000;");
                endBlock.append("LOGGER.info(\"[Application] testExceptionTruncate completed in:\" + opTime + \" seconds!\");");
                m.insertAfter(endBlock.toString());
                byteCode = cc.toBytecode();
                cc.detach();
            } catch (NotFoundException | CannotCompileException | IOException e) {
                LOGGER.error("Exception", e);
            }
        }
        return byteCode;
    }

}
