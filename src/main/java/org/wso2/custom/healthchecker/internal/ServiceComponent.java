package org.wso2.custom.healthchecker.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.wso2.carbon.healthcheck.api.core.HealthChecker;
import org.wso2.carbon.utils.ConfigurationContextService;
import org.wso2.custom.healthchecker.TenantLoadingHealthChecker;

@Component(
        name = "org.wso2.custom.healthchecker",
        immediate = true
)
public class ServiceComponent {

    private static final Log log = LogFactory.getLog(ServiceComponent.class);

    @Activate
    protected void activate(ComponentContext context) {

        try {
            BundleContext bundleContext = context.getBundleContext();
            bundleContext.registerService(HealthChecker.class.getName(), new TenantLoadingHealthChecker(), null);

            log.info("org.wso2.custom.healthchecker bundle is activated");
        } catch (Throwable e) {
            log.error("Error while activating org.wso2.custom.healthchecker", e);
        }
    }

    @Deactivate
    protected void deactivate(ComponentContext context) {

        log.info("org.wso2.custom.healthchecker bundle is deactivated");
    }

    @Reference(
            name = "config.context.service",
            service = org.wso2.carbon.utils.ConfigurationContextService.class,
            cardinality = ReferenceCardinality.OPTIONAL,
            policy = ReferencePolicy.DYNAMIC,
            unbind = "unsetConfigurationContextService")
    protected void setConfigurationContextService(ConfigurationContextService contextService) {
        DataHolder.setMainServerConfigContext(contextService.getServerConfigContext());
    }

    protected void unsetConfigurationContextService(ConfigurationContextService contextService) {
        DataHolder.setMainServerConfigContext(null);
    }
}
