package com.cognixia.jump.jdbc.ems;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
public class AddressDAOClass implements AddressDAO{
	private Connection conn = ConnectionManager.getConnection();

	@Override
	public List<Address> getAllAddress() {
		try {
			// find all the address...
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM address");
			List<Address> addressList = new ArrayList<Address>();
			while(rs.next()) {
				// ...iterate through to get column info...
				int id = rs.getInt("address_id");
				int streetNum = rs.getInt("address_streetnumber");
				String streetName = rs.getString("address_streetname");
				String city = rs.getString("address_city");
    			String state = rs.getString("address_state");
    			int zip = rs.getInt("address_zipcode");
				// ...then add them to a list...
				Address address = new Address(id, streetNum, streetName, city, state, zip);
				addressList.add(address);
			}
			// ...and return that list once finished
			return addressList;
		} catch (SQLException e) {
			System.out.println("Could not retrieve list of address from database");
		}	
		// return null just in case exception is thrown
		return null;
	}
	@Override
	public Address getAddressById(int addId) {
		try {
			// set up prepared statement to get a Address using its id
			PreparedStatement pstmt = conn.prepareStatement("select * from address where address_id = ?");
			pstmt.setInt(1, addId);
			ResultSet rs = pstmt.executeQuery();
			rs.next();
			// retrieve all column info and save it to Address object and return that object
			int id = rs.getInt("address_id");
			int streetNum = rs.getInt("address_streetnumber");
			String streetName = rs.getString("address_streetname");
			String city = rs.getString("address_city");
			String state = rs.getString("address_state");
			int zip = rs.getInt("address_zipcode");
			Address address = new Address(id, streetNum, streetName, city, state, zip);
			return address;
		} catch (SQLException e) {
			System.out.println("Address with id = " + addId + " not found.");
		}
		// if address not found, will return null
		return null;
	}
	@Override
	public Address getAddressByName(String streetName) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("select * from address where address_streetname = ?");
		pstmt.setString(1, streetName);
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int id = rs.getInt("address_id");
		int streetNum = rs.getInt("address_streetnumber");
		String streetName1 = rs.getString("address_streetname");
		String city = rs.getString("address_city");
		String state = rs.getString("address_state");
		int zip = rs.getInt("address_zipcode");
		Address address = new Address(id, streetNum, streetName1, city, state, zip);
		return address;
	} catch (SQLException e) {
		System.out.println("Address with name = " + streetName + " not found.");
	}
		return null;
	}
	@Override
	public boolean addAddress(Address address) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("insert into address (address_streetnumber, address_streetname, address_city, address_state, address_zipcode) values (?, ?, ?, ?, ?)");
		pstmt.setInt(1, address.getStreetNum()); 
		pstmt.setString(2, address.getStreetName());
		pstmt.setString(3, address.getCity());
		pstmt.setString(4, address.getState());
		pstmt.setInt(5, address.getZipCode());
		int i = pstmt.executeUpdate();
		if(i > 0) {
			return true;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		return false;
	}
	@Override
	public boolean deleteAddress(int addId) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("delete from address where address_id = ?");
		pstmt.setInt(1, addId);
		int i = pstmt.executeUpdate();
		if(i > 0) {
			return true;
		}
	} catch (SQLException e) {
		System.out.println("Address with id = " + addId + " not found.");
	}
		return false;
	}
	@Override
	public boolean updateAddress(Address address) {
		try {
		PreparedStatement pstmt = conn.prepareStatement("update address set address_streetnumber = ?," + 
																			"address_streetname = ?," + 
																			"address_city = ?, " +
																			"address_state = ?," + 
																			"address_zipcode = ? WHERE address_id = ?");
		pstmt.setInt(1, address.getStreetNum()); 
		pstmt.setString(2, address.getStreetName());
		pstmt.setString(3, address.getCity());
		pstmt.setString(4, address.getState());
		pstmt.setInt(5, address.getZipCode());;
		pstmt.setInt(6, address.getAddId());
		int i = pstmt.executeUpdate();
		if(i > 0) {
			return true;
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
		return false;
	}
}	