package org.garden.planner;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.garden.planner.model.Item;
import org.garden.planner.service.ItemService;

@Path("/v0/items")
public class ItemResource {

    @Inject
    ItemService itemService;

    @POST
    public Uni<Item> post(@NotNull Item item) {
        return itemService.post(item);
    }
}
