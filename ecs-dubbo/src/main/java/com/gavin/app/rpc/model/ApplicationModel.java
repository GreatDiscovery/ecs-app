package com.gavin.app.rpc.model;

import com.gavin.app.common.context.FrameworkExt;
import com.gavin.app.common.extension.ExtensionLoader;
import com.gavin.app.config.context.ConfigManager;

/**
 * 类似容器类，用来存储全局的模型单例对象。
 * 单例对象暂时使用new生成，后面可以根据需要进行SPI改写
 *
 * @author gavin
 * @date 2021/6/20 下午8:54
 */
public class ApplicationModel {

    ExtensionLoader<FrameworkExt> LOADER = new ExtensionLoader<>();

    public ConfigManager getConfigManager() {
        return (ConfigManager) LOADER.getExtension(ConfigManager.NAME);
    }
}
