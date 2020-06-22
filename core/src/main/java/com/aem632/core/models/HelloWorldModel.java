/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem632.core.models;

import static org.apache.sling.api.resource.ResourceResolver.PROPERTY_RESOURCE_TYPE;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.jackrabbit.util.ISO8601;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.settings.SlingSettingsService;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = SlingHttpServletRequest.class)
public class HelloWorldModel {

    @ValueMapValue(name=PROPERTY_RESOURCE_TYPE, injectionStrategy=InjectionStrategy.OPTIONAL)
    @Default(values="No resourceType")
    protected String resourceType;

    @OSGiService
    private SlingSettingsService settings;
    @SlingObject
    private Resource currentResource;
    @SlingObject
    private ResourceResolver resourceResolver;
    @SlingObject
    private SlingHttpServletRequest request;
    @SlingObject
    private SlingHttpServletResponse response;

    private String message;

    @PostConstruct
    protected void init() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        String currentPagePath = Optional.ofNullable(pageManager)
                .map(pm -> pm.getContainingPage(currentResource))
                .map(Page::getPath).orElse("");

        StringBuilder msg = new StringBuilder();
        msg.append("Hello World\n");
        msg.append("Resource type is: " + resourceType + "\n");
        msg.append("Current page is:  " + currentPagePath + "\n");
        msg.append("This is instance: " + settings.getSlingId() + "\n");
        msg.append("Rendered on:      " + ISO8601.format(System.currentTimeMillis()) + "\n");

        // set custom non-caching response headers
        Map<String,String> responseHeaders = new LinkedHashMap<>();

        Set<String> selectors = new HashSet<>(Arrays.asList(request.getRequestPathInfo().getSelectors()));
        if (selectors.contains("dispatcher-no-cache")) {
            responseHeaders.put("Dispatcher", "no-cache");
        }
        if (selectors.contains("no-cache")) {
            responseHeaders.put("Pragma", "no-cache");
            responseHeaders.put("Cache-Control", "no-cache");
            responseHeaders.put("Expires", "0");
        }

        if (!responseHeaders.isEmpty()) {
            msg.append("\n");
            msg.append("Custom response headers:\n");
            for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
                msg.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                response.addHeader(entry.getKey(), entry.getValue());
            }
        }
        
        message = msg.toString();
    }

    public String getMessage() {
        return message;
    }

}
