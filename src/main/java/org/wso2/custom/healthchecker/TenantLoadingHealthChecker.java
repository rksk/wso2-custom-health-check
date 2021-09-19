package org.wso2.custom.healthchecker;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.healthcheck.api.core.HealthChecker;
import org.wso2.carbon.healthcheck.api.core.exception.BadHealthException;
import org.wso2.carbon.healthcheck.api.core.model.HealthCheckerConfig;
import org.wso2.custom.healthchecker.internal.DataHolder;

import java.util.Map;
import java.util.Properties;

public class TenantLoadingHealthChecker implements HealthChecker {

    private static final Log log = LogFactory.getLog(TenantLoadingHealthChecker.class);
    protected HealthCheckerConfig healthCheckerConfig = null;
    private static final String HEALTH_CHECKER_NAME = "TenantLoadingHealthChecker";
    private static final String TENANT_CONFIGURATION_CONTEXTS = "tenant.config.contexts";
    private static final String NUMBER_OF_LOADED_TENANTS = "loaded.tenants";

    @Override
    public String getName() {

        return HEALTH_CHECKER_NAME;
    }

    @Override
    public void init(HealthCheckerConfig healthCheckerConfig) {

        this.healthCheckerConfig = healthCheckerConfig;
    }

    @Override
    public Properties checkHealth() throws BadHealthException {
        
        Properties properties = new Properties();
        int loadedTenants = 0;
        
        ConfigurationContext mainConfigCtx = DataHolder.getMainServerConfigContext();
        if (mainConfigCtx != null) {
            Map<String, ConfigurationContext> tenantConfigContexts = (Map<String, ConfigurationContext>)
                    mainConfigCtx.getProperty(TENANT_CONFIGURATION_CONTEXTS);
            if (tenantConfigContexts != null) {
                loadedTenants = tenantConfigContexts.size();
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("Number of loaded tenants : " + loadedTenants);
        }
        properties.put(NUMBER_OF_LOADED_TENANTS, loadedTenants);
        return properties;
    }

    @Override
    public boolean isEnabled() {

        return this.healthCheckerConfig == null || healthCheckerConfig.isEnable();
    }

    @Override
    public int getOrder() {

        if (this.healthCheckerConfig == null) {
            return 0;
        } else {
            return healthCheckerConfig.getOrder();
        }
    }
}
