package com.gavin.app.config.util;

import com.gavin.app.common.CommonConstants;
import com.gavin.app.common.URL;
import com.gavin.app.common.config.AbstractConfig;
import com.gavin.app.common.config.AbstractInterfaceConfig;
import com.gavin.app.common.config.ApplicationConfig;
import com.gavin.app.common.config.RegistryConfig;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 验证几个主要的配置类
 * @author gavin
 * @date 2021/6/28 下午11:08
 */
public class ConfigValidationUtils {

    public static List<URL> loadRegistries(AbstractInterfaceConfig interfaceConfig, boolean provider) {
        List<URL> registerList = new ArrayList<>();
        ApplicationConfig application = interfaceConfig.getApplication();
        List<RegistryConfig> registries = interfaceConfig.getRegistries();
        if (!CollectionUtils.isEmpty(registries)) {
            // 可以注册多个注册中心，但是只有一个能生效
            for (RegistryConfig registry : registries) {
                String address = registry.getAddress();
                if (StringUtils.isEmpty(address)) {
                    address = CommonConstants.ANYHOST_VALUE;
                }

                Map<String, String> map = new HashMap(16);
                AbstractConfig.appendParameters(map, application);
                AbstractConfig.appendParameters(map, registry);
                AbstractConfig.appendRuntimeParameters(map);
            }
        }
        return null;
    }
}
