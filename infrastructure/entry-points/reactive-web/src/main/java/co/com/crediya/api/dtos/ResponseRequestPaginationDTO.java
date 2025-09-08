package co.com.crediya.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseRequestPaginationDTO<T> {
    private int page;
    private int size;
    private List<T> content;

}
