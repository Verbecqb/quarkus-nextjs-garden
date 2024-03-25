package org.garden.inventory.mappers;

import org.garden.inventory.model.Inventory;
import org.garden.inventory.model.record.InventoryRequestDTO;
import org.garden.inventory.model.record.InventoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.DayOfWeek;

@Mapper(componentModel = "cdi")
public interface InventoryMapper {
    @Mapping(target="date", expression = "java(inventoryRequestDTO.date().with(java.time.DayOfWeek.MONDAY))")
    Inventory requestDtoToEntity(InventoryRequestDTO inventoryRequestDTO);

    InventoryResponseDTO entityToResponseDTO(Inventory inventory);

}
