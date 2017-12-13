package com.rohit.javabrains.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.beans.CustomerInstallment;

@Path("/customer_installments")
public class CustomerInstallmentResource {

	@GET
	@Produces({MediaType.APPLICATION_JSON})	
	public List<CustomerInstallment> getAllCustomerInstallments(@QueryParam("id") int id, 
											@QueryParam("item_id") int item_id,
											@QueryParam("customer_id") int cust_id,
											@QueryParam("item_id") int cust_item_id) {
		List<CustomerInstallment> customer_installments_list = CustomerInstallment.getAllCustomerItemMap(id, cust_item_id, cust_id, item_id);
		if(!customer_installments_list.isEmpty()){
			return customer_installments_list;
		}
		return null;
	}
	
	@POST
    public Response createCustomerInstallment(CustomerInstallment cust_instal) {
		return cust_instal.save();
    }
}
