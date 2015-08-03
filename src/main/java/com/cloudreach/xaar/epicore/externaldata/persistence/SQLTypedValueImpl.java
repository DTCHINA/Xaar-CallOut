package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author alirezafallahi
 */
public abstract class SQLTypedValueImpl implements SQLTypedValue {
	
	public static final String NULL = "NULL";
		
	public final String columnName;
	public final Object value;
	
	public SQLTypedValueImpl(String columnName) {
		this(columnName, null);
	}
	protected SQLTypedValueImpl(String columnName, Object value) {
		super();
		this.columnName = columnName;
		this.value = value;
	}
	@Override
	public void setValue(PreparedStatement statem, int index) throws SQLException {
		setValue(statem, index, value());
	}
	public void setValue(PreparedStatement statem, int index, Object value) throws SQLException {
		statem.setObject(index, value);
	}
	@Override
	public Object getValue(ResultSet rs) throws SQLException {
		return rs.getObject(columnName);
	}		
	@Override
	public Object value() {
		return value;
	}
	@Override
	public String printValue() {
		if (null == value)
			return NULL;
		return value().toString();
	}
	@Override
	public String name() {
		return columnName;
	}
}
