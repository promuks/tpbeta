package net.atpco.tp.client.service;

import java.util.ArrayList;
import java.util.List;

import net.atpco.common.model.pricing.TotalPricePricingResult;

import com.farecompare.atppricing.calendarpricingmodule.api.data.LowestPerDayPricingContainer;
import com.farecompare.atppricing.commons.components.PricingAlgorithmType;
import com.farecompare.atppricing.commons.data.Priced;
import com.farecompare.atppricing.commons.data.PricedOneway;
import com.farecompare.atppricing.commons.data.PricedRoundtrip;
import com.farecompare.atppricing.commons.data.PricingResult;

public class TPPricingResultsProcessor {

	private static final int NO_OF_DECIMALS = 2;
	
	public List<TotalPricePricingResult> processResults(final PricingResult pricingResult) {		
		final List<TotalPricePricingResult> adhocPricingResultsList = new ArrayList<TotalPricePricingResult>();
		final LowestPerDayPricingContainer lowestPerDayPC = (LowestPerDayPricingContainer) pricingResult;
		final Priced[] resultsForCabinPerDay = lowestPerDayPC.getPricedResults();
		int index = 0;
		for (int i = 0; i < resultsForCabinPerDay.length; i++ ) {
			index = i + 1; 
			final Priced priced = resultsForCabinPerDay[i];
			
			// TP-1719 (fix for null cabinTypes)
			if(priced!=null && priced.getCabinType() == null){
				return adhocPricingResultsList;
			}
			
			if(priced instanceof PricedOneway){
				final PricedOneway pricedOneway = (PricedOneway) priced;
				
				final TotalPricePricingResult pricedFare = setResults(pricedOneway, index, pricingResult.getRequest().getPricingAlgorithmType());
				adhocPricingResultsList.add(pricedFare);
			}
			if(priced instanceof PricedRoundtrip){
				final PricedRoundtrip pricedRT = (PricedRoundtrip) priced;

				final TotalPricePricingResult pricedFare = setResults(pricedRT, index, pricingResult.getRequest().getPricingAlgorithmType());
				adhocPricingResultsList.add(pricedFare);
			}
		}
		return adhocPricingResultsList;
	}
	
	
	
	/**
	 * Converting mergedfares to a DBObject
	 * @param mergedFares
	 * @param taskId 
	 * @param pricingAlgorithm 
	 * @param count 
	 * @return DBObject
	 */
	private TotalPricePricingResult setResults(final PricedOneway priced, final int index, final PricingAlgorithmType pricingAlgorithm) {
		
		final TotalPricePricingResult pricedFare = new TotalPricePricingResult();
		
		pricedFare.setDay(index);
		pricedFare.setOrigin(priced.getOriginCityCode());
		pricedFare.setDestination(priced.getDestinationCityCode());
		pricedFare.setCarrierCode(priced.getCarrierCode());
		pricedFare.setCurrencyCd(priced.getCurrencyCode());
		pricedFare.setCabinType(priced.getCabinType()!=null?priced.getCabinType().toString():"");
		
		pricedFare.setBaseAmt(priced.getBasePriceAsLong());
		pricedFare.setYqyrAmt(priced.getYqyrAsLong());
		pricedFare.setSurChgAmt(priced.getCat12AsLong());
		pricedFare.setTaxAmt(priced.getTaxesAsLong());
		pricedFare.setBaseWithSurChg(priced.getBasePriceWithSurchargesAsLong());
		pricedFare.setTotalAmt(priced.getTotalAsLong());
		pricedFare.setDecimals(NO_OF_DECIMALS);
			
		pricedFare.setOutboundFareClass(priced.getFareClass());				
		pricedFare.setBookingCds(priced.getBookingCodes());	
		
		pricedFare.setLastSaleDt(priced.getLastSaleDate());
		pricedFare.setLastTravelDt(priced.getLastTravelDate());
		pricedFare.setRtFare(priced.isRoundtripFare());
		pricedFare.setOutRoute(priced.getRoute()); 
		pricedFare.setOutDate(priced.getDepartureDate());
		pricedFare.setNoOfStops(priced.getNumberOfStops());
		pricedFare.setConnectionType(priced.getConnectionType().toString());
		pricedFare.setConFares(priced.isConstructedFare());
		pricedFare.setPvtFares(priced.isPrivateFare());
		pricedFare.setFbrInd(priced.isFareByRuleFare());
		
		pricedFare.setCreationDate(priced.getCreationDate());
		pricedFare.setCreationTime(priced.getCreationTime());
		pricedFare.setDiscontinueDate(priced.getDiscontinueDate());
		pricedFare.setPricingAlgorithm(pricingAlgorithm.toString());
		pricedFare.setLinkNo(priced.getLinkNumber());
		pricedFare.setSeqNo(priced.getSequenceNumber());
		
		return pricedFare;
	}
	

