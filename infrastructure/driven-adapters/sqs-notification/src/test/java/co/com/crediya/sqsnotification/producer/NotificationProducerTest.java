package co.com.crediya.sqsnotification.producer;

import co.com.crediya.model.notification.model.AutoValidationPayload;
import co.com.crediya.model.notification.model.MessageNotification;
import co.com.crediya.sqsnotification.dto.NotificationMessageDTO;
import co.com.crediya.sqsnotification.mapper.SqsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class NotificationProducerTest {

    @Mock
    private SqsAsyncClient sqs;

    @Mock
    private SqsMapper sqsMapper;

    private NotificationProducer notificationProducer;

    @BeforeEach
    void setUp() {
        notificationProducer = new NotificationProducer(sqs, sqsMapper);
        // Inyectamos las URLs de SQS (porque @Value no se procesa en tests sin Spring)
        ReflectionTestUtils.setField(notificationProducer, "queueUrl", "https://fake-queue");
        ReflectionTestUtils.setField(notificationProducer, "autoValidationQueueUrl", "https://fake-auto");
        ReflectionTestUtils.setField(notificationProducer, "autoValidationResultQueueUrl", "https://fake-auto-result");
    }

    @Test
    void publishChangeStatus_ShouldSendMessageToSqs() throws Exception {
        // Arrange
        MessageNotification message = MessageNotification.builder()
                .idRequest(1L)
                .state("APPROVED")
                .email("test@test.com")
                .build();

        NotificationMessageDTO messageDTO= new NotificationMessageDTO(12L,"","");

        ObjectMapper mapper = new ObjectMapper();
        // SqsMapper debe convertir a otro objeto, lo podemos devolver igual
        when(sqsMapper.messageToResponse(any())).thenReturn(messageDTO);

        // Simular que SQS responde con un future completado
        SendMessageResponse response = SendMessageResponse.builder()
                .messageId("123")
                .build();
        when(sqs.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        // Act
        Mono<Void> result = notificationProducer.publishChangeStatus(message);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(sqs).sendMessage(any(SendMessageRequest.class));
    }

    @Test
    void publishValidationResult_ShouldSendMessageToSqs() {
        // Arrange
        AutoValidationPayload payload = AutoValidationPayload.builder()
                .requestId("REQ-1")
                .applicantId("user@test.com")
                .build();

        SendMessageResponse response = SendMessageResponse.builder()
                .messageId("789")
                .build();
        when(sqs.sendMessage(any(SendMessageRequest.class)))
                .thenReturn(CompletableFuture.completedFuture(response));

        // Act
        Mono<Void> result = notificationProducer.publishValidationResult(payload);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(sqs).sendMessage(any(SendMessageRequest.class));
    }



}