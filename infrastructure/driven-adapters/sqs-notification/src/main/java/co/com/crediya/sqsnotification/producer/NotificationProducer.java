package co.com.crediya.sqsnotification.producer;

import co.com.crediya.model.notification.QueuePort;
import co.com.crediya.model.notification.model.MessageNotification;
import co.com.crediya.sqsnotification.mapper.SqsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;


@Service
@Log4j2
@RequiredArgsConstructor
public class NotificationProducer implements QueuePort {
    private final SqsAsyncClient sqs;
    private final ObjectMapper mapper = new ObjectMapper();
    private final SqsMapper sqsMapper;
    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @Override
    public Mono<Void> publishChangeStatus(MessageNotification messageNotification) {
        return Mono.fromCallable(() -> mapper.writeValueAsString(sqsMapper.messageToResponse(messageNotification)))
                .flatMap(json -> Mono.fromFuture(
                        sqs.sendMessage(SendMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .messageBody(json)
                                .build())))
                .doOnNext(response -> log.info("Message sent {}", response.messageId()))
                .then();
    }
}
