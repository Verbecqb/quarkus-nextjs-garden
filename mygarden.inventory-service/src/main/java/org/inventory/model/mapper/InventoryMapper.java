package org.inventory.model.mapper;

import org.inventory.model.record.InventoryRequestDTO;
import org.inventory.model.record.InventoryResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.inventory.model.Inventory;

@Mapper(componentModel = "cdi")
public interface InventoryMapper {
    InventoryMapper INSTANCE = Mappers.getMapper(InventoryMapper.class);
    Inventory requestDtoToEntity(InventoryRequestDTO inventoryRequestDTO);
    InventoryResponseDTO entityToResponseDTO(Inventory inventory);

}
