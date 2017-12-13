package com.rohit.javabrains.messenger.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.util.DatabaseConnection;
import com.rohit.javabrains.messenger.util.LabelUtils;

public class CustomerSchemeItem {
	private int mapping_id;
	private int customer_id;
	private int item_id;
	private String scheme_name;
	private java.util.Date activation_date;
	private String comment;
	private boolean is_active;
	
	public CustomerSchemeItem() {}
	
	public CustomerSchemeItem(int mapping_id, int customer_id, int item_id, String scheme_name, Date activation_date,
			String comment, int is_active) {
		super();
		this.mapping_id = mapping_id;
		this.customer_id = customer_id;
		this.item_id = item_id;
		this.scheme_name = scheme_name;
		this.activation_date = activation_date;
		this.comment = comment;
		this.is_active = false;
		if(is_active == 1) {
			this.is_active = true;
		}
	}
	public int getMapping_id() {
		return mapping_id;
	}
	public void setMapping_id(int mapping_id) {
		this.mapping_id = mapping_id;
	}
	public int getCustomer_id() {
		return customer_id;
	}
	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public String getScheme_name() {
		return scheme_name;
	}
	public void setScheme_name(String scheme_name) {
		this.scheme_name = scheme_name;
	}
	public java.util.Date getActivation_date() {
		return activation_date;
	}
	public void setActivation_date(java.util.Date activation_date) {
		this.activation_date = activation_date;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public boolean isIs_active() {
		return is_active;
	}
	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}
	
	public static List<CustomerSchemeItem> getAllCustomerItemMap(int cust_id, int item_id){
		List<CustomerSchemeItem> cust_item_list = new ArrayList<CustomerSchemeItem>();
		try {
			String where_clause = "";
			if(cust_id != 0 && item_id != 0) {
				where_clause = "customer_id = "+cust_id+" and item_id = "+item_id;
			}else if(cust_id != 0) {
				where_clause = "customer_id = "+cust_id;
			}else if(item_id != 0) {
				where_clause = "item_id = "+item_id;
			}
			ResultSet rs = DatabaseConnection.getAllRecords(LabelUtils.CUSTOMER_SCHEME_ITEM_TABLE, where_clause);
			while(rs.next()){
				java.util.Date activation_date = new java.util.Date(rs.getDate("activation_date").getTime());
				CustomerSchemeItem csi = new CustomerSchemeItem(rs.getInt("mapping_id"),
										rs.getInt("customer_id"),
										rs.getInt("item_id"),
										rs.getString("scheme_name"),
										activation_date,
										rs.getString("comment"),
										rs.getInt("is_active")
									);
				cust_item_list.add(csi);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cust_item_list;
	}
	
	public Response save() {
		java.sql.Date activationDate = new java.sql.Date(activation_date.getTime());
		String query = "INSERT INTO "+LabelUtils.CUSTOMER_SCHEME_ITEM_TABLE+" (customer_id, item_id, activation_date, comment,"
				+ "	is_active) values "
				+ "("+customer_id+", "+item_id+", '"+activationDate+"', '"+comment+"', "+is_active+");";
		if(mapping_id != 0) {
			query = "UPDATE "+LabelUtils.CUSTOMER_SCHEME_ITEM_TABLE+" SET "+
						"customer_id = "+customer_id+", item_id = "+item_id+", activation_date = '"+activationDate+
						"', comment = '"+comment+"', is_active = "+is_active+
						" WHERE mapping_id = "+mapping_id+";";
			
		}
		try {
			mapping_id = DatabaseConnection.execute(query);
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
