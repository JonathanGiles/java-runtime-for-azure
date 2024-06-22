package com.microsoft.aspire;

import com.microsoft.aspire.resources.Resource;

import java.util.List;

public interface Extension {
    String getName();

    String getDescription();

    List<Class<? extends Resource>> getAvailableResources();
}
