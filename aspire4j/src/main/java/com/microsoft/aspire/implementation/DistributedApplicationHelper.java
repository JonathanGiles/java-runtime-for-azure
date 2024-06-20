package com.microsoft.aspire.implementation;

import com.microsoft.aspire.DistributedApplication;

public class DistributedApplicationHelper {
    private static DistributedApplicationAccessor accessor;

    public interface DistributedApplicationAccessor {
        DistributedApplication getDistributedApplication();
    }

    public static void setAccessor(DistributedApplicationAccessor accessor) {
        DistributedApplicationHelper.accessor = accessor;
    }

    public static DistributedApplication getDistributedApplication() {
        return accessor.getDistributedApplication();
    }
}
