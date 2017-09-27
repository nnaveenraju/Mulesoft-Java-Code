package com.bisk.msu;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;

import com.<>.msu.model.MSUSISData;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RemoveDuplicates implements Callable {
//as :object { class : "com.bisk.msu.model.MSUSISData" }
	
	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		// TODO Auto-generated method stub
		
		//ArrayList<MSUSISData> msudata =  (ArrayList<MSUSISData>)eventContext.getMessage().getPayload();
 

		ArrayList<MSUSISData> msudata =  (ArrayList<MSUSISData>)eventContext.getMessage().getPayload();
		ArrayList<MSUSISData> msudata_dedup =  new ArrayList<MSUSISData>();
		
//		Collections.sort(msudata, new Comparator<MSUSISData>() {
//
//			@Override
//			public int compare(MSUSISData o1, MSUSISData o2) {
//				// TODO Auto-generated method stub
//				int value1 =  o2.getOpportunity().compareTo(o1.getOpportunity());
//				if (value1 == 0)
//				{
//		            return o1.getBisk_Course_ID().compareTo(o2.getBisk_Course_ID());
//
//				}
//				else
//					return value1;
//			}
//		});
		Boolean add = true;
		int cnt = 0;
		MSUSISData d1, d2 = null;
		ObjectMapper obj = new ObjectMapper();
		
		for(Object data : msudata)
		{
			
			d1 = obj.convertValue(data, MSUSISData.class);
			
			for(Object temp : msudata_dedup)
			{
				d2 = obj.convertValue(temp, MSUSISData.class);
				add = true;
				if (!(d2.getBisk_Course_ID().equals(d1.getBisk_Course_ID()) && 
						d2.getOpportunity().equals(d1.getOpportunity())))
				{
					add = true;
				}
				else
				{
					cnt++;
				}
			}
			if (msudata_dedup.size()== 0)
				msudata_dedup.add(d1);
			
			if (add && cnt == 0)
			{
				msudata_dedup.add(d1);
			}
			cnt = 0;
			add = false;
			
		}

		return msudata_dedup;
	}

}
