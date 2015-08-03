package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.security.CodeSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.cloudreach.alumina.utils.UT;
import com.cloudreach.xaar.epicore.application.exceptions.HerokuClientStartupException;
import com.cloudreach.xaar.epicore.externaldata.rdbms.config.JDBCConfig;

/**
 * @author alirezafallahi
 */
public class HerokuStorageImpl implements HerokuStorage 
{
	private final JDBCConfig config;
	private ConnectionWrapper connectionWrapper;
	
	private HerokuStorageImpl(JDBCConfig config){
		this.config = config;
		try {
			open();
		} catch (HerokuClientStartupException e) {
			throw e;
		}
	}
	//TableName-> <ColumnName, Array of position of column in query as Integer[0] and column type as Integer[1]
	private final Map<String, Map<String, Integer[]>> boColumnMaps = new java.util.HashMap<String, Map<String, Integer[]>>(1023); 
	
	/**
	 * Open the underlying connection to the storage mechanism.
	 */
	public void open() throws HerokuClientStartupException {
		try {
			Class<? extends Driver> driverClass;
			if(config.loader() == null){
				driverClass = (Class<? extends Driver>) Class.forName(jdbcConfig().getDriverClass());
			}else{
				driverClass = (Class<? extends Driver>) config.loader().loadClass(jdbcConfig().getDriverClass());
			}
			CodeSource driverCodeSource = driverClass.getProtectionDomain().getCodeSource();
			UT.LOG("Driver class  " + driverCodeSource);
		}
		catch (ClassNotFoundException e) {
			UT.LOG("%%%%%%"+e.getMessage());
			throw new HerokuClientStartupException(e);
		}
		try 
		{
			Connection conn = DriverManager.getConnection(jdbcConfig().getUrl(), getConnnectionProperties());
			connectionWrapper(new ConnectionWrapper(conn));
			conn.setAutoCommit(false);
			conn.setReadOnly(false);
			DatabaseMetaData meta = conn.getMetaData();
			//	 gets driver info:
			StringBuilder buffy = new StringBuilder(2000);
			buffy.append("****** Database Information ******").append("\n")
				.append("\tDatabase product name:     ").append(meta.getDatabaseProductName()).append("\n")
				.append("\tDatabase product version:  ").append(meta.getDatabaseProductVersion()).append("\n")
				//.append("\tDatabase Major version:    ").append(meta.getDatabaseMajorVersion()).append("\n")
				//.append("\tDatabase Minor version:    ").append(meta.getDatabaseMinorVersion()).append("\n")
				.append("\tJDBC Driver name:          ").append(meta.getDriverName()).append("\n")
				.append("\tJDBC Driver version:       ").append(meta.getDriverVersion()).append("\n")
				.append("\tJDBC Driver Major version: ").append(meta.getDriverMajorVersion()).append("\n")
				.append("\tJDBC Driver Minor version: ").append(meta.getDriverMinorVersion());
			UT.LOG(buffy.toString());
			connectionWrapper().logString("Heroku");
		}
		catch (SQLException e) {
			UT.LOG("$$$$$$$$$"+e.getMessage());
			throw new HerokuClientStartupException(e);
		}
	}
	private JDBCConfig jdbcConfig(){
		return config;
	}
	protected Properties getConnnectionProperties()
	{
		return jdbcConfig().getConnectionProperties();
	}
	private void connectionWrapper(ConnectionWrapper connectionWrapper)
	{
		this.connectionWrapper = connectionWrapper;
	}
	private ConnectionWrapper connectionWrapper(){
		return connectionWrapper;
	}
	protected Connection newConnection(Properties logonInfo) throws SQLException
	{
		Connection con = DriverManager.getConnection(jdbcConfig().getUrl(), getConnnectionProperties());
		return con;
	}
	public void close()
	{
		if(connectionWrapper() != null) {
			connectionWrapper().close();
		}
	}
	/**
	 * 
	 * @param tableName
	 * @param metadata
	 * @return A Map of Column Name -> Object[] where the array contains String (Column Type) and Index
	 * @throws SQLException
	 */
	private Map<String, Integer[]> columnNameMap(String tableName, ResultSetMetaData metadata) throws SQLException {
		Map<String, Integer[]> map = boColumnMaps.get(tableName);
		if (map == null) {
			map = new java.util.LinkedHashMap<String, Integer[]>(101);
			for (int i = 1; i <= metadata.getColumnCount(); i++) {
				map.put(metadata.getColumnName(i).toUpperCase(), new Integer[]{Integer.valueOf(i),Integer.valueOf(metadata.getColumnType(i))});	
			}
			boColumnMaps.put(tableName, map);
		}
		return map;
	}
	private Map<String, Integer[]> columnNameMapNoCache(ResultSetMetaData metadata) throws SQLException {
	
		Map<String, Integer[]> map = new java.util.LinkedHashMap<String, Integer[]>(101);
		for (int i = 1; i <= metadata.getColumnCount(); i++) {
			map.put(metadata.getColumnName(i).toUpperCase(), new Integer[]{Integer.valueOf(i),Integer.valueOf(metadata.getColumnType(i))});	
		}
		return map;
	}
	@Override
	public void beginTransaction() throws SQLException {
		//nothing to do here as the transaction is locally managed and not distributed across resources.
		//The two parts of the transaction are writing the serialised BusinessObject to underlying OutPutStream to create
		//JSON object and the other part of the transaction is reading off EpiCore storage.
	}
	@Override
	public void commitTransaction() throws SQLException {
		connectionWrapper().commitTransaction();
	}
	@Override
	public void prepareTransaction() throws SQLException {
		//Nothing to do here.
	}
	@Override
	public void rollbackTransaction() throws SQLException {
		connectionWrapper().rollbackTransaction();	
	}
	@Override
	public void insert(String fullQualifiedSQL,List<SQLTypedValue> values) throws SQLException {
		try {
			PreparedStatement psInsert = connectionWrapper().prepare(null, fullQualifiedSQL,values);
			psInsert.executeUpdate();
			
		} catch (SQLException e) {
			throw new SQLException(fullQualifiedSQL + " failed",e);
		}
	}
}