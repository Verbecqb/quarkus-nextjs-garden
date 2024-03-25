package org.garden.orders.mapper;

import jakarta.inject.Inject;
import org.garden.orders.model.Order;
import org.garden.orders.model.OrderItem;
import org.garden.orders.model.OrderState;
import org.garden.orders.model.record.OrderItemDTO;
import org.garden.orders.model.record.OrderRequestDTO;
import org.garden.orders.model.record.OrderResponseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);


    @Mapping(source = "orderItemList", target = "order_items")
    OrderResponseDTO entityToResponseDTO(Order order);


    @Mapping(source = "order_items", target = "orderItemList")
    Order requestDtoToEntity(OrderRequestDTO orderDto);

    @Mapping(target="createdAt", expression = "java(java.time.Instant.now())")
    @Mapping(target = "state", constant = "PENDING") // New entity default to pending state
    @Mapping(source = "order_items", target = "orderItemList")
    Order requestDtoToNewEntity(OrderRequestDTO orderDto);

    @Mapping(source = "order_items", target = "orderItemList")
    Order requestDtoWithIDToEntity(OrderRequestDTO orderDto);

    default List<OrderItemDTO> mapOrderItemsToDTO(OrderRequestDTO order, List<OrderItem> orderItem) {
        return orderItem.stream()
                .map(OrderItemMapper.INSTANCE::orderItemToOrderItemDTO)
                .collect(Collectors.toList());
    }

}
