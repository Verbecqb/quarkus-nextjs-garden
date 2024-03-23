package org.order.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.order.model.OrderItem;
import org.order.model.record.OrderItemDTO;

@Mapper(componentModel = "cdi")
public interface OrderItemMapper {

    OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    OrderItem dtoToEntity(OrderItemDTO orderItemDTO);

}
