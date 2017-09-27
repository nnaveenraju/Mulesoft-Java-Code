package GenericDatetimeFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

public class Datetimeformat_Gmt extends AbstractMessageTransformer {
	
	public Object transformMessage(MuleMessage message, String outputEncoding)
			throws TransformerException {
		
		DateFormat isoFormat;

		if (message.getInvocationProperty("InputDate").toString().contains("T")) {
			isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		}
		else
		{
			isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		}
		
		//SimpleDateFormat isoFormat = new SimpleDateFormat("MM/dd/yyyy'T'HH:mm:ss");
		//SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		isoFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		//DateFormat gmtDf = null;
		Date date = null;
		try {
			// replace String date from datamapper here
			//date = isoFormat.parse("01/19/2016 12:08:00");
			date = isoFormat.parse((String)message.getInvocationProperty("InputDate"));
			//gmtDf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			isoFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
			System.out.println("GMT   : " + isoFormat.format(date));
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return (isoFormat.format(date)).toString();
	}
}

