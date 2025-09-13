package co.com.crediya.sqs.listener.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateStateLoanListenerDTO (@JsonProperty("requestId") String idRequest,
                                          @JsonProperty("decision") String stateLoan){

}
