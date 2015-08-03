package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cloudreach.alumina.utils.UT;

/**
 * @author alirezafallahi
 */
public class ConnectionWrapper implements HerokuTransactional {
	
	private final Map<String, PreparedStatement> cachedStatements = new HashMap<String, PreparedStatement>();
	private final Connection connection;
	
	public ConnectionWrapper(Connection conn){
		connection = conn;
	}
	@Override
	public void beginTransaction() throws SQLException
	{
		// Not for this non-xa transaction.
	}
	protected Connection connection(){
		return connection;
	}
	void close()
	{
		for (PreparedStatement cachedStatement : cachedStatements.values()) {
			try {
				cachedStatement.close();
			}
			catch (SQLException exe) {}
		}
		cachedStatements.clear(); //If we reopen the connection, we cannot use cached statements from the old closed one.
		try {
			connection().rollback(); //Always roll back as commit on closure is implementation-dependent.
			connection().close();
		}
		catch (SQLException exe) {}
	}
	private PreparedStatement getCachedStatement(String command) throws SQLException
	{
		PreparedStatement statem = cachedStatements().get(command);
		if(null != statem && statem.isClosed()) {
			UT.LOG("SQL, Cached closed statement.");
			statem = null;
		}
		if(statem == null) {
			statem = connection().prepareStatement(command,ResultSet.TYPE_SCROLL_INSENSITIVE,java.sql.ResultSet.CONCUR_READ_ONLY);
			cachedStatements().put(command, statem);
		}
		return statem;
	}
	private Map<String, PreparedStatement> cachedStatements()
	{
		return cachedStatements;
	}
	public PreparedStatement prepare(String tableName, String command, List<SQLTypedValue> args) throws SQLException
	{
		PreparedStatement statem;
		try {
			statem = getStatement(command, args);
		}
		catch (SQLException e) {
			throw e;
		}
		UT.LOG("Query: "+ command);
		return statem;
	}
	protected PreparedStatement getStatement(String sql, List<SQLTypedValue> args) throws SQLException
	{
		PreparedStatement statem = getCachedStatement(sql);
		setArgs(statem, args);
		return statem;
	}
	void setArgs(PreparedStatement statem, List<SQLTypedValue> args) throws SQLException {
		if(null != args) {
			int i = 1;
			for (SQLTypedValue columnValuePair : args)
				setValue(statem, i++, columnValuePair);
		}
	}
	public void setValue(PreparedStatement statem, int i, SQLTypedValue columnValuePair) throws SQLException {
		columnValuePair.setValue(statem, i);
	}
	public String sql(String tableName)
	{ 
		StringBuilder sql = new StringBuilder().append("SELECT *")
			.append(" FROM ").append(tableName);
		UT.LOG("["+ Thread.currentThread().getName() +"]:" + sql.toString());
		return sql.toString();
	}
	public String sql(String tableName, String[] columns)
	{ 
		StringBuilder sql = new StringBuilder("SELECT ");
		int counter = columns.length;
		for(String each: columns){
			 sql.append(each);
			 if(counter>1){
				 sql.append(",");
			 }
			 counter--;
		}
		sql.append(" FROM ").append(tableName);
		UT.LOG("["+ Thread.currentThread().getName() +"]:" + sql.toString());
		return sql.toString();
	}
	@Override
	public void commitTransaction() throws SQLException {
		try {
			connection().commit();
		}
		catch (SQLException exe) {
			throw new SQLException("Exception caught committing transaction!", exe);
		}
		UT.LOG("["+ Thread.currentThread().getName() +"]:" + "transaction committed");
	}
	@Override
	public void prepareTransaction() throws SQLException {
		// TODO Auto-generated method stub
	}
	@Override
	public void rollbackTransaction() throws SQLException {
		try {
			connection().rollback();
		}
		catch (SQLException exe) {
			throw new SQLException("Exception caught committing transaction!", exe);
		}
		UT.LOG("["+ Thread.currentThread().getName() +"]:" + "transaction rolled back");
	}
	public void logString(String storageName){
		StringBuilder bob = new StringBuilder();
		bob.append("Creating a ").append("read/write ").append("connection to ").append(storageName).append("\n");
		UT.LOG(bob.toString());
	}
	/**
	 * if createTable method returns -1 it means the table exists.
	 * if createTable method returns  1 it means the table is created successfully and needs to be initialized.
	 * @param ddl
	 * @return
	 */
	public int createTable(String ddl) {
	
		try {
			Statement stmt = connection().createStatement();
			stmt.executeUpdate(ddl);
			return 1;
		} catch (SQLException e) {
			//The table already exists, so ignore.
			return -1;
		}	
	}
}
