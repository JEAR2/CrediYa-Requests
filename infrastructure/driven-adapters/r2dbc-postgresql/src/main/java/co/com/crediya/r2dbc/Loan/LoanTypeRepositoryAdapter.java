package co.com.crediya.r2dbc.Loan;

import co.com.crediya.model.loantype.LoanType;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.r2dbc.entities.LoanTypeEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
        Long,
        LoanTypeReactiveRepository
        > implements LoanTypeRepository {

    public LoanTypeRepositoryAdapter(LoanTypeReactiveRepository repository,
                                     ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }
}
