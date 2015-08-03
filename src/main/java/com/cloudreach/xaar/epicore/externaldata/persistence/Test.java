package com.cloudreach.xaar.epicore.externaldata.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cloudreach.xaar.epicore.externaldata.persistence.factory.HerokuStorageFactory;

public class Test {
	
	public static void main(String[] args) throws SQLException{
		HerokuStorage storage= HerokuStorageFactory.createStorage();
		String sql = "INSERT INTO ACCOUNT_TB(SALESFORCEID,CUSTNUM, NAME) VALUES(?,?,?)";
		List<SQLTypedValue> values = new ArrayList<>(3);
		StringValue  idValue = new StringValue("SALESFORCEID","21781278".length(),"21781278");
		IntegerValue intValue = new IntegerValue("CUSTNUM",5);
		StringValue  nameValue = new StringValue("NAME","Alireza".length(),"Alireza");
		values.add(idValue);
		values.add(intValue);
		values.add(nameValue);
		storage.insert(sql, values);
		storage.commitTransaction();
		storage.close();
	}

}
