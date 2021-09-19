package org.wso2.custom.healthchecker.internal;

import org.apache.axis2.context.ConfigurationContext;

public class DataHolder {
    private static ConfigurationContext mainServerConfigContext;

    public static ConfigurationContext getMainServerConfigContext() {
        return mainServerConfigContext;
    }

    public static void setMainServerConfigContext(ConfigurationContext mainServerConfigContext) {
        DataHolder.mainServerConfigContext = mainServerConfigContext;
    }
}
