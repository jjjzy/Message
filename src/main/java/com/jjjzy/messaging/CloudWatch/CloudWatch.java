package com.jjjzy.messaging.CloudWatch;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudWatch {

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Autowired
    AWSCredentialsProvider awsCredentialsProvider;

    @Bean
    public AmazonCloudWatch cloudWatchAsyncClient() {
        return AmazonCloudWatchClient
                .builder()
                .withRegion(this.awsRegion)
                .withCredentials(this.awsCredentialsProvider)
                .build();
    }
}