package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.SQLException;

/**
 * @author alirezafallahi
 */
public interface LightWeightStorage extends HerokuTransactional {
	
	void close();
	public void store(int lastProcessedPointer,String dataObjectType) throws SQLException;
	public int queryLastProcessedPointerFor(String id) throws SQLException;
}
