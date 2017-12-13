package com.rohit.javabrains.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.beans.Customer;

@Path("/customer")
public class CustomerResource {
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})	
	public List<Customer> getAllCustomers(@QueryParam("id") int id, @QueryParam("agent_id") int agent_id, @QueryParam("card_number") String card_number) {
		List<Customer> customer_list = Customer.getAllCustomer(id, agent_id, card_number);
		if(!customer_list.isEmpty()){
			return customer_list;
		}
		return null;
	}
	
	@POST
    public Response createCustomer(Customer customer) {
		return customer.save();
    }
}
