package co.com.crediya.sqs.listener;

import co.com.crediya.sqs.listener.dtos.UpdateStateLoanListenerDTO;
import co.com.crediya.usecase.request.IRequestUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.model.Message;

import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class SQSProcessor implements Function<Message, Mono<Void>> {
    private final IRequestUseCase requestUseCase;
    private final ObjectMapper objectMapper;
    @Override
    public Mono<Void> apply(Message message) {
        log.info("Received SQS message - update state of loan: {}", message);
        log.info("SQS raw body={}", message.body());
        return Mono.fromCallable( () ->  objectMapper.readValue(message.body(), UpdateStateLoanListenerDTO.class))
                .flatMap( data -> requestUseCase.updateStateRequestWithOutEmail(data.idRequest(), data.stateLoan()) )
                .then(Mono.empty());
    }
}
