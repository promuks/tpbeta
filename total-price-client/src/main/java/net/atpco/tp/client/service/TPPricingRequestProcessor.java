package net.atpco.tp.client.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.atpco.common.enums.AlgorithmType;
import net.atpco.common.model.pricing.TotalPriceClientRequest;
import net.atpco.common.util.StringUtil;

import com.farecompare.atpcore.engines.pricing.data.itinerary.PricingCalendarImpl;
import com.farecompare.atppricing.calendarpricingmodule.api.data.PricingRequest;
import com.farecompare.atppricing.commons.components.PricingAlgorithmRequest;
import com.farecompare.atppricing.commons.components.PricingAlgorithmType;
import com.farecompare.atppricing.commons.routes.RouteSelectionPreferences;
import com.xxi.framework.travel.data.airfare.AtpQueryContext;

public class TPPricingRequestProcessor {

	public PricingRequest buildPricingRequest(final TotalPriceClientRequest tpClientRequest) {						
		return getPricingRequest(tpClientRequest);		
	}	

	private List<AlgorithmType> getPricingAlgorithmList (final boolean oneWay) {
		final List<AlgorithmType> algorithmList =  new ArrayList<AlgorithmType>();
		algorithmList.add(oneWay ? AlgorithmType.LowestOnewayForCabinPerDay 
				:AlgorithmType.LowestRoundtripUsingHalfFaresForCabinPerDay);

		return algorithmList;
	}

	public PricingRequest getPricingRequest(final TotalPriceClientRequest tpClientRequest) {
		final boolean oneWay = (tpClientRequest.getOwrtIndicator() != null && tpClientRequest.getOwrtIndicator().equalsIgnoreCase("O")) ? true : false;
		
		final List<AlgorithmType> algorithmList = getPricingAlgorithmList(oneWay);
		final int numberOfDays = tpClientRequest.getShopperDuration();
		final int lengthOfStay = tpClientRequest.getTravelDuration();
		final int travelDate = tpClientRequest.getTravelDate();
		final PricingCalendarImpl commonDatesField = new PricingCalendarImpl(travelDate,
				numberOfDays + lengthOfStay, lengthOfStay);
		final RouteSelectionPreferences routeSelectionPreferences = new RouteSelectionPreferences();	
		routeSelectionPreferences.setNonStopsOnly(false);
		
		final List<PricingAlgorithmRequest> requestList = switchToFCAlgorithmList(algorithmList, lengthOfStay);

		final PricingRequest request = new PricingRequest();
		request.setAtpQueryContext(new AtpQueryContext());
		request.setCarrierCode(tpClientRequest.getCarrier());
		request.setOriginCityCode(tpClientRequest.getOrigin());
		request.setDestinationCityCode(tpClientRequest.getDestination());
		request.setCommonDatesField(commonDatesField);			
		request.setFirstOutboundDate(commonDatesField.getFirstDate());
		request.setNumberOfOutboundDays(numberOfDays);
		request.setRouteSelectionPreferencesInbound(routeSelectionPreferences);
		request.setRouteSelectionPreferencesOutbound(routeSelectionPreferences);
		request.setPricingAlgorithmRequests(requestList);	
		if (StringUtil.isNotEmpty(tpClientRequest.getFareClass())) {
			request.setIncludeFareClasses(getFareClassSet(tpClientRequest.getFareClass()));
		}

		return request;
	}

	private Set<String> getFareClassSet(final String fareClass) {
		final Set<String> fareClassSet = new HashSet<String>();
		fareClassSet.add(fareClass);
		return fareClassSet;
	}	

	private static List<PricingAlgorithmRequest> switchToFCAlgorithmList (final List<AlgorithmType> algorithmTypeList, final int lengthOfStay) {
		final List<PricingAlgorithmRequest> requestList = new ArrayList<PricingAlgorithmRequest>(algorithmTypeList.size());
		for (final AlgorithmType atpcoAlgo : algorithmTypeList) {

			requestList.add(addRequest(lengthOfStay, atpcoAlgo.name()));		    
		}
		return requestList;
	}

	private static PricingAlgorithmRequest addRequest(final int lenghtOfStay, final String name) {
		final PricingAlgorithmRequest request = new PricingAlgorithmRequest();		   
		final PricingAlgorithmType parType = PricingAlgorithmType.valueOf(name);		  
		request.setPricingAlgorithmType(parType);
		request.setLengthOfStay(lenghtOfStay);
		return request;
	}
}
