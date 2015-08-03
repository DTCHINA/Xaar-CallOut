package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.SQLException;

/**
 * @author alirezafallahi
 */
public interface HerokuTransactional{
	/**
	 * Open a transaction
	 */
	void beginTransaction() throws SQLException;
	/**
	 * Commit the current transaction
	 */
	void commitTransaction() throws SQLException;
	/**
	 * Prepare the current transaction
	 */
	void prepareTransaction() throws SQLException;
	/**
	 * Roll back the current transaction
	 */
	void rollbackTransaction() throws SQLException;
}
