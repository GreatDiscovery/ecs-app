package com.gavin.app.common.config;

/**
 * @author gavin
 * @date 2021/6/13 下午3:21
 */
public class ServiceConfigBase<T> {
    // 导出服务的名称
    protected String interfaceName;

    protected Class<?> interfaceClass;

    // 接口的实现类
    protected T ref;

    public void setInterface(Class<?> interfaceClass) {
        if (interfaceClass == null || !interfaceClass.isInterface()) {
            throw new IllegalStateException("The interface class " + interfaceClass + " is not a interface!");
        }
        setInterfaceClass(interfaceClass);
        setInterfaceName(interfaceClass.getName());
    }

    public void setInterfaceClass(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setRef(T ref) {
        this.ref = ref;
    }
}