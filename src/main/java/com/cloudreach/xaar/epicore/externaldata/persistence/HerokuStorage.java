package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.SQLException;
import java.util.List;

/**
 * @author alirezafallahi
 */
public interface HerokuStorage extends HerokuTransactional 
{
	public void insert(String fullQualifiedSQL,List<SQLTypedValue> values) throws SQLException;
	void close();
}
