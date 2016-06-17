package net.atpco.tp.client.controller;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import net.atpco.common.model.ValidationError;
import net.atpco.common.model.pricing.TotalPriceClientRequest;
import net.atpco.common.model.pricing.TotalPriceClientResponse;
import net.atpco.tp.client.exception.TotalPriceClientException;
import net.atpco.tp.client.service.TotalPriceClientService;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Api(name = "TotalPriceClient", description = "price total price requests")
@RestController
@RequestMapping("/api/tp/client/")

public class TotalPriceClientController {
	
	@Autowired
    private TotalPriceClientService totalPriceClientService;


	@ApiMethod(
	        path = "/api/tp/client/pricing",
	        description = "Process pricing request based on the search details passed as input and produces response object with priced results. "
	        		+ "If the request contains fare class then it will be priced for that fare class otherwise it will price for lowest price fare class for the given market.",
	        produces = { MediaType.APPLICATION_JSON_VALUE },
	        consumes = { MediaType.APPLICATION_JSON_VALUE }
	        )
	@RequestMapping(value = "pricing", method = RequestMethod.POST, consumes = "application/json")
	public @ApiResponseObject @ResponseBody TotalPriceClientResponse processPricingRequest(@ApiBodyObject @Valid @RequestBody final TotalPriceClientRequest tpClientRequest) throws TotalPriceClientException {		
		return totalPriceClientService.processAdhocPricingRequest(tpClientRequest);		
	}
	
	@RequestMapping(value = "allpricing", method = RequestMethod.POST, consumes = "application/json")
	public @ApiResponseObject @ResponseBody List<TotalPriceClientResponse> processPricingRequests(@ApiBodyObject @Valid @RequestBody final List<TotalPriceClientRequest> tpClientRequests) throws TotalPriceClientException {		
		return totalPriceClientService.processAdhocPricingRequests(tpClientRequests);		
	}	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	public @ResponseBody String handleValidationException(final MethodArgumentNotValidException ex) throws JsonGenerationException, JsonMappingException, IOException {
		final BindingResult result = ex.getBindingResult();

		final ObjectMapper mapper = new ObjectMapper();

		final List<ValidationError> validationErrors = new ArrayList<ValidationError>();
		for (final FieldError error : result.getFieldErrors()) {
			final ValidationError validationError = getValidationErrorObj();
			validationError.setFieldName(error.getField());
			validationError.setErrorMessage(error.getDefaultMessage());

			validationErrors.add(validationError);
		}
		return "{\"code\":\"400\",  \"systemMessage\":" + mapper.writeValueAsString(validationErrors) + "}";
	}
	
	private ValidationError getValidationErrorObj() {
		return new ValidationError();
	}
	
	@ResponseStatus(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ Exception.class })
	public @ResponseBody String handleValidationException(final Exception ex) {
		return "{\"code\":\"500\",  \"systemMessage\":\"" + ex.getMessage() + "\"}";
	}

	public TotalPriceClientService getTotalPriceClientService() {
		return totalPriceClientService;
	}

	public void setTotalPriceClientService(
			TotalPriceClientService totalPriceClientService) {
		this.totalPriceClientService = totalPriceClientService;
	}
	
}
