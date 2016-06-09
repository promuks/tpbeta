package net.atpco.directory.lookup.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.atpco.directory.lookup.model.Lookup;

import org.apache.commons.collections4.map.ListOrderedMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LookupUtil {
	
	private static final String LOOKUP_CODE="code"; 
	private static final String LOOKUP_DESC="desc";
	
	/*
	 * Get the response from the Directory service FW call and filter
	 * 1.Match value exactly to the code and retain that as the 1st value in the list
	 * 2.Match to description that starts with the value 
	 */
	public static String filterValue(String response, String value) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String filteredResponse = null;
		
		ListOrderedMap<String,String> filteredMap = new ListOrderedMap<String,String>();
		HashMap<String,String>[] values = 
				mapper.readValue(response, new TypeReference<HashMap<String,String>[]>() {});

		for (HashMap<String,String> h : values) {
			String code = h.get(LOOKUP_CODE);
			String desc = h.get(LOOKUP_DESC);
			if (value.equals(code)) {
				filteredMap.put(0,code, desc);
			} else if (desc.startsWith(value)) {
				filteredMap.put(code, desc);
			}
		}

		if (!filteredMap.isEmpty()) {
			filteredResponse = mapper.writeValueAsString(filteredMap);
		}
		
		return filteredResponse;
	}
	
	public static String filter(String response, String value) throws JsonParseException, JsonMappingException, IOException {
		String filteredResponse = null;
		List<Lookup> filteredList =  new ArrayList<Lookup>();
		List<Lookup> tempList =  new ArrayList<Lookup>();
		
		ObjectMapper mapper = new ObjectMapper();
		/*
		 * Get the response from the Directory service FW call and filter
		 * 1.Match value exactly to the code and retain that as the 1st value in the list
		 * 2.Match to description that starts with the value 
		 */
		List<Lookup> lookupList = mapper.readValue(
				response,
				mapper.getTypeFactory().constructCollectionType(List.class, Lookup.class));
		
		//filter the list
		if (lookupList != null) {
			for (Lookup l : lookupList) {
				if (l.getCode().equals(value)) {
					filteredList.add(l);
				} else if (l.getDesc().startsWith(value)) {
					tempList.add(l);
				}
			}
			filteredList.addAll(tempList);
		}
		if (!filteredList.isEmpty()) {
			filteredResponse = mapper.writeValueAsString(filteredList);
		}

		return filteredResponse;
	}
	
	

}
