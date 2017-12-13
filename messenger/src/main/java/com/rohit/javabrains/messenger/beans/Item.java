package com.rohit.javabrains.messenger.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.util.DatabaseConnection;
import com.rohit.javabrains.messenger.util.LabelUtils;

public class Item {
	private int item_id;
	private int scheme_id;
	private String name;
	private String description;
	private float price;
	
	public enum Status {
       Available, Out_Of_Stock;
    };    
    private Status status;
    
    public Item() {}
	public Item(int item_id, int scheme_id, String name, String description, float price, Status status) {
		super();
		this.item_id = item_id;
		this.scheme_id = scheme_id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.status = status;
	}
	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
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
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
    
	public static List<Item> getAllItems(int id, int scheme_id){
		List<Item> item_list = new ArrayList<Item>();
		try {
			String where_clause = "";
			if(id != 0) {
				where_clause = "item_id = "+id;
			}else if(scheme_id != 0) {
				where_clause = "scheme_id = "+scheme_id;
			}
			ResultSet rs = DatabaseConnection.getAllRecords(LabelUtils.ITEM_TABLE, where_clause);
			while(rs.next()){
				Item i = new Item(rs.getInt("item_id"),
										rs.getInt("scheme_id"),
										rs.getString("name"),
										rs.getString("description"),
										rs.getFloat("price"),
										Item.Status.valueOf(rs.getString("status"))
									);
				item_list.add(i);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return item_list;
	}
	
	public Response save() {
		String query = "INSERT INTO "+LabelUtils.ITEM_TABLE+" (scheme_id, name, description, price,"
				+ "	status) values "
				+ "('"+scheme_id+"', '"+name+"', '"+description+"', "+price+", '"+status+"');";
		if(item_id != 0) {
			query = "UPDATE "+LabelUtils.ITEM_TABLE+" SET "+
						"scheme_id = '"+scheme_id+"', name = '"+name+"', description = '"+description+
						"', price = "+price+", status = '"+status+"'"+
						" WHERE item_id = "+item_id+";";
			
		}
		try {
			item_id = DatabaseConnection.execute(query);
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
