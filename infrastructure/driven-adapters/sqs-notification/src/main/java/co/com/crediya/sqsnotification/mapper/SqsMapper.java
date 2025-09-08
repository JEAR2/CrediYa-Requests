package co.com.crediya.sqsnotification.mapper;

import co.com.crediya.model.notification.model.MessageNotification;
import co.com.crediya.sqsnotification.dto.NotificationMessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface SqsMapper {
    NotificationMessageDTO messageToResponse(MessageNotification messageNotification);
}
