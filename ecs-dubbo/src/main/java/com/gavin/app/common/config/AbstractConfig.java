package com.gavin.app.common.config;

import com.gavin.app.common.util.MethodUtils;
import com.gavin.app.common.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author gavin
 * @date 2021/6/13 下午3:33
 */
public class AbstractConfig {

    /**
     * The suffix container
     */
    private static final String[] SUFFIXES = new String[]{"Config", "Bean", "ConfigBase"};

    /**
     * 配置ID
     */
    protected String id;

    /**
     * 分组
     */
    protected String group;
    /**
     * 接口版本
     */
    protected String version;

    /**
     * The protocol list the service will export with
     *  only one of them will work.
     */
    protected List<ProtocolConfig> protocols;

    public String getId() {
        return id;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static String getTagName(Class<?> cls) {
        String simpleName = cls.getSimpleName();
        for (String suffix : SUFFIXES) {
            if (simpleName.endsWith(suffix)) {
                simpleName = simpleName.substring(0, simpleName.length() - suffix.length());
                break;
            }
        }
        return StringUtils.camelToSplitName(simpleName, "-");
    }

    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("<dubbo ");
            sb.append(getTagName(getClass()));
            // 打印所有可以get的属性
            for (Method method : getClass().getMethods()) {
                try {
                    if (!MethodUtils.isGetter(method)) {
                        continue;
                    }
                    String key = MethodUtils.getGetterAttribute(method.getName());
                    try {
                        getClass().getDeclaredField(key);
                    } catch (NoSuchFieldException e) {
                        continue;
                    }
                    Object value = method.invoke(this);
                    sb.append(" ").append(key).append("=\"").append(value).append("\"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            sb.append(" />");
            return sb.toString();
        } catch (Throwable throwable) {
            return super.toString();
        }
    }
}
