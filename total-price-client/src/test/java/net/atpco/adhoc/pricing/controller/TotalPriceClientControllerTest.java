package net.atpco.adhoc.pricing.controller;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.atpco.common.model.adhoc.AdhocSearchRequest;
import net.atpco.common.model.filter.FilterMergedFare;
import net.atpco.common.model.pricing.TotalPriceClientRequest;
import net.atpco.common.model.pricing.TotalPriceClientResponse;
import net.atpco.common.model.search.request.SearchCriteria;
import net.atpco.common.model.search.request.SearchRequest;
import net.atpco.common.util.DateUtil;
import net.atpco.tp.client.application.Application;
import net.atpco.tp.client.controller.TotalPriceClientController;
import net.atpco.tp.client.exception.TotalPriceClientException;
import net.atpco.tp.client.service.TotalPriceClientService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TotalPriceClientControllerTest {
     
     private static final String APPLICATION_JSON_CHARSET_UTF_8 = "application/json;charset=UTF-8";
     private static final String APPLICATION_JSON = "application/json";
     private static final String API_ADHOC_PRICING_URL = "/api/tp/client/pricing";

     private MockMvc mockMvc = null;
     
     private TotalPriceClientController adhocController = null;
     
     private TotalPriceClientService totalPriceClientService;
     
     @Before
     public void setup() {
           adhocController = new TotalPriceClientController();
           totalPriceClientService = createMockBuilder(TotalPriceClientService.class)
                .addMockedMethod("processAdhocPricingRequest").createMock();
           ReflectionTestUtils.setField(adhocController, "totalPriceClientService", totalPriceClientService);
           mockMvc = MockMvcBuilders.standaloneSetup(adhocController).build();
     }
     
     @Test
     public void invalidRequestShouldReturn400BadRequest() throws Exception {
           AdhocSearchRequest request = new AdhocSearchRequest();
           this.mockMvc.perform(post(API_ADHOC_PRICING_URL)
                           .accept(APPLICATION_JSON)
                           .contentType(APPLICATION_JSON)
                           .content(convertObjectToString(request)))
                     .andExpect(status().isBadRequest())
                     .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8));
     }
     
     @Test
     public void internalServerErrorShouldReturn500Error() throws Exception {
           TotalPriceClientRequest request = buildAdhocSearchRequest();
          expect(totalPriceClientService.processAdhocPricingRequest(anyObject(TotalPriceClientRequest.class))).andThrow(new RuntimeException("TEST"));
           replay(totalPriceClientService);
           System.out.println();
           this.mockMvc.perform(post(API_ADHOC_PRICING_URL)
                           .accept(APPLICATION_JSON)
                           .contentType(APPLICATION_JSON)
                           .content(convertObjectToString(request)))
                     .andExpect(status().isInternalServerError())
                     .andExpect(content().contentType(APPLICATION_JSON_CHARSET_UTF_8));
           
           verify(totalPriceClientService);
     }
     
     @Test
     public void validRequestShouldReturn200() throws Exception {
           TotalPriceClientRequest request = buildAdhocSearchRequest();
           TotalPriceClientResponse response = new TotalPriceClientResponse();
           response.setStatus("SUCCESS");
           expect(totalPriceClientService.processAdhocPricingRequest(request)).andReturn(response);
           replay(totalPriceClientService);
           System.out.println("Test");
           this.mockMvc.perform(post(API_ADHOC_PRICING_URL)
                           .accept(APPLICATION_JSON)
                           .contentType(APPLICATION_JSON)
                           .content(convertObjectToString(request)))
                     .andExpect(status().isOk())
                     .andExpect(content().string(convertObjectToString(response)));           
           verify(totalPriceClientService);
     }
   
    private TotalPriceClientRequest buildAdhocSearchRequest() {
        TotalPriceClientRequest request = new TotalPriceClientRequest();
        
        request.setQueryId("Test2455555");
        request.setTravelDate(DateUtil.getDate(new java.util.Date()));
        request.setTravelDuration(7);
        request.setShopperDuration(180);
        request.setOneWay(false);                        
        request.setOwrtIndicator("R");
        request.setOrigin("WAS");
        request.setDestination("LON");
        request.setCarrier("AA");
        request.setFareClass("FC_Y1OWJB");
        
        return request;
}


     public String convertObjectToString(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

}

