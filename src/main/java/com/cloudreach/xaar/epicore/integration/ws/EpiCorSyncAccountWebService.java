package com.cloudreach.xaar.epicore.integration.ws;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.cloudreach.alumina.framework.RestService;
import com.cloudreach.alumina.utils.UT;
import com.cloudreach.xaar.epicore.externaldata.persistence.HerokuStorage;
import com.cloudreach.xaar.epicore.externaldata.persistence.IntegerValue;
import com.cloudreach.xaar.epicore.externaldata.persistence.SQLTypedValue;
import com.cloudreach.xaar.epicore.externaldata.persistence.StringValue;
import com.cloudreach.xaar.epicore.externaldata.persistence.factory.HerokuStorageFactory;
/**
 * 
 * @author alirezafallahi
 * This web service is called from Salesforce when the flag "EpiCor Sync" is checked on Account.
 * Upon click on Save, a POST request is sent which then this web service catches the request and does what it needs to do.
 * In order to trigger this web service use this URL:http://cloudreach-connect-shared.herokuapp.com/ccsvc/xaar/epicore/syncaccount?key=039a7511-a302-40b4-b0c6-2d1b9f7c7ef6
 * Note typing this URL into Browser or issuing it with 'curl' command will cause a GET request to be made. For debugging purposes I make the GET method to call the POST method
 * to see what happens in the browser.
 */
public class EpiCorSyncAccountWebService extends RestService {
	
	@Override
	protected Object POST() throws Exception {
		
		InputStream messageBodyStream = REQUEST_BODY();
		BufferedReader reader =  new BufferedReader(new InputStreamReader(messageBodyStream));
		String data,id = null,name = null,custNum = null;
		while((data = reader.readLine())!=null){
			if (data.contains("<sf:Id>") || data.contains("</sf:Id>")){
				//we are on the correct line I suppose
				int id_sIndex = data.indexOf("<sf:Id>");
				int id_eIndex = data.indexOf("</sf:Id>");
				id = data.substring(id_sIndex + 7, id_eIndex);
				
			}
			if (data.contains("<sf:Name>") || data.contains("</sf:Name>")){
				//we are on the correct line I suppose
				int name_sIndex = data.indexOf("<sf:Name>");
				int name_eIndex = data.indexOf("</sf:Name>");
				name = data.substring(name_sIndex + 9 , name_eIndex);
				
			}
			if (data.contains("<sf:CustNum__c>") || data.contains("</sf:CustNum__c>")){
				//we are on the correct line I suppose
				int custNum_sIndex = data.indexOf("<sf:CustNum__c>");
				int custNum_eIndex = data.indexOf("</sf:CustNum__c>");
				custNum = data.substring(custNum_sIndex + 15, custNum_eIndex);
			}
		}
		reader.close();
		UT.LOG("XAAR[Account Sync] "+"Id:"+id+";"+"Name:"+name+";"+"CustNum:"+custNum);
		HerokuStorage storage= HerokuStorageFactory.createStorage();
		String sql = "INSERT INTO ACCOUNT_TB(SALESFORCEID,CUSTNUM, NAME) VALUES(?,?,?)";
		List<SQLTypedValue> values = new ArrayList<>(3);
		StringValue  idValue = new StringValue("SALESFORCEID",id.length(),id);
		IntegerValue intValue = new IntegerValue("CUSTNUM",Integer.valueOf(custNum));
		StringValue  nameValue = new StringValue("NAME",name.length(),name);
		values.add(idValue);
		values.add(intValue);
		values.add(nameValue);
		storage.insert(sql, values);
		storage.commitTransaction();
		//storage.close();
		return super.POST();
	} 
}
