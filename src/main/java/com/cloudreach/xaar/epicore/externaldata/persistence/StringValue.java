package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author alirezafallahi
 */
public class StringValue extends SQLTypedValueImpl  {
	
	private final int columnWidth;
	
	public StringValue(String columnName, int columnWidth) {
		this(columnName, columnWidth, null);
	}
	public StringValue(String columnName, int columnWidth, String str) {
		super(columnName, str);
		this.columnWidth = columnWidth;
	}
	
	@Override
	public void setValue(PreparedStatement statem, int index, Object value) throws SQLException {
		statem.setString(index, (String) value);
	}
	@Override
	public String getValue(ResultSet rs) throws SQLException {
		return (String) super.getValue(rs);
	}
}
