package co.com.crediya.api.dtos;

import jakarta.validation.constraints.NotBlank;


public record UpdateStateRequestDTO(@NotBlank(message = "es required.")String state){
}
