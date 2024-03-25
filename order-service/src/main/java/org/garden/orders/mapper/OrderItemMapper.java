package org.garden.orders.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.garden.orders.model.OrderItem;
import org.garden.orders.model.record.OrderItemDTO;

@Mapper(componentModel = "cdi")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    OrderItem dtoToEntity(OrderItemDTO orderItemDTO);

}
