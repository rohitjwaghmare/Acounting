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

public class CustomerInstallment {
	
	private int installment_id;
	private int cust_item_id;
	private java.util.Date installment_date;
	private java.util.Date paid_date;
	private float amount;
	private float fine;
	private float total_amount;
	private String comment;
	
	public enum Status{
		Pending, Paid, Skipped;
	}
	
	private Status status;

	public CustomerInstallment() {}
	
	public CustomerInstallment(int installment_id, int cust_item_id, Date installment_date, Date paid_date,
			float amount, float fine, float total_amount, String comment, Status status) {
		super();
		this.installment_id = installment_id;
		this.cust_item_id = cust_item_id;
		this.installment_date = installment_date;
		this.paid_date = paid_date;
		this.amount = amount;
		this.fine = fine;
		this.total_amount = total_amount;
		this.comment = comment;
		this.status = status;
	}

	public int getInstallment_id() {
		return installment_id;
	}

	public void setInstallment_id(int installment_id) {
		this.installment_id = installment_id;
	}

	public int getCust_item_id() {
		return cust_item_id;
	}

	public void setCust_item_id(int cust_item_id) {
		this.cust_item_id = cust_item_id;
	}

	public java.util.Date getInstallment_date() {
		return installment_date;
	}

	public void setInstallment_date(java.util.Date installment_date) {
		this.installment_date = installment_date;
	}

	public java.util.Date getPaid_date() {
		return paid_date;
	}

	public void setPaid_date(java.util.Date paid_date) {
		this.paid_date = paid_date;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public float getFine() {
		return fine;
	}

	public void setFine(float fine) {
		this.fine = fine;
	}

	public float getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(float total_amount) {
		this.total_amount = total_amount;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public static List<CustomerInstallment> getAllCustomerItemMap(int id, int cust_item_id, int customer_id, int item_id){
		List<CustomerInstallment> cust_item_list = new ArrayList<CustomerInstallment>();
		try {
			String where_clause = "";
			if(id != 0) {
				where_clause = "installment_id = "+id;
			}else if(cust_item_id != 0) {
				where_clause = "cust_item_id = "+cust_item_id;
			}else if(customer_id != 0 && item_id != 0) {
				//TODO to add query for fetching mapping from CUSTOMER_SCHEME_ITEM_TABLE
				where_clause = "cust_item_id = (select mapping_id from "+LabelUtils.CUSTOMER_SCHEME_ITEM_TABLE+
									" where customer_id = "+customer_id+" and item_id = "+item_id+")"; 
			}
			ResultSet rs = DatabaseConnection.getAllRecords(LabelUtils.CUSTOMER_INSTALLMENTS, where_clause);
			while(rs.next()){
				java.util.Date installment_date = new java.util.Date(rs.getDate("installment_date").getTime());
				java.util.Date paid_date = new java.util.Date(rs.getDate("paid_date").getTime());
				CustomerInstallment csi = new CustomerInstallment(rs.getInt("installment_id"),
										rs.getInt("cust_item_id"),
										installment_date,
										paid_date,
										rs.getFloat("amount"),
										rs.getFloat("fine"),
										rs.getFloat("total_amount"),
										rs.getString("comment"),
										CustomerInstallment.Status.valueOf(rs.getString("status"))
									);
				cust_item_list.add(csi);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cust_item_list;
	}
	
	public Response save() {
		java.sql.Date installmentDate = new java.sql.Date(installment_date.getTime());
		java.sql.Date paidDate = new java.sql.Date(paid_date.getTime());
		float totalAmount = amount+fine;
		String query = "INSERT INTO "+LabelUtils.CUSTOMER_INSTALLMENTS+" (cust_item_id, installment_date, paid_date, amount,"
				+ "fine, total_amount, comment) values "
				+ "("+cust_item_id+", '"+installmentDate+"', '"+paidDate+"', "+amount+", "+fine+", "+totalAmount+", '"+comment+"');";
		if(installment_id != 0) {
			query = "UPDATE "+LabelUtils.CUSTOMER_INSTALLMENTS+" SET "+
						"cust_item_id = "+cust_item_id+", installment_date = '"+installmentDate+"', paid_date = '"+paidDate+
						"', amount = "+amount+", fine = "+fine+", total_amount = "+totalAmount+", comment = '"+comment+"' "+
						" WHERE installment_id = "+installment_id+";";
			
		}
		try {
			installment_id = DatabaseConnection.execute(query);
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
