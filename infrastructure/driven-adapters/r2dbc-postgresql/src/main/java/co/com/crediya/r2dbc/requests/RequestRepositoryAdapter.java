package co.com.crediya.r2dbc.requests;

import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.r2dbc.entities.RequestsEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public class RequestRepositoryAdapter extends ReactiveAdapterOperations<
        Request,
        RequestsEntity,
        String,
        RequestReactiveRepository
> implements RequestRepository {
    public RequestRepositoryAdapter(RequestReactiveRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Request.class));
    }





    @Override
    public Flux<Request> findRequestsByState(List<Long> states, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findByIdStateIn(states, pageable)
                .map(super::toEntity);
    }
}
