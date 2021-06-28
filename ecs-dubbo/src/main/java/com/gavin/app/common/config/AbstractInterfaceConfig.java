package com.gavin.app.common.config;

import com.gavin.app.common.util.StringUtils;
import com.gavin.app.rpc.model.ApplicationModel;
import org.springframework.util.CollectionUtils;

import javax.print.attribute.standard.PresentationDirection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author gavin
 * @date 2021/6/20 下午8:47
 */
public abstract class AbstractInterfaceConfig extends AbstractMethodConfig {

    /**
     * The application info
     */
    protected ApplicationConfig application;

    /**
     * The registry list the service will register to
     * , only one of them will work.
     */
    protected List<RegistryConfig> registries;

    /**
     * The id list of registries the service will register to
     * Also see {@link #registries}, only one of them will work.
     */
    protected String registryIds;

    public void setRegistries(List<? extends AbstractConfig> list) {
        this.registries = (List<RegistryConfig>) list;
    }

    public List<RegistryConfig> getRegistries() {
        return registries;
    }

    public ApplicationConfig getApplication() {
        if (application != null) {
            return application;
        }
        return ApplicationModel.getConfigManager().getApplicationOrElseThrow();
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public void checkRegistry() {
        if (StringUtils.isEmpty(registryIds)) {
            if (CollectionUtils.isEmpty(registries)) {
                Collection<AbstractConfig> collection = ApplicationModel.getConfigManager().getConfigs(AbstractConfig.getTagName(RegistryConfig.class));
                List<AbstractConfig> registries = new ArrayList<>(collection);
                setRegistries(registries);
            }
        }
    }
}
