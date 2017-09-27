package com.bisk.qualtrics;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.codehaus.jettison.json.JSONObject;
import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

import java.io.InputStream;
import java.util.zip.*;

public class UncompressAndGiveOuput implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		
		MuleMessage msg = eventContext.getMessage();
		ZipInputStream zipIn = new ZipInputStream((InputStream) msg.getPayload());
        
		ZipEntry entry;
		
		StringBuilder buffer = new StringBuilder();
		
        while((entry = zipIn.getNextEntry())!=null)
        {
        	for (int c = zipIn.read(); c != -1; c = zipIn.read()) {
        		buffer.append(Character.toString ((char) c));
              }
        }
        
        JSONObject jsonObj = new JSONObject(buffer.toString());
        
        /*
         * //ByteArrayOutputStream baos = new ByteArrayOutputStream();
         * byte[] bytesIn = new byte[2048];
		Scanner sc = new Scanner(zipIn); 
        while (sc.hasNextLine()) {
            System.out.println(sc.nextLine());
        }*/
        
       /* while ((entry = zis.getNextEntry()) != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int count;

            while ((count = zis.read(buffer)) != -1) {
                baos.write(buffer, 0, count);
            }
            
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
        	baos.write(bytesIn,0,read);
        	
        }*/
        
	
		return jsonObj;
	}

}
