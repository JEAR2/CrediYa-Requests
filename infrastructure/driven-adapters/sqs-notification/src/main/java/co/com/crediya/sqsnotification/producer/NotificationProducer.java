package co.com.crediya.sqsnotification.producer;

import co.com.crediya.model.notification.QueuePort;
import co.com.crediya.model.notification.model.AutoValidationPayload;
import co.com.crediya.model.notification.model.EventReport;
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
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    ;
    private final SqsMapper sqsMapper;
    @Value("${aws.sqs.queueUrl}")
    private String queueUrl;

    @Value("${aws.sqs.queueAutomaticUrl}")
    private String autoValidationQueueUrl;

    @Value("${aws.sqs.queueAutomaticUrlResult}")
    private String autoValidationResultQueueUrl;

    @Value("${aws.sqs.queueRequestApprovedReport}")
    private String requestsApprovedReportQueueUrl;

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

    @Override
    public Mono<Void> publishAutoValidation(AutoValidationPayload payload) {
        log.info("Se enviará payload a SQS: {}", payload);
        return Mono.fromCallable(() -> mapper.writeValueAsString(payload))
                .flatMap(json -> Mono.fromFuture(
                        sqs.sendMessage(SendMessageRequest.builder()
                                .queueUrl(autoValidationQueueUrl)
                                .messageBody(json)
                                .build())))
                .doOnNext(resp -> log.info("AutoValidation gluing messageId={}", resp.messageId()))
                .then();
    }

    @Override
    public Mono<Void> publishValidationResult(AutoValidationPayload payload) {
        return Mono.fromCallable(() -> mapper.writeValueAsString(payload))
                .flatMap(json -> Mono.fromFuture(
                        sqs.sendMessage(SendMessageRequest.builder()
                                .queueUrl(autoValidationResultQueueUrl)
                                .messageBody(json)
                                .build())))
                .doOnNext(resp -> log.info("AutoValidationResult messageId={}", resp.messageId()))
                .then();
    }

    @Override
    public Mono<Void> publishStatusApprovedReport(EventReport eventReport) {
        log.info("Se enviará payload a SQS: {}", eventReport);
        return Mono.fromCallable(() -> mapper.writeValueAsString(eventReport))
                .flatMap(json -> Mono.fromFuture(
                        sqs.sendMessage(SendMessageRequest.builder()
                                .queueUrl(requestsApprovedReportQueueUrl)
                                .messageBody(json)
                                .build())))
                .doOnNext(resp -> log.info("StatusApprovedReport gluing messageId={}", resp.messageId()))
                .then();
    }
}
