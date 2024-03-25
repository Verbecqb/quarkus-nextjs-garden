package org.garden.orders.rest;

import io.quarkus.test.junit.QuarkusIntegrationTest;

// Quarkus integration test are run in NATIVE
// Can simply extend OrderResourceTest which does not use any Dependency Injection

@QuarkusIntegrationTest
public class OrderResourceIT extends OrderResourceTest {


}
