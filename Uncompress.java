package demo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;

public class Uncompress implements Callable {

	private final static int BUFFER_SIZE = 2048;
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		
		MuleMessage msg = eventContext.getMessage();
		
		//new ZipInputStream((InputStream) msg.getPayload());
        final ZipInputStream zis = new ZipInputStream(
        								new BufferedInputStream(new ByteArrayInputStream((byte[]) msg.getPayload())));
        
        	       
        ZipEntry zipEntry = zis.getNextEntry();
        Map<String, ByteArrayOutputStream> files = new HashMap<String, ByteArrayOutputStream>();
        		
        while (zipEntry != null) {
            final String fileName = zipEntry.getName();
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            System.out.println("\tExtracting entry: " + fileName);
            int count;
            byte data[] = new byte[BUFFER_SIZE];

            if (!zipEntry.isDirectory()) {
            	BufferedOutputStream out = new BufferedOutputStream(outputStream, BUFFER_SIZE);
            
            	while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
            		out.write(data, 0, count);
            	}
            	files.put(fileName, outputStream);
            	
            zipEntry = zis.getNextEntry();
            out.flush();
            out.close();
	        }
		}
        zis.closeEntry();
        zis.close();
			
        return files;
        
	}
}
