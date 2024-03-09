package org.example.controller;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.model.Weather;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/weather")
public class WeatherResource {

    @RestClient
    WeatherProxy weatherProxy;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public RestResponse<Weather> getWeather(@RestQuery("longitude") double longitude, @RestQuery("latitude") double latitude) {
        Weather weather = weatherProxy.getWeather(longitude, latitude);
        return RestResponse.ResponseBuilder.ok(weather).build();
    }
}
