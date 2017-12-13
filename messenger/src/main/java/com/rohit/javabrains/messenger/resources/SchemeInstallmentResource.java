package com.rohit.javabrains.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.beans.SchemeInstallment;

@Path("/scheme_installment")
public class SchemeInstallmentResource {

	@GET
	@Produces({MediaType.APPLICATION_JSON})	
	public List<SchemeInstallment> getAllSchemes(@QueryParam("id") int id, @QueryParam("scheme_id") int scheme_id) {
		List<SchemeInstallment> scheme_installment_list = SchemeInstallment.getAllSchemeInstallment(id, scheme_id);
		if(!scheme_installment_list.isEmpty()){
			return scheme_installment_list;
		}
		return null;
	}
	
	@POST
    public Response createSchemeInstallment(SchemeInstallment schemeInstallment) {
		return schemeInstallment.save();
    }
}
