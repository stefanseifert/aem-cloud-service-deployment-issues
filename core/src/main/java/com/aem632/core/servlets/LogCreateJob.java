package com.aem632.core.servlets;

import static com.aem632.core.servlets.BulkCreateLogMessagesServlet.PROPERTY_ID;
import static com.aem632.core.servlets.BulkCreateLogMessagesServlet.PROPERTY_NUMBER_MESSAGES_PER_JOB;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job that creates given number of log messages.
 */
@Component(service = JobConsumer.class, property = {
    JobConsumer.PROPERTY_TOPICS + "=" + LogCreateJob.JOB_TOPIC
})
public class LogCreateJob implements JobConsumer {

  static final String JOB_TOPIC = "test/logcreatejob";

  private static final Logger log = LoggerFactory.getLogger(LogCreateJob.class);

  @Override
  @SuppressWarnings("squid:S00112")
  public JobResult process(Job job) {

    String uniqueID = job.getProperty(PROPERTY_ID, "");
    int logMessageCount = job.getProperty(PROPERTY_NUMBER_MESSAGES_PER_JOB, 0);

    for (int i=0; i<logMessageCount; i++) {
      log.info("{} - TEST log message job {} #{}", uniqueID, job.getId(), i);
      try {
        Thread.sleep(1);
      }
      catch (InterruptedException ex) {
        throw new RuntimeException("Thread interrupted.", ex);
      }
    }

    return JobResult.OK;
  }

}
