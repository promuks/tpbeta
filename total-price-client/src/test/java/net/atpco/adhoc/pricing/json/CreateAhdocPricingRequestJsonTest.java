package net.atpco.adhoc.pricing.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.atpco.common.enums.AlgorithmType;
import net.atpco.common.model.Fare;
import net.atpco.common.model.filter.FilterMergedFare;
import net.atpco.common.model.search.request.SearchRequest;
import net.atpco.common.util.DateUtil;
import net.atpco.common.model.adhoc.AdhocSearchRequest;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CreateAhdocPricingRequestJsonTest {
	
	private static final Logger LOGGER = Logger.getLogger(CreateAhdocPricingRequestJsonTest.class);
	public List<FilterMergedFare> createFilterMergedList() {
		final String origin ="WAS";
		final String destination ="LON";
		final List<FilterMergedFare> fareList = new ArrayList<FilterMergedFare> ();
		fareList.add(getFilterMergedFare(origin,destination,"AA","Y1OWJB"));
		fareList.add(getFilterMergedFare(origin,destination,"AA","QJA0Q2B1"));
		fareList.add(getFilterMergedFare(origin,destination,"AA","W1NAJB"));
		
		fareList.add(getFilterMergedFare(origin,destination,"BA","Y1OWJB"));
		fareList.add(getFilterMergedFare(origin,destination,"BA","QJA0Q2B1"));
		fareList.add(getFilterMergedFare(origin,destination,"BA","W1NAJB"));
		
		fareList.add(getFilterMergedFare(origin,destination,"UA","Y1OWJB"));
		fareList.add(getFilterMergedFare(origin,destination,"UA","QJA0Q2B1"));
		fareList.add(getFilterMergedFare(origin,destination,"UA","W1NAJB"));					
		
		return fareList;
	}

	
	private FilterMergedFare getFilterMergedFare (String origin, String destination, String carrier, String fareClass) {
		final FilterMergedFare filterMergedFare = new FilterMergedFare();
		filterMergedFare.setOrigin(origin);
		filterMergedFare.setDestination(destination);
		filterMergedFare.setCarrier(carrier);
		
		final Fare fare = new Fare();
		fare.setFareClass(fareClass);
		filterMergedFare.setFare(fare);
		return filterMergedFare;
	}
	
	public SearchRequest createSearchRequest() {
		final SearchRequest request = new SearchRequest();
		request.setQueryId("Test1234567890");
	/*	request.setShopperDuration(180);
		request.setTravelDuration(7);
		final List<AlgorithmType> algorithmType = new ArrayList<AlgorithmType>();
		final AlgorithmType algorithm = request.isOneWay() ? AlgorithmType.LowestOnewayPerDayPerCabin : AlgorithmType.LowestRoundtripUsingHalfFaresForCabinPerDay;
		algorithmType.add(algorithm);*/
		
		request.setTravelDateAfter(DateUtil.getDate(new java.util.Date()));
		return request;		
	}


	public static void main(final String[] args) {		
		final CreateAhdocPricingRequestJsonTest t = new CreateAhdocPricingRequestJsonTest();
		final List<FilterMergedFare> filteredMergedFares= t.createFilterMergedList();
		final SearchRequest searchRequest = t.createSearchRequest();
		final AdhocSearchRequest adhoc = new AdhocSearchRequest();
		adhoc.setFilteredMergedFares(filteredMergedFares);
		adhoc.setSearchRequest(searchRequest);
        
        final ObjectMapper mapper = new ObjectMapper();
        File f = null;     
        FileWriter w = null;
        BufferedWriter b = null;
        try {        	
        	mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    		mapper.setSerializationInclusion(Include.NON_NULL);
    		mapper.setSerializationInclusion(Include.NON_EMPTY);
    		
    		f = new File("src/test/java/net/atpco/adhoc/pricing/json/PricingRequestJson.json");
    		if (f.exists()) {
    			f.delete();
    			
    		} 
    		f.createNewFile();
    		w = new FileWriter(f.getAbsoluteFile());
    		b = new BufferedWriter(w);
    		final String s = mapper.writeValueAsString(adhoc);
    		b.write(s);
    		//System.out.println(s);    		
        } catch(Exception e) {
        	LOGGER.error(e);
        } finally{
        	if (b != null) {
        		try {
					b.close();
				} catch (IOException e) {					
					LOGGER.error(e);
				}
        	}
        	if (w != null) {
        		try {
					w.close();
				} catch (IOException e) {					
					LOGGER.error(e);
				}
        	}
        	
        }

	}

}
