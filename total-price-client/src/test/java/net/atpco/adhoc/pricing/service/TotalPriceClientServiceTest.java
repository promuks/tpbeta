package net.atpco.adhoc.pricing.service;


import static org.junit.Assert.assertTrue;
import net.atpco.common.enums.ConnectionType;
import net.atpco.common.model.pricing.TotalPriceClientRequest;
import net.atpco.common.model.pricing.TotalPriceClientResponse;
import net.atpco.common.util.DateUtil;
import net.atpco.tp.client.service.TotalPriceClientService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/TestTPCalculator.xml"})
public class TotalPriceClientServiceTest {
	private static final String ORIGIN_WAS = "WAS";
	private static final String DEST_LON = "LON";
	private static final String CR_AA = "AA";
		
	private static final String DEST_MAD = "MAD";
	private static final String CR_BA = "BA";
	private static final String FC_J1NAJB = "J1NAJB";
	private static final String FC_W1NAJB = "W1NAJB";
	private static final String FC_Y1OWJB = "Y1OWJB";
	
	@Autowired
	private TotalPriceClientService adhocPricingService;	
	
	
	public TotalPriceClientService getAdhocPricingService() {
		return adhocPricingService;
	}

	public void setAdhocPricingService(final TotalPriceClientService adhocPricingService) {
		this.adhocPricingService = adhocPricingService;
	}
		
		
	@Test
	public void processAdhocPricingRequestForRoundTripTest() throws Exception {
		final TotalPriceClientResponse response = adhocPricingService.processAdhocPricingRequest(buildAdhocSearchRequestForTest(false, "Test1111",ORIGIN_WAS, DEST_LON, CR_AA, FC_Y1OWJB));
		assertTrue(response.getAdhocPricingResults().size()>0);
	}
	
	/*@Test
	public void processAdhocPricingRequestForMaxStopsConnectionTypeTest() throws Exception {
		final TotalPriceClientResponse response = adhocPricingService.processAdhocPricingRequest(buildAdhocSearchRequestForTest(true, "Test1112", ConnectionType.MAX_ONE_STOP,ORIGIN_WAS, DEST_LON, CR_AA, FC_Y1OWJB));
		assertTrue(response.getPricingClinetResult().size()>0);
	}
	
	@Test
	public void processAdhocPricingRequestForNonStopConnectionTypeTest() throws Exception {
		final TotalPriceClientResponse response = adhocPricingService.processAdhocPricingRequest(buildAdhocSearchRequestForTest(true, "Test1113", ConnectionType.NON_STOP, ORIGIN_WAS, DEST_LON, CR_AA, FC_Y1OWJB));
		assertTrue(response.getPricingClinetResult().size()>0);
	}*/
	
	@Test
	public void processAdhocPricingRequestForOneWayTest() throws Exception {
		final TotalPriceClientResponse response = adhocPricingService.processAdhocPricingRequest(buildAdhocSearchRequestForTest(true, "Test11114", ORIGIN_WAS, DEST_LON, CR_AA, FC_Y1OWJB));
		assertTrue(response.getAdhocPricingResults().size()>0);
	}	
	
	private TotalPriceClientRequest buildAdhocSearchRequestForTest(boolean oneWay, String queryId, String origin, String destination, String carrier, String fareClass) {
		return buildAdhocSearchRequestForTest(oneWay, queryId, null, origin, destination, carrier, fareClass);
	}
	
	private TotalPriceClientRequest buildAdhocSearchRequestForTest(boolean oneWay, String queryId, ConnectionType connectionType,String origin, String destination, String carrier, String fareClass) {
		final TotalPriceClientRequest adhocSearchRequest = new TotalPriceClientRequest();
		adhocSearchRequest.setQueryId(queryId);
		adhocSearchRequest.setTravelDate(DateUtil.getDate(new java.util.Date()));
		adhocSearchRequest.setTravelDuration(7);
		adhocSearchRequest.setShopperDuration(180);
		adhocSearchRequest.setOneWay(oneWay);		
		adhocSearchRequest.setOwrtIndicator(oneWay ? "O" : "R");
		adhocSearchRequest.setOrigin(origin);
		adhocSearchRequest.setDestination(destination);
		adhocSearchRequest.setCarrier(carrier);
		adhocSearchRequest.setFareClass(fareClass);
		return adhocSearchRequest;
	}		
}
