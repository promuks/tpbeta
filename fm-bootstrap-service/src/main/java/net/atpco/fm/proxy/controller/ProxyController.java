package net.atpco.fm.proxy.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import net.atpco.common.model.search.SearchResponse;
import net.atpco.common.model.search.SearchResponseStatus;
import net.atpco.common.model.search.request.AmountsByMktCabinRequest;
import net.atpco.common.model.search.request.CalculatorUISearchRequest;
import net.atpco.common.model.search.request.OneDayAmountsByMktCabinRequest;
import net.atpco.common.model.search.request.PricingDetailsByMktFareClassRequest;

import org.jsondoc.core.annotation.ApiPathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class ProxyController {
	
	@Resource(name="propertiesSet")
	private Map<String, String> propertiesSet;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value = "/api/fares/status/{queryId}", method = RequestMethod.GET)
    public @ResponseBody SearchResponseStatus getFaresResultStatus(
    		@PathVariable(value="queryId") String queryId, HttpServletResponse httpServletResponse) throws URISyntaxException {
		String redirectUrl = propertiesSet.get("/api/fares/status")+"/"+queryId;
		URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<SearchResponseStatus> response= restTemplate.getForEntity(targetUrl, SearchResponseStatus.class);
		return response.getBody();
    }
	
	@RequestMapping(value = "/ws/lookup/search/{type}", method = RequestMethod.GET)
    public @ResponseBody String directoryServiceWithType(
    		final @ApiPathParam @PathVariable("type") String searchType,
    		HttpServletResponse httpServletResponse) throws URISyntaxException {
		String redirectUrl = propertiesSet.get("/ws/lookup/search");
		redirectUrl = redirectUrl+"/"+searchType;
		URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<String> response= restTemplate.getForEntity(targetUrl, String.class);
		return response.getBody();
    }
	
	@RequestMapping(value = "/ws/lookup/search/{type}/{value}", method = RequestMethod.GET)
    public @ResponseBody String directoryServiceWithValue(
    		final @ApiPathParam @PathVariable("type") String searchType,
			final @ApiPathParam @PathVariable("value") String searchValue, HttpServletResponse httpServletResponse) throws URISyntaxException {
		String redirectUrl = propertiesSet.get("/ws/lookup/search");
		redirectUrl = redirectUrl+"/"+searchType+"/"+searchValue;
		URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<String> response= restTemplate.getForEntity(targetUrl, String.class);
		return response.getBody();
    }
	
	@RequestMapping(value = "/api/calc/process", method = RequestMethod.POST)
    public @ResponseBody SearchResponse calcProcess(
    		@Valid @RequestBody final CalculatorUISearchRequest request, HttpServletResponse httpServletResponse) throws URISyntaxException {
        String redirectUrl = propertiesSet.get("/api/calc/process");
        URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<SearchResponse> response= restTemplate.postForEntity(targetUrl, request, SearchResponse.class);
		return response.getBody();
    }
	
	@RequestMapping(value = "/api/fares/fareClassAndCabinByMkt", method = RequestMethod.POST)
    public @ResponseBody String fareClassAndCabinByMkt(
    		@RequestBody final AmountsByMktCabinRequest request, HttpServletResponse httpServletResponse) throws URISyntaxException {
        String redirectUrl = propertiesSet.get("/api/fares/fareClassAndCabinByMkt");
        URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<String> response= restTemplate.postForEntity(targetUrl, request, String.class);
        return response.getBody();
    }
	
	@RequestMapping(value = "/api/fares/amountsByMktAndCabin", method = RequestMethod.POST)
    public @ResponseBody String amountsByMktAndCabin(
    		@RequestBody final AmountsByMktCabinRequest request, 
    		HttpServletResponse httpServletResponse) throws URISyntaxException {
        String redirectUrl = propertiesSet.get("/api/fares/amountsByMktAndCabin");
        URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<String> response= restTemplate.postForEntity(targetUrl, request, String.class);
        return response.getBody();
    }
	
	@RequestMapping(value = "/api/fares/oneDayAmountsByMktCabinRequest", method = RequestMethod.POST)
    public @ResponseBody String oneDayAmountsByMktCabinRequest(
    		@RequestBody final OneDayAmountsByMktCabinRequest request, HttpServletResponse httpServletResponse) throws URISyntaxException {
        String redirectUrl = propertiesSet.get("/api/fares/oneDayAmountsByMktCabinRequest");
        URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<String> response= restTemplate.postForEntity(targetUrl, request, String.class);
        return response.getBody();
    }

	@RequestMapping(value = "/api/fares/pricingDetailsByMktAndFareClass", method = RequestMethod.POST)
    public @ResponseBody String pricingDetailsByMktAndFareClass(
    		@RequestBody final PricingDetailsByMktFareClassRequest request, HttpServletResponse httpServletResponse) throws URISyntaxException {
        String redirectUrl = propertiesSet.get("/api/fares/pricingDetailsByMktAndFareClass");
        URI targetUrl= UriComponentsBuilder.fromUriString(redirectUrl).build().toUri();
        ResponseEntity<String> response= restTemplate.postForEntity(targetUrl, request, String.class);
        return response.getBody();
    }

}
