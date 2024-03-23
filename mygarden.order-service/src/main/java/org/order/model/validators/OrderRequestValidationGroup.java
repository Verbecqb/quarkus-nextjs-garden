package org.order.model.validators;

import jakarta.validation.groups.Default;

public interface OrderRequestValidationGroup {
     interface POST extends Default {};
     interface PUT extends Default {};
}
