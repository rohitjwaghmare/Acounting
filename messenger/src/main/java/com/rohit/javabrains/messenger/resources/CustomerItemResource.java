package com.rohit.javabrains.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.beans.CustomerSchemeItem;

@Path("/customer_item")
public class CustomerItemResource {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})	
	public List<CustomerSchemeItem> getAllCustomerSchemeItems(@QueryParam("cust_id") int cust_id, @QueryParam("item_id") int item_id) {
		List<CustomerSchemeItem> customer_item_list = CustomerSchemeItem.getAllCustomerItemMap(cust_id, item_id);
		if(!customer_item_list.isEmpty()){
			return customer_item_list;
		}
		return null;
	}
	
	@POST
    public Response createCustomerSchemeItem(CustomerSchemeItem cust_item) {
		
        return cust_item.save();
    }
}
