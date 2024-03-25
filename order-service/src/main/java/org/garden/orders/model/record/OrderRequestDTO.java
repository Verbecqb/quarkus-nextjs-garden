package org.garden.orders.model.record;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.garden.orders.model.validators.OrderRequestValidationGroup;

import java.util.List;

public record OrderRequestDTO (

        @Null(groups = OrderRequestValidationGroup.POST.class)
        @NotNull(groups = OrderRequestValidationGroup.PUT.class)
        Long id,

        @NotBlank(message = "Lastname should be provided.")
        String lastname,

        @NotBlank(message = "Firstname should be provided.")
        String firstname,

        @NotEmpty(message = "At least on item should be ordered.")
        @Valid
        List<OrderItemDTO> order_items

) {  }

