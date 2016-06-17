package net.atpco.tp.client.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.atpco.common.model.filter.FilterMergedFare;
import net.atpco.common.model.pricing.TotalPriceClientRequest;
import net.atpco.tp.client.exception.TotalPriceClientException;

public class PricingUtil {

	public static Set<FilterMergedFare> getUniqueMergedFares(final List<FilterMergedFare> filterMergedFareList) {
		final Set<FilterMergedFare> faresSet = new HashSet<FilterMergedFare>();
		if (filterMergedFareList != null && filterMergedFareList.size() > 0) {
			final Map<String, FilterMergedFare> filterMap = new HashMap<String, FilterMergedFare>();
			for (final FilterMergedFare mergedFare : filterMergedFareList) {
				final String market = mergedFare.getOrigin()+mergedFare.getDestination()+mergedFare.getCarrier()+mergedFare.getFare().getFareClass();
				if (market != null && !filterMap.containsKey(market)) {
					filterMap.put(market, mergedFare);
				}
			}
			faresSet.addAll(filterMap.values());
		}
		return faresSet;
	}
	
	public static TotalPriceClientException createTPClientException(
			final Throwable throwable, final TotalPriceClientRequest request){

		final TotalPriceClientException tpClientEx = new TotalPriceClientException(
				request.getQueryId(), request.getOrigin(), request.getDestination(), request.getCarrier(), request.getFareClass(), throwable);
		
		return tpClientEx;		
	} 
}