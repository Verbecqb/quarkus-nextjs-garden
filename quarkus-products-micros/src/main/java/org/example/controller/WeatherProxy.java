package org.example.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.model.Weather;
import org.jboss.resteasy.reactive.RestQuery;


@Path("/v1")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(baseUri = "http://api.open-meteo.com/")
public interface WeatherProxy {

    @GET
    @Path("/forecast")
    Weather getWeather(@RestQuery("longitude") double longitude, @RestQuery("latitude") double latitude);

}
