package co.com.crediya.sqsnotification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsConfig {
    @Bean
    public SqsAsyncClient sqsClient() {
        return SqsAsyncClient.builder().region(Region.US_EAST_2).build();
    }
}
