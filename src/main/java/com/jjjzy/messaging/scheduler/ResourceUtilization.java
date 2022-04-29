package com.jjjzy.messaging.scheduler;


import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.MetricDatum;
import com.amazonaws.services.cloudwatch.model.PutMetricDataRequest;
import com.amazonaws.services.cloudwatch.model.StandardUnit;
import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@EnableScheduling
@Component
public class ResourceUtilization {
    @Autowired
    private AmazonCloudWatch amazonCloudWatch;

    private static final Logger log = LoggerFactory.getLogger(ResourceUtilization.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 60000)
    public void reportCurrentTime() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);

        List<Dimension> cpuDimensions = List.of(new Dimension()
                .withName("Resources")
                .withValue("Messaging CPU Load at " + new Date()));

        List<Dimension> memoryDimension = List.of(new Dimension()
                .withName("Resources")
                .withValue("Messaging Memory Usage at " + new Date()));


        log.info("Cpu load: {}, free memory: {}", operatingSystemMXBean.getProcessCpuLoad(), operatingSystemMXBean.getFreePhysicalMemorySize());
        MetricDatum cpuDatum = new MetricDatum()
                .withMetricName("CPU load")
                .withUnit(StandardUnit.Percent)
                .withValue(operatingSystemMXBean.getProcessCpuLoad() * 100)
                .withTimestamp(new Date())
                .withDimensions(cpuDimensions);
        MetricDatum memoryDatum = new MetricDatum()
                .withMetricName("Memory Usage")
                .withUnit(StandardUnit.Count)
                .withValue((double) operatingSystemMXBean.getFreePhysicalMemorySize())
                .withTimestamp(new Date())
                .withDimensions(memoryDimension);
        this.amazonCloudWatch.putMetricData(new PutMetricDataRequest()
                .withNamespace("messaging")
                .withMetricData(cpuDatum, memoryDatum));
    }
}
