package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author alirezafallahi
 */
public class IntegerValue extends SQLTypedValueImpl{

	public IntegerValue(String columnName) {
		this(columnName, null);
	}
	public IntegerValue(String columnName, Integer inte) {
		super(columnName, inte);
	}
	@Override
	public void setValue(PreparedStatement statem, int index, Object value) throws SQLException {
		statem.setInt(index, (Integer) value);
	}		
	@Override
	public Object getValue(ResultSet rs) throws SQLException {
		return super.getValue(rs);
	}
}
