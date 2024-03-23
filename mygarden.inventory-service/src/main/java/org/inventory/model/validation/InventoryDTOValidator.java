package org.inventory.model.validation;

import jakarta.validation.groups.Default;

/**
 *
 */
public interface InventoryDTOValidator {
    interface POST extends Default {};
    interface PUT extends Default {};

}
