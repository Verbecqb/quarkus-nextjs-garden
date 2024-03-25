package org.garden.orders.rest;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.keycloak.client.KeycloakTestClient;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Uni;

import org.garden.orders.OrderResource;
import org.garden.orders.clients.InventoryClient;
import org.garden.orders.clients.InventoryResponseDTO;
import org.garden.orders.model.Order;
import org.garden.orders.model.OrderItem;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;


import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;


import static jakarta.ws.rs.core.Response.Status.*;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class OrderResourceTest {

    @TestHTTPEndpoint(OrderResource.class)
    @TestHTTPResource
    URL orderResource;

    static final UUID uuid1 = UUID.randomUUID();
    static final UUID uuid2 = UUID.randomUUID();

    KeycloakTestClient keycloakClient = new KeycloakTestClient();


    protected String getAccessToken(String userName) {
        //return "";
        return keycloakClient.getAccessToken(userName);
    }

    @Test
    public void successful_order() {

        OrderItem orderItem1 = OrderItem.builder().product_id(uuid1).quantity(2).build();
        OrderItem orderItem2 = OrderItem.builder().product_id(uuid2).quantity(1).build();

        Order order1 = Order.builder()
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        RestAssured
                .given()
                .auth().oauth2(getAccessToken("bob"))
                .contentType(ContentType.JSON)
                .body(order1)
                .when()
                .post(orderResource)
                .then()
                .statusCode(CREATED.getStatusCode())
                .body("id", notNullValue());

    }

    @Test
    public void non_authenticated_user_response_401() {

        OrderItem orderItem1 = OrderItem.builder().product_id(uuid1).quantity(2).build();
        OrderItem orderItem2 = OrderItem.builder().product_id(uuid2).quantity(1).build();

        Order order1 = Order.builder()
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(order1)
                .when()
                .post(orderResource).then().statusCode(UNAUTHORIZED.getStatusCode());

    }

    @Test
    public void order_with_non_existing_inventory_ids() {

        OrderItem orderItem1 = OrderItem.builder().product_id(uuid1).quantity(2).build();
        OrderItem orderItem2 = OrderItem.builder().product_id(uuid2).quantity(1).build();

        Order order1 = Order.builder()
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        RestAssured
                .given()
                .auth().oauth2("Bearer " + getAccessToken("alice"))
                .contentType(ContentType.JSON)
                .body(order1)
                .when()
                .post(orderResource)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode());

    }

    @Test
    public void order_with_not_enough_stock() {

        OrderItem orderItem1 = OrderItem.builder().product_id(uuid1).quantity(25).build();
        OrderItem orderItem2 = OrderItem.builder().product_id(uuid2).quantity(1).build();

        Order order1 = Order.builder()
                .firstname("John").lastname("Tod")
                .orderItemList(Arrays.asList(orderItem1, orderItem2))
                .build();

        RestAssured
                .given()
                .auth().oauth2(getAccessToken("bob"))
                .contentType(ContentType.JSON)
                .body(order1)
                .when()
                .post(orderResource)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode());

    }


    public void create_order_with_sufficient_inventory() {

        UUID uuid = UUID.randomUUID();

        InventoryClient inventoryClientMock = Mockito.mock(InventoryClient.class);

        Mockito.when(
                        inventoryClientMock.getProductsByUUID(ArgumentMatchers.anySet()))
                .thenReturn(Uni.createFrom().item(Set.of(new InventoryResponseDTO(5, 0))));

        QuarkusMock.installMockForType(inventoryClientMock, InventoryClient.class);

        OrderItem orderItem1 = OrderItem.builder().product_id(uuid).quantity(5).build();

        Order order = Order.builder()
                .firstname("John").lastname("Tod")
                .orderItemList(Collections.singletonList(orderItem1))
                .build();



        RestAssured
                .given()
                .auth().oauth2(getAccessToken("bob"))
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(orderResource)
                .then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode());


    }

    public void create_order_with_unsufficient_inventory() {

        InventoryClient inventoryClient = Mockito.mock(InventoryClient.class);
    }
}
