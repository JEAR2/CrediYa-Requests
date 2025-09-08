package co.com.crediya.model.notification;

import co.com.crediya.model.notification.model.MessageNotification;
import reactor.core.publisher.Mono;

public interface QueuePort {
    Mono<Void> publishChangeStatus(MessageNotification messageNotification);
}
