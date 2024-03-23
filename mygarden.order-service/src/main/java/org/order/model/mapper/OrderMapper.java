package org.order.model.mapper;

import org.mapstruct.*;
import org.order.model.Order;
import org.order.model.record.OrderRequestDTO;
import org.order.model.record.OrderResponseDTO;
import org.mapstruct.factory.Mappers;
import org.order.model.OrderItem;
import org.order.model.record.OrderItemDTO;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "cdi")
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "orderItemList", target = "order_items")
    OrderResponseDTO entityToResponseDTO(Order order);

    @Mapping(target="id", ignore= true)
    @Mapping(source = "order_items", target = "orderItemList")
    Order requestDtoToEntity(OrderRequestDTO orderDto);

    @Mapping(target="id", ignore= true)
    @Mapping(source = "createdAt", target="createdAt", expression = "java(Instant.now())")
    @Mapping(source = "order_items", target = "orderItemList")
    @ValueMappings({
            @ValueMapping( source = MappingConstants.ANY_REMAINING, target = "PENDING" ),
    })
    Order requestDtoToNewEntity(OrderRequestDTO orderDto);

    @Mapping(source = "order_items", target = "orderItemList")
    Order requestDtoWithIDToEntity(OrderRequestDTO orderDto);

    default List<OrderItemDTO> mapOrderItemsToDTO(OrderRequestDTO order, List<OrderItem> orderItem) {
        return orderItem.stream()
                .map(OrderItemMapper.INSTANCE::orderItemToOrderItemDTO)
                .collect(Collectors.toList());
    }


}