	/**
	 * Converting mergedfares to a DBObject
	 * @param mergedFares
	 * @param taskId 
	 * @return DBObject
	 */
	private TotalPricePricingResult setResults(final PricedRoundtrip priced, final int index, final PricingAlgorithmType pricingAlgorithm) {

		final TotalPricePricingResult pricedFare = new TotalPricePricingResult();
		
		pricedFare.setDay(index);
		pricedFare.setOrigin(priced.getOriginCityCode());
		pricedFare.setDestination(priced.getDestinationCityCode());
		pricedFare.setCarrierCode(priced.getCarrierCode());
		pricedFare.setCurrencyCd(priced.getCurrencyCode());
		pricedFare.setCabinType(priced.getCabinType()!=null?priced.getCabinType().toString():"");
		
		pricedFare.setBaseAmt(priced.getBasePriceAsLong());
		pricedFare.setYqyrAmt(priced.getYqyrAsLong());
		pricedFare.setSurChgAmt(priced.getCat12AsLong());
		pricedFare.setTaxAmt(priced.getTaxesAsLong());
		pricedFare.setBaseWithSurChg(priced.getBasePriceWithSurchargesAsLong());
		pricedFare.setTotalAmt(priced.getTotalAsLong());
		pricedFare.setDecimals(NO_OF_DECIMALS);
		pricedFare.setOutDate(priced.getDepartureDate()); 
		pricedFare.setInDate(priced.getReturnDate());
		
		if(priced.getOutboundFare() != null){
			pricedFare.setOutboundFareClass(priced.getOutboundFare().getFareClass());				
			pricedFare.setBookingCds(priced.getOutboundFare().getBookingCodes());
			
			pricedFare.setLastSaleDt(priced.getOutboundFare().getLastSaleDate());
			pricedFare.setLastTravelDt(priced.getOutboundFare().getLastTravelDate());
			pricedFare.setRtFare(priced.getOutboundFare().isRoundtripFare());
			
			pricedFare.setOutRoute(priced.getOutboundFare().getRoute()); // outbound route
			if(priced.getInboundFare() != null){
				pricedFare.setInRoute(priced.getInboundFare().getRoute()); // inbound route
				pricedFare.setInboundFareClass(priced.getInboundFare().getFareClass());
			}
			pricedFare.setNoOfStops(priced.getOutboundFare().getNumberOfStops());
			pricedFare.setConnectionType(priced.getOutboundFare().getConnectionType().toString());
			pricedFare.setConFares(priced.getOutboundFare().isConstructedFare());
			pricedFare.setPvtFares(priced.getOutboundFare().isPrivateFare());
			pricedFare.setFbrInd(priced.getOutboundFare().isFareByRuleFare());			
			pricedFare.setCreationDate(priced.getOutboundFare().getCreationDate());
			pricedFare.setCreationTime(priced.getOutboundFare().getCreationTime());
			pricedFare.setDiscontinueDate(priced.getOutboundFare().getDiscontinueDate());
			pricedFare.setPricingAlgorithm(pricingAlgorithm.toString());
			pricedFare.setLinkNo(priced.getOutboundFare().getLinkNumber());
			pricedFare.setSeqNo(priced.getOutboundFare().getSequenceNumber());			
	
		}
		
		return pricedFare;
	}
}
