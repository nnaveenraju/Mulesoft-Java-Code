package com.bisk.export_csv;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import com.bisk.util.*;

import com.bisk.importcsv.model.Document;

public class TransformToPOJO extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		
		int counter = 0;
		Document docs_payload = (Document) message.getPayload();
		Util util = new Util();

		List<Map<String, Object>> lst_of_docs = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> pojoData = new HashMap<String, Object>();
		Boolean transcript = false;
		
		//String[] docs = null;
		
		//int externalCount = 0;
		 if (docs_payload != null)
		 {
			 if (docs_payload.document_ids.length > 0 )
			 {
				 //for (Document_status doc_status :  docs_payload.document_status)
				 for (int i=0; i< docs_payload.document_ids.length; i++)
				 {
					 String document = new StringBuilder(docs_payload.document_ids[i].toString()).toString();
					 //docs = document.split("|");
					 
					 String[] docs = util.splitDataByChar(docs_payload.document_ids[i].toString().replace("|", "~"),"~");
					 
					 //Document_status doc_status =  docs_payload.document_status[externalCount];
					 
					 pojoData = new HashMap<String, Object>();
					 
					if (docs_payload.document_ids[i].toString()  != "") {
						pojoData.put("Opportunity__c", docs_payload.opportunity_id);
						pojoData.put("Type__c", "Academic");
						
						 pojoData.put("BISKDocumentID__c","BISK-" + docs_payload.slateid + "-" + docs[0]);
								
						//String[] Document_Name_with_status = splitDataBy(docs_payload.document_ids[i].toString(),"|");
						//List<String> itemList = Arrays.asList(Document_Name_with_status);
						
						if (document.toUpperCase().contains("ESSAY") || document.toUpperCase().contains("RESUME") )
						{
							pojoData.put("Document_Name__c",docs[1].toString());
							pojoData.put("Document_Tracker_Document_Title__c",docs[1].toString());
							pojoData.put("Document_Tracker_Section__c","Background Documents");
						}
						
						else if (document.toUpperCase().contains("LICENSE")) 
						{
							pojoData.put("Document_Name__c","Identification");
							pojoData.put("Document_Tracker_Document_Title__c","RN License");
							pojoData.put("Document_Tracker_Section__c","Background Documents");
						}
						else if (document.toUpperCase().contains("IDENTIFICATION") )
						{
							pojoData.put("Document_Name__c","Identification");
							pojoData.put("Document_Tracker_Document_Title__c",docs[1].toString());
							pojoData.put("Document_Tracker_Section__c","Background Documents");
						}
						else if (document.toUpperCase().contains("VISA") )
						{
							pojoData.put("Document_Name__c","Proof of Residency");
							pojoData.put("Document_Tracker_Document_Title__c",docs[1].toString());
							pojoData.put("Document_Tracker_Section__c","Visa");
						}
						else if (document.toUpperCase().contains("REFERENCE") )
						{
							pojoData.put("Document_Name__c","Letter of Recommendation");
							int index = docs[1].toString().indexOf(":");
							pojoData.put("Document_Tracker_Document_Title__c",docs[1].toString().substring(index + 1).trim());
							pojoData.put("Document_Tracker_Section__c","Recommendations");
						}
						else if(document.toUpperCase().contains("TRANSCRIPT"))
						{
							transcript = true;
							pojoData.put("Document_Name__c","Official Transcript");
							int index = docs[1].toString().indexOf(":");
							pojoData.put("Document_Tracker_Document_Title__c",docs[1].toString().substring(index + 1).trim());
							pojoData.put("Document_Tracker_Section__c","Transcripts");
							pojoData.put("Transcript_School_Name__c",docs[1].toString().substring(index + 1).trim());
							
							
							getDatesandArrayValues(docs_payload,counter,pojoData);
							getSchool_demographicData(counter,docs_payload,pojoData,docs[1].toString().substring(index + 1).trim());
							
							counter++;
						}
						
						if (!transcript)
						{
							transcript=false;
							ArrayList<String> nullFields = new ArrayList<String>();
							
							nullFields.add("Transcript_Attendance_Start_Date__c");
							nullFields.add("Transcript_Attendance_End_Date__c");
							nullFields.add("Transcript_Graduate_Date__c");
							nullFields.add("Transcript_GPA__c");
							nullFields.add("Transcript_School_CEEB__c");
							nullFields.add("Transcript_School_City__c");
							nullFields.add("Transcript_School_Country_Code__c");
							nullFields.add("Transcript_School_State_Code__c");
							nullFields.add("Institution__c");
							nullFields.add("Transcript_School_Name__c");
							
							pojoData.put("fieldsToNull",nullFields);
							
							
						}
						
						if (docs != null )
						{
							if (docs[2].toString().toUpperCase().contains("MISSING"))
							{
								pojoData.put("Status__c", "Not Submitted");
								pojoData.put("Document_Tracker_Section_Status__c", "Not Started");
							}
							else if (docs[2].trim().toString().toUpperCase().contains("RECEIVED") || docs[2].trim().toString().toUpperCase().contains("WAIVED"))	
							{
								pojoData.put("Status__c", "Accepted");
								pojoData.put("Document_Tracker_Section_Status__c","Approved");
								
								if (docs[2].trim().toString().toUpperCase().equals("RECEIVED COPY"))
								{
									pojoData.remove("Document_Name__c");
									pojoData.put("Document_Name__c","Unofficial Transcript");
								}
							}
						}	
					}
					//externalCount++;
					lst_of_docs.add(pojoData);
				 }
			 }
			 else
			 {
				 
			 }
		 }
		
		return lst_of_docs;
	}
	
	private void getSchool_demographicData(int counter,Document docs_payload, Map<String, Object> pojoData, String schoolName) {
		
		int l_counter = counter;
//		for (int i=0; i< docs_payload.college_institution.length; i++)
//		{
//			if (docs_payload.college_institution[i].toUpperCase().toString().trim().contains(schoolName.toUpperCase().toString().trim()))
//			{
//				l_counter = i;
//			}
//		}
		
		if (docs_payload.college_city.length > l_counter)
		{
			pojoData.put("Transcript_School_City__c",docs_payload.college_city[l_counter].toString());
		}
		
		if (docs_payload.college_country.length> l_counter)
		{
			pojoData.put("Transcript_School_Country_Code__c",docs_payload.college_country[l_counter].toString());
		}
		
		if (docs_payload.college_state.length> l_counter)
		{
			pojoData.put("Transcript_School_State_Code__c",docs_payload.college_state[l_counter].toString());
		}
		
	}


	private void getDatesandArrayValues(Document docs_payload, int counter, Map<String, Object> pojoData_l) {
		
		if (docs_payload.college_from_month.length>counter && docs_payload.college_from_month.length !=0)
		{
			pojoData_l.put("Transcript_Attendance_Start_Date__c", getDateFromString(docs_payload.college_from_month[counter],docs_payload.college_from_year[counter]));
			pojoData_l.put("Transcript_Attendance_End_Date__c", getDateFromString(docs_payload.college_to_month[counter],docs_payload.college_to_year[counter]));
		}
		if (docs_payload.college_deg_conferred.length>counter && docs_payload.college_deg_conferred.length != 0)
			pojoData_l.put("Transcript_Graduate_Date__c", getDate(docs_payload.college_deg_conferred[counter], "MM/dd/yyyy"));
		
		if (docs_payload.college_gpa.length>counter && docs_payload.college_gpa.length != 0)
			pojoData_l.put("Transcript_GPA__c", docs_payload.college_gpa[counter]);
		
		if (docs_payload.college_ceed_codes.length>counter && docs_payload.college_ceed_codes.length != 0)
			pojoData_l.put("Transcript_School_CEEB__c", docs_payload.college_ceed_codes[counter]);
		
	}

	private Date getDateFromString(String month, String year) {
		
		DateFormat isoFormat = new SimpleDateFormat("yyyy/MM/dd");;
		Date date = null;
		String strDate = year + "/" + month + "/" + "01";
		if (month.trim() != "" && year.trim()!="")
		{
			try {
				date = isoFormat.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return date;
	}

	private Date getDate(String strDate,String format) {
		
		DateFormat isoFormat = new SimpleDateFormat(format);
		Date date = null;
		
		if (!strDate.trim().isEmpty())
		{
			try {
				date = isoFormat.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
		
		return date;
	}


	

}

