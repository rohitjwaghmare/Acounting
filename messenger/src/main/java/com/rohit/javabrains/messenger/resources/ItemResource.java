package com.rohit.javabrains.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.beans.Item;

@Path("/item")
public class ItemResource {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})	
	public List<Item> getAllItems(@QueryParam("id") int id, @QueryParam("scheme_id") int scheme_id) {
		List<Item> item_list = Item.getAllItems(id, scheme_id);
		if(!item_list.isEmpty()){
			return item_list;
		}
		return null;
	}
	
	@POST
    public Response createSchemeInstallment(Item item) {
		return item.save();
    }
}
