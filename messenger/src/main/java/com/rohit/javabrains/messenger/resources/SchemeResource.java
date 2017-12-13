package com.rohit.javabrains.messenger.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.beans.Scheme;

@Path("/scheme")
public class SchemeResource {

	@GET
	@Produces({MediaType.APPLICATION_JSON})	
	public List<Scheme> getAllSchemes(@QueryParam("id") int id) {
		List<Scheme> scheme_list = Scheme.getAllScheme(id);
		
		if(!scheme_list.isEmpty()){
			return scheme_list;
		}
		return null;
	}
	
	@POST
    public Response createScheme(Scheme scheme) {        
        return scheme.save();
    }
}
