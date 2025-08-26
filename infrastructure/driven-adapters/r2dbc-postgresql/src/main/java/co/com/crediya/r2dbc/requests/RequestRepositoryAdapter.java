package co.com.crediya.r2dbc.requests;

import co.com.crediya.model.request.Request;
import co.com.crediya.model.request.gateways.RequestRepository;
import co.com.crediya.r2dbc.entities.RequestsEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RequestRepositoryAdapter extends ReactiveAdapterOperations<
        Request,
        RequestsEntity,
        Long,
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



}
