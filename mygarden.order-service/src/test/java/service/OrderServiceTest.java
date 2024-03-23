package service;


import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.order.inventory.InventoryClient;
import org.order.inventory.InventoryID;
import org.order.inventory.InventoryResponseDTO;
import org.order.model.Order;
import org.order.model.OrderItem;
import org.order.services.OrderService;


import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@QuarkusTest
public class OrderServiceTest {

    @Inject
    OrderService orderService;

}
