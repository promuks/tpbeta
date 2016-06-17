package net.atpco.tp.client.service;


import java.util.ArrayList;
import java.util.List;

import net.atpco.common.model.pricing.TotalPriceClientRequest;
import net.atpco.common.model.pricing.TotalPriceClientResponse;
import net.atpco.common.model.pricing.TotalPricePricingResult;
import net.atpco.common.util.DateUtil;
import net.atpco.tp.client.exception.TotalPriceClientException;
import net.atpco.tp.client.util.PricingUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Component;

import com.farecompare.atppricing.calendarpricingmodule.api.CalendarPricingModuleAPI;
import com.farecompare.atppricing.calendarpricingmodule.api.data.PricingRequest;
import com.farecompare.atppricing.commons.data.PricingResult;
import com.farecompare.atppricing.commons.data.PricingResultsContainer;

@Component
@ImportResource("/TPCalculator.xml")
public class TotalPriceClientService {	
	private static final Logger LOGGER = LoggerFactory.getLogger(TotalPriceClientService.class);

	@Autowired
	private CalendarPricingModuleAPI calendarPricingModule;			

	@Autowired
	private TPPricingRequestProcessor requestProcessor;		

	@Autowired
	private TPPricingResultsProcessor resultsProcessor;		

	public TotalPriceClientResponse processAdhocPricingRequest(final TotalPriceClientRequest tpClientRequest) throws TotalPriceClientException {
		LOGGER.info("Begin processing pricing request...{}-{}-{}-{}",tpClientRequest.getOrigin(), tpClientRequest.getDestination(),tpClientRequest.getCarrier(),tpClientRequest.getFareClass());
		try {
			final PricingRequest pricingRequest = getRequestProcessor().buildPricingRequest(tpClientRequest);
			String status = "";	
			String pricedMarket="";
			PricingResultsContainer container;
			container = getCalendarPricingModule().priceUsingMultipleAlgorithms(pricingRequest, null);
			List<TotalPricePricingResult> resultsList = null;
			if (container != null) {
				status = container.getResultStatus().name();
				pricedMarket = container.getOriginCityCode()+container.getDestinationCityCode()+container.getCarrierCode();

				final List<PricingResult> pricingResultsList = container.getPricingResults();
				if (pricingResultsList != null && pricingResultsList.size() > 0) {
					for (final PricingResult pricingResult: pricingResultsList) {
						resultsList = getResultsProcessor().processResults(pricingResult);

					}
				} 
			}		
			LOGGER.info("Finished processing pricing request...{}-{}-{}-{}",tpClientRequest.getOrigin(), tpClientRequest.getDestination(),tpClientRequest.getCarrier(),tpClientRequest.getFareClass());
			return getCalculatorResults(tpClientRequest.getQueryId(), status, pricingRequest, resultsList,pricedMarket);
		} catch(Exception e) {
			final TotalPriceClientException tpClientEx = PricingUtil.createTPClientException(e, tpClientRequest);
			LOGGER.error("Error in pricing service: {}-{}",tpClientEx);
			throw tpClientEx;
		}
	}
	
	public List<TotalPriceClientResponse> processAdhocPricingRequests(final List<TotalPriceClientRequest> tpClientRequests) throws TotalPriceClientException {
		List<TotalPriceClientResponse> results = new ArrayList<TotalPriceClientResponse>();
		for (TotalPriceClientRequest tpClientRequest : tpClientRequests) {
			TotalPriceClientResponse response = processAdhocPricingRequest(tpClientRequest);
			results.add(response);
		}
		return results;
	}

	private TotalPriceClientResponse getCalculatorResults(final String queryId, final String resultStatus,  final PricingRequest pricingRequest, final List<TotalPricePricingResult> resultsList, String pricedMarket ) {
		final TotalPriceClientResponse tpClientResponse = new TotalPriceClientResponse();
		tpClientResponse.setStatus(resultStatus );
		tpClientResponse.setQueryId(queryId);
		tpClientResponse.setMarket(pricedMarket);		
		if(pricingRequest.getIncludeFareClasses() != null  ){
			tpClientResponse.setFareClass(pricingRequest.getIncludeFareClasses().iterator().next());
		}
		tpClientResponse.setRequestDate(DateUtil.getCurrentDateTime());
		tpClientResponse.setAdhocPricingResults(resultsList);
		return tpClientResponse;
	}	

	public CalendarPricingModuleAPI getCalendarPricingModule() {
		return calendarPricingModule;
	}

	public void setCalendarPricingModule(
			final CalendarPricingModuleAPI calendarPricingModule) {
		this.calendarPricingModule = calendarPricingModule;
	}

	public TPPricingRequestProcessor getRequestProcessor() {
		return requestProcessor;
	}

	public void setRequestProcessor(final TPPricingRequestProcessor requestProcessor) {
		this.requestProcessor = requestProcessor;
	}

	public TPPricingResultsProcessor getResultsProcessor() {
		return resultsProcessor;
	}

	public void setResultsProcessor(final TPPricingResultsProcessor resultsProcessor) {
		this.resultsProcessor = resultsProcessor;
	}
}