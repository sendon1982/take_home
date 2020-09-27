package com.takehome.web.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takehome.config.AppConfig;
import com.takehome.gen.model.MedallionTrip;
import com.takehome.gen.model.TripSearchRequest;
import com.takehome.gen.model.TripSearchResponse;
import com.takehome.gen.model.TripSearchResponseData;
import com.takehome.service.TripSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TripSearchRestController.class)
class TripSearchRestControllerTest {

    public static final LocalDate PICKUP_DATE = LocalDate.parse("2020-02-02");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppConfig appConfig;

    @MockBean
    private TripSearchService tripSearchService;

    @Test
    void test_searchTrip_success() throws Exception {
        String json = "{\"data\":{\"medallionTrips\":[{\"medallion\":\"123a\",\"tripCount\":12}],\"pickupDate\":\"2020-02-02\"}}";

        Mockito.when(tripSearchService.findTripInfosByIdAndDate(ArgumentMatchers.anySet(), ArgumentMatchers.any(LocalDate.class))).thenReturn(
            buildTripSearchResponse());

        TripSearchRequest request = new TripSearchRequest();

        List<String> medallions = List.of("123a");
        request.setMedallions(medallions);
        request.setPickupDate(PICKUP_DATE);

        MvcResult mvcResult = mockMvc.perform(
            post("/trip/search")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                                     .andExpect(status().isOk()).andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();

        JSONAssert.assertEquals(json, contentAsString, JSONCompareMode.STRICT);

        Mockito.verify(tripSearchService).findTripInfosByIdAndDate(new TreeSet<>(medallions), PICKUP_DATE);
        Mockito.verifyNoMoreInteractions(tripSearchService);
    }

    private TripSearchResponse buildTripSearchResponse() {
        TripSearchResponse tripSearchResponse = new TripSearchResponse();

        TripSearchResponseData data = new TripSearchResponseData();

        MedallionTrip trip = new MedallionTrip();
        trip.setMedallion("123a");
        trip.setTripCount(12L);

        data.setMedallionTrips(List.of(trip));
        data.setPickupDate(PICKUP_DATE);

        tripSearchResponse.setData(data);

        return tripSearchResponse;
    }
}