package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Item {

	
	public Connection connect()
	{
	 Connection con = null;

	 try
	 {
	 Class.forName("com.mysql.jdbc.Driver");
	 con= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/test",
	 "root", "");
	 //For testing
	 System.out.print("Successfully connected");
	 }
	 catch(Exception e)
	 {
	 e.printStackTrace();
	 }

	 return con;
	}
	
	public String insertItem(String code, String name, String price, String desc)
	{
	String output = "";
	try
	{
	Connection con = connect();
	if (con == null)
	{
	return "Error while connecting to the database";
	}
	// create a prepared statement
	String query = " insert into items (`itemID`,`itemCode`,`itemName`,`itemPrice`,`itemDesc`)"
	+ " values (?, ?, ?, ?, ?)";
	PreparedStatement preparedStmt = con.prepareStatement(query);
	// binding values
	preparedStmt.setInt(1, 0);
	preparedStmt.setString(2, code);
	preparedStmt.setString(3, name);
	preparedStmt.setDouble(4, Double.parseDouble(price));
	preparedStmt.setString(5, desc);
	
	//execute the statement
	preparedStmt.execute();
	con.close();
	output = "Inserted successfully";
	}
	catch (Exception e)
	{
	output = "Error while inserting";
	System.err.println(e.getMessage());
	}
	return output;
	}
	
	public String readItems()
	{
	String output = "";
	try
	{
	Connection con = connect();
	if (con == null)
	{
	return "Error while connecting to the database for reading.";
	}
	// Prepare the html table to be displayed
	output = "<table border='1'><tr><th>Item Code</th>"
	+"<th>Item Name</th><th>Item Price</th>"
	+ "<th>Item Description</th>"
	+ "<th>Update</th><th>Remove</th></tr>";
	
	
	String query = "select * from items";
	Statement stmt = con.createStatement();
	ResultSet rs = stmt.executeQuery(query);
	// iterate through the rows in the result set
	while (rs.next())
	{
	String itemID = Integer.toString(rs.getInt("itemID"));
	String itemCode = rs.getString("itemCode");
	String itemName = rs.getString("itemName");
	String itemPrice = Double.toString(rs.getDouble("itemPrice"));
	String itemDesc = rs.getString("itemDesc");
	// Add a row into the html table
	output += "<tr><td>" + itemCode + "</td>";
	output += "<td>" + itemName + "</td>";
	output += "<td>" + itemPrice + "</td>";
	
	output += "<td>" + itemDesc + "</td>";
	// buttons
	output += "<td><input name='btnUpdate' "
	+ " type='button' value='Update'></td>"
	+ "<td><form method='post' action='items.jsp'>"
	+ "<input name='btnRemove' "
	+ " type='submit' value='Remove'>"
	+ "<input name='itemID' type='hidden' "
	+ " value='" + itemID + "'>" + "</form></td></tr>";
	}
	con.close();
	// Complete the html table
	output += "</table>";
	}
	catch (Exception e)
	{
	output = "Error while reading the items.";
	System.err.println(e.getMessage());
	}
	return output;
	}
	
	//delete items
	
	public String deleteItem(String itemID)
	{
	 String output = "";
	try
	 {
	 Connection con = connect();
	 if (con == null)
	 {
	 return "Error while connecting to the database for deleting.";
	 }
	 // create a prepared statement
	 String query = "delete from items where itemID=?";
	 PreparedStatement preparedStmt = con.prepareStatement(query);
	 // binding values
	 preparedStmt.setInt(1, Integer.parseInt(itemID));

	 // execute the statement
	 preparedStmt.execute();
	 con.close();
	 output = "Deleted successfully";
	 }
	catch (Exception e)
	 {
	 output = "Error while deleting the item.";
	 System.err.println(e.getMessage());
	 }
	return output;
	}
	
	//Update Item
	
			public String updateItem(int id, String code, String name, String price, String desc)
			{
				String output = "";
				try
				{
					Connection con = connect();
					if (con == null)
					{
						return "Error while connecting to the database";
					}

					// create a prepared statement
					
					String query = "update item set `itemCode`=?,`itemName`=?,`itemPrice`=?,`itemDesc`=? where `itemID`=?";
					PreparedStatement preparedStmt = con.prepareStatement(query);

					// binding values
					preparedStmt.setString(1, code);
					preparedStmt.setString(2, name);
					preparedStmt.setDouble(3, Double.parseDouble(price));
					preparedStmt.setString(4, desc);
					preparedStmt.setInt(5, id);

					//execute the statement
					preparedStmt.executeUpdate();
					con.close();
					output = "Item " + id + " Updated successfully";
				}
				catch (Exception e)
				{
					output = "Error while updating";
					System.err.println(e.getMessage());
				}
				return output;
			}
			
			//View Item
			public String viewItem(int id)
			{
				String output = "";
				try
				{
					Connection con = connect();
					if (con == null)
					{
						return "Error while connecting to the database for reading.";
					}

					String query = "select * from item where `itemID`=?;";
					PreparedStatement preparedStmt = con.prepareStatement(query);

					// binding values
					preparedStmt.setInt(1, id);
					ResultSet rs = preparedStmt.executeQuery();

					// iterate through the rows in the result set
					if (rs.next())
					{
						String itemID = Integer.toString(rs.getInt("itemID"));
						String itemCode = rs.getString("itemCode");
						String itemName = rs.getString("itemName");
						String itemPrice = Double.toString(rs.getDouble("itemPrice"));
						String itemDesc = rs.getString("itemDesc");

						output += "<form method='post' action='Items.jsp'> "
								+ "Item code: <input name='itemCode' type='text' class='form-control' value='"+ itemCode +"'><br>" 
								+ "Item name: <input name='itemName' type='text' class='form-control' value='"+ itemName +"'><br> "
								+ "Item price: <input name='itemPrice' type='text' class='form-control' value='"+ itemPrice +"'><br> "
								+ "Item description: <input name='itemDesc' type='text' class='form-control' value='"+ itemDesc +"'><br> "
								+ "<input name='action' value='update' type='hidden'> "
								+ "<input name='itemID' class='form-control' value='"+ itemID +"' type='hidden'> "
								+ "<input name='btnSubmit' type='submit' class='btn btn-secondary' value='Update Item "+ id +"'> "
								+ "</form> <br>"
								+ "<a href='Items.jsp'>Cancel Updating</a>";
					}

					con.close();
				}
				catch (Exception e)
				{
					output = "Error while reading the selected item.\nGo back to <a href='items.jsp'>Items.jsp</a>";
					System.err.println(e.getMessage());
				}
				return output;
			}


}
