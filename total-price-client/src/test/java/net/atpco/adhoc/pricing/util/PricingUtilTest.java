/*package net.atpco.adhoc.pricing.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.atpco.common.model.Fare;
import net.atpco.common.model.filter.FilterMergedFare;
import net.atpco.tp.client.util.PricingUtil;

import org.junit.Test;

public class PricingUtilTest {

	private static final String ORIGIN_WAS = "WAS";
	private static final String DEST_LON = "LON";
	private static final String CR_AA = "AA";
		
	private static final String DEST_MAD = "MAD";
	private static final String CR_BA = "BA";
	private static final String FC_J1NAJB = "J1NAJB";
	private static final String FC_W1NAJB = "W1NAJB";
	
	@Test
	public void testUniqueMergedFaresGivenNullFilterMergedFares() {
		final List<FilterMergedFare> filterMergedFareList = null;
				
		final Set<FilterMergedFare> faresSet = PricingUtil.getUniqueMergedFares(filterMergedFareList);
		assertNotNull(faresSet);
		assertEquals(0, faresSet.size());
		
	}
	
	@Test
	public void testUniqueMergedFaresGivenEmptyFilterMergedFares() {
		final List<FilterMergedFare> filterMergedFareList = new ArrayList<>();
				
		final Set<FilterMergedFare> faresSet = PricingUtil.getUniqueMergedFares(filterMergedFareList);
		assertNotNull(faresSet);
		assertEquals(0, faresSet.size());
		
	}
	
	@Test
	public void testUniqueMergedFaresGivenNullODCFareClass() {
		final List<FilterMergedFare> filterMergedFareList = new ArrayList<FilterMergedFare>();
		FilterMergedFare mergedFare = new FilterMergedFare();
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_MAD);
		mergedFare.setCarrier(CR_BA);
		Fare fare = new Fare();
		fare.setFareClass(FC_J1NAJB);
		mergedFare.setFare(fare);		
		
		filterMergedFareList.add(mergedFare);
				
		final Set<FilterMergedFare> faresSet = PricingUtil.getUniqueMergedFares(filterMergedFareList);
		assertNotNull(faresSet);
		assertEquals(0, faresSet.size());
	}
		
	@Test
	public void testUniqueMergedFaresGivenODCWithDifferentFareClass() {
		final List<FilterMergedFare> filterMergedFareList = new ArrayList<FilterMergedFare>();
		FilterMergedFare mergedFare = new FilterMergedFare();
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_MAD);
		mergedFare.setCarrier(CR_BA);
		Fare fare = new Fare();
		fare.setFareClass(FC_J1NAJB);
		mergedFare.setFare(fare);		
		
		filterMergedFareList.add(mergedFare);
		
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_MAD);
		mergedFare.setCarrier(CR_BA);
		fare = new Fare();
		fare.setFareClass(FC_W1NAJB);
		mergedFare.setFare(fare);
		
		filterMergedFareList.add(mergedFare);
				
		final Set<FilterMergedFare> faresSet = PricingUtil.getUniqueMergedFares(filterMergedFareList);
		assertNotNull(faresSet);
		assertEquals(2, faresSet.size());
	}
	
	@Test
	public void testUniqueMergedFaresGivenODCWithSameFareClass() {
		final List<FilterMergedFare> filterMergedFareList = new ArrayList<FilterMergedFare>();
		FilterMergedFare mergedFare = new FilterMergedFare();
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_MAD);
		mergedFare.setCarrier(CR_BA);
		Fare fare = new Fare();
		fare.setFareClass(FC_J1NAJB);
		mergedFare.setFare(fare);		
		
		filterMergedFareList.add(mergedFare);
		
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_MAD);
		mergedFare.setCarrier(CR_BA);
		fare = new Fare();
		fare.setFareClass(FC_J1NAJB);
		mergedFare.setFare(fare);
		
		filterMergedFareList.add(mergedFare);
				
		final Set<FilterMergedFare> faresSet = PricingUtil.getUniqueMergedFares(filterMergedFareList);
		assertNotNull(faresSet);
		assertEquals(1, faresSet.size());
	}
	
	@Test
	public void testUniqueMergedFaresGivenDiffMarketsWithSameFareClass() {
		final List<FilterMergedFare> filterMergedFareList = new ArrayList<FilterMergedFare>();
		FilterMergedFare mergedFare = new FilterMergedFare();
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_LON);
		mergedFare.setCarrier(CR_BA);
		Fare fare = new Fare();
		fare.setFareClass(FC_J1NAJB);
		mergedFare.setFare(fare);		
		
		filterMergedFareList.add(mergedFare);				
		
		mergedFare = new FilterMergedFare();
		mergedFare.setOrigin(ORIGIN_WAS);
		mergedFare.setDestination(DEST_LON);
		mergedFare.setCarrier(CR_AA);
		fare = new Fare();
		fare.setFareClass(FC_J1NAJB);
		mergedFare.setFare(fare);
		
		filterMergedFareList.add(mergedFare);
				
		final Set<FilterMergedFare> faresSet = PricingUtil.getUniqueMergedFares(filterMergedFareList);
		assertNotNull(faresSet);
		assertEquals(2, faresSet.size());
	}
}
*/