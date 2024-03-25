package org.garden.orders.service;


import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import org.garden.orders.services.OrderService;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;



import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@QuarkusTest
public class OrderServiceTest {

    @Inject
    OrderService orderService;

}
