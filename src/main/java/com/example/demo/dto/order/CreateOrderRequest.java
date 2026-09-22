package com.example.demo.dto.order;


import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Setter
@Getter
public class CreateOrderRequest {

    @NotNull(message = "{validation.requested-date.required}")
    @FutureOrPresent(message = "{validation.requested-date.future-or-present}")
    private LocalDate requestedDate;

    private UUID serviceCategoryUuid;

    @NotNull(message = "{validation.customer.required}")
    private UUID customerUuid;

    @NotNull(message = "{validation.address.required}")
    private UUID addressUuid;
}
