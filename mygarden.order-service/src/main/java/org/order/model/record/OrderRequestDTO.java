package org.order.model.record;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Null;
import org.order.model.validators.OrderRequestValidationGroup;

import java.util.List;
import java.util.UUID;

public record OrderRequestDTO (

        @Null(groups = OrderRequestValidationGroup.POST.class)
        @NotBlank(groups = OrderRequestValidationGroup.PUT.class)
        Long id,

        @NotBlank(message = "Lastname should be provided.")
        String lastname,

        @NotBlank(message = "Firstname should be provided.")
        String firstname,

        @NotEmpty(message = "At least on item should be ordered.")
        List<OrderItemDTO> order_items

) {  }

