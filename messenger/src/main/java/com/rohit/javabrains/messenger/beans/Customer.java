package com.rohit.javabrains.messenger.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.util.DatabaseConnection;
import com.rohit.javabrains.messenger.util.LabelUtils;

public class Customer {
	private int customer_id;
	private int agent_id;
	private String name;
	private String address;
	private String mobile_number;
	
	public enum Gender{
		M, F;
	}
	private Gender gender; 
	private String email;
	 
	public enum Status {
       Active, In_Active;
    }; 
	public Status status;
	private boolean is_agent_too;
	private String card_number;
	
	public Customer() {}
	
	public Customer(int customer_id, int agent_id, String name, String address, String mobile_number, Gender gender,
			String email, Status status, int is_agent_too, String card_number) {
		super();
		this.customer_id = customer_id;
		this.agent_id = agent_id;
		this.name = name;
		this.address = address;
		this.mobile_number = mobile_number;
		this.gender = gender;
		this.email = email;
		this.status = status;
		this.is_agent_too = false;
		if(is_agent_too == 1) {
			this.is_agent_too = true;
		}
		this.card_number = card_number;
	}

	public int getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}

	public int getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(int agent_id) {
		this.agent_id = agent_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobile_number() {
		return mobile_number;
	}

	public void setMobile_number(String mobile_number) {
		this.mobile_number = mobile_number;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public boolean isIs_agent_too() {
		return is_agent_too;
	}

	public void setIs_agent_too(boolean is_agent_too) {
		this.is_agent_too = is_agent_too;
	}	
	
	public static List<Customer> getAllCustomer(int id, int agent_id, String card_number){
		List<Customer> customer_list = new ArrayList<Customer>();
		try {
			String where_clause = "";
			if(id != 0) {
				where_clause = "customer_id = "+id;
			}else if(card_number != null) {
				where_clause = "card_number = "+card_number;
			}else if(agent_id != 0) {
				where_clause = "agent_id = "+agent_id;
			}
			System.out.println(where_clause);
			ResultSet rs = DatabaseConnection.getAllRecords(LabelUtils.CUSTOMER_TABLE, where_clause);
			while(rs.next()){
				Customer i = new Customer(rs.getInt("customer_id"),
										rs.getInt("agent_id"),
										rs.getString("name"),
										rs.getString("address"),
										rs.getString("mobile_number"),
										Customer.Gender.valueOf(rs.getString("gender")),
										rs.getString("email"),
										Customer.Status.valueOf(rs.getString("status")),
										rs.getInt("is_agent_too"),
										rs.getString("card_number")
									);
				customer_list.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return customer_list;
	}
	
	public Response save() {
		Object agent = null;
		if(agent_id != 0) {
			agent = agent_id;
		}
		String query = "INSERT INTO "+LabelUtils.CUSTOMER_TABLE+" (agent_id, name, address, mobile_number, "
				+ " gender, email, is_agent_too, status, card_number) values "
				+ "( "+agent+", '"+name+"', '"+address+"', "+mobile_number+", '"+gender
				+ "', '"+email+"', "+is_agent_too+", '"+status+"', '"+card_number+"');";
		if(customer_id != 0) {
			query = "UPDATE "+LabelUtils.CUSTOMER_TABLE+" SET "+
						"customer_id = "+customer_id+", agent_id = "+agent+", name = '"+name+
						"', address = '"+address+"', mobile_number = '"+mobile_number+"', gender = '"+gender+
						"', email = '"+email+"', is_agent_too = "+is_agent_too+", card_number = '"+card_number+"'"+
						" WHERE customer_id = "+customer_id+";";
			
		}
		try {
			System.out.println(query);
			customer_id = DatabaseConnection.execute(query);
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

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}
}
