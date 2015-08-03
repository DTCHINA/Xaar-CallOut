package com.cloudreach.xaar.epicore.externaldata.rdbms.config;

/**
 * @author alirezafallahi
 */
public class PostgreSQLConfig extends JDBCConfig {

	public PostgreSQLConfig(String url, String logon_id, String password,
			String driverClass) {
		super(url, logon_id, password, driverClass);
	}
	
	public String sql(){
		
		StringBuilder bob = new StringBuilder();
		bob.append("SELECT");
		bob.append("p.partnum").append(",");
		bob.append("p.partnum").append(",");
		bob.append("pl.ListCode").append(",");
		bob.append("p.listdescription").append(",");
		bob.append("array_to_string(").append("array_agg");
		bob.append("( distinct coalesce(currencycode,'unknown')");
		bob.append("|| ").append("##");
		bob.append("|| ");
		bob.append("cast(coalesce(plp.BasePrice,0) as varchar(200))");
		bob.append("|| ").append(',').append(")").append(", ").append(" - ").append("as Prices");
	    bob.append("FROM ");
	    bob.append("pricelstparts plp");
	    bob.append("INNER JOIN ");
	    bob.append("pricelst pl on pl.ListCode=plp.ListCode");
	    bob.append("INNER JOIN ");
	    bob.append("part p on p.partnum = plp.partnum");
	    bob.append("group by pl.ListCode,p.partnum,p.listdescription");
	    return bob.toString();
	}
}
