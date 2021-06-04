package com.aem632.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.JobManager;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * Servlet that creates a gerate number of log messages in parallel using Sling Jobs to test that all log messages are actually logged and can be retrieved
 * later.
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(resourceTypes = "aem632/components/page",
    methods = HttpConstants.METHOD_GET,
    selectors = "bulk-create-log-messages",
    extensions = "html")
public class BulkCreateLogMessagesServlet extends SlingSafeMethodsServlet {

  private static final long serialVersionUID = 1L;

  static final String PROPERTY_ID = "uniqueId";
  static final String PROPERTY_NUMBER_MESSAGES_PER_JOB = "numberLogMessagesPerJob";
  static final String PROPERTY_NUMBER_JOBS = "numberJobs";

  private static final Logger log = LoggerFactory.getLogger(BulkCreateLogMessagesServlet.class);

  @Reference
  @SuppressWarnings("squid:S1948")
  private JobManager jobManager;

  @Override
  protected void doGet(final SlingHttpServletRequest req,
      final SlingHttpServletResponse resp) throws ServletException, IOException {

    String uniqueId = req.getParameter(PROPERTY_ID);
    int numberMessagesPerJob = NumberUtils.toInt(req.getParameter(PROPERTY_NUMBER_MESSAGES_PER_JOB));
    int numberJobs = NumberUtils.toInt(req.getParameter(PROPERTY_NUMBER_MESSAGES_PER_JOB));

    if (!StringUtils.isBlank(uniqueId)) {
      for (int i=0; i<numberJobs;i++) {
        Map<String,Object> props = new HashMap<>();
        props.put(PROPERTY_ID, uniqueId);
        props.put(PROPERTY_NUMBER_MESSAGES_PER_JOB, numberMessagesPerJob);
        Job job = jobManager.addJob(LogCreateJob.JOB_TOPIC, props);
        log.info("TEST created job: {}", job.getId());
      }
    }

    PageManager pageManager = req.getResourceResolver().adaptTo(PageManager.class);
    Page page = pageManager.getContainingPage(req.getResource());
    if (page != null) {
      resp.sendRedirect(page.getPath() + ".html");
    }
  }
}
