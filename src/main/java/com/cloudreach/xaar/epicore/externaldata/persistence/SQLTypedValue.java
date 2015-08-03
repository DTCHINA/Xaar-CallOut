package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author alirezafallahi
 */
public interface SQLTypedValue {
	
	void setValue(PreparedStatement stmt, int index) throws SQLException;
	Object getValue(ResultSet rs) throws SQLException;
	Object value(); 
	String printValue();
	String name();
}
