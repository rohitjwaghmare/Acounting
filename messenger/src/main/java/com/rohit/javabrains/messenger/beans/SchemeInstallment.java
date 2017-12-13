package com.rohit.javabrains.messenger.beans;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.rohit.javabrains.messenger.util.DatabaseConnection;
import com.rohit.javabrains.messenger.util.LabelUtils;

public class SchemeInstallment {
	
	private int scheme_installment_id;
	private int scheme_id;
	
	public enum Month {
       January, February, March, April, May, June, July, August, September, October, November, December;
    };    
    private Month month;
    
    private java.util.Date installment_date;
    private float installment_price;
    private float fine;
    private String comment;
    
    public SchemeInstallment(){}
    
    public SchemeInstallment(int scheme_installment_id, int scheme_id, Month month, Date installment_date,
			float installment_price, String comment) {
		super();
		this.scheme_installment_id = scheme_installment_id;
		this.scheme_id = scheme_id;
		this.month = month;
		this.installment_date = installment_date;
		this.installment_price = installment_price;
		this.comment = comment;
	}

	public int getScheme_installment_id() {
		return scheme_installment_id;
	}

	public void setScheme_installment_id(int scheme_installment_id) {
		this.scheme_installment_id = scheme_installment_id;
	}

	public int getScheme_id() {
		return scheme_id;
	}

	public void setScheme_id(int scheme_id) {
		this.scheme_id = scheme_id;
	}

	public Month getMonth() {
		return month;
	}

	public void setMonth(Month month) {
		this.month = month;
	}

	public Date getInstallment_date() {
		return installment_date;
	}

	public void setInstallment_date(java.util.Date installment_date) {
		this.installment_date = installment_date;
	}

	public float getInstallment_price() {
		return installment_price;
	}

	public void setInstallment_price(float installment_price) {
		this.installment_price = installment_price;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public static List<SchemeInstallment> getAllSchemeInstallment(int id, int scheme_id){
		List<SchemeInstallment> scheme_installment_list = new ArrayList<SchemeInstallment>();
		try {
			String where_clause = "";
			if(id != 0) {
				where_clause = "scheme_installment_id = "+id;
			}else if(scheme_id != 0) {
				where_clause = "scheme_id = "+scheme_id;
			}
			ResultSet rs = DatabaseConnection.getAllRecords(LabelUtils.SCHEME_INSTALLMENT_TABLE, where_clause);
			while(rs.next()){
				java.util.Date newDate = new java.util.Date(rs.getDate("installment_date").getTime());
				SchemeInstallment si = new SchemeInstallment(rs.getInt("scheme_installment_id"),
										rs.getInt("scheme_id"),
										SchemeInstallment.Month.valueOf(rs.getString("month")),
										newDate,
										rs.getFloat("installment_price"),
										rs.getString("comment")
									);
				System.out.println(si);
				scheme_installment_list.add(si);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return scheme_installment_list;
	}
	
	public Response save() {
		java.sql.Date installmentDate = new java.sql.Date(installment_date.getTime());
		String query = "INSERT INTO "+LabelUtils.SCHEME_INSTALLMENT_TABLE+" (scheme_id, month, installment_date, installment_price,"
				+ "	comment) values "
				+ "('"+scheme_id+"', '"+month+"', '"+installmentDate+"', "+installment_price+", '"+comment+"');";
		if(scheme_installment_id != 0) {
			query = "UPDATE "+LabelUtils.SCHEME_INSTALLMENT_TABLE+" SET "+
						"scheme_id = '"+scheme_id+"', month = '"+month+"', installment_date = "+installmentDate+
						", installment_price = "+installment_price+", comment = "+comment+
						" WHERE scheme_installment_id = "+scheme_installment_id+";";
		}
		try {
			scheme_installment_id = DatabaseConnection.execute(query);
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