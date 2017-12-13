package com.rohit.javabrains.messenger.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.util.DatabaseConnection;
import com.rohit.javabrains.messenger.util.LabelUtils;

public class Scheme {
	
	private int scheme_id;
	private String name;
	private String description;
	private float total_price;
	private float fine;
	
	public enum Status {
       Active, Close, Suspended;
    };
    
    private Status status;
    
    public Scheme(){};

	@Override
	public String toString() {
		return "Scheme [scheme_id=" + scheme_id + ", name=" + name + ", description=" + description + ", total_price="
				+ total_price + ", fine=" + fine + ", status=" + status + "]";
	}

	public Scheme(int scheme_id, String name, String description, float total_price, float fine, Status status) {
		super();
		this.scheme_id = scheme_id;
		this.name = name;
		this.description = description;
		this.total_price = total_price;
		this.fine = fine;
		this.status = status;
	}

	public int getScheme_id() {
		return scheme_id;
	}

	public void setScheme_id(int scheme_id) {
		this.scheme_id = scheme_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getTotal_price() {
		return total_price;
	}

	public void setTotal_price(float total_price) {
		this.total_price = total_price;
	}

	public float getFine() {
		return fine;
	}

	public void setFine(float fine) {
		this.fine = fine;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public static List<Scheme> getAllScheme(int id){
		List<Scheme> scheme_list = new ArrayList<Scheme>();
		try {
			String where_clause = "";
			if(id != 0) {
				where_clause = "scheme_id = "+id;
			}
			ResultSet rs = DatabaseConnection.getAllRecords(LabelUtils.SCHEME_TABLE, where_clause);
			System.out.println(rs);
			while(rs.next()) {
				Scheme s = new Scheme(rs.getInt("scheme_id"),
									rs.getString("name"),
									rs.getString("description"),
									rs.getFloat("total_price"),
									rs.getFloat("fine"),
									Scheme.Status.valueOf(rs.getString("status")));
				scheme_list.add(s);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scheme_list;
	}
	
	public Response save() {
		String query = "INSERT INTO "+LabelUtils.SCHEME_TABLE+" (name, description, total_price, fine) values"
				+ "('"+name+"', '"+description+"', "+total_price+", "+fine+");";
		
		if(scheme_id != 0) {
			query = "UPDATE "+LabelUtils.SCHEME_TABLE+" SET "+
						"name = '"+name+"', description = '"+description+"', total_price = "+total_price+", fine = "+fine+
						" WHERE scheme_id = "+scheme_id+";";
			
		}
		try {
			scheme_id = DatabaseConnection.execute(query);
			Response response = Response.status(Response.Status.OK)
	                .entity(this)
	                .type(MediaType.APPLICATION_JSON).build();
			return response;
		}catch(Exception e) {
			StatusObject s = new StatusObject(400, e.getMessage()); 
			Response response = Response.status(Response.Status.BAD_REQUEST)
	                .entity(s)
	                .type(MediaType.APPLICATION_JSON).build();
			return response;
		}
	}
} 
