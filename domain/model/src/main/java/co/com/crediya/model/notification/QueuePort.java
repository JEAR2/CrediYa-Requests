package co.com.crediya.model.notification;

import co.com.crediya.model.notification.model.AutoValidationPayload;
import co.com.crediya.model.notification.model.MessageNotification;
import reactor.core.publisher.Mono;

public interface QueuePort {
    Mono<Void> publishChangeStatus(MessageNotification messageNotification);
    Mono<Void> publishAutoValidation(AutoValidationPayload payload);
    Mono<Void> publishValidationResult(AutoValidationPayload payload);
}
